package com.hangout.core.utils.mc;

import com.hangout.core.Config;

public class DebugUtils {
	
	public static enum DebugMode {
		WARNING(1),
		INFO(2),
		DEBUG(3),
		EXTENSIVE(4),
		COMPLETE(5);
		
		private int rank;
		DebugMode(int rank){
			this.rank = rank;
		}
		
		public boolean isHigherOrSame(DebugMode m){
			if(rank >= m.getRank()){
				return true;
			}
			return false;
		}
		
		public int getRank(){
			return rank;
		}
	}
	
	public static void sendDebugMessage(String message, DebugMode mode){
		if(Config.getDebugMode().isHigherOrSame(mode)){
			System.out.print(message);
		}
	}
}
