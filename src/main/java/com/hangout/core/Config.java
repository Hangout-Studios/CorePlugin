package com.hangout.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.hangout.core.utils.hologram.HologramManager;
import com.hangout.core.utils.mc.DebugUtils.DebugMode;

public class Config {

    public static String host;
    public static String username;
    public static String password;
    public static String databaseName;
    
    private static boolean realServer = false;
    private static DebugMode debugMode = DebugMode.INFO;

    public static boolean loadData() {        
        realServer = Plugin.getInstance().getConfig().getBoolean("RealServer", false);
        String configHost = realServer ? "Hangout" : "Local";
        
        host = Plugin.getInstance().getConfig().getString("SQL." + configHost + ".IP");
        username = Plugin.getInstance().getConfig().getString("SQL." + configHost + ".Username");
        password = Plugin.getInstance().getConfig().getString("SQL." + configHost + ".Password");
        databaseName = Plugin.getInstance().getConfig().getString("SQL.Database");
        
        debugMode = DebugMode.valueOf(Plugin.getInstance().getConfig().getString("Debug_Mode", "WARNING"));
        
        for(String hologram : Plugin.getInstance().getConfig().getStringList("Holograms")){
        	//tag,name=world,x,y,x
        	String[] s1 = hologram.split("=");
        	String[] s2 = s1[0].split(",");
        	String[] s3 = s1[1].split(",");
        	
        	HologramManager.createHologram(s2[0], Arrays.asList(s2[1]), 
        			new Location(Bukkit.getWorld(s3[0]), Integer.parseInt(s3[1]), Integer.parseInt(s3[2]), Integer.parseInt(s3[3])));
        }
        
        return true;
    }
    
    public static void saveData(){
    	
    	FileConfiguration file = Plugin.getInstance().getConfig();
    	
    	file.set("Holograms", null);
    	List<String> list = new ArrayList<String>();
    	for(Entry<String, Hologram> entry : HologramManager.getHologramMap().entrySet()){
    		//tag,name=world,x,y,x
    		Hologram h = entry.getValue();
    		Location hLoc = h.getLocation();
    		list.add(entry.getKey() + "," + h.getLine(0).toString() + "=" +
    				hLoc.getWorld().getName() + "," + hLoc.getBlockX() + "," + hLoc.getBlockY() + "," + hLoc.getBlockZ());
    	}
    	
    	file.set("Holograms", list);
    	
    	try {
			file.save(Plugin.getInstance().getDataFolder() + File.separator + "config.yml");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public static boolean isRealServer() {
        return realServer;
    }
    
    public static DebugMode getDebugMode(){
    	return debugMode;
    }
}
