package com.hangout.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import com.hangout.core.item.CustomItem;
import com.hangout.core.item.CustomItemManager;
import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.player.HangoutPlayerManager;

public class BattleListener implements Listener {
	
	@EventHandler
	public void onPlayerDamageByPlayer(EntityDamageByEntityEvent e){
		
		Player hitP = null;
		Player damagerP = null;
		
		if(e.getEntity() instanceof Player){
			hitP = (Player)e.getEntity();
		}
		
		if(e.getEntity() instanceof Player){
			damagerP = (Player)e.getDamager();
		}
		
		if(e.getEntity() instanceof Projectile){
			Projectile projectile = (Projectile)e.getEntity();
			if(projectile.getShooter() instanceof Player){
				damagerP = (Player)projectile.getShooter();
			}
		}
		
		if(hitP == null || damagerP == null){
			return;
		}

		HangoutPlayer hitHP = HangoutPlayerManager.getPlayer(hitP);
		HangoutPlayer damagerHP = HangoutPlayerManager.getPlayer(damagerP);
		
		if(hitHP == null || damagerHP == null){
			return;
		}
		
		if(!hitHP.isPvpEnabled() || !damagerHP.isPvpEnabled()){
			e.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e){
		for(int i = e.getDrops().size() - 1; i > 0; i--){
			ItemStack item = e.getDrops().get(i);
			CustomItem customItem = CustomItemManager.getItem(item);
			if(customItem != null && !customItem.allowDropOnDeath()){
				e.getDrops().remove(item);
			}
		}
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e){
		HangoutPlayerManager.getPlayer(e.getPlayer()).reset();
	}
}
