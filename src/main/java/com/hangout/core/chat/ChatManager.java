package com.hangout.core.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.hangout.core.chat.ChatChannel.ChatChannelType;
import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.player.HangoutPlayerManager;

public class ChatManager {
	
	private static HashMap<String, ChatChannel> channels = new HashMap<String, ChatChannel>();
	
	public static ChatChannel getChannel(String s){
		if(channels.containsKey(s)){
			return channels.get(s);
		}
		return null;
	}
	
	public static ChatChannel createChannel(String tag, String displayName, List<String> description,
			ChatChannelType type, Material mat, boolean isSelectable, boolean isMutable){
		ChatChannel c = new ChatChannel(tag, displayName, description, type, mat, isSelectable, isMutable);
		channels.put(tag, c);		
		return c;
	}
	
	public static List<ChatChannel> getChannels(){
		return new ArrayList<ChatChannel>(channels.values());
	}
	
	public static void sendMessage(HangoutPlayer p, String message, ChatChannel channel, List<HangoutPlayer> receivers){
		
		if(p.isMuted()){
			p.getPlayer().sendMessage(ChatColor.RED + "You are muted. Time left: " + p.getViolationReport().getTimeLeftMute());
			return;
		}
		
		List<HangoutPlayer> players = new ArrayList<HangoutPlayer>();
		
		if(receivers == null || receivers.isEmpty()){
			switch(channel.getType()){
			case SERVER_WIDE:
			case GLOBAL:
			case REGION:
				for(Player online : Bukkit.getOnlinePlayers()){
					HangoutPlayer onlineHP = HangoutPlayerManager.getPlayer(online);
					players.add(onlineHP);
				}
				break;
				
			case LOCAL:
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
		}else{
			players = receivers;
		}
		
		for(HangoutPlayer otherP : players){
			if(otherP.getMutedPlayers().contains(p.getUUID()) || !otherP.isSubscribedToChannel(channel)) continue;
			
			p.getClickableName(otherP, true, false)
				.then(" > ")
					.color(ChatColor.WHITE)
					.style(ChatColor.BOLD)
				.then(message)
					.color(ChatColor.GRAY)
			.send(otherP.getPlayer());
		}
	}
}
