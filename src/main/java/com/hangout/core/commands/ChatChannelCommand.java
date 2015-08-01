package com.hangout.core.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.hangout.core.chat.ChatChannel;
import com.hangout.core.chat.ChatManager;
import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.player.HangoutPlayerManager;

public class ChatChannelCommand  implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		
		if(!(sender instanceof Player)) return false;
		HangoutPlayer p = HangoutPlayerManager.getPlayer((Player)sender);
		
		String channelTag = args[0].toLowerCase();
		ChatChannel channel = ChatManager.getChannel(channelTag);
		if(channel == null){
			for(ChatChannel c : ChatManager.getChannels()){
				if(!c.isSelectable()) continue;
				if(c.getTag().startsWith(channelTag)){
					channel = c;
					break;
				}
			}
		}
		
		if(channel == null){
			return false;
		}
		
		p.setChatChannel(channel);
		return true;
	}

}
