package com.hangout.core.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.FurnaceInventory;

import com.hangout.core.utils.mc.FurnaceManager;

public class FurnaceListener implements Listener {
	
	@EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
        
        //Furnace opening
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.FURNACE){
        	Block b = e.getClickedBlock();
        	
        	if(FurnaceManager.isClaimed(b) && !FurnaceManager.isClaimedBy(b, p.getUniqueId())){
        		p.sendMessage("That furnace is already in use!");
        		e.setCancelled(true);
        		return;
        	}else{
        		FurnaceManager.claimFurnace(b, p.getUniqueId());
        		FurnaceManager.openFurnace(p.getUniqueId(), b);
        	}
        }
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e){
		Player p = (Player)e.getPlayer();
		
		if(e.getInventory() instanceof FurnaceInventory){
			Block b = FurnaceManager.closeFurnace(p.getUniqueId());
			FurnaceInventory inv = (FurnaceInventory)e.getInventory();
			
			if(inv.getSmelting() == null && inv.getResult() == null){
				FurnaceManager.removeFurnace(b);
			}
		}
	}
}
