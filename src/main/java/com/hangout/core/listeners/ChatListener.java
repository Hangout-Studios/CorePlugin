package com.hangout.core.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.hangout.core.chat.ChatManager;
import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.player.HangoutPlayerManager;

public class ChatListener implements Listener {
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e){
		e.setCancelled(true);
		
		HangoutPlayer p = HangoutPlayerManager.getPlayer(e.getPlayer());
		if(!p.getChatChannel().getPlugin().equals("core")) return;
		
		String message = e.getMessage();
		ChatManager.sendMessage(p, message, p.getChatChannel(), null);
	}
}