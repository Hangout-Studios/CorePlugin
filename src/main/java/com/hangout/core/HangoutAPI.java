package com.hangout.core;

import java.sql.Connection;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.hangout.core.item.CustomItem;
import com.hangout.core.item.CustomItemManager;
import com.hangout.core.menu.MenuInventory;
import com.hangout.core.menu.MenuItem;
import com.hangout.core.menu.MenuManager;
import com.hangout.core.player.CommonPlayerBundle;
import com.hangout.core.player.CommonPlayerManager;
import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.player.HangoutPlayerManager;
import com.hangout.core.utils.database.Database;
import com.hangout.core.utils.database.Database.PropertyTypes;

public class HangoutAPI {
	
	/*
	 * General
	 */
	public static Connection getDatabase(){
		return Database.getConnection();
	}
	
	public static boolean isDebugMode(){
		return Config.isDebugMode();
	}
	
	public static boolean isRealServer(){
		return Config.isRealServer();
	}
	
	public static void sendDebugMessage(String message){
		Plugin.sendDebugMessage(message);
	}
	
	/*
	 * Loadouts
	 */
	public static void addStandardLoadoutItem(ItemStack item, int position){
		HangoutPlayerManager.addStandardLoadoutItem(item, position);
	}
	
	/*
	 * Items
	 */
	public static void addCustomItem(ItemStack item) {
		addCustomItem(new CustomItem(item));		
	}
	
	public static void addCustomItem(CustomItem item){
		CustomItemManager.addItem(item);
	}
	
	public static CustomItem getCustomItem(String name){
		return CustomItemManager.getItem(name);
	}
	
	/*
	 * Players
	 */
	public static HangoutPlayer getPlayer(UUID id){
		return HangoutPlayerManager.getPlayer(id);
	}
	
	public static HangoutPlayer getPlayer(Player p){
		return HangoutPlayerManager.getPlayer(p);
	}
	
	public static void addCommonPlayer(UUID id, String type, Object p){
		CommonPlayerManager.addPlayer(id, type, p);
	}
	
	public static CommonPlayerBundle getPlayerBundle(UUID id){
		return CommonPlayerManager.getPlayer(id);
	}
	
	/*
	 * Menus
	 */
	public static MenuInventory createMenu(ItemStack item, String title, int rows, String tag){
		return MenuManager.createMenu(item, title, rows, tag);
	}
	
	public static MenuItem createMenuItem(MenuInventory menu, Material mat, String itemName, List<String> description, int menuPosition, String menuTag){
		MenuItem menuItem = new MenuItem(mat, itemName, description, menuTag);
		menu.addMenuItem(menuItem, menuPosition);
		return menuItem;
	}
	
	public static MenuInventory getMenu(String tag){
		return MenuManager.getMenu(tag);
	}
	
	/*
	 * Saving and loading
	 */
	public static void addCustomPlayerProperty(String databaseColumn, PropertyTypes type){
		Database.addCustomPlayerProperty(databaseColumn, type);
	}
}
