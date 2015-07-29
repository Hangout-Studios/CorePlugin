package com.hangout.core.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import com.hangout.core.Plugin;

public class MenuManager {
	
	private static HashMap<String, MenuInventory> menuMap = new HashMap<String, MenuInventory>();
	
	public static MenuInventory createMenu(ItemStack item, String title, int rows, String menuTag){
		MenuInventory menu = new MenuInventory(item, Bukkit.createInventory(null, rows * 9, title), menuTag);
		menuMap.put(menuTag, menu);
		Plugin.sendDebugMessage("Created custom menu: " + title);
		return menu;
	}
	
	public static MenuInventory getMenu(String tag){
		if(menuMap.containsKey(tag)){
			return menuMap.get(tag);
		}
		return null;
	}
	
	public static void removeMenu(String tag){
		if(menuMap.containsKey(tag)){
			menuMap.remove(tag);
		}
	}
	
	public static List<MenuInventory> getMenus(){
		return new ArrayList<MenuInventory>(menuMap.values());
	}
}
