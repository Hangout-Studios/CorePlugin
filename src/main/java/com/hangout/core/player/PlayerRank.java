package com.hangout.core.player;

public enum PlayerRank {
	DEFAULT("Default", 0),
	DEFAULT_PLUS("Default Plus", 1),
	YOUTUBER("Youtuber", 2),
	DONATOR_TIER1("Donator 1", 3),
	DONATOR_TIER2("Donator 2", 4),
	DONATOR_TIER3("Donator 3", 5),
	MODERATOR_TRIAL("Moderator", 6),
	MODERATOR("Moderator", 7),
	ADMIN("Admin", 8),
	DEVELOPER("Developer", 9);
	
	private String displayName;
	private int priorityValue;
	PlayerRank(String displayName, int priorityValue){
		this.displayName = displayName;
		this.priorityValue = priorityValue;
	}
	
	public String getDisplayName(){
		return displayName;
	}
	
	public boolean isRankOrHigher(PlayerRank rank){
		if(this.priorityValue >= rank.priorityValue){
			return true;
		}
		return false;
	}
}
