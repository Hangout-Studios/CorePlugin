package com.hangout.core.events;

import java.util.HashMap;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.utils.mc.DebugUtils;
import com.hangout.core.utils.mc.DebugUtils.DebugMode;

public class PlayerPostLoadEvent extends Event {
	
	HangoutPlayer p;
	HashMap<String, Object> propertyMap;
	public PlayerPostLoadEvent(HangoutPlayer p, HashMap<String, Object> propertyMap){
		this.p = p;
		this.propertyMap = propertyMap;
		
		DebugUtils.sendDebugMessage("Properties loaded:", DebugMode.COMPLETE);
		for(String key : propertyMap.keySet()){
			DebugUtils.sendDebugMessage("Key: " + key + ", value: " + propertyMap.get(key), DebugMode.COMPLETE);
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
