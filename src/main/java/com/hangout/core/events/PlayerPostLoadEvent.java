package com.hangout.core.events;

import java.util.HashMap;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.hangout.core.Plugin;
import com.hangout.core.player.HangoutPlayer;

public class PlayerPostLoadEvent extends Event {
	
	HangoutPlayer p;
	HashMap<String, Object> propertyMap;
	public PlayerPostLoadEvent(HangoutPlayer p, HashMap<String, Object> propertyMap){
		this.p = p;
		this.propertyMap = propertyMap;
		
		Plugin.sendDebugMessage("Properties loaded:");
		for(String key : propertyMap.keySet()){
			Plugin.sendDebugMessage("Key: " + key + ", value: " + propertyMap.get(key));
		}
	}
	
	public HangoutPlayer getPlayer(){
		return p;
	}
	
	public Object getProperty(String key){
		if(propertyMap.containsKey(key)){
			return propertyMap.get(key);
		}
		return null;
	}
	
	/*
	 * Handlers
	 */
	private static final HandlerList handlers = new HandlerList();
	 
	public HandlerList getHandlers() {
	    return handlers;
	}
	 
	public static HandlerList getHandlerList() {
	    return handlers;
	}
}
