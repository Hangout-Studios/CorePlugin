package com.hangout.core.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.hangout.core.Plugin;
import com.hangout.core.events.MenuOpenEvent;
import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.player.HangoutPlayerManager;

public class MenuInventory {
	
	private ItemStack item;
	private Inventory inventory;
	private String tag;
	private HashMap<String, MenuItem> itemMap = new HashMap<String, MenuItem>();
	
	private boolean isTemporary = false;
	
	public MenuInventory(ItemStack item, Inventory inventory, String menuTag){
		this.item = item;
		this.inventory = inventory;
		this.tag = menuTag;
	}
	
	public void openMenu(HangoutPlayer p){
		p.getPlayer().openInventory(inventory);
		p.setOpenMenu(this);
		Bukkit.getPluginManager().callEvent(new MenuOpenEvent(p, this));
		Plugin.sendDebugMessage(p.getName() + " opened menu " + getTitle());
	}
	
	public ItemStack getItemStack(){
		return item;
	}
	
	public List<MenuItem> getMenuItems(){
		return new ArrayList<MenuItem>(itemMap.values());
	}
	
	public String getTitle(){
		return inventory.getName();
	}
	
	public String getTag(){
		return tag;
	}
	
	public boolean isTemporary(){
		return isTemporary;
	}
	
	public void setTemporary(boolean b){
		isTemporary = b;
	}
	
	public void addMenuItem(MenuItem item, int inventoryPosition){
		if(inventory.getItem(inventoryPosition) != null){
			return;
		}
		
		if(itemMap.containsKey(item.getTag())){
			removeMenuItem(item);
		}
		
		inventory.setItem(inventoryPosition, item.getItemStack());
		itemMap.put(item.getTag(), item);
	}
	
	public void removeMenuItem(MenuItem item){
		if(itemMap.containsKey(item.getTag())){
			itemMap.remove(item.getTag());
		}
		inventory.remove(item.getItemStack());
	}
	
	public MenuItem getMenuItem(String tag){
		if(itemMap.containsKey(tag)){
			return itemMap.get(tag);
		}
		return null;
	}
	
	public void update(){
		for(HangoutPlayer p : HangoutPlayerManager.getPlayers()){
			if(p.getOpenMenu() == this){
				p.getPlayer().updateInventory();
			}
		}
	}
}
