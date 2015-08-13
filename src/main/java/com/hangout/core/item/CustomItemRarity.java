package com.hangout.core.item;

import org.bukkit.ChatColor;

public enum CustomItemRarity {
	JUNK(ChatColor.GRAY),
	COMMON(ChatColor.WHITE),
	UNCOMMON(ChatColor.GREEN),
	RARE(ChatColor.BLUE),
	MYTHIC(ChatColor.DARK_PURPLE),
	LEGENDARY(ChatColor.GOLD);
	
	private ChatColor color;
	CustomItemRarity(ChatColor color){
		this.color = color;
	}
	
	public ChatColor getColor(){
		return color;
	}
}
