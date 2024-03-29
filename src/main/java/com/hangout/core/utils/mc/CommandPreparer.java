package com.hangout.core.utils.mc;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.utils.mc.DebugUtils.DebugMode;

public class CommandPreparer {
	
	private HangoutPlayer p;
	private List<String> parts = new ArrayList<String>();
	private String tag;
	private int key;
	private int stage = 0;
	
	public CommandPreparer(HangoutPlayer p, String tag, int key){
		this.p = p;
		this.tag = tag;
		this.key = key;
	}
	
	public String getTag(){
		return tag;
	}
	
	public int getStage(){
		return stage;
	}
	
	public void nextStage(){
		stage++;
	}
	
	public void append(String s){
		parts.add(s);
	}
	
	public void execute(){
		if(p.getCommandKey(tag) != key){
			p.getPlayer().sendMessage("This is no longer valid.");
		}
		
		p.removeCommandKey(tag);
		
		String command = "";
		for(String s : parts){
			command += s + " ";
		}
		command = command.substring(0,command.length() - 1);
		
		DebugUtils.sendDebugMessage("Command via preparer: /" + command, DebugMode.INFO);
		Bukkit.dispatchCommand(p.getPlayer(), command);
	}
}
