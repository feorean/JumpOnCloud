package io.jumpon.api.library;

public class Log {
	
	public static enum LogType { DEBUG, PERFORMANCE, ERROR, INFO, ALL };
	
	private static LogType convertLogLevel(String logLevel) {
		
		LogType type = null;
		
		String level = logLevel.toUpperCase().trim();
		
		if (level.equals("DEBUG")) type = LogType.DEBUG;
		if (level.equals("PERFORMANCE")) type = LogType.PERFORMANCE;
		if (level.equals("ERROR")) type = LogType.ERROR;
		if (level.equals("INFO")) type = LogType.INFO;
		if (level.equals("ALL")) type = LogType.ALL;
		
		return type;
	}
	
	private static boolean assertLogLevel(String logLevel, LogType logType) {
		
		String[] levels = logLevel.split(",");
		
		for (String level: levels) {
			
			if (convertLogLevel(level) == logType) return true;
			
		}
		
		return false;
	}
	
	public static void add(String data, LogType type) {
		
		//Print if logLevel is matching with the environment variable
		if (assertLogLevel(Globals.getLogLevel(), type)) 
				System.err.println("***"+type+"***:"+data);
		
	}
	
	public static void debug(String data) {
		
		add(data, LogType.DEBUG);
	}

	public static void performance(String data) {
		
		add(data, LogType.PERFORMANCE);
	}	

	public static void info(String data) {
		
		add(data, LogType.INFO);
	}	
	
	public static void error(String data) {
		
		add(data, LogType.ERROR);
	}	
}
