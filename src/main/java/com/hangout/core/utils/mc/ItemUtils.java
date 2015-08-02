package com.hangout.core.utils.mc;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemUtils {
	
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
}
