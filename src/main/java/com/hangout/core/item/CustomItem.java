package com.hangout.core.item;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CustomItem {
	
	private ItemStack item;
	
	public CustomItem(ItemStack item){
		this.item = item;
	}
	
	public ItemStack getItemStack(){
		return item.clone();
	}
	
	public String getName(){
		return item.getItemMeta().getDisplayName();
	}
	
	public List<String> getDescription(){
		return item.getItemMeta().getLore();
	}
	
	public Material getMaterial(){
		return item.getType();
	}
}
