package com.hangout.core.utils.scoreboard;

import java.util.HashMap;
import java.util.UUID;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class Displayboard {
	
	private UUID id;
	private Scoreboard sb;
	private Objective sidebar;
	private HashMap<Integer, String> entries = new HashMap<Integer, String>();
	
	public Displayboard(UUID id){
		this.id = id;
		
		sb = Bukkit.getScoreboardManager().getNewScoreboard();
		
		Bukkit.getPlayer(id).setScoreboard(sb);
		
		registerObjectives();
	}
	
	public void setSidebarLine(int line, String s){
		registerObjectives();
		
		line = 16 - line;
		
		if(entries.containsKey(line)){
			String oldLine = entries.get(line);
			sb.resetScores(oldLine);
		}
		
		sidebar.getScore(s).setScore(line);
		entries.put(line, s);
	}
	
	public void registerObjectives(){
		if(sidebar != null) return;
		
		sidebar = sb.registerNewObjective(getSlotTag(DisplaySlot.SIDEBAR), "dummy");
		sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
		sidebar.setDisplayName(""+ ChatColor.AQUA + ChatColor.BOLD + "The Hangout");
	}
	
	public void setNameTags(String name, String prefix, String suffix){
		Team t = sb.getTeam(name);
		
		
		if(prefix == null){
			if(t != null){
				t.unregister();
			}
			return;
		}else{
			if(t == null){
				t = sb.registerNewTeam(name);
			}
			t.setPrefix(ChatColor.RED + "[" + prefix + "]" + ChatColor.RESET);
			t.setSuffix(""+ChatColor.RESET + ChatColor.RED + "[" + suffix + "]");
		}
		
		if(!t.hasEntry(name)){
			t.addEntry(name);
		}
	}
	
	public void clear(){
		sidebar.unregister();
		
		for(Team t: sb.getTeams()){
			t.unregister();
		}
	}
	
	private String getSlotTag(DisplaySlot slot){
		String s = id.toString();
		return slot.toString().substring(0,1) + "_" + s.substring(0,4) + s.substring(10,14);
	}
}
