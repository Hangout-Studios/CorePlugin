package com.hangout.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.player.HangoutPlayerManager;

public class BattleListener implements Listener {
	
	@EventHandler
	public void onPlayerDamageByPlayer(EntityDamageByEntityEvent e){
		if(!(e.getEntity() instanceof Player && e.getDamager() instanceof Player)){
			return;
		}
		
		Player hitP = (Player)e.getEntity();
		Player damagerP = (Player)e.getDamager();
		
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
}
