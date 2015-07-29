package com.hangout.core.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.hangout.core.item.CustomItem;
import com.hangout.core.player.HangoutPlayer;

public class CustomItemUseEvent extends Event {
	
	private CustomItem item;
	private HangoutPlayer p;
	
	public CustomItemUseEvent(CustomItem item, HangoutPlayer p){
		this.item = item;
		this.p = p;
	}
	
	public CustomItem getCustomItem(){
		return item;
	}
	
	public HangoutPlayer getHangoutPlayer(){
		return p;
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
