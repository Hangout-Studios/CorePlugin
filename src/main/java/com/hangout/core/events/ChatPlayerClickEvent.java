package com.hangout.core.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.hangout.core.player.HangoutPlayer;

public class ChatPlayerClickEvent extends Event {
	
	private HangoutPlayer clicked;
	private HangoutPlayer player;
	
	public ChatPlayerClickEvent(HangoutPlayer clicked, HangoutPlayer player){
		this.clicked = clicked;
		this.player = player;
	}
	
	public HangoutPlayer getClicked(){
		return clicked;
	}
	
	public HangoutPlayer getPlayer(){
		return player;
	}
	
	public boolean isClickedOnline(){
		return !(clicked == null || !clicked.isOnline());
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
