package com.hangout.core.utils.npc;

public enum NPCType {
	OCCUPATION_CHANGER("Master Crafter"),
	GUILD_EDITOR("Guild Manager"),
	RACE_CHANGER("Race Transmogifier");
	
	String displayName;
	
	NPCType(String displayName){
		this.displayName = displayName;
	}
	
	public String getDisplayName(){
		return displayName;
	}
}
