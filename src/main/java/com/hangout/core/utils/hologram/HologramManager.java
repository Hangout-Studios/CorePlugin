package com.hangout.core.utils.hologram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.hangout.core.Plugin;

public class HologramManager {
	
	private static HashMap<String, Hologram> globalHolograms = new HashMap<String, Hologram>();
	private static HashMap<UUID, HashMap<String, Hologram>> personalHolograms = new HashMap<UUID, HashMap<String, Hologram>>();
	
	/*
	 * Creating
	 */
	public static void createHologram(String tag, List<String> lines, Location loc){
		Hologram h = HologramsAPI.createHologram(Plugin.getInstance(), loc);
		for(String s : lines){
			h.appendTextLine(s);
		}
		
		globalHolograms.put(tag, h);
	}
	
	public static void createHologram(UUID id, String tag, List<String> lines, Location loc){
		Hologram h = HologramsAPI.createHologram(Plugin.getInstance(), loc);
		for(String s : lines){
			h.appendTextLine(s);
		}
		
		h.getVisibilityManager().showTo(Bukkit.getPlayer(id));
		h.getVisibilityManager().setVisibleByDefault(false);
		
		HashMap<String, Hologram> map = null;
		if(personalHolograms.containsKey(id)){
			map = personalHolograms.get(id);
		}else{
			map = new HashMap<String, Hologram>();
		}
		map.put(tag, h);
		personalHolograms.put(id, map);
	}
	
	/*
	 * Removing
	 */
	public static void removeHologram(String tag){
		if(!globalHolograms.containsKey(tag)) return;
		
		globalHolograms.get(tag).delete();
		globalHolograms.remove(tag);
	}
	
	public static void removeHologram(UUID id, String tag){
		if(!personalHolograms.containsKey(id)){
			return;
		}
		
		if(!personalHolograms.get(id).containsKey(tag)){
			return;
		}
		
		personalHolograms.get(id).get(tag).delete();
		personalHolograms.get(id).remove(tag);
	}
	
	public static void clearPlayer(UUID id){
		if(!personalHolograms.containsKey(id)) return;
		
		for(Hologram h : personalHolograms.get(id).values()){
			h.delete();
		}
		
		personalHolograms.remove(id);
	}
	
	/*
	 * Getting
	 */
	public static List<Hologram> getHolograms(){
		return new ArrayList<Hologram>(globalHolograms.values());
	}
	
	public static HashMap<String, Hologram> getHologramMap(){
		return globalHolograms;
	}
	
	public static List<Hologram> getHolograms(UUID id){
		if(!personalHolograms.containsKey(id)){
			return new ArrayList<Hologram>();
		}else{
			return new ArrayList<Hologram>(personalHolograms.get(id).values());
		}
	}
	
	public static Hologram getHologram(String tag){
		if(!globalHolograms.containsKey(tag)) return null;
		
		return globalHolograms.get(tag);
	}
	
	public static Hologram getHologram(UUID id, String tag){
		if(!personalHolograms.containsKey(id)){
			return null;
		}
		
		if(!personalHolograms.get(id).containsKey(tag)){
			return null;
		}
		
		return personalHolograms.get(id).get(tag);
	}
}
