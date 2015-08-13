package com.hangout.core.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

import com.hangout.core.item.CustomItem;
import com.hangout.core.item.CustomItemManager;

public class ReplaceBlockListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent e){
		Block b = e.getBlock();
		
		e.setCancelled(true);
		
		for(ItemStack item : b.getDrops(e.getPlayer().getItemInHand())){
			CustomItem i = CustomItemManager.getDefaultItem(item.getType());
			if(i == null) continue;
			ItemStack newItem = i.getItemStack();
			newItem.setAmount(item.getAmount());
			b.getLocation().getWorld().dropItemNaturally(b.getLocation(), newItem);
		}
		
		b.setType(Material.AIR);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPrepareItemCraft(PrepareItemCraftEvent e){
		if(e.isRepair()) return;
		ItemStack item = e.getInventory().getResult();
		CustomItem i = CustomItemManager.getDefaultItem(item.getType());
		if(i == null) return;
		ItemStack newItem = i.getItemStack();
		newItem.setAmount(item.getAmount());
		e.getInventory().setResult(newItem);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onFurnaceSmelt(FurnaceSmeltEvent e){		
		ItemStack item = e.getResult();
		CustomItem i = CustomItemManager.getDefaultItem(item.getType());
		if(i == null) return;
		ItemStack newItem = i.getItemStack();
		newItem.setAmount(item.getAmount());
		e.setResult(newItem);
	}
}
