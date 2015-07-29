package com.hangout.core.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.hangout.core.Plugin;
import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.player.HangoutPlayerManager;

public class MuteCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, final String[] args) {
		
		if(!(sender instanceof Player)) return false;
		final HangoutPlayer p = HangoutPlayerManager.getPlayer((Player)sender);
		
		if(label.equalsIgnoreCase("mute")){
			if(args.length == 0 || args[0].equals("help")){
				p.getPlayer().sendMessage("/mute <player name>");
				return true;
			}
			
			Bukkit.getScheduler().runTaskAsynchronously(Plugin.getInstance(), new Runnable(){
				@Override
				public void run() {
					UUID mutedID = null;
					
					Player otherP = Bukkit.getPlayer(args[0]);
					if(otherP != null){
						mutedID = otherP.getUniqueId();
					}else{
						mutedID = HangoutPlayerManager.getUUID(args[0]);
					}					
					
					if(mutedID == null){
						p.getPlayer().sendMessage("Player is not found. If the player is offline, it must be the EXACT name.");
						return;
					}
					
					p.addMutedPlayer(mutedID, true);
					
					p.getPlayer().sendMessage("You have muted " + (otherP != null ? otherP.getName() : args[0]));
				}
			});
			return true;
		}
		
		if(label.equalsIgnoreCase("unmute")){
			if(args.length == 0 || args[0].equals("help")){
				p.getPlayer().sendMessage("/unmute <player name>");
				return true;
			}
			
			Bukkit.getScheduler().runTaskAsynchronously(Plugin.getInstance(), new Runnable(){
				@Override
				public void run() {
					UUID mutedID = null;
					
					Player otherP = Bukkit.getPlayer(args[0]);
					if(otherP != null){
						mutedID = otherP.getUniqueId();
					}else{
						mutedID = HangoutPlayerManager.getUUID(args[0]);
					}					
					
					if(mutedID == null){
						p.getPlayer().sendMessage("Player is not found. If the player is offline, it must be the EXACT name.");
						return;
					}
					
					p.removeMutedPlayer(mutedID, true);
					
					p.getPlayer().sendMessage("You have unmuted " + (otherP != null ? otherP.getName() : args[0]));
				}
			});
			return true;
		}
		return false;
	}

}
