package com.hangout.core.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.hangout.core.chat.ChatChannel;
import com.hangout.core.chat.ChatManager;
import com.hangout.core.events.CustomItemClickEvent;
import com.hangout.core.events.CustomItemUseEvent;
import com.hangout.core.events.MenuItemClickEvent;
import com.hangout.core.menu.MenuUtils;
import com.hangout.core.player.HangoutPlayer;

public class MenuListener implements Listener {
	
	@EventHandler
	public void onCustomItemClick(CustomItemClickEvent e){
		if(e.getCustomItem().getTag().equals("main_menu")){
			MenuUtils.createMainMenu(e.getHangoutPlayer()).openMenu(e.getHangoutPlayer());
			return;
		}
	}
	
	@EventHandler
	public void onCustomItemUse(CustomItemUseEvent e){
		if(e.getCustomItem().getTag().equals("main_menu")){
			MenuUtils.createMainMenu(e.getHangoutPlayer()).openMenu(e.getHangoutPlayer());
			return;
		}
	}
	
	@EventHandler
	public void onMenuClick(MenuItemClickEvent e){
		
		//Settings button
		HangoutPlayer p = e.getPlayer();
		if(e.getItem().getTag().equals("settings")){
			MenuUtils.createSettingsMenu(p).openMenu(p);
		}
		
		//Channel stuff
		if(e.getItem().getTag().startsWith("channel_")){
			String[] split = e.getItem().getTag().split("_");
			
			String command = split[1];
			ChatChannel channel = ChatManager.getChannel(split[2]);
			
			if(channel == null) return;
			
			if(command.equals("subscribe")){
				p.addSubscribedChannel(channel);
				p.getPlayer().sendMessage("You started listening to channel: " + channel.getDisplayName());
			}else if(command.equals("unsubscribe")){
				p.removeSubscribedChannel(channel);
				p.getPlayer().sendMessage("You stopped listening to channel: " + channel.getDisplayName());
			}else if(command.equals("setactive")){
				p.setChatChannel(channel);
				p.getPlayer().sendMessage("You are now chatting in channel: " + channel.getDisplayName());
			}
			
			if(command != "description"){
				MenuUtils.createSettingsMenu(p).openMenu(p);
			}
			return;
		}
		
		if(e.getItem().getTag().startsWith("pvp_enable")){
			p.setPvpEnabled(true);
			p.getPlayer().sendMessage("You have enabled PvP!");
			MenuUtils.createSettingsMenu(p).openMenu(p);
		}
		
		if(e.getItem().getTag().startsWith("pvp_disable")){
			p.setPvpEnabled(false);
			p.getPlayer().sendMessage("You have disabled PvP!");
			MenuUtils.createSettingsMenu(p).openMenu(p);
		}
	}
}
