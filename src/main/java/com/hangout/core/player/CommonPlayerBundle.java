package com.hangout.core.player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class CommonPlayerBundle {
	
	private HashMap<String, Object> playerTypes = new HashMap<String, Object>();
	
	public CommonPlayerBundle(){}
	
	public void addPlayerType(String type, Object player){
		playerTypes.put(type, player);
	}
	
	public Object getPlayer(String type){
		if(playerTypes.containsKey(type)){
			return playerTypes.get(type);
		}
		return null;
	}
	
	public int getRPGLevel(){
		if(!playerTypes.containsKey("RPG")) return 0;
		return (Integer) invokeMethod(playerTypes.get("RPG"), "getLevel");
	}
	
	public String getRPGRace(){
		if(!playerTypes.containsKey("RPG")) return "HUMAN";
		return invokeMethod(playerTypes.get("RPG"), "getRace").toString();
	}
	
	public String getRPGOccupation(){
		if(!playerTypes.containsKey("RPG")) return "WARRIOR";
		return invokeMethod(playerTypes.get("RPG"), "getOccupation").toString();
	}
	
	public void addRPGExperience(int value, String source){
		if(!playerTypes.containsKey("RPG")) return;
		
		Class<?>[] classes = new Class<?>[3];
		classes[0] = int.class;
		classes[1] = boolean.class;
		classes[2] = String.class;
		
		Object[] values = new Object[3];
		values[0] = value;
		values[1] = true;
		values[2] = source;
		
		invokeMethod(playerTypes.get("RPG"), "addExperience", classes, values);
	}
	
	public void unlockRPGOccupation(String occupation){
		if(!playerTypes.containsKey("RPG")) return;
		
		Class<?>[] classes = new Class<?>[1];
		classes[0] = String.class;
		
		Object[] values = new Object[1];
		values[0] = occupation;
		
		invokeMethod(playerTypes.get("RPG"), "unlockOccupation", classes, values);
	}
	
	private Object invokeMethod(Object playerObject, String method){		
		return invokeMethod(playerObject, method, (Class<?>[])null, (Object[])null);
	}
	
	private Object invokeMethod(Object playerObject, String method, Class<?>[] argClasses, Object[] argValues){
		Method m = null;
		try {
			m = playerObject.getClass().getMethod(method, argClasses);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			return null;
		}
		
		Object result = null;
		try {
			result = m.invoke(playerObject, argValues);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
		
		return result;
	}
}
