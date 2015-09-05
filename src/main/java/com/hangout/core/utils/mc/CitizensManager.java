package com.hangout.core.utils.mc;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.hangout.core.utils.hologram.HologramManager;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

public class CitizensManager {
	
	private static NPCRegistry npcRegistry = CitizensAPI.getNPCRegistry();
	private static HashMap<NPC, String> hologramTags = new HashMap<NPC, String>();
	
	public static NPC getNPC(Entity entity){
		return npcRegistry.getNPC(entity);
	}
	
	public static NPC createNPC(EntityType entType, String name, Location loc, String npcType){
		NPC npc = npcRegistry.createNPC(entType, name);
		npc.setProtected(true);
		npc.spawn(loc);
		
		String hologramTag = npcType + "_" + name.replace(" ", "_");
		HologramManager.createHologram(hologramTag, Arrays.asList("<" + npcType + ">"), loc.add(0, 2, 0));
		hologramTags.put(npc, hologramTag);
		return npc;
	}
	
	public static boolean isNPC(Entity entity){
		if(getNPC(entity) != null) return true;
		return false;
	}
}
