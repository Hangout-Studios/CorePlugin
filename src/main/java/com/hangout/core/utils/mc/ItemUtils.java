package com.hangout.core.utils.mc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemUtils {
	
	private static List<Material> crops = Arrays.asList(Material.BROWN_MUSHROOM, Material.CACTUS, Material.CARROT, Material.CARROT_ITEM,
			Material.COCOA, Material.MELON, Material.MELON_BLOCK, Material.MELON_SEEDS, Material.POTATO, Material.POTATO_ITEM, Material.PUMPKIN,
			Material.PUMPKIN_SEEDS, Material.RED_MUSHROOM, Material.SAPLING, Material.SEEDS, Material.SPECKLED_MELON, Material.SUGAR_CANE,
			Material.SUGAR_CANE_BLOCK, Material.WHEAT);
	
	private static List<Material> ores = Arrays.asList(Material.COAL_ORE, Material.IRON_ORE, Material.LAPIS_ORE, Material.GOLD_ORE,
			Material.REDSTONE_ORE, Material.DIAMOND_ORE, Material.EMERALD_ORE, Material.QUARTZ_ORE);
	
	private static List<Material> pickaxes = Arrays.asList(Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE,
			Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE);
	
	private static List<Material> axes = Arrays.asList(Material.WOOD_AXE, Material.STONE_AXE, Material.IRON_AXE,
			Material.GOLD_AXE, Material.DIAMOND_AXE);
	
	private static List<Material> hoes = Arrays.asList(Material.WOOD_HOE, Material.STONE_HOE, Material.IRON_HOE,
			Material.GOLD_HOE, Material.DIAMOND_HOE);
	
	private static List<Material> spades = Arrays.asList(Material.WOOD_SPADE, Material.STONE_SPADE, Material.IRON_SPADE,
			Material.GOLD_SPADE, Material.DIAMOND_SPADE);
	
	public static ItemStack createItem(Material mat, String name, List<String> description){
		ItemStack item = new ItemStack(mat);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(description);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getPlayerHead(UUID id, String name, List<String> description){
		ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
		SkullMeta im = (SkullMeta) head.getItemMeta();
        im.setOwner(Bukkit.getOfflinePlayer(id).getName());
        im.setDisplayName(name);
        im.setLore(description);
        head.setItemMeta(im);
        return head;
	}
	
	public static boolean isCrop(Material mat){
		if(crops.contains(mat)) return true;
		return false;
	}
	
	public static boolean isOre(Material mat){
		if(ores.contains(mat)) return true;
		return false;
	}
	
	public static boolean isPickaxe(Material mat){
		if(pickaxes.contains(mat)) return true;
		return false;
	}
	
	public static boolean isAxe(Material mat){
		if(axes.contains(mat)) return true;
		return false;
	}
	
	public static boolean isHoe(Material mat){
		if(hoes.contains(mat)) return true;
		return false;
	}
	
	public static boolean isSpade(Material mat){
		if(spades.contains(mat)) return true;
		return false;
	}
}
