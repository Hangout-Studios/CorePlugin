package com.hangout.core.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.hangout.core.Plugin;
import com.hangout.core.events.MenuClickEvent;
import com.hangout.core.events.MenuCloseEvent;
import com.hangout.core.menu.MenuInventory;
import com.hangout.core.menu.MenuItem;
import com.hangout.core.menu.MenuManager;
import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.player.HangoutPlayerManager;

public class MenuListener implements Listener {
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e){
		HangoutPlayer p = HangoutPlayerManager.getPlayer((Player)e.getPlayer());
		MenuInventory menu = p.getOpenMenu();
		
		if(menu != null){
			p.setOpenMenu(null);
			
			if(menu.isTemporary()){
				MenuManager.removeMenu(menu.getTag());
			}
			
			Plugin.sendDebugMessage(p.getName() + " closed menu " + menu.getTitle());
			
			Bukkit.getPluginManager().callEvent(new MenuCloseEvent(p, menu));
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e){
		if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)
            return;
		
		HangoutPlayer p = HangoutPlayerManager.getPlayer((Player)e.getWhoClicked());
		
		ItemStack item = e.getCurrentItem();
		
		for(MenuInventory menu : MenuManager.getMenus()){
			if(menu.getItemStack().equals(item)){
				menu.openMenu(p);
				e.setCancelled(true);
				return;
			}
		}
		
		if(p.isInMenu()){
			for(MenuItem menuItem : p.getOpenMenu().getMenuItems()){
				if(menuItem.getItemStack().equals(item)){
					Bukkit.getPluginManager().callEvent(new MenuClickEvent(p, menuItem));
					Plugin.sendDebugMessage(p.getName() + " has clicked item in menu " + menuItem.getName());
					e.setCancelled(true);
					return;
				}
			}
		}
	}
	
	@EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
        HangoutPlayer hp = HangoutPlayerManager.getPlayer(p.getUniqueId());
        
        //Only work with right clicks
        if(e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        
      //Check if the player has an item in his hand
        if (!e.hasItem())
            return;
        if (e.getItem().getType() == Material.AIR)
            return;
        
        for(MenuInventory menu : MenuManager.getMenus()){
			if(menu.getItemStack().equals(e.getItem())){
				menu.openMenu(hp);
				e.setCancelled(true);
				return;
			}
		}
	}
}
