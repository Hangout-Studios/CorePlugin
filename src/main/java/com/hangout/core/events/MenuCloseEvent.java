package com.hangout.core.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.hangout.core.menu.MenuInventory;
import com.hangout.core.player.HangoutPlayer;

public class MenuCloseEvent extends Event {
	
	private HangoutPlayer p;
	private MenuInventory menu;
	
	public MenuCloseEvent(HangoutPlayer p, MenuInventory menu){
		this.p = p;
		this.menu = menu;
	}
	
	public HangoutPlayer getPlayer(){
		return p;
	}
	
	public MenuInventory getMenu(){
		return menu;
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
