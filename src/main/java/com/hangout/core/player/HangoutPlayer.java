package com.hangout.core.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.UUID;

import mkremins.fanciful.FancyMessage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.joda.time.DateTime;

import com.hangout.core.chat.ChatChannel;
import com.hangout.core.chat.ChatManager;
import com.hangout.core.events.PlayerDataReleaseEvent;
import com.hangout.core.menu.MenuInventory;
import com.hangout.core.utils.database.Database;
import com.hangout.core.utils.mc.CommandPreparer;
import com.hangout.core.utils.mc.DebugUtils;
import com.hangout.core.utils.mc.DebugUtils.DebugMode;

public class HangoutPlayer {
	
	public static enum PlayerState{
		LOADING,
		UNLINKED,
		COMPLETED,
		DISCONNECTING
	}
	
	private Player p;
	private UUID id;
	private String name;
	private List<HangoutPlayer> friends = new ArrayList<HangoutPlayer>();
	private List<String> tooltipDescription = new ArrayList<String>();
	private DateTime lastOnline = DateTime.now();
	private PlayerState playerState = PlayerState.LOADING;
	private ViolationReport violations = new ViolationReport();
	private List<PlayerRank> ranks = new ArrayList<PlayerRank>();
	private ChatChannel channel;
	private List<ChatChannel> subscribedChannels = new ArrayList<ChatChannel>();
	private List<UUID> mutedPlayers = new ArrayList<UUID>();
	private boolean pvpEnabled = false;
	private int gold = 0;
	private HashMap<String, CommandPreparer> commands = new HashMap<String, CommandPreparer>();
	private HashMap<String, Integer> commandKeys = new HashMap<String, Integer>();
	
	private Stack<MenuInventory> openMenu = new Stack<MenuInventory>();
	private boolean isInInventory = false;
	
	private HashMap<String, Boolean> loadingProgress = new HashMap<String, Boolean>();
	
	public HangoutPlayer(Player p){
		this.p = p;
		id = p.getUniqueId();
		name = p.getName();
		
		resetChatChannels();
	}
	
	public HangoutPlayer(UUID id, String name){
		this.id = id;
		this.name = name;
		resetChatChannels();
	}
	
	public Player getPlayer(){
		if(p == null) return null;
		
		if(p.isOnline()){
			return p;
		}
		return null;
	}
	
	public void setPlayer(Player p){
		this.p = p;
	}
	
