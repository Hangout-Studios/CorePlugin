package com.hangout.core.chat;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class ChatChannel {
	
	public static enum ChatChannelType{
		LOCAL,
		REGION,
		GLOBAL,
		SERVER_WIDE
	}
	
	private String tag;
	private String displayTag;
	private String displayName;
	List<String> description;
	private ChatChannelType type;
	private Material mat;
	
	ChatChannel(String tag, String displayTag, String displayName, List<String> description, ChatChannelType type, Material mat){
		this.tag = tag;
		this.displayTag = displayTag;
		this.displayName = displayName;
		this.description = description;
		this.type = type;
		this.mat = mat;
	}
	
	public String getTag(){
		return tag;
	}
	
	public String getDisplayTag(){
		return ChatColor.ITALIC + displayTag;
	}
	
	public String getDisplayName(){
		return displayName;
	}
	
	public List<String> getDescription(){
		return description;
	}
	
	public ChatChannelType getType(){
		return type;
	}
	
	public Material getMaterial(){
		return mat;
	}
}
