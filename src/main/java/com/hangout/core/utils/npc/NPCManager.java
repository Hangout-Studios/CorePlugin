package com.hangout.core.utils.npc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.hangout.core.utils.hologram.HologramManager;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

public class NPCManager {
	
	private static NPCRegistry npcRegistry = CitizensAPI.getNPCRegistry();
	private static HashMap<NPC, NPCData> data = new HashMap<NPC, NPCData>();
	
	public static NPC getNPC(Entity entity){
		return npcRegistry.getNPC(entity);
	}
	
	public static NPCData getNPCData(Entity entity){
		if(!isNPC(entity)) return null;
		return data.get(entity);
	}
	
	public static NPC createNPC(EntityType entType, String name, Location loc, NPCType npcType){
		NPC npc = npcRegistry.createNPC(entType, name);
		npc.setProtected(true);
		npc.spawn(loc);
		
		String hologramTag = npcType + "_" + name.replace(" ", "_");
		HologramManager.createHologram(hologramTag, Arrays.asList("<" + npcType.getDisplayName() + ">"), loc.add(0, 2.5f, 0));
		
		data.put(npc, new NPCData(npc.getEntity(), npcType, name, "-"));
		return npc;
	}
	
	public static boolean isNPC(Entity entity){
		if(getNPC(entity) != null) return true;
		return false;
	}
	
	public static List<NPCData> getNPCData(){
		return new ArrayList<NPCData>(data.values());
	}
	
	public static class NPCData {
		
		private Entity e;
		private NPCType type;
		private String name;
		private String subtitle;
		
		public NPCData(Entity e, NPCType type, String name, String subtitle){
			this.e = e;
			this.type = type;
			this.name = name;
			this.subtitle = subtitle;
		}
		
		public Entity getEntity(){
			return e;
		}
		
		public NPCType getType(){
			return type;
		}
		
		public String getName(){
			return name;
		}
		
		public String getSubtitle(){
			return subtitle;
		}
	}
}
