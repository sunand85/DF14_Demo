package com.dreamforce.demo.util;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Report {
	
	private static Logger logger = Logger.getLogger(Report.class);
	
	public static void logInfo(String message) {
		logger.log(Report.class.getCanonicalName(), Level.INFO, message, null);
	}
}
