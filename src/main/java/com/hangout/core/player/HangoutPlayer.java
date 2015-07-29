package com.hangout.core.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import mkremins.fanciful.FancyMessage;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.joda.time.DateTime;

import com.hangout.core.HangoutAPI;
import com.hangout.core.chat.ChatChannel;
import com.hangout.core.chat.ChatManager;
import com.hangout.core.menu.MenuInventory;
import com.hangout.core.utils.database.Database;
import com.hangout.core.utils.mc.CommandPreparer;

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
	private List<ChatChannel> subscribedChannels;
	private List<UUID> mutedPlayers = new ArrayList<UUID>();
	private boolean pvpEnabled = false;
	private int gold = 0;
	private HashMap<String, CommandPreparer> commands = new HashMap<String, CommandPreparer>();
	private HashMap<String, Integer> commandKeys = new HashMap<String, Integer>();
	
	private MenuInventory openMenu = null;
	
	private HashMap<String, Boolean> loadingProgress = new HashMap<String, Boolean>();
	
	public HangoutPlayer(Player p){
		this.p = p;
		id = p.getUniqueId();
		name = p.getName();
		subscribedChannels = ChatManager.getChannels();
		channel = subscribedChannels.get(0);
	}
	
	public HangoutPlayer(UUID id, String name){
		this.id = id;
		this.name = name;
	}
	
	public Player getPlayer(){
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
	
	public void setOpenMenu(MenuInventory menu){
		openMenu = menu;
	}
	
	public MenuInventory getOpenMenu(){
		return openMenu;
	}
	
	public boolean isInMenu(){
		return getOpenMenu() != null;
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
	
	@SuppressWarnings("unchecked")
	public FancyMessage getClickableName(HangoutPlayer toPlayer, boolean addChatTag){
		FancyMessage message = new FancyMessage("");
		
		if(addChatTag){
			message.then(getChatChannel().getTag() + " ");
		}
		
		HashMap<String, Object> nameConfig =  getClickableNameConfig(toPlayer);
		
		return message
			.then((String)nameConfig.get("text"))
				.color((ChatColor)nameConfig.get("color"))
				.style((ChatColor[])nameConfig.get("styles"))
				.command((String)nameConfig.get("command"))
			.tooltip((List<String>)nameConfig.get("tooltip"));
	}
	
	public HashMap<String, Object> getClickableNameConfig(HangoutPlayer toPlayer){
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		ChatColor[] styles = new ChatColor[2];
		styles[0] = ChatColor.ITALIC;
		styles[1] = ChatColor.BOLD;
		
		h.put("text", getDisplayName());
		h.put("color", ChatColor.GOLD);
		h.put("styles", styles);
		h.put("command", "/text player " + getName() + " " + toPlayer.getName());
		h.put("tooltip", getDescription());
		return h;
		
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
			HangoutAPI.sendDebugMessage(this.getName() + " has added " + p.getName() + " as friend");
			if(executeDatabaseCommand) Database.executeFriendAction(id, p.getUUID(), true);
			return true;
		}else{
			return false;
		}
	}
	
	public boolean removeFriend(HangoutPlayer p, boolean executeDatabaseCommand){
		if(friends.contains(p)){
			friends.remove(p);
			HangoutAPI.sendDebugMessage(this.getName() + " has removed " + p.getName() + " as friend");
			if(executeDatabaseCommand) Database.executeFriendAction(id, p.getUUID(), false);
			p.attemptRemove();
			return true;
		}else{
			return false;
		}
	}
	
	public void setFriends(List<HangoutPlayer> list){
		friends = list;
	}
	
	public boolean isFriend(UUID id){
		if(friends.contains(id)){
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
		
		HangoutAPI.sendDebugMessage(adminP.getName() + " has muted " + this.getName() + " for " + time + " because: " + reason);
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
		
		HangoutAPI.sendDebugMessage(adminP.getName() + " has banned " + this.getName() + " for " + time + " because: " + reason);
	}
	
	public void kick(String reason, HangoutPlayer adminP, boolean commitToDatabase){
		String fullMessage = "You have been kicked: " + reason;
		if(fullMessage.length() > 256){
			fullMessage = fullMessage.substring(0, 255);
		}
		
		p.kickPlayer(fullMessage);
		
		if(commitToDatabase){
			HangoutAPI.sendDebugMessage(adminP.getName() + " has kicked " + this.getName() + " because: " + reason);
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
	
	public ChatChannel getChatChannel(){
		return channel;
	}
	
	public void setChatChannel(ChatChannel c){
		channel = c;
	}
	
	public List<ChatChannel> getSubscribedChannels(){
		return subscribedChannels;
	}
	
	public void removeSubscribedChannel(ChatChannel c){
		if(subscribedChannels.contains(c)){
			subscribedChannels.remove(c);
		}
	}
	
	public void addSubscribedChannel(ChatChannel c){
		if(!subscribedChannels.contains(c)){
			subscribedChannels.add(c);
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
	}
	
	public boolean isPvpEnabled(){
		return pvpEnabled;
	}
	
	public void modifyGold(int gold, String source, boolean commitToDatabase){
		this.gold += gold;
		
		HangoutAPI.sendDebugMessage(getName() + " got " + gold + " from " + source);
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
	
	public void clearComandPreparer(String tag){
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
		
		if(clearToRemove){
			
			//Clear player
			CommonPlayerManager.removePlayer(getUUID());			
			HangoutPlayerManager.removePlayer(this);
		}
	}
}
