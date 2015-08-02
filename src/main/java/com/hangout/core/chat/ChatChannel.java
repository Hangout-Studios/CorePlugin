package com.hangout.core.chat;

import java.util.List;

import org.bukkit.Material;

public class ChatChannel {
	
	public static enum ChatChannelType{
		LOCAL,
		REGION,
		GLOBAL,
		SERVER_WIDE
	}
	
	private String tag;
	private String displayName;
	List<String> description;
	private ChatChannelType type;
	private Material mat;
	private boolean isSelectable = true;
	private boolean isMutable = false;
	private String plugin;
	
	ChatChannel(String tag, String displayName, List<String> description, ChatChannelType type, Material mat, boolean isSelectable, boolean isMutable, String plugin){
		this.tag = tag;
		this.displayName = displayName;
		this.description = description;
		this.type = type;
		this.mat = mat;
		this.isSelectable = isSelectable;
		this.isMutable = isMutable;
		this.plugin = plugin;
	}
	
	public String getTag(){
		return tag;
	}
	
	public String getFullTag(){
		return plugin + "_" + tag;
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
	
	public boolean isSelectable(){
		return isSelectable;
	}
	
	public boolean isMutable(){
		return isMutable;
	}
	
	public String getPlugin(){
		return plugin;
	}
}
