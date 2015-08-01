package com.hangout.core.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import com.hangout.core.events.MenuOpenEvent;
import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.player.HangoutPlayerManager;
import com.hangout.core.utils.mc.DebugUtils;
import com.hangout.core.utils.mc.DebugUtils.DebugMode;
import com.hangout.core.utils.mc.ItemUtils;

public class MenuInventory {
	
	private Inventory inventory;
	private String tag;
	private HashMap<String, MenuItem> itemMap = new HashMap<String, MenuItem>();
	
	private boolean isTemporary = false;
	
	public MenuInventory(Inventory inventory, String menuTag, HangoutPlayer p ){
		this.inventory = inventory;
		this.tag = menuTag;
		
		if(p.hasLastMenu(1)){
			MenuUtils.createMenuItem(this, ItemUtils.createItem(Material.PAPER, "Previous menu", Arrays.asList("Click to return to", "the previous menu.")), 26, "previous_menu");
		}
	}
	
	public void openMenu(HangoutPlayer p, boolean addToStack){
		p.getPlayer().openInventory(inventory);
		p.addOpenMenu(this, !addToStack);
		Bukkit.getPluginManager().callEvent(new MenuOpenEvent(p, this));
		DebugUtils.sendDebugMessage(p.getName() + " opened menu " + getTitle(), DebugMode.EXTENSIVE);
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
