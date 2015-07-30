package com.hangout.core.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import com.hangout.core.Plugin;
import com.hangout.core.events.MenuCloseEvent;
import com.hangout.core.menu.MenuInventory;
import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.player.HangoutPlayerManager;

public class InventoryListener  implements Listener{
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e){
		HangoutPlayer p = HangoutPlayerManager.getPlayer((Player)e.getPlayer());
		MenuInventory menu = p.getOpenMenu();
		
		if(menu != null){
			p.setOpenMenu(null);
			
			Plugin.sendDebugMessage(p.getName() + " closed menu " + menu.getTitle());
			
			Bukkit.getPluginManager().callEvent(new MenuCloseEvent(p, menu));
		}
	}
}
