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
		HangoutPlayer p = HangoutPlayerManager.getPlayer(e.getPlayer());
		String message = e.getMessage();
		ChatManager.sendMessage(p, message, p.getChatChannel(), null);
		e.setCancelled(true);
	}
}
