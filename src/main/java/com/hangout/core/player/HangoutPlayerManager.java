package com.hangout.core.player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.hangout.core.Config;
import com.hangout.core.utils.database.Database;
import com.hangout.core.utils.mc.DebugUtils;
import com.hangout.core.utils.mc.DebugUtils.DebugMode;

public class HangoutPlayerManager {
	
	private static HashMap<UUID, HangoutPlayer> players = new HashMap<UUID, HangoutPlayer>();
	private static HashMap<Integer, ItemStack> standardLoadout = new HashMap<Integer, ItemStack>();
	
	public static HangoutPlayer getPlayer(Player p){
		if(p.isOnline()){
			return getPlayer(p.getUniqueId());
		}
		return null;
	}
	
	public static HangoutPlayer getPlayer(UUID id){
		if(players.containsKey(id)){
			return players.get(id);
		}
		return null;
	}
	
	public static void addPlayer(HangoutPlayer p){
		players.put(p.getUUID(), p);
		DebugUtils.sendDebugMessage("Player added: " + p.getName(), DebugMode.DEBUG);
	}
	
	public static void setPlayer(HangoutPlayer p){
		players.put(p.getUUID(), p);
		DebugUtils.sendDebugMessage("Player set with class " + p.getClass().toGenericString(), DebugMode.DEBUG);
	}
	
	public static void removePlayer(HangoutPlayer p){
		if(!players.containsKey(p.getUUID())) return;
		players.remove(p.getUUID());
		DebugUtils.sendDebugMessage("Player removed: " + p.getName(), DebugMode.DEBUG);
	}
	
	public static List<HangoutPlayer> getPlayers(){
		return new ArrayList<HangoutPlayer>(players.values());
	}
	
	public static void addStandardLoadoutItem(ItemStack item, int position){
		standardLoadout.put(position, item);
	}
	
	public static HashMap<Integer, ItemStack> getStandardLoadout(){
		return standardLoadout;
	}
	
	public static UUID getUUID(String playername){
		try (PreparedStatement pst = Database.getConnection().prepareStatement(
                "SELECT player_id FROM " + Config.databaseName + ".players WHERE name = ?;")) {
            pst.setString(1, playername);
            
            UUID id = null;
            ResultSet rs = pst.executeQuery();
            
            if(rs.first()){
            	String name = rs.getString("player_id");
            	if(!rs.wasNull()){
            		id = UUID.fromString(name);
            	}
            }
            
            pst.close();
            
            return id;
            
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void alertModerators(PlayerRank fromRank, String message){
		for(HangoutPlayer p : players.values()){
			if(!p.isOnline() || !p.getHighestRank().isRankOrHigher(fromRank)){
				continue;
			}
			
			p.getPlayer().sendMessage(message);
		}
	}
}
