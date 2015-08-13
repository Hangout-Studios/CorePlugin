package com.hangout.core.utils.mc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemUtils {
	
	private static HashSet<Material> crops = new HashSet<Material>(Arrays.asList(
			Material.BROWN_MUSHROOM, 
			Material.CACTUS, 
			Material.CARROT, 
			Material.CARROT_ITEM,
			Material.COCOA, 
			Material.CROPS,
			Material.MELON, 
			Material.MELON_BLOCK, 
			Material.MELON_SEEDS, 
			Material.POTATO, 
			Material.POTATO_ITEM,
			Material.PUMPKIN,
			Material.PUMPKIN_SEEDS,
			Material.RED_MUSHROOM,
			Material.SEEDS,
			Material.SPECKLED_MELON,
			Material.SUGAR_CANE,
			Material.SUGAR_CANE_BLOCK,
			Material.WHEAT));
	
	private static HashSet<Material> ores = new HashSet<Material>(Arrays.asList(
			Material.COAL_ORE, 
			Material.IRON_ORE, 
			Material.LAPIS_ORE, 
			Material.GOLD_ORE,			
			Material.REDSTONE_ORE, 
			Material.DIAMOND_ORE, 
			Material.EMERALD_ORE, 
			Material.QUARTZ_ORE,
			Material.CLAY));
	
	private static HashSet<Material> buildingMaterial = new HashSet<Material>(Arrays.asList(
			Material.LOG,
			Material.LOG_2,
			Material.CLAY,
			Material.STONE,    
			Material.SANDSTONE,
			Material.RED_SANDSTONE,
			Material.SOUL_SAND,
			Material.GLOWSTONE,
			Material.SPONGE,
			Material.DEAD_BUSH,
			Material.MOB_SPAWNER));
	
	private static HashSet<Material> food = new HashSet<Material>(Arrays.asList(
			Material.BAKED_POTATO,
			Material.BREAD,
			Material.CAKE,
			Material.COOKED_BEEF,
			Material.COOKED_CHICKEN,
			Material.COOKED_FISH,
			Material.COOKED_MUTTON,
			Material.COOKED_RABBIT,
			Material.COOKIE,
			Material.GOLDEN_APPLE,
			Material.GOLDEN_CARROT,
			Material.GRILLED_PORK,
			Material.MUSHROOM_SOUP,
			Material.PUMPKIN_PIE,
			Material.RABBIT_STEW));
	
	private static HashSet<Material> buildingBlock = new HashSet<Material>(Arrays.asList(
			Material.ACACIA_DOOR_ITEM,
			Material.ACACIA_FENCE,
			Material.ACACIA_FENCE_GATE,
			Material.ACACIA_STAIRS,
			Material.ANVIL,
			Material.ARMOR_STAND,
			Material.BANNER,
			Material.BED,
			Material.BIRCH_DOOR_ITEM,
			Material.BIRCH_FENCE,
			Material.BIRCH_FENCE_GATE,
			Material.BIRCH_WOOD_STAIRS,
			Material.BLAZE_POWDER,
			Material.BOAT,
			Material.BOOK,
			Material.BOOK_AND_QUILL,
			Material.BOOKSHELF,
			Material.BOWL,
			Material.BREWING_STAND_ITEM,
			Material.BRICK,
			Material.BRICK_STAIRS,
			Material.BUCKET,
			Material.CARPET,
			Material.CAULDRON_ITEM,
			Material.CHEST,
			Material.COBBLE_WALL,
			Material.COBBLESTONE_STAIRS,
			Material.DARK_OAK_DOOR_ITEM,
			Material.DARK_OAK_FENCE,
			Material.DARK_OAK_FENCE_GATE,
			Material.DARK_OAK_STAIRS,
			Material.ENCHANTMENT_TABLE,
			Material.FENCE,
			Material.FENCE_GATE,
			Material.FIREWORK,
			Material.FLOWER_POT_ITEM,
			Material.GLASS,
			Material.IRON_FENCE,
			Material.ITEM_FRAME,
			Material.JACK_O_LANTERN,
			Material.JUKEBOX,
			Material.JUNGLE_DOOR_ITEM,
			Material.JUNGLE_FENCE,
			Material.JUNGLE_FENCE_GATE,
			Material.JUNGLE_WOOD_STAIRS,
			Material.LADDER,
			Material.NETHER_BRICK,
			Material.NETHER_BRICK_STAIRS,
			Material.NETHER_FENCE,
			Material.PAINTING,
			Material.QUARTZ_STAIRS,
			Material.RED_SANDSTONE_STAIRS,
			Material.SANDSTONE_STAIRS,
			Material.SEA_LANTERN,
			Material.SIGN,
			Material.SMOOTH_BRICK,
			Material.SMOOTH_STAIRS,
			Material.SPRUCE_DOOR_ITEM,
			Material.SPRUCE_FENCE,
			Material.SPRUCE_FENCE_GATE,
			Material.SPRUCE_WOOD_STAIRS,
			Material.STANDING_BANNER,
			Material.STEP,
			Material.STONE_SLAB2,
			Material.TRAP_DOOR,
			Material.WOODEN_DOOR,
			Material.WOOD_DOOR,
			Material.WOOD_STAIRS,
			Material.WOOD_STEP));
	
	private static HashSet<Material> redstoneComponents = new HashSet<Material>(Arrays.asList(
			Material.ACTIVATOR_RAIL,
			Material.COMMAND,
			Material.COMMAND_MINECART,
			Material.COMPASS,
			Material.DAYLIGHT_DETECTOR,
			Material.DAYLIGHT_DETECTOR_INVERTED,
			Material.DETECTOR_RAIL,
			Material.DIODE,
			Material.DISPENSER,
			Material.DROPPER,
			Material.EMPTY_MAP,
			Material.EXPLOSIVE_MINECART,
			Material.BEACON,
			Material.HOPPER,
			Material.HOPPER_MINECART,
			Material.IRON_DOOR,
			Material.IRON_TRAPDOOR,
			Material.LEVER,
			Material.MAP,
			Material.MINECART,
			Material.NOTE_BLOCK,
			Material.PISTON_BASE,
			Material.PISTON_EXTENSION,
			Material.PISTON_MOVING_PIECE,
			Material.PISTON_STICKY_BASE,
			Material.POWERED_MINECART,
			Material.POWERED_RAIL,
			Material.RAILS,
			Material.REDSTONE_COMPARATOR,
			Material.REDSTONE_COMPARATOR_OFF,
			Material.REDSTONE_COMPARATOR_ON,
			Material.REDSTONE_LAMP_OFF,
			Material.REDSTONE_LAMP_ON,
			Material.REDSTONE_TORCH_OFF,
			Material.REDSTONE_TORCH_ON,
			Material.STONE_BUTTON,
			Material.STONE_PLATE,
			Material.STORAGE_MINECART,
			Material.TNT,
			Material.TRAPPED_CHEST,
			Material.TRIPWIRE,
			Material.TRIPWIRE_HOOK,
			Material.WATCH,
			Material.WOOD_BUTTON,
			Material.WOOD_PLATE));
	
	private static HashSet<Material> blacksmithItems = new HashSet<Material>(Arrays.asList(
			Material.CHAINMAIL_BOOTS,
			Material.CHAINMAIL_CHESTPLATE,
			Material.CHAINMAIL_HELMET,
			Material.CHAINMAIL_LEGGINGS,
			Material.DIAMOND_AXE,
			Material.DIAMOND_BARDING,
			Material.DIAMOND_BOOTS,
			Material.DIAMOND_CHESTPLATE,
			Material.DIAMOND_HELMET,
			Material.DIAMOND_HOE,
			Material.DIAMOND_LEGGINGS,
			Material.DIAMOND_PICKAXE,
			Material.DIAMOND_SPADE,
			Material.DIAMOND_SWORD,
			Material.ENDER_CHEST,
			Material.ARROW,
			Material.BOW,
			Material.FISHING_ROD,
			Material.FURNACE,
			Material.GOLD_AXE,
			Material.GOLD_BARDING,
			Material.GOLD_BOOTS,
			Material.GOLD_CHESTPLATE,
			Material.GOLD_HELMET,
			Material.GOLD_HOE,
			Material.GOLD_LEGGINGS,
			Material.GOLD_PICKAXE,
			Material.GOLD_PLATE,
			Material.GOLD_SPADE,
			Material.GOLD_SWORD,
			Material.IRON_AXE,
			Material.IRON_BARDING,
			Material.IRON_BOOTS,
			Material.IRON_CHESTPLATE,
			Material.IRON_HELMET,
			Material.IRON_HOE,
			Material.IRON_LEGGINGS,
			Material.IRON_PICKAXE,
			Material.IRON_PLATE,
			Material.IRON_SPADE,
			Material.IRON_SWORD,
			Material.LEATHER_BOOTS,
			Material.LEATHER_CHESTPLATE,
			Material.LEATHER_HELMET,
			Material.LEATHER_LEGGINGS,
			Material.SADDLE,
			Material.STONE_AXE,
			Material.STONE_HOE,
			Material.STONE_PICKAXE,
			Material.STONE_SPADE,
			Material.STONE_SWORD,
			Material.WOOD_AXE,
			Material.WOOD_HOE,
			Material.WOOD_PICKAXE,
			Material.WOOD_SPADE,
			Material.WOOD_SWORD,
			Material.WORKBENCH));
	
	private static HashSet<Material> pickaxes = new HashSet<Material>(Arrays.asList(Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE,
			Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE));
	
	private static HashSet<Material> axes = new HashSet<Material>(Arrays.asList(Material.WOOD_AXE, Material.STONE_AXE, Material.IRON_AXE,
			Material.GOLD_AXE, Material.DIAMOND_AXE));
	
	private static HashSet<Material> hoes = new HashSet<Material>(Arrays.asList(Material.WOOD_HOE, Material.STONE_HOE, Material.IRON_HOE,
			Material.GOLD_HOE, Material.DIAMOND_HOE));
	
	private static HashSet<Material> spades = new HashSet<Material>(Arrays.asList(Material.WOOD_SPADE, Material.STONE_SPADE, Material.IRON_SPADE,
			Material.GOLD_SPADE, Material.DIAMOND_SPADE));
	
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
	
	public static boolean isBuildingMaterial(Material mat){
		if(buildingMaterial.contains(mat)) return true;
		return false;
	}
	
	public static boolean isBuildingBlock(Material mat){
		if(buildingBlock.contains(mat)) return true;
		return false;
	}
	
	public static boolean isFood(Material mat){
		if(food.contains(mat)) return true;
		return false;
	}
	
	public static boolean isRedstoneComponent(Material mat){
		if(redstoneComponents.contains(mat)) return true;
		return false;
	}
	
	public static boolean isBlacksmithItem(Material mat){
		if(blacksmithItems.contains(mat)) return true;
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
