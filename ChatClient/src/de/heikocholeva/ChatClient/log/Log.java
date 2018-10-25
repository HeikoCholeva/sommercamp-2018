package de.heikocholeva.ChatClient.log;

import java.text.SimpleDateFormat;

public class Log {
	
	private SimpleDateFormat sdf;
	private LogLevel logLevel;
	
	public Log(SimpleDateFormat sdf, LogLevel logLevel) {
		this.sdf = sdf;
		this.logLevel = logLevel;
	}
	
	public void write(LogLevel level, String args) {
		if(this.logLevel == LogLevel.ALL || level == this.logLevel) {
			System.out.println("[" + sdf.format(System.currentTimeMillis()) + "]: " + level.name() + " > " + args);
			// TODO: write into a log file
		}
	}
	
	public enum LogLevel {
		ALL, CHAT_EVENTS, DEBUG;
	}

}
