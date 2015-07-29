package com.hangout.core.player;

import java.util.HashMap;
import java.util.UUID;

public class CommonPlayerManager {
	
	public static HashMap<UUID, CommonPlayerBundle> players = new HashMap<UUID, CommonPlayerBundle>();
	
	public static void addPlayer(UUID id, String type, Object p){
		CommonPlayerBundle bundle = null;
		if(players.containsKey(id)){
			bundle = players.get(id);
		}else{
			bundle = new CommonPlayerBundle();
			players.put(id, bundle);
		}
		bundle.addPlayerType(type, p);
	}
	
	public static CommonPlayerBundle getPlayer(UUID p){
		if(players.containsKey(p)){
			return players.get(p);
		}
		return null;
	}
	
	public static void removePlayer(UUID p){
		if(players.containsKey(p)){
			players.remove(p);
		}
	}
}
