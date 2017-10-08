package io.jumpon.api.library;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Latency {
	
	public static void printTime() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("ss.S");
    	sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
    	Date now = new Date();
    	System.out.println(sdf.format(now));
	}

	public static String getTime() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("ss.S");
    	sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
    	Date now = new Date();
    	
    	return sdf.format(now);
	}	
	
}
