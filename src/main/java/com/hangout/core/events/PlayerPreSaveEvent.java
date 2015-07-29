package com.hangout.core.events;

import java.util.HashMap;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.hangout.core.Plugin;
import com.hangout.core.player.HangoutPlayer;

public class PlayerPreSaveEvent extends Event {
	
	HangoutPlayer p;
	HashMap<String, Object> primary = new HashMap<String, Object>();
	HashMap<String, Object> secondary = new HashMap<String, Object>();
	
	public PlayerPreSaveEvent(HangoutPlayer p){
		this.p = p;
	}
	
	public HangoutPlayer getPlayer(){
		return p;
	}
	
	public void savePrimaryProperty(String key, Object value){
		primary.put(key, value);
		Plugin.sendDebugMessage("Set primary key " + key + " to " + value);
	}
	
	public void saveSecondaryProperty(String key, Object value){
		secondary.put(key, value);
		Plugin.sendDebugMessage("Set secondary key " + key + " to " + value);
	}
	
	public HashMap<String, Object> getPrimaryMap(){
		return primary;
	}
	
	public HashMap<String, Object> getSecondaryMap(){
		return secondary;
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
