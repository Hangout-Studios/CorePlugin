package com.hangout.core.commands;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.hangout.core.Plugin;
import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.player.HangoutPlayerManager;
import com.hangout.core.player.PlayerRank;
import com.hangout.core.player.ViolationReport;

public class AdminCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, final String[] args) {
		
		if(!(sender instanceof Player)) return false;
		final HangoutPlayer p = HangoutPlayerManager.getPlayer((Player)sender);
		
		if(!p.getHighestRank().isRankOrHigher(PlayerRank.MODERATOR_TRIAL)) return true;
		
		//admin help
		if(args.length == 0 || args[0].equals("help") && p.getHighestRank().isRankOrHigher(PlayerRank.MODERATOR_TRIAL)){
			p.getPlayer().sendMessage("/admin getreport <playername>");
			p.getPlayer().sendMessage("/admin kick <playername> <reason>");
			p.getPlayer().sendMessage("/admin mute <playername> <time in minutes> <reason>");
			
			if(p.getHighestRank().isRankOrHigher(PlayerRank.ADMIN)){
				p.getPlayer().sendMessage("/admin ban <playername> <time in minutes> <reason>");
			}
			return true;
		}
		
		//admin ban <playername> <time in minutes> <reason>
		if(args[0].equalsIgnoreCase("ban") && p.getHighestRank().isRankOrHigher(PlayerRank.ADMIN)){
			if(args.length < 4){
				p.getPlayer().sendMessage("Please provide a reason.");
				p.getPlayer().sendMessage("/admin ban <playername> <time in minutes> <reason>");
				return true;
			}
			
			Player otherP = Bukkit.getPlayer(args[1]);
			if(otherP == null){
				p.getPlayer().sendMessage("That player is not found.");
				return true;
			}
			
			HangoutPlayer otherHP = HangoutPlayerManager.getPlayer(otherP);
			if(otherHP == null){
				p.getPlayer().sendMessage("That hangout player is not found.");
				return true;
			}
			
			Integer time = 0;
			if(StringUtils.isNumeric(args[2])){
				time = Integer.parseInt(args[2]);
			}else{
				p.getPlayer().sendMessage("No time found.");
				return true;
			}
			
			String reason = "";
			for(int i = 3; i < args.length; i++){
				reason += args[i] + " ";
			}
			reason = reason.substring(0, reason.length() - 1);
			
			otherHP.setBannedUntil(reason, time, p, true);
		}
		
		//admin kick <playername> <reason>
		if(args[0].equalsIgnoreCase("kick") && p.getHighestRank().isRankOrHigher(PlayerRank.MODERATOR_TRIAL)){
			if(args.length < 3){
				p.getPlayer().sendMessage("Please provide a reason.");
				p.getPlayer().sendMessage("/admin ban <playername> <time in minutes> <reason>");
				return true;
			}
			
			Player otherP = Bukkit.getPlayer(args[1]);
			if(otherP == null){
				p.getPlayer().sendMessage("That player is not found.");
				return true;
			}
			
			HangoutPlayer otherHP = HangoutPlayerManager.getPlayer(otherP);
			if(otherHP == null){
				p.getPlayer().sendMessage("That hangout player is not found.");
				return true;
			}
			
			String reason = "";
			for(int i = 2; i < args.length; i++){
				reason += args[i] + " ";
			}
			reason = reason.substring(0, reason.length() - 1);
			
			otherP.kickPlayer(reason);
			
			otherHP.kick(reason, p, true);
		}
		
		//admin mute <playername> <time in minutes> <reason>
		if(args[0].equalsIgnoreCase("mute") && p.getHighestRank().isRankOrHigher(PlayerRank.MODERATOR_TRIAL)){
			if(args.length < 4){
				p.getPlayer().sendMessage("Please provide a reason.");
				p.getPlayer().sendMessage("/admin ban <playername> <time in minutes> <reason>");
				return true;
			}
			
			Player otherP = Bukkit.getPlayer(args[1]);
			if(otherP == null){
				p.getPlayer().sendMessage("That player is not found.");
				return true;
			}
			
			HangoutPlayer otherHP = HangoutPlayerManager.getPlayer(otherP);
			if(otherHP == null){
				p.getPlayer().sendMessage("That hangout player is not found.");
				return true;
			}
			
			Integer time = 0;
			if(StringUtils.isNumeric(args[2])){
				time = Integer.parseInt(args[2]);
			}else{
				p.getPlayer().sendMessage("No time found.");
				return true;
			}
			
			String reason = "";
			for(int i = 3; i < args.length; i++){
				reason += args[i] + " ";
			}
			reason = reason.substring(0, reason.length() - 1);
			
			otherHP.setMutedUntil(reason, time, p, true);
		}
		
		//admin getreport <playername>
		if(args[0].equals("getreport")  && p.getHighestRank().isRankOrHigher(PlayerRank.MODERATOR_TRIAL)){
			
			if(args.length < 2){
				p.getPlayer().sendMessage("Please provide a player name.");
				p.getPlayer().sendMessage("/admin getreport <playername>");
				return true;
			}
			
			final Player otherP = Bukkit.getPlayer(args[1]);
			
			Bukkit.getScheduler().runTaskAsynchronously(Plugin.getInstance(), new Runnable(){
				@Override
				public void run() {
					UUID id = null;
					if(otherP == null){
						id = HangoutPlayerManager.getUUID(args[1]);
					}else{
						id = otherP.getUniqueId();
					}
					
					if(id == null){
						p.getPlayer().sendMessage("Player not found.");
						return;
					}else{
						p.getPlayer().sendMessage("Violation record of " + args[1]);
						for(String s :ViolationReport.getReport(id)){
							p.getPlayer().sendMessage(s);
						}
					}
				}
			});
			
			return true;
		}
		
		return false;
	}
}
