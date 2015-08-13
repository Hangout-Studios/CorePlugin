package com.hangout.core.item;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomItem {
	
	private ItemStack item;
	private String tag;
	private boolean allowItemClick = false;
	private boolean allowRightClick = true;
	private boolean dropable = true;
	private boolean dropOnDeath = true;
	private CustomItemRarity rarity;
	
	public CustomItem(ItemStack item, String tag, boolean allowItemClick, boolean allowRightClick, boolean dropable, boolean dropOnDeath, CustomItemRarity rarity){
		this.item = item;
		this.tag = tag;
		this.allowItemClick = allowItemClick;
		this.allowRightClick = allowRightClick;
		this.dropable = dropable;
		this.dropOnDeath = dropOnDeath;
		this.rarity = rarity;
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(rarity.getColor() + meta.getDisplayName());
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
	
	public boolean allowRightClick(){
		return allowRightClick;
	}
	
	public boolean allowItemClick(){
		return allowItemClick;
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
