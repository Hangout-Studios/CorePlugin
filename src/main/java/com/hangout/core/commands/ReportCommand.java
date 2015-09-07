package com.hangout.core.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.hangout.core.Plugin;
import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.player.HangoutPlayerManager;
import com.hangout.core.player.PlayerRank;
import com.hangout.core.utils.database.Database;

public class ReportCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, final String[] args) {
		
		if(!(sender instanceof Player)) return false;
		final HangoutPlayer p = HangoutPlayerManager.getPlayer((Player)sender);
		
		if(args.length == 0 || args[0].equals("help")){
			p.getPlayer().sendMessage("/report bug <explain bug>");
			p.getPlayer().sendMessage("/report player <player name> <explain>");
			return true;
		}
		
		//report bug <report>
		if(args[0].equals("bug")){
			if(args.length < 2){
				p.getPlayer().sendMessage("Please explain the bug.");
				p.getPlayer().sendMessage("/report bug <explain bug>");
				return true;
			}
			
			String explaination = "";
			for(int i = 1; i < args.length; i++){
				explaination += args[i] + " ";
			}
			explaination = explaination.substring(0, explaination.length() - 1);
			
			Database.executeBugReport(p.getUUID(), explaination);
			
			p.getPlayer().sendMessage("Thank you for reporting! We will check it out as soon as we can!");
			
			HangoutPlayerManager.alertModerators(PlayerRank.ADMIN,
					""+ChatColor.RED + ChatColor.BOLD + "ALERT: " + 
					ChatColor.WHITE + p.getName() + " has reported a bug: " + ChatColor.ITALIC + explaination);
			
			return true;
		}
		
		//report player <player> <report>
		if(args[0].equals("player")){
			if(args.length < 3){
				p.getPlayer().sendMessage("/report player <player name> <explain>");
				return true;
			}
			
			Bukkit.getScheduler().runTaskAsynchronously(Plugin.getInstance(), new Runnable(){
				@Override
				public void run() {
					UUID reportedID = null;
					
					Player otherP = Bukkit.getPlayer(args[1]);
					if(otherP != null){
						reportedID = otherP.getUniqueId();
					}else{
						reportedID = HangoutPlayerManager.getUUID(args[1]);
					}					
					
					if(reportedID == null){
						p.getPlayer().sendMessage("Player is not found. If the player is offline, it must be the EXACT name.");
						return;
					}
					
					String explaination = "";
					for(int i = 2; i < args.length; i++){
						explaination += args[i] + " ";
					}
					explaination = explaination.substring(0, explaination.length() - 1);
					
					Database.executePlayerReport(p.getUUID(), reportedID, explaination);
					
					p.getPlayer().sendMessage("Thank you for reporting! We will check it out as soon as we can!");
					
					HangoutPlayerManager.alertModerators(PlayerRank.MODERATOR_TRIAL,
							""+ChatColor.RED + ChatColor.BOLD + " ALERT: " + 
							ChatColor.WHITE + p.getName() + " has reported player: " + ChatColor.ITALIC + args[1]);
				}
			});
			
			return true;
		}
		
		return false;
	}

}
