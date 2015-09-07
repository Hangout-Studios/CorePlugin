package com.hangout.core.utils.scoreboard;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DisplayboardManager {
	
	private static HashMap<UUID, Displayboard> scoreboards = new HashMap<UUID, Displayboard>();
	private static HashMap<UUID, DisplayBoardTags> nameTags = new HashMap<UUID, DisplayBoardTags>();
	
	public static Displayboard getScoreboard(UUID id){
		if(scoreboards.containsKey(id)){
			return scoreboards.get(id);
		}
		
		return createScoreboard(id);
	}
	
	private static Displayboard createScoreboard(UUID id){
		Displayboard sb = new Displayboard(id);
		scoreboards.put(id, sb);
		nameTags.put(id, new DisplayBoardTags(Bukkit.getPlayer(id).getName(), null, null));
		return sb;
	}
	
	public static void removeScoreboard(UUID id){
		if(scoreboards.containsKey(id)){
			Displayboard sb = scoreboards.get(id);
			sb.clear();
			scoreboards.remove(id);
		}
	}
	
	public static void setPrefix(Player p, String s){
		DisplayBoardTags tags = nameTags.get(p.getUniqueId());
		tags.setPrefix(s);
		
		for(Displayboard b : scoreboards.values()){
			tags.applyTo(b);
		}
	}
	
	public static void setSuffix(Player p, String s){
		DisplayBoardTags tags = nameTags.get(p.getUniqueId());
		tags.setSuffix(s);
		
		for(Displayboard b : scoreboards.values()){
			tags.applyTo(b);
		}
	}
	
	public static void setTags(Displayboard b){
		for(DisplayBoardTags tags : nameTags.values()){
			tags.applyTo(b);
		}
	}
}
