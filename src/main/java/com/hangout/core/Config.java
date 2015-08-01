package com.hangout.core;

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
        return true;
    }

    public static boolean isRealServer() {
        return realServer;
    }
    
    public static DebugMode getDebugMode(){
    	return debugMode;
    }
}
