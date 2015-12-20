package com.hangout.core.listeners;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.hangout.core.Plugin;
import com.hangout.core.chat.ChatChannel;
import com.hangout.core.chat.ChatManager;
import com.hangout.core.events.CustomItemClickEvent;
import com.hangout.core.events.CustomItemUseEvent;
import com.hangout.core.events.MenuCloseEvent;
import com.hangout.core.events.MenuItemClickEvent;
import com.hangout.core.menu.MenuUtils;
import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.player.HangoutPlayerManager;
import com.hangout.core.reports.BugReport;
import com.hangout.core.reports.PlayerReport;
import com.hangout.core.reports.ReportDatabase;
import com.hangout.core.utils.database.Database;
import com.hangout.core.utils.mc.CommandPreparer;
import com.hangout.core.utils.mc.DebugUtils;
import com.hangout.core.utils.mc.DebugUtils.DebugMode;

public class MenuListener implements Listener {
	
	@EventHandler
	public void onCustomItemClick(CustomItemClickEvent e){
		if(e.getCustomItem().getTag().equals("main_menu")){
			MenuUtils.createMainMenu(e.getHangoutPlayer()).openMenu(e.getHangoutPlayer(), true);
			return;
		}
	}
	
	@EventHandler
	public void onCustomItemUse(CustomItemUseEvent e){
		if(e.getCustomItem().getTag().equals("main_menu")){
			MenuUtils.createMainMenu(e.getHangoutPlayer()).openMenu(e.getHangoutPlayer(), true);
			return;
		}
	}
	
	@EventHandler
	public void onMenuClick(MenuItemClickEvent e){
		HangoutPlayer p = e.getPlayer();
		
		if(e.getItem().getTag().equals("previous_menu")){
			if(p.hasLastMenu(2)){
				p.getLastMenu().openMenu(p, false);
			}
		}
		
		//Settings button
		if(e.getItem().getTag().equals("settings")){
			MenuUtils.createSettingsMenu(p).openMenu(p, true);
			return;
		}
		
		//Channel stuff
		if(e.getItem().getTag().startsWith("channel_")){
			String[] split = e.getItem().getTag().split("_");
			
			String command = split[1];
			ChatChannel channel = ChatManager.getChannel(split[2], split[3]);
			
			if(channel == null) return;
			
			if(command.equals("subscribe")){
				p.addSubscribedChannel(channel, true);
			}else if(command.equals("unsubscribe")){
				p.removeSubscribedChannel(channel, true);
			}else if(command.equals("setactive")){
				p.setChatChannel(channel, true);
			}
			
			if(command != "description"){
				MenuUtils.createSettingsMenu(p).openMenu(p, false);
			}
			return;
		}
		
		if(e.getItem().getTag().equals("pvp_enable")){
			p.setPvpEnabled(true);
			Database.executePvpFlagAction(p.getUUID(), true);
			MenuUtils.createSettingsMenu(p).openMenu(p, false);
			return;
		}
		
		if(e.getItem().getTag().equals("pvp_disable")){
			p.setPvpEnabled(false);
			Database.executePvpFlagAction(p.getUUID(), false);
			MenuUtils.createSettingsMenu(p).openMenu(p, false);
			return;
		}
		
		//Admin button
		if(e.getItem().getTag().equals("admin_tools")){
			MenuUtils.createAdminMenu(p).openMenu(p, true);
			return;
		}
		
		if(e.getItem().getTag().equals("bug_reports")){
			MenuUtils.createBugReportsMenu(p).openMenu(p, true);
			return;
		}
		
		if(e.getItem().getTag().equals("player_reports")){
			MenuUtils.createPlayerReportsMenu(p).openMenu(p, true);
			return;
		}
		
		if(e.getItem().getTag().startsWith("player_report_")){
			String[] split = e.getItem().getTag().split("_");
			int id = Integer.parseInt(split[2]);
			PlayerReport r = ReportDatabase.removePlayerReport(id);
			Database.finishReport(r.getID(), p.getUUID(), "player");
			
			p.getPlayer().sendMessage(""+ ChatColor.RED + ChatColor.BOLD + "You have checked off report #" + id);
			
			MenuUtils.createPlayerReportsMenu(p).openMenu(p, false);
			return;
		}
		
		if(e.getItem().getTag().startsWith("bug_report_")){
			String[] split = e.getItem().getTag().split("_");
			int id = Integer.parseInt(split[2]);
			BugReport r = ReportDatabase.removeBugReport(id);
			Database.finishReport(r.getID(), p.getUUID(), "bug");
			
			p.getPlayer().sendMessage(""+ ChatColor.RED + ChatColor.BOLD + "You have checked off bug #" + id);
			
			MenuUtils.createBugReportsMenu(p).openMenu(p, false);
			return;
		}
		
		if(e.getItem().getTag().equals("report_bug")){
			e.getPlayer().getPlayer().closeInventory();
			
			CommandPreparer c = e.getPlayer().createCommandPreparer("report_bug", true);
			c.append("report bug");
			
			e.getPlayer().getPlayer().sendMessage(ChatColor.ITALIC + "Please write your explaination of the bug in chat.");
		}
		
		if(e.getItem().getTag().equals("npc_options")){
			MenuUtils.createNpcOptionsMenu(p).openMenu(p, true);
		}
		
		if(e.getItem().getTag().equals("npc_create")){
			MenuUtils.createNpcCreateMenu(p).openMenu(p, true);
		}
		
		if(e.getItem().getTag().equals("npc_edit")){
			MenuUtils.createNpcEditMenu(p).openMenu(p, true);
		}
	}
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void onMenuClose(MenuCloseEvent e){
		final HangoutPlayer p = e.getPlayer();
		Bukkit.getScheduler().runTaskLater(Plugin.getInstance(), new Runnable(){

			@Override
			public void run() {
				if(p.isInInventory()){
					DebugUtils.sendDebugMessage("Player opened another menu", DebugMode.EXTENSIVE);
				}else{
					p.clearMenuStack();
					DebugUtils.sendDebugMessage("Player closed all menus", DebugMode.EXTENSIVE);
				}
			}
			
		}, 1L);
	}
}
