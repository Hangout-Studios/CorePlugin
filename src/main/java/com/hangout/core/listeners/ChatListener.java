package com.hangout.core.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.hangout.core.chat.ChatManager;
import com.hangout.core.events.CommandPreparerEvent;
import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.player.HangoutPlayerManager;
import com.hangout.core.utils.mc.CommandPreparer;

public class ChatListener implements Listener {
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e){
		e.setCancelled(true);
		
		HangoutPlayer p = HangoutPlayerManager.getPlayer(e.getPlayer());
		if(p.getListeningCommand() != null){
			Bukkit.getPluginManager().callEvent(new CommandPreparerEvent(p, p.getListeningCommand(), e.getMessage()));
			return;
		}
		
		if(!p.getChatChannel().getPlugin().equals("core")) return;
		
		String message = e.getMessage();
		ChatManager.sendMessage(p, message, p.getChatChannel(), null);
	}
	
	@EventHandler
	public void onCommandPreparer(CommandPreparerEvent e){
		CommandPreparer command = e.getCommand();
		String tag = command.getTag();
		int stage = command.getStage();
		String message = e.getMessage();
		
		if(tag.equals("report_player")){
			command.append(message);
			command.execute();
			e.getPlayer().clearCommandPreparer(command.getTag());
			return;
		}
		
		if(tag.equals("report_bug")){
			command.append(message);
			command.execute();
			e.getPlayer().clearCommandPreparer(command.getTag());
			return;
		}
	}
}