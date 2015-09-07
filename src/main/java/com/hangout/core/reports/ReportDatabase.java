package com.hangout.core.reports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReportDatabase {
	
	private static HashMap<Integer, BugReport> bugReports = new HashMap<Integer, BugReport>();
	private static HashMap<Integer, PlayerReport> playerReports = new HashMap<Integer, PlayerReport>();
	private static int bugCount = 0;
	private static int playerCount = 0;
	
	public static void addBugReport(BugReport r){
		bugReports.put(r.getID(), r);
	}
	
	public static BugReport removeBugReport(int id){
		if(bugReports.containsKey(id)){
			return bugReports.remove(id);
		}
		return null;
	}
	
	public static List<BugReport> getBugReports(){
		return new ArrayList<BugReport>(bugReports.values());
	}
	
	public static BugReport getBugReport(int id){
		if(bugReports.containsKey(id)){
			return bugReports.get(id);
		}
		return null;
	}
	
	public static void addPlayerReport(PlayerReport r){
		playerReports.put(r.getID(), r);
	}
	
	public static PlayerReport removePlayerReport(int id){
		if(playerReports.containsKey(id)){
			return playerReports.remove(id);
		}
		return null;
	}
	
	public static List<PlayerReport> getPlayerReports(){
		return new ArrayList<PlayerReport>(playerReports.values());
	}
	
	public static int nextBugID(){
		bugCount++;
		return bugCount;
	}
	
	public static int nextPlayerID(){
		playerCount++;
		return playerCount;
	}
}
