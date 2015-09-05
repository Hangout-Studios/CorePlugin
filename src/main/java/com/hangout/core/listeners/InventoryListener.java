package com.hangout.core.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import com.hangout.core.events.MenuCloseEvent;
import com.hangout.core.menu.MenuInventory;
import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.player.HangoutPlayerManager;
import com.hangout.core.utils.mc.DebugUtils;
import com.hangout.core.utils.mc.DebugUtils.DebugMode;

public class InventoryListener  implements Listener{
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e){
		HangoutPlayer p = HangoutPlayerManager.getPlayer((Player)e.getPlayer());
		MenuInventory menu = p.getOpenMenu();
		
		p.setInInventory(false);
		
		if(menu != null){			
			DebugUtils.sendDebugMessage(p.getName() + " closed menu " + menu.getTitle(), DebugMode.EXTENSIVE);
			
			Bukkit.getPluginManager().callEvent(new MenuCloseEvent(p, menu));
		}
	}
	
	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent e){
		HangoutPlayer p = HangoutPlayerManager.getPlayer((Player)e.getPlayer());
		p.setInInventory(true);
	}
}
