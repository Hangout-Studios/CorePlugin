package com.hangout.core.item;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.hangout.core.utils.mc.DebugUtils;
import com.hangout.core.utils.mc.DebugUtils.DebugMode;

public class CustomItemManager {
	
	private static HashMap<String, CustomItem> items = new HashMap<String, CustomItem>();
	private static HashMap<Material, CustomItem> defaultItems = new HashMap<Material, CustomItem>();
	
	public static void addItem(CustomItem item){
		String name = item.getName();
		if(!items.containsKey(name)){
			items.put(name, item);
			DebugUtils.sendDebugMessage("Added custom item: " + item.getName(), DebugMode.DEBUG);
		}
	}
	
	public static void removeItem(CustomItem item){
		if(items.containsKey(item.getName())){
			items.remove(item.getName());
		}
	}
	
	public static CustomItem getItem(String name){
		if(items.containsKey(name)){
			return items.get(name);
		}
		return null;
	}
	
	public static CustomItem getItem(ItemStack item){
		if(!item.hasItemMeta()) return null;
		return getItem(item.getItemMeta().getDisplayName());
	}
	
	public static boolean isCustomItem(ItemStack i){
		if(!i.hasItemMeta()) return false;
		
		String name = i.getItemMeta().getDisplayName();
		if(items.containsKey(name)){
			return true;
		}
		return false;
	}
	
	public static void addDefaultItem(CustomItem item){
		String name = item.getName();
		if(!items.containsKey(name)){
			items.put(name, item);
			DebugUtils.sendDebugMessage("Added custom item: " + item.getName(), DebugMode.DEBUG);
		}
		
		defaultItems.put(item.getMaterial(), item);
	}
	
	public static CustomItem getDefaultItem(Material mat){
		if(defaultItems.containsKey(mat)){
			return defaultItems.get(mat);
		}
		return null;
	}
}
