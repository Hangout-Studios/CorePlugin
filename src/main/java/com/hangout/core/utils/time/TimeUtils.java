package com.hangout.core.utils.time;

import org.joda.time.DateTime;
import org.joda.time.Seconds;

public class TimeUtils {
	public static String getTimeUntil(DateTime time){
		int totalSeconds = Seconds.secondsBetween(DateTime.now(), time).getSeconds();
		int numberOfDays = totalSeconds / 86400;
		int numberOfHours = (totalSeconds % 86400 ) / 3600;
		int numberOfMinutes = ((totalSeconds % 86400 ) % 3600 ) / 60;
		int numberOfSeconds = ((totalSeconds % 86400 ) % 3600 ) % 60;
		
		String s = "";
		if(numberOfDays != 0){
			s += numberOfDays + "d ";
		}
		if(numberOfHours != 0){
			s += numberOfHours + "h ";
		}
		if(numberOfMinutes != 0){
			s += numberOfMinutes + "m ";
		}
		s += numberOfSeconds + "s";
		
	    return s;
	}
}
