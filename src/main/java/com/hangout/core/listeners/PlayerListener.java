package com.hangout.core.listeners;

import java.sql.Timestamp;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.joda.time.DateTime;

import com.hangout.core.events.PlayerDataReleaseEvent;
import com.hangout.core.events.PlayerJoinCompleteEvent;
import com.hangout.core.events.PlayerPostLoadEvent;
import com.hangout.core.events.PlayerPreSaveEvent;
import com.hangout.core.events.PlayerQuitCompleteEvent;
import com.hangout.core.player.CommonPlayerManager;
import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.player.HangoutPlayer.PlayerState;
import com.hangout.core.player.HangoutPlayerManager;
import com.hangout.core.utils.database.Database;
import com.hangout.core.utils.hologram.HologramManager;
import com.hangout.core.utils.mc.DebugUtils;
import com.hangout.core.utils.mc.DebugUtils.DebugMode;

public class PlayerListener implements Listener {
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerPreLoginEvent(final AsyncPlayerPreLoginEvent e){		
		UUID playerID = e.getUniqueId();
		String playerName = e.getName();
		HangoutPlayer hp = HangoutPlayerManager.getPlayer(playerID);
		
		if(hp == null){
			hp = Database.loadPlayer(playerID, playerName);
		}
		
		//We don't have to wait for this, since it's loaded when the player is created
		if(hp.isBanned()){
			e.disallow(Result.KICK_BANNED, "You are banned. Time left: " + hp.getViolationReport().getTimeLeftBanned());
			DebugUtils.sendDebugMessage("Player " + playerName + " tried logging on, but is banned", DebugMode.INFO);
			HangoutPlayerManager.removePlayer(hp);
			return;
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		e.setJoinMessage(null);
		
		HangoutPlayer hp = HangoutPlayerManager.getPlayer(e.getPlayer());
		
		hp.setPlayer(e.getPlayer());
		hp.setPlayerState(PlayerState.COMPLETED);
		DebugUtils.sendDebugMessage("Player logged in and linked: " + hp.getName(), DebugMode.DEBUG);						
		Bukkit.getServer().getPluginManager().callEvent(new PlayerJoinCompleteEvent(hp));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoinComplete(PlayerJoinCompleteEvent e){
		//e.getPlayer().reset();
		
		for(Player p : Bukkit.getOnlinePlayers()){
			HangoutPlayer otherP = HangoutPlayerManager.getPlayer(p);
			
			e.getPlayer().getClickableName(otherP, false, false)
				.then(" has entered the realm.")
			.send(otherP.getPlayer());
		}
	}
	
	@EventHandler
	public void onPlayerPostLoad(PlayerPostLoadEvent e){
		e.getPlayer().setLastOnline(new DateTime((Timestamp)e.getProperty("last_online")));
		CommonPlayerManager.addPlayer(e.getPlayer().getUUID(), "Hangout", e.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerQuit(PlayerQuitEvent e){
		e.setQuitMessage(null);
		
		HangoutPlayer p = HangoutPlayerManager.getPlayer(e.getPlayer());
		if(p == null){
			DebugUtils.sendDebugMessage("Tried to save player " + e.getPlayer().getName() + " but hangoutplayer wasn't found.", DebugMode.WARNING);
			return;
		}
		
		p.setPlayerState(PlayerState.DISCONNECTING);
		
		//Get values to save from other plugins
		PlayerPreSaveEvent save = new PlayerPreSaveEvent(p);
		Bukkit.getPluginManager().callEvent(save);
		Database.savePlayer(save);
		
		HologramManager.clearPlayer(p.getUUID());
		
		p.attemptRemove();
		
		Bukkit.getServer().getPluginManager().callEvent(new PlayerQuitCompleteEvent(p));
	}
	
	@EventHandler
	public void onPlayerPreSave(PlayerPreSaveEvent e){
		e.savePrimaryProperty("uuid", e.getPlayer().getUUID().toString());
		e.saveSecondaryProperty("name", e.getPlayer().getPlayer().getName());
		e.saveSecondaryProperty("last_online", new Timestamp(DateTime.now().getMillis()));
	}
	
	@EventHandler
	public void onPlayerDataRelease(PlayerDataReleaseEvent e){
		
		//Clear player
		CommonPlayerManager.removePlayer(e.getUUID());			
		HangoutPlayerManager.removePlayer(e.getPlayer());
	}
}
