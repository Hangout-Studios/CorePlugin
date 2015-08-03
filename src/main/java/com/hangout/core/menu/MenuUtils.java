package com.hangout.core.menu;

import java.util.Arrays;
import java.util.List;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.hangout.core.chat.ChatChannel;
import com.hangout.core.chat.ChatManager;
import com.hangout.core.events.MenuCreateEvent;
import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.utils.mc.DebugUtils;
import com.hangout.core.utils.mc.DebugUtils.DebugMode;

public class MenuUtils {
	
	public static MenuInventory createMenu(String title, String menuTag, HangoutPlayer p){
		MenuInventory menu = new MenuInventory(Bukkit.createInventory(null, 3 * 9, title), menuTag, p);
		Bukkit.getPluginManager().callEvent(new MenuCreateEvent(p, menu));
		DebugUtils.sendDebugMessage("Created custom menu: " + title, DebugMode.DEBUG);
		return menu;
	}
	
	public static MenuItem createMenuItem(MenuInventory menu, Material mat, String itemName, List<String> description, int menuPosition, String itemTag){
		MenuItem menuItem = new MenuItem(mat, ChatColor.WHITE + itemName, description, itemTag);
		menu.addMenuItem(menuItem, menuPosition);
		
		DebugUtils.sendDebugMessage("Put item " + itemTag + " at " + menuPosition, DebugMode.EXTENSIVE);
		return menuItem;
	}
	
	public static MenuItem createMenuItem(MenuInventory menu, ItemStack item, int menuPosition, String itemTag){
		MenuItem menuItem = new MenuItem(item, itemTag);
		menu.addMenuItem(menuItem, menuPosition);
		
		DebugUtils.sendDebugMessage("Put item " + itemTag + " at " + menuPosition, DebugMode.EXTENSIVE);
		return menuItem;
	}
	
	public static MenuInventory createMainMenu(HangoutPlayer p){
		MenuInventory inventory = createMenu("Main menu", "main_menu", p);
		createMenuItem(inventory, Material.SKULL_ITEM, "Friend list", Arrays.asList("Check out your friends!"), 6 + 8, "friend_list");
		createMenuItem(inventory, Material.BED, "Settings", Arrays.asList("Find various settings here!"), 8 + 8, "settings");
		return inventory;
	}
	
	public static MenuInventory createSettingsMenu(HangoutPlayer p){
		MenuInventory inventory = createMenu("Settings", "settings_menu", p);
		
		int channelCount = 0;
		for(ChatChannel c : ChatManager.getChannels()){
			if(!c.isSelectable() && !c.isMutable()) continue;
			
			boolean isSubscribed = p.isSubscribedToChannel(c);
			boolean isActive = p.getChatChannel() == c;
			
			createMenuItem(inventory, c.getMaterial(), c.getDisplayName(), c.getDescription(), 0 + channelCount, "c_description_" + c.getDisplayName());
			if(c.isMutable()){
				if(isSubscribed){
					createMenuItem(inventory, Material.DETECTOR_RAIL, "Channel active - " + c.getDisplayName(), Arrays.asList("Click to stop listening."), 9 + channelCount, "channel_unsubscribe_" + c.getFullTag());
				}else{
					createMenuItem(inventory, Material.BARRIER, "Channel disabled - " + c.getDisplayName(), Arrays.asList("Click to listen."), 9 + channelCount, "channel_subscribe_" + c.getFullTag());
				}
			}else{
				createMenuItem(inventory, Material.BARRIER, "Blocked - " + c.getDisplayName(), Arrays.asList("You cannot stop listening", "to this channel."), 9 + channelCount, "channel_blocked_" + c.getFullTag());
			}
			
			if(c.isSelectable()){
				if(isActive){
					createMenuItem(inventory, Material.DETECTOR_RAIL, "Current channel - " + c.getDisplayName(), Arrays.asList("This is the channel you", "talk to when you chat."), 18 + channelCount, "channel_isactive_" + c.getFullTag());
				}else{
					createMenuItem(inventory, Material.BARRIER, "Not active - " + c.getDisplayName(), Arrays.asList("Click to make this the", "channel you chat in."), 18 + channelCount, "channel_setactive_" + c.getFullTag());
				}
			}else{
				createMenuItem(inventory, Material.BARRIER, "Blocked - " + c.getDisplayName(), Arrays.asList("You cannot select this", "channel as active channel."), 18 + channelCount, "channel_blocked_" + c.getFullTag());
			}
			
			channelCount++;
		}
		
		if(p.isPvpEnabled()){
			createMenuItem(inventory, Material.SKULL_ITEM, "Disable PvP", Arrays.asList("Disable PvP with other players.", "Disabling PvP takes", "5 minutes without combat!"), 7 + 9, "pvp_disable");
		}else{
			createMenuItem(inventory, Material.CAKE, "Enable PvP", Arrays.asList("Enable PvP with other players.", "Warning: disabling PvP takes", "5 minutes without combat!"), 7 + 9, "pvp_enable");
		}
		return inventory;
	}
}
