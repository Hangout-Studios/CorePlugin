package com.hangout.core.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.utils.mc.CommandPreparer;

public class CommandPreparerEvent extends Event {
	
	private HangoutPlayer p;
	private CommandPreparer command;
	private String message;
	
	public CommandPreparerEvent(HangoutPlayer p, CommandPreparer command, String message){
		this.p = p;
		this.command = command;
		this.message = message;
	}
	
	public HangoutPlayer getPlayer(){
		return p;
	}
	
	public CommandPreparer getCommand(){
		return command;
	}
	
	public String getMessage(){
		return message;
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
