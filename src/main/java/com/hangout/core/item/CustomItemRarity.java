package com.hangout.core.item;

import org.bukkit.ChatColor;

public enum CustomItemRarity {
	COMMON(ChatColor.WHITE, "Common"),
	UNCOMMON(ChatColor.GREEN, "Uncommon"),
	RARE(ChatColor.BLUE, "Rare"),
	MYTHIC(ChatColor.DARK_PURPLE, "Mythic"),
	LEGENDARY(ChatColor.GOLD, "Legendary");
	
	private ChatColor color;
	private String displayName;
	CustomItemRarity(ChatColor color, String displayName){
		this.color = color;
		this.displayName = displayName;
	}
	
	public ChatColor getColor(){
		return color;
	}
	
	public String getDisplayName(){
		return displayName;
	}
}