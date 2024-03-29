package com.hangout.core.utils.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.joda.time.DateTime;

import com.hangout.core.Config;
import com.hangout.core.chat.ChatChannel;
import com.hangout.core.chat.ChatManager;
import com.hangout.core.events.PlayerPostLoadEvent;
import com.hangout.core.events.PlayerPreSaveEvent;
import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.player.HangoutPlayer.PlayerState;
import com.hangout.core.player.HangoutPlayerManager;
import com.hangout.core.player.PlayerRank;
import com.hangout.core.reports.BugReport;
import com.hangout.core.reports.PlayerReport;
import com.hangout.core.reports.ReportDatabase;
import com.hangout.core.reports.ViolationReport;
import com.hangout.core.utils.mc.DebugUtils;
import com.hangout.core.utils.mc.DebugUtils.DebugMode;
import com.mysql.jdbc.StringUtils;

public class Database {
	
	public static enum PropertyTypes {
		STRING("Unknown"),
		INTEGER(0), 
		TIMESTAMP(new Timestamp(DateTime.now().getMillis()));
		
		public Object defaultValue;
		PropertyTypes(Object defaultValue){
			this.defaultValue = defaultValue;
		}
	}
	
	private static HashMap<String, PropertyTypes> customProperties = new HashMap<String, PropertyTypes>();
	
    protected static Connection connection;

