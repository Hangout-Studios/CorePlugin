package com.hangout.core.menu;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.hangout.core.utils.mc.ItemUtils;

public class MenuItem {
	
	private ItemStack item;
	private String tag;
	
	public MenuItem(Material mat, String name, List<String> description, String itemTag){
		item = ItemUtils.createItem(mat, name, description);
		this.tag = itemTag;
	}
	
	public MenuItem(ItemStack item, String itemTag){
		this.item = item;
		this.tag = itemTag;
	}
	
	public ItemStack getItemStack(){
		return item;
	}
	
	public String getName(){
		return item.getItemMeta().getDisplayName();
	}
	
	public List<String> getDescription(){
		return item.getItemMeta().getLore();
	}
	
	public String getTag(){
		return tag;
	}
	
	public void updateItemStack(Material mat, String name, List<String> description){
		item = ItemUtils.createItem(mat, name, description);
	}
}
