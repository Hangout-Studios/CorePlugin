package com.hangout.core.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.hangout.core.menu.MenuItem;
import com.hangout.core.player.HangoutPlayer;

public class MenuItemClickEvent extends Event {
	
	private HangoutPlayer p;
	private MenuItem item;
	
	public MenuItemClickEvent(HangoutPlayer p, MenuItem item){
		this.p = p;
		this.item = item;
	}
	
	public HangoutPlayer getPlayer(){
		return p;
	}
	
	public MenuItem getItem(){
		return item;
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
