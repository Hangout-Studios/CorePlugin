package com.hangout.core.utils.scoreboard;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

public class DisplayboardManager {
	
	private static HashMap<UUID, Displayboard> scoreboards = new HashMap<UUID, Displayboard>();
	private static HashMap<String, String> prefixes = new HashMap<String, String>();
	
	public static Displayboard getScoreboard(UUID id){
		if(scoreboards.containsKey(id)){
			return scoreboards.get(id);
		}
		
		return createScoreboard(id);
	}
	
	private static Displayboard createScoreboard(UUID id){
		Displayboard sb = new Displayboard(id);
		scoreboards.put(id, sb);
		return sb;
	}
	
	public static void removeScoreboard(UUID id){
		if(scoreboards.containsKey(id)){
			Displayboard sb = scoreboards.get(id);
			sb.clear();
			scoreboards.remove(id);
		}
	}
	
	public static void setPrefix(Player p, String prefix){
		String name = p.getName();
		if(prefix == null){
			if(prefixes.containsKey(name)){
				prefixes.remove(name);
			}
		}else{
			prefixes.put(name, prefix);
		}
		
		System.out.print("Setting prefix: " + name + ", " + prefix);
		
		for(Displayboard b : scoreboards.values()){
			b.setPrefix(name, prefix);
		}
	}
	
	public static void setPrefix(Displayboard b){
		for(String name : prefixes.keySet()){
			b.setPrefix(name, prefixes.get(name));
		}
	}
}
