package com.hangout.core.utils.mc;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
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
	
	public static ItemStack getPlayerHead(Player p, String name, List<String> description){
		ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
		SkullMeta im = (SkullMeta) head.getItemMeta();
        im.setOwner(p.getName());
        im.setDisplayName(name);
        im.setLore(description);
        head.setItemMeta(im);
        return head;
	}
}
