package com.hangout.core.player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;

import com.hangout.core.Config;
import com.hangout.core.utils.database.Database;
import com.hangout.core.utils.time.TimeUtils;

public class ViolationReport {
	
	private boolean isFinished = false;
	
	private DateTime mutedUntil;
	private DateTime bannedUntil;
	
	private String lastBanReason = "";
	
	public ViolationReport(){}
	
	public boolean isMuted(){
		if(mutedUntil == null) return false;
		
		if(mutedUntil.isAfter(DateTime.now())){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isBanned(){
		if(bannedUntil == null) return false;
		
		if(bannedUntil.isAfter(DateTime.now())){
			return true;
		}else{
			return false;
		}
	}
	
	public void setMutedUntil(DateTime time){
		mutedUntil = time;
	}
	
	public void setBannedUntil(DateTime time, String reason){
		bannedUntil = time;
		lastBanReason = reason;
	}
	
	public String getBanReason(){
		return lastBanReason;
	}
	
	public boolean isFinishedLoading(){
		return isFinished;
	}
	
	public String getTimeLeftMute(){
		if(!isMuted()) return "None";
		return TimeUtils.getTimeUntil(mutedUntil);
	}
	
	public String getTimeLeftBanned(){
		if(!isBanned()) return "None";
		return TimeUtils.getTimeUntil(bannedUntil);
	}
	
	public static List<String> getReport(UUID id){
		
		try (PreparedStatement pst = Database.getConnection().prepareStatement(
                "SELECT action, COUNT(*) as 'count' FROM " + Config.databaseName + ".admin_action WHERE player_id = ? GROUP BY action;")) {
            pst.setString(1, id.toString());
            ResultSet rs = pst.executeQuery();
            
            int bannedCount = 0;
            int mutedCount = 0;
            int kickedCount = 0;
            
            if(rs.first()){
            	rs.beforeFirst();
	            while(rs.next()){
	            	String action = rs.getString("action");
	            	int count = rs.getInt("count");
	            	
	            	if(action.equals("BAN")){
	            		bannedCount = count;
	            	}else if(action.equals("KICK")){
	            		kickedCount = count;
	            	}else if(action.equals("MUTE")){
	            		mutedCount = count;
	            	}
	            }
			}
            pst.close();
            
            List<String> results = new ArrayList<String>();
            results.add("Kicked amount: " + kickedCount);
            results.add("Banned amount: " + bannedCount);
            results.add("Muted amount: " + mutedCount);
            return results;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return new ArrayList<String>();
	}
}
