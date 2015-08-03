package com.hangout.core.utils.mc;

import java.util.Random;

import com.hangout.core.utils.mc.DebugUtils.DebugMode;

public class NumberUtils {
	private static Random randomGen = new Random();
	
	public static Random getRandom(){
		return randomGen;
	}
	
	public static boolean rollPercentage(int percentage){
		int number = randomGen.nextInt(100);
		DebugUtils.sendDebugMessage("Generated number: " + number, DebugMode.EXTENSIVE);
		if(number > percentage){
			return true;
		}
		return false;
	}
	
	public static int multiplyByPercentage(int number, int percentage){
		return (int)(number * ((float)percentage / 100));
	}
}
