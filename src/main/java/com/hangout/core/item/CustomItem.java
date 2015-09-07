package com.hangout.core.item;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomItem {
	
	private ItemStack item;
	private String tag;
	private boolean fireItemClickEvent = false;
	private boolean fireRightClickEvent = true;
	private boolean fireSwitchEvent = false;
	private boolean dropable = true;
	private boolean dropOnDeath = true;
	private CustomItemRarity rarity;
	
	public CustomItem(ItemStack item, String tag, 
			boolean fireItemClickEvent, boolean fireRightClickEvent, boolean fireSwitchEvent, 
			boolean dropable, boolean dropOnDeath, CustomItemRarity rarity, boolean customDescriptionColor){
		this.item = item;
		this.tag = tag;
		this.fireItemClickEvent = fireItemClickEvent;
		this.fireRightClickEvent = fireRightClickEvent;
		this.fireSwitchEvent = fireSwitchEvent;
		this.dropable = dropable;
		this.dropOnDeath = dropOnDeath;
		this.rarity = rarity;
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(rarity.getColor() + meta.getDisplayName());
		
		if(!customDescriptionColor){
			List<String> desc = new ArrayList<String>();
			for(String s : meta.getLore()){
				desc.add(ChatColor.WHITE + s);
			}
			meta.setLore(desc);
		}
		
		item.setItemMeta(meta);
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
	
	public String getTag(){
		return tag;
	}
	
	public boolean fireRightClickEvent(){
		return fireRightClickEvent;
	}
	
	public boolean fireItemClickEvent(){
		return fireItemClickEvent;
	}
	
	public boolean fireItemSwitchEvent(){
		return fireSwitchEvent;
	}
	
	public boolean allowDrop(){
		return dropable;
	}
	
	public boolean allowDropOnDeath(){
		return dropOnDeath;
	}
	
	public CustomItemRarity getRarity(){
		return rarity;
	}
}
