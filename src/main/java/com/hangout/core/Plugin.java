package com.hangout.core;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.hangout.core.chat.ChatChannel.ChatChannelType;
import com.hangout.core.chat.ChatManager;
import com.hangout.core.commands.AdminCommand;
import com.hangout.core.commands.ChannelCommand;
import com.hangout.core.commands.MuteCommand;
import com.hangout.core.commands.ReportCommand;
import com.hangout.core.commands.TextCommand;
import com.hangout.core.item.CustomItem;
import com.hangout.core.item.CustomItemManager;
import com.hangout.core.item.CustomItemRarity;
import com.hangout.core.listeners.BattleListener;
import com.hangout.core.listeners.ReplaceBlockListener;
import com.hangout.core.listeners.ChatListener;
import com.hangout.core.listeners.FurnaceListener;
import com.hangout.core.listeners.InventoryListener;
import com.hangout.core.listeners.ItemListener;
import com.hangout.core.listeners.MenuListener;
import com.hangout.core.listeners.PlayerListener;
import com.hangout.core.player.HangoutPlayerManager;
import com.hangout.core.utils.database.Database;
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
		this.getServer().getPluginManager().registerEvents(new InventoryListener(), this);
		this.getServer().getPluginManager().registerEvents(new FurnaceListener(), this);
		this.getServer().getPluginManager().registerEvents(new ReplaceBlockListener(), this);
		
		this.getCommand("text").setExecutor(new TextCommand());
		this.getCommand("admin").setExecutor(new AdminCommand());
		this.getCommand("report").setExecutor(new ReportCommand());
		this.getCommand("mute").setExecutor(new MuteCommand());
		this.getCommand("unmute").setExecutor(new MuteCommand());
		this.getCommand("channel").setExecutor(new ChannelCommand());
		
		//Add custom loading properties
		Database.addCustomPlayerProperty("name", PropertyTypes.STRING);
		Database.addCustomPlayerProperty("last_online", PropertyTypes.TIMESTAMP);
		
		//Create some custom items
		ItemStack menuItem = ItemUtils.createItem(Material.PAPER, "Open interface", Arrays.asList("Right click to use"));
		CustomItemManager.addItem(new CustomItem(menuItem, "main_menu", true, true, false, false, false, CustomItemRarity.COMMON));
		HangoutPlayerManager.addStandardLoadoutItem(menuItem, 8);
		
		//Add chat channels
		ChatManager.createChannel("core", "local", ChatColor.GRAY + "Local", Arrays.asList("Local chat only works in", "a certain around the area,", "like it would in real life."), ChatChannelType.LOCAL, Material.DIRT, true, true);
		ChatManager.createChannel("core", "area", ChatColor.RED + "Area", Arrays.asList("Area chat works in a", "declared area, such as", "a city or wilderness zone."), ChatChannelType.REGION, Material.GRASS, true, true);
		ChatManager.createChannel("core", "world", ChatColor.GOLD + "World", Arrays.asList("World chat is server-wide."), ChatChannelType.GLOBAL, Material.NETHER_STAR, true, true);
		ChatManager.createChannel("core", "whisper", ChatColor.LIGHT_PURPLE + "Whisper", Arrays.asList("Personal messages from", "other players."), ChatChannelType.SERVER_WIDE, Material.SKULL_ITEM, false, true);
		ChatManager.createChannel("core", "system", ""+ ChatColor.BLUE + ChatColor.BOLD + "SERVER", Arrays.asList("System messages"), ChatChannelType.SERVER_WIDE, Material.SKULL_ITEM, false, false);
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
}
