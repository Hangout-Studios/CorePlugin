package com.hangout.core.utils.mc;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

public class CitizensManager {

	private static NPCRegistry npcRegistry = CitizensAPI.getNPCRegistry();
	
	public static NPC getNPC(Entity entity){
		return npcRegistry.getNPC(entity);
	}
	
	public static NPC createNPC(EntityType type, String name, Location loc){
		NPC npc = npcRegistry.createNPC(type, name);
		npc.setProtected(true);
		npc.spawn(loc);
		return npc;
	}
	
	public static boolean isNPC(Entity entity){
		if(getNPC(entity) != null) return true;
		return false;
	}
}
