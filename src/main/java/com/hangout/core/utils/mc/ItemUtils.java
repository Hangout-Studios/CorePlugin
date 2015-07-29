package com.hangout.core.utils.mc;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtils {
	
	public static ItemStack createItem(Material mat, String name, List<String> description){
		ItemStack item = new ItemStack(mat);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(description);
		item.setItemMeta(meta);
		return item;
	}
	
}
