package com.hangout.core.menu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.hangout.core.utils.mc.ItemUtils;

public class MenuItem {
	
	private ItemStack item;
	private String tag;
	
	public MenuItem(Material mat, String name, List<String> description, String itemTag){
		item = ItemUtils.createItem(mat, name, description);
		
		ItemMeta meta = item.getItemMeta();
		List<String> desc = new ArrayList<String>();
		for(String s : meta.getLore()){
			desc.add(""+ ChatColor.GRAY + ChatColor.ITALIC + s);
		}
		meta.setLore(desc);
		item.setItemMeta(meta);
		
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
