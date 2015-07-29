package com.hangout.core.chat;

import org.bukkit.ChatColor;

public enum ChatChannel {
	SAY("(S)"),
	LOCAL("(L)"),
	GLOBAL("(G)");
	
	private String tag;
	ChatChannel(String tag){
		this.tag = tag;
	}
	
	public String getTag(){
		return ChatColor.ITALIC + tag;
	}
}
