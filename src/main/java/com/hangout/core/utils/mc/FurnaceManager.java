package com.hangout.core.utils.mc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.block.Block;

public class FurnaceManager {
	
	private static HashMap<Block, UUID> claimedFurnaces = new HashMap<Block, UUID>();
	private static HashMap<UUID, Block> openFurnaces = new HashMap<UUID, Block>();
	
	public static void claimFurnace(Block b, UUID p){
		if(!claimedFurnaces.containsKey(b)){
			claimedFurnaces.put(b, p);
			System.out.print("Player claimed a furnace...");
		}
	}
	
	public static void removeFurnace(Block b){
		if(claimedFurnaces.containsKey(b)){
			claimedFurnaces.remove(b);
			System.out.print("Player released a furnace..");
		}
	}
	
	public static boolean isClaimed(Block b){
		if(claimedFurnaces.containsKey(b)){
			return true;
		}
		return false;
	}
	
	public static boolean isClaimedBy(Block b, UUID p){
		if(isClaimed(b) && claimedFurnaces.get(b) == p){
			return true;
		}
		return false;
	}
	
	public static UUID getPlayer(Block b){
		if(claimedFurnaces.containsKey(b)){
			return claimedFurnaces.get(b);
		}
		return null;
	}
	
	public static void removePlayerLocks(UUID p){
		List<Block> toRemove = new ArrayList<Block>();
		for(Block b : claimedFurnaces.keySet()){
			if(claimedFurnaces.get(b) == p){
				toRemove.add(b);
			}
		}
		for(Block b : toRemove){
			claimedFurnaces.remove(b);
		}
	}
	
	public static void openFurnace(UUID p, Block b){
		if(!openFurnaces.containsKey(p)){
			openFurnaces.put(p, b);
		}
	}
	
	public static Block closeFurnace(UUID p){
		if(openFurnaces.containsKey(p)){
			return openFurnaces.remove(p);
		}
		return null;
	}
	
	public static Block getOpenFurnace(UUID p){
		if(openFurnaces.containsKey(p)){
			return openFurnaces.get(p);
		}
		return null;
	}
}
