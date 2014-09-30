package com.dreamforce.demo.db;

import java.util.logging.Logger;

public class H2Db extends Db {

	Logger logger;
	String h2DriverForClass = "org.h2.Driver";

	public H2Db(String h2UrlKey, String username, String password) {
		logger = Logger.getLogger(this.getClass().getName());
		try {
			init(h2DriverForClass, h2UrlKey, username, password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (conn != null) {
			logger.info("Connected OK using " + h2DriverForClass + " to "
					+ h2UrlKey);
		} else {
			logger.info("Connection failed: " + h2UrlKey);
			throw new RuntimeException("Connection Failed for H2DB URL: " + h2UrlKey);
		}
	}

	public void close() {
		// TODO Auto-generated method stub
		if (statement != null) {
			try {
				statement.close();
			} catch (Exception ignore) {
			}
		}
		
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception ignore) {
			}
		}
	}
}
