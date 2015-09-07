package com.hangout.core.reports;

import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.joda.time.DateTime;

public class PlayerReport {
	
	private String reportedBy;
	private String reportedPlayer;
	private List<String> reason;
	private DateTime time;
	private int id;
	
	private ItemStack item;
	
	public PlayerReport(String reportedBy, String reportedPlayer, String reason, DateTime time, int reportID){
		this.reportedBy = reportedBy;
		this.reportedPlayer = reportedPlayer;
		this.time = time;
		this.reason = getDescription(reason);
		
		if(reportID == -1){
			id = ReportDatabase.nextPlayerID();
		}else{
			id = reportID;
		}
		
		item = new ItemStack(Material.APPLE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(""+ChatColor.RED + ChatColor.BOLD + "Report #" + id);
		meta.setLore(this.reason);
		item.setItemMeta(meta);
	}
	
	public ItemStack getItem(){
		return item.clone();
	}
	
	public int getID(){
		return id;
	}
	
	public List<String> getDescription(String s){
		List<String> list = new ArrayList<String>();
		
		//Report description
		list.add(""+ ChatColor.GOLD + ChatColor.BOLD + "Reported player: " + ChatColor.RESET + ChatColor.ITALIC + reportedPlayer);
		list.add(""+ ChatColor.GOLD + ChatColor.BOLD + "Report description:");
		int charactersPerLine = 25;
		while(true){
			if(s.length() <= charactersPerLine){
				list.add(s);
				break;
			}
			
			list.add(ChatColor.ITALIC + s.substring(0, charactersPerLine));
			s = s.substring(charactersPerLine, s.length());
		}
		
		//More info
		list.add(" ");
		list.add(""+ ChatColor.GOLD + ChatColor.BOLD + "Reported by: " + ChatColor.RESET + ChatColor.ITALIC + reportedBy);
		list.add(""+ ChatColor.GOLD + ChatColor.BOLD + "Date: " + ChatColor.RESET + ChatColor.ITALIC + 
				time.getDayOfMonth() + "/" + time.getMonthOfYear() + "/" + time.getYear());
		list.add(""+ ChatColor.GOLD + ChatColor.BOLD + "Time: " + ChatColor.RESET + ChatColor.ITALIC + 
				time.getHourOfDay() + ":" + time.getMinuteOfHour() + ":" + time.getSecondOfMinute());
		
		list.add(" ");
		list.add("Click to mark as checked.");
		return list;
	}
}
