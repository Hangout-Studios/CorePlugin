package com.hangout.core.events;

import java.util.UUID;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.hangout.core.player.HangoutPlayer;

public class PlayerJoinCompleteEvent  extends Event {
	
	private HangoutPlayer p;
	
	public PlayerJoinCompleteEvent(HangoutPlayer p){
		this.p = p;
	}
	
	public HangoutPlayer getPlayer(){
		return p;
	}
	
	public UUID getUUID(){
		return p.getUUID();
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
