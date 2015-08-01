package com.hangout.core.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.hangout.core.events.CustomItemClickEvent;
import com.hangout.core.events.CustomItemUseEvent;
import com.hangout.core.events.MenuItemClickEvent;
import com.hangout.core.item.CustomItem;
import com.hangout.core.item.CustomItemManager;
import com.hangout.core.menu.MenuItem;
import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.player.HangoutPlayerManager;
import com.hangout.core.utils.mc.DebugUtils;
import com.hangout.core.utils.mc.DebugUtils.DebugMode;

public class ItemListener implements Listener {
	
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
        if(!e.getItem().hasItemMeta()) return;
        
        CustomItem cm = CustomItemManager.getItem(e.getItem().getItemMeta().getDisplayName());
        if(cm != null && cm.allowRightClick()){
        	Bukkit.getPluginManager().callEvent(new CustomItemUseEvent(cm, hp));
        	DebugUtils.sendDebugMessage(hp.getDisplayName() + " used custom item " + cm.getName(), DebugMode.DEBUG);
        }
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e){
		if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)
            return;
		
		HangoutPlayer p = HangoutPlayerManager.getPlayer((Player)e.getWhoClicked());
		ItemStack item = e.getCurrentItem();
		
		CustomItem ci = CustomItemManager.getItem(item.getItemMeta().getDisplayName());
		if(ci != null){
			Bukkit.getPluginManager().callEvent(new CustomItemClickEvent(ci, p));
			DebugUtils.sendDebugMessage(p.getName() + " has clicked custom item in inventory " + ci.getName(), DebugMode.EXTENSIVE);
		}
		
		if(p.isInMenu()){
			for(MenuItem menuItem : p.getOpenMenu().getMenuItems()){
				if(menuItem.getItemStack().equals(item)){
					Bukkit.getPluginManager().callEvent(new MenuItemClickEvent(p, menuItem));
					DebugUtils.sendDebugMessage(p.getName() + " has clicked item in menu " + menuItem.getName(), DebugMode.EXTENSIVE);
					e.setCancelled(true);
					return;
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent e){
		ItemStack item = e.getItemDrop().getItemStack();
		
		CustomItem ci = CustomItemManager.getItem(item.getItemMeta().getDisplayName());
		if(ci != null && !ci.allowDrop()){
			e.setCancelled(true);
		}
	}
}
