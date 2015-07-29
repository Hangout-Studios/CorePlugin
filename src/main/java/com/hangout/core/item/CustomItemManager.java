package com.hangout.core.item;

import java.util.HashMap;

import com.hangout.core.Plugin;

public class CustomItemManager {
	
	private static HashMap<String, CustomItem> items = new HashMap<String, CustomItem>();
	
	public static void addItem(CustomItem item){
		if(!items.containsKey(item.getName())){
			items.put(item.getName(), item);
			Plugin.sendDebugMessage("Added custom item: " + item.getName());
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
}
