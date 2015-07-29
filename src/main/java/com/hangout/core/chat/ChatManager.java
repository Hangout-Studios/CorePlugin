package com.hangout.core.chat;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.player.HangoutPlayerManager;

public class ChatManager {
	
	public static void sendMessage(HangoutPlayer p, String message, ChatChannel channel){
		
		if(p.isMuted()){
			p.getPlayer().sendMessage(ChatColor.RED + "You are muted. Time left: " + p.getViolationReport().getTimeLeftMute());
			return;
		}
		
		List<HangoutPlayer> players = new ArrayList<HangoutPlayer>();
		
		switch(channel){
		case GLOBAL:
		case LOCAL:
			for(Player online : Bukkit.getOnlinePlayers()){
				HangoutPlayer onlineHP = HangoutPlayerManager.getPlayer(online);
				players.add(onlineHP);
			}
			break;
		case SAY:
			Vector pV = p.getPlayer().getLocation().toVector();
			for(Player online : Bukkit.getOnlinePlayers()){
				if(online.getLocation().toVector().distance(pV) <= 15){
					players.add(HangoutPlayerManager.getPlayer(online));
				}
			}
			break;
		default:
			break;
			
		}
		
		for(HangoutPlayer otherP : players){
			if(otherP.getMutedPlayers().contains(p.getUUID())) continue;
			
			p.getClickableName(otherP, true)
				.then(" > ")
					.color(ChatColor.WHITE)
					.style(ChatColor.BOLD)
				.then(message)
					.color(ChatColor.GRAY)
			.send(otherP.getPlayer());
		}
	}
}
