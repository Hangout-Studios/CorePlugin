package com.hangout.core.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.hangout.core.Plugin;
import com.hangout.core.events.CustomItemUseEvent;
import com.hangout.core.item.CustomItem;
import com.hangout.core.item.CustomItemManager;
import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.player.HangoutPlayerManager;

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
        if(cm != null){
        	Bukkit.getPluginManager().callEvent(new CustomItemUseEvent(cm, hp));
        	Plugin.sendDebugMessage(hp.getDisplayName() + " used custom item " + cm.getName());
        }
	}
}
