package com.hangout.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.hangout.core.events.ChatPlayerClickEvent;
import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.player.HangoutPlayerManager;
import com.hangout.core.utils.mc.CommandPreparer;
import com.hangout.core.utils.mc.DebugUtils;
import com.hangout.core.utils.mc.DebugUtils.DebugMode;

public class TextCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		
		//text player <clicked player> <destination player>
		if(args.length == 3 && args[0].equals("player")){
			Player clicked = Bukkit.getPlayer(args[1]);
			Player interact = Bukkit.getPlayer(args[2]);
			
			if(!clicked.isOnline()){
				interact.sendMessage("That player isn't online anymore.");
				return true;
			}
			
			Bukkit.getPluginManager().callEvent(
					new ChatPlayerClickEvent(HangoutPlayerManager.getPlayer(clicked), HangoutPlayerManager.getPlayer(interact)));
			DebugUtils.sendDebugMessage(interact.getName() + " clicked " + clicked.getName() + " in chat", DebugMode.COMPLETE);
			return true;
		}
		
		if(args[0].equals("execute") && args[1].equals("prepared")){
			HangoutPlayer p = HangoutPlayerManager.getPlayer((Player)sender);
			CommandPreparer c = p.getCommandPreparer(args[2]);
			if(c != null){
				c.execute();
			}
			return true;
		}
		
		return true;
	}

}
