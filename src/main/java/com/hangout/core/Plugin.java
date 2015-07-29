package com.hangout.core;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.hangout.core.commands.AdminCommand;
import com.hangout.core.commands.MuteCommand;
import com.hangout.core.commands.ReportCommand;
import com.hangout.core.commands.TextCommand;
import com.hangout.core.listeners.BattleListener;
import com.hangout.core.listeners.ChatListener;
import com.hangout.core.listeners.ItemListener;
import com.hangout.core.listeners.MenuListener;
import com.hangout.core.listeners.PlayerListener;
import com.hangout.core.menu.MenuInventory;
import com.hangout.core.utils.database.Database.PropertyTypes;
import com.hangout.core.utils.lang.MessageManager;
import com.hangout.core.utils.mc.ItemUtils;
import com.mysql.jdbc.StringUtils;

public class Plugin extends JavaPlugin {
	
	private static Plugin instance;
	private MessageManager messageBundle;
	
	public void onEnable(){
		
		instance = this;
		messageBundle = new MessageManager(getConfig().getString("locale", "en"));
		
		this.saveDefaultConfig();
		Config.loadData();
		
		this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		this.getServer().getPluginManager().registerEvents(new MenuListener(), this);
		this.getServer().getPluginManager().registerEvents(new ChatListener(), this);
		this.getServer().getPluginManager().registerEvents(new ItemListener(), this);
		this.getServer().getPluginManager().registerEvents(new BattleListener(), this);
		
		this.getCommand("text").setExecutor(new TextCommand());
		this.getCommand("admin").setExecutor(new AdminCommand());
		this.getCommand("report").setExecutor(new ReportCommand());
		this.getCommand("mute").setExecutor(new MuteCommand());
		this.getCommand("unmute").setExecutor(new MuteCommand());
		
		//Add custom loading properties
		HangoutAPI.addCustomPlayerProperty("name", PropertyTypes.STRING);
		HangoutAPI.addCustomPlayerProperty("last_online", PropertyTypes.TIMESTAMP);
		
		//Create some custom items
		ItemStack menuItem = ItemUtils.createItem(Material.PAPER, "Open interface", Arrays.asList("Right click to use"));
		HangoutAPI.addStandardLoadoutItem(menuItem, 8);
		MenuInventory mainMenu = HangoutAPI.createMenu(menuItem, "Main menu", 3, "mainmenu");
		
		HangoutAPI.createMenuItem(mainMenu, Material.SKULL, "Friend list", Arrays.asList("Check out your friends!"), 2 + 9, "friendlist");
	}
	
	public void onDisable(){
		
	}
	
	public static Plugin getInstance(){
		return instance;
	}
	
	public static String getString(String s){
        if (Plugin.instance.messageBundle == null) {
            return s;
        }
        String message = Plugin.instance.messageBundle.getString(s);
        if (StringUtils.isNullOrEmpty(message)) {
            return s;
        } else {
            return ChatColor.translateAlternateColorCodes('&', message.replace("%&", " "));
        }
	}
	
	public static void sendDebugMessage(String s){
		if(Config.isDebugMode()){
			System.out.print(s);
		}
	}
}