    public synchronized static Connection getConnection() {
        try {
            if (connection != null) {
                if (connection.isValid(1)) {
                    return connection;
                } else {
                    connection.close();
                }
            }
            DebugUtils.sendDebugMessage("Connecting to SQL: " + Config.host + ", " + Config.username + ", " + Config.password, DebugMode.INFO);
            connection = DriverManager.getConnection("jdbc:mysql://" + Config.host, Config.username, Config.password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
    
    public static void addCustomPlayerProperty(String databaseColumn, PropertyTypes type){
    	customProperties.put(databaseColumn, type);
    }
    
    public static void loadReports(){
    	
    	//Load bug reports
    	try (PreparedStatement pst = getConnection().prepareStatement(
                "SELECT report_id, timestamp, report, accepted_by, player_id, v_player.name as name " +
                "FROM " + Config.databaseName + ".bug_report as t_report " +
                "JOIN(" +
                	"SELECT t_players.uuid, t_players.name " +
                	"FROM " + Config.databaseName + ".players as t_players " +
    			") v_player ON(t_report.player_id = v_player.uuid) " +
    			"WHERE accepted_by = '-';")) {
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
            	ReportDatabase.addBugReport(new BugReport(rs.getString("name"), rs.getString("report"),
            			new DateTime(rs.getTimestamp("timestamp")), rs.getInt("report_id")));
            }
    	} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	//Load player reports
    	try (PreparedStatement pst = getConnection().prepareStatement(
                "SELECT report_id, timestamp, report, accepted_by, player_id, reported_id, v_player1.name as name1, v_player2.name as name2 " +
                "FROM " + Config.databaseName + ".player_report as t_report " +
                "JOIN(" +
                	"SELECT t_players.uuid, t_players.name " +
                	"FROM " + Config.databaseName + ".players as t_players " +
    			") v_player1 ON(t_report.player_id = v_player1.uuid) " +
                "JOIN(" +
            		"SELECT t_players.uuid, t_players.name " +
            		"FROM " + Config.databaseName + ".players as t_players " +
            	") v_player2 ON(t_report.reported_id = v_player2.uuid) " +
    			"WHERE accepted_by = '-';")) {
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
            	ReportDatabase.addPlayerReport(new PlayerReport(rs.getString("name1"), rs.getString("name2"), rs.getString("report"),
            			new DateTime(rs.getTimestamp("timestamp")), rs.getInt("report_id")));
            }
    	} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public static HangoutPlayer loadPlayer(UUID id, String playerName){
    	HangoutPlayer hp = null;
    	
    	if (!StringUtils.isNullOrEmpty(playerName)){
    		
    		//Create from id and name
    		hp = new HangoutPlayer(id, playerName);
    		DebugUtils.sendDebugMessage("Created player from ID and name.", DebugMode.EXTENSIVE);
    	}
    	
    	HashMap<String, Object> customPropertiesResult = new HashMap<String, Object>();
    	String columns = "";
    	for(String s : customProperties.keySet()){
    		columns += s + ", ";
    	}
    	columns = columns.substring(0, columns.length() - 2);
    	
    	DebugUtils.sendDebugMessage("Loading these values for " + playerName + ": " + columns, DebugMode.EXTENSIVE);
    	
    	//Load stats
    	try (PreparedStatement pst = getConnection().prepareStatement(
                "SELECT " + columns + " FROM " + Config.databaseName + ".players WHERE uuid = ?;")) {
            pst.setString(1, id.toString());
            ResultSet rs = pst.executeQuery();
            
            if(rs.first()){
            	for(String key : customProperties.keySet()){
            		PropertyTypes type = customProperties.get(key);
            		Object result = null;
	            	
	            	switch(type){
					case INTEGER:
						result = rs.getInt(key);
						break;
					case STRING:
						result = rs.getString(key);
						break;
					case TIMESTAMP:
						result = rs.getTimestamp(key);
						break;
	            	}
	            	
	            	if(rs.wasNull()){
	            		result = type.defaultValue;
	            	}
	            	customPropertiesResult.put(key, result);
            	}
            }else{
	            for(String key : customProperties.keySet()){
	            	PropertyTypes type = customProperties.get(key);
	            	customPropertiesResult.put(key, type.defaultValue);
	            }
            }
            
            //Player was created but offline, so we get the name from the db
            if(hp == null){
            	hp = new HangoutPlayer(id, (String) customPropertiesResult.get("name"));
            	DebugUtils.sendDebugMessage("Created player from database.", DebugMode.EXTENSIVE);
            }
    	} catch (SQLException e) {
    		DebugUtils.sendDebugMessage("Loading of player failed: " + id.toString(), DebugMode.WARNING);
			e.printStackTrace();
		}
    	
    	//Add player to list
    	HangoutPlayerManager.addPlayer(hp);
    	hp.setLoadingState("core", false);
    	
    	//Call the event so other players can get their properties
    	PlayerPostLoadEvent postLoadEvent = new PlayerPostLoadEvent(hp, customPropertiesResult);
    	Bukkit.getPluginManager().callEvent(postLoadEvent);
    	
    	//Load friends
    	try (PreparedStatement pst = getConnection().prepareStatement(
                "SELECT max(action_id), player2, add_friend FROM " + Config.databaseName + ".friend_action WHERE player1 = ? AND add_friend = true GROUP BY player2;")) {
            pst.setString(1, id.toString());
            ResultSet rs = pst.executeQuery();
            
            List<UUID> friendsUUID = new ArrayList<UUID>();
            while(rs.next()){
            	UUID friendID = UUID.fromString(rs.getString("player2"));
            	if(!friendsUUID.contains(friendID)){
            		friendsUUID.add(friendID);
            	}
            }
            
            List<HangoutPlayer> friends = new ArrayList<HangoutPlayer>();
            for(UUID friend : friendsUUID){
            	if(HangoutPlayerManager.getPlayer(friend) == null){
            		HangoutPlayer friendPlayer = loadPlayer(friend, null);
            		HangoutPlayerManager.addPlayer(friendPlayer);
            		friends.add(friendPlayer);
            	}
            }
            
            hp.setFriends(friends);
            
            pst.close();
            
            DebugUtils.sendDebugMessage("Loading " + friends.size() + " friends", DebugMode.EXTENSIVE);
    	} catch (SQLException e) {
    		DebugUtils.sendDebugMessage("Loading of friends failed: " + id.toString(), DebugMode.WARNING);
			e.printStackTrace();
		}
    	
    	//Load violations
    	ViolationReport report = hp.getViolationReport();
		try (PreparedStatement pst = Database.getConnection().prepareStatement(
                "SELECT action, duration, timestamp, reason FROM " + Config.databaseName + ".admin_action WHERE player_id = ? ORDER BY action_id ASC;")) {
            pst.setString(1, id.toString());
            ResultSet rs = pst.executeQuery();
            
            while(rs.next()){
            	String action = rs.getString("action");
            	int duration = rs.getInt("duration");
            	String reason = rs.getString("reason");
            	DateTime time = new DateTime(rs.getTimestamp("timestamp"));
            	DateTime until = time.plusMinutes(duration);
            	
            	if(action.equals("MUTE")){
            		report.setMutedUntil(until);
            	}else if(action.equals("BAN")){
            		report.setBannedUntil(until, reason);
            	}
            }
            
            pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Load ranks
		try (PreparedStatement pst = Database.getConnection().prepareStatement(
                "SELECT action, rank FROM " + Config.databaseName + ".rank_action WHERE player_id = ? ORDER BY action_id ASC;")) {
            pst.setString(1, id.toString());
            ResultSet rs = pst.executeQuery();
            
            if(!rs.first()){
            	hp.addRank(PlayerRank.DEFAULT, true);
            }else{
            	rs.beforeFirst();
	            while(rs.next()){
	            	String action = rs.getString("action");
	            	String rankString = rs.getString("rank");
	            	PlayerRank rank = null;
	            	for(PlayerRank r : PlayerRank.values()){
	            		if(r.toString().equals(rankString)){
	            			rank = r;
	            			break;
	            		}
	            	}
	            	
	            	if(action.equals("GRANT")){
	            		hp.addRank(rank, false);
	            	}else if(action.equals("REMOVE")){
	            		hp.removeRank(rank, false);
	            	}
	            }
            }
            
            pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Load mute actions
		try (PreparedStatement pst = Database.getConnection().prepareStatement(
				"SELECT action, muted_id FROM " + Config.databaseName + ".mute_action WHERE player_id = ? ORDER BY action_id ASC;")) {
			pst.setString(1, id.toString());
			ResultSet rs = pst.executeQuery();
			
			while(rs.next()){
				UUID mutedID = UUID.fromString(rs.getString("muted_id"));
				String action = rs.getString("action");
				
				if(action.equals("MUTE")){
					hp.addMutedPlayer(mutedID, false);
				}else if(action.equals("UNMUTE")){
					hp.removeMutedPlayer(mutedID, false);
				}
			}

			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Load pvp actions
		try (PreparedStatement pst = Database.getConnection().prepareStatement(
				"SELECT pvp_on FROM " + Config.databaseName + ".pvpflag_action WHERE player_id = ? ORDER BY action_id DESC LIMIT 1;")) {
			pst.setString(1, id.toString());
			ResultSet rs = pst.executeQuery();
			
			if(rs.first()){
				boolean pvpOn = rs.getBoolean("pvp_on");
				hp.setPvpEnabled(pvpOn);
			}

			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Load gold
		try (PreparedStatement pst = Database.getConnection().prepareStatement(
				"SELECT sum(gold) as 'goldsum' FROM " + Config.databaseName + ".gold_action WHERE player_id = ?;")) {
			pst.setString(1, id.toString());
			ResultSet rs = pst.executeQuery();
			
			if(rs.first()){
				int gold = rs.getInt("goldsum");
				hp.modifyGold(gold, "DATABASE", false);
			}

			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Load channels
		try (PreparedStatement pst = Database.getConnection().prepareStatement(
				"SELECT action, channel FROM " + Config.databaseName + ".chatchannel_action WHERE player_id = ? ORDER BY action_id ASC;")) {
			pst.setString(1, id.toString());
			ResultSet rs = pst.executeQuery();
			
			while(rs.next()){
				String[] channelSplit = rs.getString("channel").split("_");
				ChatChannel channel = ChatManager.getChannel(channelSplit[0], channelSplit[1]);
				String action = rs.getString("action");
				
				if(action.equals("MUTE")){
					hp.removeSubscribedChannel(channel, false);
				}else if(action.equals("UNMUTE")){
					hp.addSubscribedChannel(channel, false);
				}else if(action.equals("CHAT")){
					hp.setChatChannel(channel, false);
				}
			}

			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	hp.setPlayerState(PlayerState.UNLINKED);
    	
    	hp.setLoadingState("core", true);
    	
    	DebugUtils.sendDebugMessage("Succesfully loaded core player: " + hp.getName(), DebugMode.INFO);
    	return hp;
    }
    
    public static void savePlayer(PlayerPreSaveEvent e){
    	saveToDatabase("players", e.getPrimaryMap(), e.getSecondaryMap());
    }
    
    public static void executeFriendAction(UUID id1, UUID id2, boolean addFriend){    	
    	HashMap<String, Object> primary = new HashMap<String, Object>();
    	HashMap<String, Object> secondary = new HashMap<String, Object>();
    	
    	secondary.put("player1", id1.toString());
    	secondary.put("player2", id2.toString());
    	secondary.put("add_friend", addFriend);
    	
    	saveToDatabase("friend_action", primary, secondary);
    }
    
	public static void executeAdminAction(UUID playerID, UUID adminID, int time, String reason, String type){
		HashMap<String, Object> primary = new HashMap<String, Object>();
    	HashMap<String, Object> secondary = new HashMap<String, Object>();
    	
    	secondary.put("player_id", playerID.toString());
    	secondary.put("admin_id", adminID.toString());
    	secondary.put("action", type);
    	secondary.put("reason", reason);
    	secondary.put("duration", time);
    	
    	saveToDatabase("admin_action", primary, secondary);
	}
	
	public static void executeRankAction(UUID playerID, PlayerRank rank, String action){
		HashMap<String, Object> primary = new HashMap<String, Object>();
    	HashMap<String, Object> secondary = new HashMap<String, Object>();
    	
    	secondary.put("player_id", playerID.toString());
    	secondary.put("rank", rank.toString());
    	secondary.put("action", action);
    	
    	saveToDatabase("rank_action", primary, secondary);
	}
	
	public static void executeBugReport(UUID playerID, String report){
		HashMap<String, Object> primary = new HashMap<String, Object>();
    	HashMap<String, Object> secondary = new HashMap<String, Object>();
    	
    	secondary.put("player_id", playerID.toString());
    	secondary.put("report", report);
    	
    	saveToDatabase("bug_report", primary, secondary);
    	
    	ReportDatabase.addBugReport(new BugReport(Bukkit.getPlayer(playerID).getName(), report, DateTime.now(), -1));
	}
	
	public static void executePlayerReport(UUID playerID, UUID reportedID, String report){
		HashMap<String, Object> primary = new HashMap<String, Object>();
    	HashMap<String, Object> secondary = new HashMap<String, Object>();
    	
    	secondary.put("player_id", playerID.toString());
    	secondary.put("reported_id", reportedID.toString());
    	secondary.put("report", report);
    	
    	saveToDatabase("player_report", primary, secondary);
    	
    	ReportDatabase.addPlayerReport(new PlayerReport(Bukkit.getPlayer(playerID).getName(), Bukkit.getPlayer(reportedID).getName(),
    			report, DateTime.now(), -1));
	}
	
	public static void finishReport(int reportID, UUID by, String reportType){
		HashMap<String, Object> primary = new HashMap<String, Object>();
    	HashMap<String, Object> secondary = new HashMap<String, Object>();
    	
    	primary.put("report_id", reportID);
    	secondary.put("accepted_by", by.toString());
    	
    	saveToDatabase(reportType + "_report", primary, secondary);
	}
	
	public static void executeMuteAction(UUID playerID, UUID mutedID, String action){
		HashMap<String, Object> primary = new HashMap<String, Object>();
    	HashMap<String, Object> secondary = new HashMap<String, Object>();
    	
    	secondary.put("player_id", playerID.toString());
    	secondary.put("muted_id", mutedID.toString());
    	secondary.put("action", action);
    	
    	saveToDatabase("mute_action", primary, secondary);
	}
	
	public static void executePvpFlagAction(UUID playerID, boolean enabled){
		HashMap<String, Object> primary = new HashMap<String, Object>();
    	HashMap<String, Object> secondary = new HashMap<String, Object>();
    	
    	secondary.put("player_id", playerID.toString());
    	secondary.put("pvp_on", enabled);
    	
    	saveToDatabase("pvpflag_action", primary, secondary);
	}
	
	public static void executeGoldAction(UUID playerID, int gold, String source){
		HashMap<String, Object> primary = new HashMap<String, Object>();
    	HashMap<String, Object> secondary = new HashMap<String, Object>();
    	
    	secondary.put("player_id", playerID.toString());
    	secondary.put("gold", gold);
    	secondary.put("source", source);
    	
    	saveToDatabase("gold_action", primary, secondary);
	}
	
	public static void executeChatChannelAction(UUID playerID, ChatChannel channel, String action){
		HashMap<String, Object> primary = new HashMap<String, Object>();
    	HashMap<String, Object> secondary = new HashMap<String, Object>();
    	
    	secondary.put("player_id", playerID.toString());
    	secondary.put("channel", channel.getFullTag());
    	secondary.put("action", action);
    	
    	saveToDatabase("chatchannel_action", primary, secondary);
	}
    
    public static void saveToDatabase(String table, HashMap<String, Object> primary, HashMap<String, Object> secondary){
    	HashMap<String, Object> compiled = new HashMap<String, Object>();
    	compiled.putAll(primary);
    	compiled.putAll(secondary);
    	
    	String keyList = "(";
    	String questionMarkList = "(";
    	String valueList = "";
    	for(String key : compiled.keySet()){
    		keyList += key + ", ";
    		questionMarkList += "?, ";
    		
    		if(secondary.containsKey(key)){
    			valueList += key + " = VALUES(" + key + "), ";
    		}
    	}
    	keyList = keyList.substring(0, keyList.length() - 2) + ")";
    	questionMarkList = questionMarkList.substring(0, questionMarkList.length() - 2) + ")";
    	valueList = valueList.substring(0, valueList.length() - 2);
    	
    	String completeQuery = "INSERT INTO " + Config.databaseName + "." + table + " " + keyList + " VALUES " + questionMarkList + 
    			" ON DUPLICATE KEY UPDATE " + valueList + ";";
    	DebugUtils.sendDebugMessage("Query to execute: " + completeQuery, DebugMode.COMPLETE);
    	
    	try (PreparedStatement pst = getConnection().prepareStatement(completeQuery)){
    		List<String> keys = new ArrayList<String>(compiled.keySet());
    		for(int i = 0; i < compiled.size(); i++){
    			Object o = compiled.get(keys.get(i));
    			if(o instanceof String){
    				pst.setString(i + 1, (String)o);
    				DebugUtils.sendDebugMessage("Key: " + (i + 1) + ", string: " + o, DebugMode.COMPLETE);
    			}
    			if(o instanceof Integer){
    				pst.setInt(i + 1, (Integer)o);
    				DebugUtils.sendDebugMessage("Key: " + (i + 1) + ", integer: " + o, DebugMode.COMPLETE);
    			}
    			if(o instanceof Timestamp){
    				pst.setTimestamp(i + 1, (Timestamp)o);
    				DebugUtils.sendDebugMessage("Key: " + (i + 1) + ", timestamp: " + o, DebugMode.COMPLETE);
    			}
    			if(o instanceof Boolean){
    				pst.setBoolean(i + 1, (Boolean)o);
    				DebugUtils.sendDebugMessage("Key: " + (i + 1) + ", boolean: " + o, DebugMode.COMPLETE);
    			}
    		}
    		pst.execute();
    		pst.close();
    	} catch (SQLException e) {
			e.printStackTrace();
		}
    }
}