	public UUID getUUID(){
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	public String getDisplayName(){
		return ChatColor.RED + getName();
	}
	
	public void addOpenMenu(MenuInventory menu, boolean replace){
		if(replace) openMenu.pop();
		openMenu.push(menu);
	}
	
	public MenuInventory getOpenMenu(){
		if(openMenu.isEmpty()) return null;
		return openMenu.peek();
	}
	
	public MenuInventory getLastMenu(){
		openMenu.pop();
		return openMenu.peek();
	}
	
	public boolean hasLastMenu(int size){
		System.out.print("Stack size: " + openMenu.size());
		if(openMenu.size() < size) return false;
		return true;
	}
	
	public void clearMenuStack(){
		openMenu.clear();
	}
	
	public boolean isInMenu(){
		return getOpenMenu() != null;
	}
	
	public boolean isInInventory(){
		return isInInventory;
	}
	
	public void setInInventory(boolean b){
		this.isInInventory = b;
	}
	
	public boolean isOnline(){
		if(p == null || !p.isOnline() || getPlayerState() != PlayerState.COMPLETED) return false;
		return true;
	}
	
	public void setPlayerState(PlayerState state){
		playerState = state;
	}
	
	public PlayerState getPlayerState(){
		return playerState;
	}
	
	public List<String> getDescription(){
		return tooltipDescription;
	}
	
	public void setDescription(List<String> desc){
		tooltipDescription = desc;
	}
	
	public FancyMessage getClickableName(HangoutPlayer toPlayer, boolean addChatTag, boolean addClanTag){
		FancyMessage message = new FancyMessage("");
		
		if(addChatTag){
			message.then(getChatChannel().getDisplayName() + ChatColor.WHITE + " ");
		}
		
		return addClickableName(message, toPlayer);
	}
	
	public FancyMessage addClickableName(FancyMessage message, HangoutPlayer toPlayer){
		return message
			.then(getDisplayName())
				.color(ChatColor.GOLD)
				.style(ChatColor.ITALIC, ChatColor.BOLD)
				.command("/text player " + getName() + " " + toPlayer.getName())
			.tooltip(getDescription());
	}
	
	public DateTime getLastOnline(){
		if(isOnline()){
			return DateTime.now();
		}
		return lastOnline;
	}
	
	public void setLastOnline(DateTime time){
		lastOnline = time;
	}
	
	public boolean isReadyLoading(){
		if(loadingProgress.isEmpty()) return false;
		
		for(String s : loadingProgress.keySet()){
			if(loadingProgress.get(s) == false) return false;
		}
		return true;
	}
	public void setLoadingState(String plugin, boolean ready){
		loadingProgress.put(plugin, ready);
	}
	
	public void reset(){
		p.setFoodLevel(20);
		p.setHealth(p.getMaxHealth());
		p.getInventory().clear();
		
		for(PotionEffect potion : p.getActivePotionEffects()){
			p.removePotionEffect(potion.getType());
		}
		
		for(int position : HangoutPlayerManager.getStandardLoadout().keySet()){
			p.getInventory().setItem(position, HangoutPlayerManager.getStandardLoadout().get(position));
		}
	}
	
	/*
	 * Friends
	 */
	public boolean addFriend(HangoutPlayer p, boolean executeDatabaseCommand){
		if(p == this){
			return false;
		}
		
		if(!friends.contains(p)){
			friends.add(p);
			
			if(executeDatabaseCommand){
				Database.executeFriendAction(id, p.getUUID(), true);
				
				p.addClickableName(new FancyMessage("You have added "), this)
					.then(" as friend!")
					.send(getPlayer());
				
				DebugUtils.sendDebugMessage(this.getName() + " has added " + p.getName() + " as friend", DebugMode.DEBUG);
			}
			return true;
		}else{
			return false;
		}
	}
	
	public boolean removeFriend(HangoutPlayer p, boolean executeDatabaseCommand){
		if(friends.contains(p)){
			friends.remove(p);
			
			if(executeDatabaseCommand){
				Database.executeFriendAction(id, p.getUUID(), false);
				
				p.addClickableName(new FancyMessage("You have removed "), this)
						.then(" as friend.")
					.send(getPlayer());
				DebugUtils.sendDebugMessage(this.getName() + " has removed " + p.getName() + " as friend", DebugMode.DEBUG);
			}
			p.attemptRemove();
			return true;
		}else{
			return false;
		}
	}
	
	public void setFriends(List<HangoutPlayer> list){
		friends = list;
	}
	
	public boolean isFriend(HangoutPlayer friend){
		if(friends.contains(friend)){
			return true;
		}
		return false;
	}
	
	public List<HangoutPlayer> getFriends(){
		return friends;
	}
	
	public List<HangoutPlayer> getOnlineFriends(){
		List<HangoutPlayer> onlineFriends = new ArrayList<HangoutPlayer>();
		for(HangoutPlayer p : friends){
			if(p.isOnline()){
				onlineFriends.add(p);
			}
		}
		return onlineFriends;
	}
	
	public boolean isMuted(){
		return violations.isMuted();
	}
	
	public void setMutedUntil(String reason, int time, HangoutPlayer adminP, boolean commitToDatabase){
		violations.setMutedUntil(DateTime.now().plusMinutes(time));
		
		if(commitToDatabase){
			Database.executeAdminAction(this.getUUID(), adminP.getUUID(), time, reason, "MUTE");
		}
		
		DebugUtils.sendDebugMessage(adminP.getName() + " has muted " + this.getName() + " for " + time + " because: " + reason, DebugMode.INFO);
	}
	
	public boolean isBanned(){
		return violations.isBanned();
	}
	
	public void setBannedUntil(String reason, int time, HangoutPlayer adminP, boolean commitToDatabase){
		violations.setBannedUntil(DateTime.now().plusMinutes(time), reason);
		
		if(commitToDatabase){
			String fullMessage = "You have been banned: " + reason;
			if(fullMessage.length() > 256){
				fullMessage = fullMessage.substring(0, 255);
			}
			
			p.kickPlayer(fullMessage);
			
			Database.executeAdminAction(this.getUUID(), adminP.getUUID(), time, reason, "BAN");
		}
		
		DebugUtils.sendDebugMessage(adminP.getName() + " has banned " + this.getName() + " for " + time + " because: " + reason, DebugMode.INFO);
	}
	
	public void kick(String reason, HangoutPlayer adminP, boolean commitToDatabase){
		String fullMessage = "You have been kicked: " + reason;
		if(fullMessage.length() > 256){
			fullMessage = fullMessage.substring(0, 255);
		}
		
		p.kickPlayer(fullMessage);
		
		if(commitToDatabase){
			DebugUtils.sendDebugMessage(adminP.getName() + " has kicked " + this.getName() + " because: " + reason, DebugMode.INFO);
			Database.executeAdminAction(this.getUUID(), adminP.getUUID(), 0, reason, "KICK");
		}
	}
	
	public ViolationReport getViolationReport(){
		return violations;
	}
	
	public List<PlayerRank> getRanks(){
		return ranks;
	}
	
	public void addRank(PlayerRank rank, boolean commitToDatabase){
		if(!getRanks().contains(rank)){
			getRanks().add(rank);
		}
		
		if(commitToDatabase){
			Database.executeRankAction(getUUID(), rank, "GRANT");
		}
	}
	
	public void removeRank(PlayerRank rank, boolean commitToDatabase){
		if(getRanks().contains(rank)){
			getRanks().remove(rank);
		}
		
		if(commitToDatabase){
			Database.executeRankAction(getUUID(), rank, "REMOVE");
		}
	}
	
	public PlayerRank getHighestRank(){
		PlayerRank highestRank = getRanks().get(0);
		for(PlayerRank r : getRanks()){
			if(r.isRankOrHigher(highestRank)){
				highestRank = r;
			}
		}
		return highestRank;
	}
	
	public boolean hasRank(PlayerRank r){
		if(getRanks().contains(r)) return true;
		return false;
	}
	
	public void resetChatChannels(){
		subscribedChannels = ChatManager.getChannels();
		channel = ChatManager.getChannel("core", "world");
	}
	
	public ChatChannel getChatChannel(){
		return channel;
	}
	
	public void setChatChannel(ChatChannel c, boolean commitToDatabase){
		channel = c;
		
		if(commitToDatabase){
			Database.executeChatChannelAction(getUUID(), c, "CHAT");
			getPlayer().sendMessage("You are now chatting in channel: " + channel.getDisplayName());
		}
	}
	
	public List<ChatChannel> getSubscribedChannels(){
		return subscribedChannels;
	}
	
	public void removeSubscribedChannel(ChatChannel c, boolean commitToDatabase){
		if(subscribedChannels.contains(c)){
			subscribedChannels.remove(c);
			
			if(commitToDatabase){
				Database.executeChatChannelAction(getUUID(), c, "MUTE");
				getPlayer().sendMessage("You stopped listening to channel: " + c.getDisplayName());
			}
		}
	}
	
	public void addSubscribedChannel(ChatChannel c, boolean commitToDatabase){
		if(!subscribedChannels.contains(c)){
			subscribedChannels.add(c);
			
			if(commitToDatabase){
				Database.executeChatChannelAction(getUUID(), c, "UNMUTE");
				getPlayer().sendMessage("You started listening to channel: " + c.getDisplayName());
			}
		}
	}
	
	public boolean isSubscribedToChannel(ChatChannel c){
		if(subscribedChannels.contains(c)){
			return true;
		}
		return false;
	}
	
	public List<UUID> getMutedPlayers(){
		return mutedPlayers;
	}
	
	public void removeMutedPlayer(UUID id, boolean commitToDatabase){
		if(mutedPlayers.contains(id)){
			mutedPlayers.remove(id);
			
			if(commitToDatabase){
				Database.executeMuteAction(getUUID(), id, "UNMUTE");
			}
		}
	}
	
	public void addMutedPlayer(UUID id, boolean commitToDatabase){
		if(!mutedPlayers.contains(id)){
			mutedPlayers.add(id);
			
			if(commitToDatabase){
				Database.executeMuteAction(getUUID(), id, "MUTE");
			}
		}
	}
	
	public boolean hasMutedPlayer(UUID id){
		if(mutedPlayers.contains(id)) return true;
		return false;
	}
	
	public void setPvpEnabled(boolean b){
		pvpEnabled = b;
		
		if(getPlayer() == null) return;
		
		if(b){
			getPlayer().sendMessage("You have enabled PvP!");
		}else{
			getPlayer().sendMessage("You have disabled PvP!");
		}
	}
	
	public boolean isPvpEnabled(){
		return pvpEnabled;
	}
	
	public void modifyGold(int gold, String source, boolean commitToDatabase){
		this.gold += gold;
		
		DebugUtils.sendDebugMessage(getName() + " got " + gold + " from " + source, DebugMode.DEBUG);
		if(commitToDatabase){
			Database.executeGoldAction(getUUID(), gold, source);
		}
	}
	
	public int getCurrency(){
		return gold;
	}
	
	public CommandPreparer createCommandPreparer(String tag){
		CommandPreparer command = new CommandPreparer(this, tag, generateNewCommandKey(tag));
		commands.put(tag, command);
		return command;
	}
	
	public CommandPreparer getCommandPreparer(String tag){
		if(commands.containsKey(tag)){
			return commands.get(tag);
		}
		return null;
	}
	
	public void clearCommandPreparer(String tag){
		if(commands.containsKey(tag)){
			commands.remove(tag);
		}
		
		if(commandKeys.containsKey(tag)){
			commandKeys.remove(tag);
		}
	}
	
	public int generateNewCommandKey(String tag){
		int commandKey = new Random().nextInt(100000);
		commandKeys.put(tag, commandKey);
		return commandKey;
	}
	
	public int getCommandKey(String tag){
		if(commandKeys.containsKey(tag)){
			return commandKeys.get(tag);
		}
		return -1;
	}
	
	public void attemptRemove(){
		boolean clearToRemove = true;
		for(HangoutPlayer otherP : HangoutPlayerManager.getPlayers()){
			if(otherP.getFriends().contains(otherP)){
				clearToRemove = false;
				break;
			}
		}
		
		CommonPlayerBundle bundle = CommonPlayerManager.getPlayer(getUUID());
		if(bundle == null || bundle.isInGuild()){
			clearToRemove = false;
		}
		
		if(clearToRemove){			
			Bukkit.getPluginManager().callEvent(new PlayerDataReleaseEvent(this));
		}
	}
}
