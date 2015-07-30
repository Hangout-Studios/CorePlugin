package com.hangout.core.utils.mc;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

import com.hangout.core.player.HangoutPlayer;

public class CommandPreparer {
	
	private HangoutPlayer p;
	private List<String> parts = new ArrayList<String>();
	private String tag;
	private int key;
	
	public CommandPreparer(HangoutPlayer p, String tag, int key){
		this.p = p;
		this.tag = tag;
		this.key = key;
	}
	
	public void append(String s){
		parts.add(s);
	}
	
	public void execute(){
		if(p.getCommandKey(tag) != key){
			p.getPlayer().sendMessage("This is no longer valid.");
		}
		
		String command = "";
		for(String s : parts){
			command += s + " ";
		}
		command = command.substring(0,command.length() - 1);
		Bukkit.dispatchCommand(p.getPlayer(), command);
	}
}
