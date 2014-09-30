package com.dreamforce.demo;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.dreamforce.demo.sfdc.bean.SFDCInfo;
import com.dreamforce.demo.util.Report;

/**
 * App Global
 *
 */
public class App {
	public static SFDCInfo sfdcInfo;
	// private static App instance = null;
	private static Properties properties;

	private static String properFileLoc = "/conf/application.properties";
	private static String PROPERTY_PREFIX;
	private static String basedir;
	
/*	private static String username;
	private static String password;
	private static String token;
	private static String partnerUrl;
	private static String apexUrl;
	private static String metadataUrl;*/

	private static Logger logger = Logger.getLogger(App.class);

	static {
		basedir = System.getProperty("basedir", ".");
		PropertyConfigurator.configure(basedir + "/conf/log4j.properties");
		PROPERTY_PREFIX = System.getProperty("build_type", "sfdc");
		sfdcInfo = new SFDCInfo();
		try {
			properties = new Properties();
			properties.load(new FileReader(basedir + properFileLoc));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Report.logInfo("Conf File Missing");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// private App() {}

	public static String getProperty(String name) {
		return properties.getProperty(name);
	}

	public static String getUserName() {
		return getProperty(PROPERTY_PREFIX + ".username");
	}

	public static String getUserPassword() {
		return getProperty(PROPERTY_PREFIX + ".password");
	}

	public static String getPartnerUrl() {
		return getProperty(PROPERTY_PREFIX + ".partnerUrl");
	}

	public static String getApexUrl() {
		return getProperty(PROPERTY_PREFIX + ".apexUrl");
	}

	public static String getMetadataUrl() {
		return getProperty(PROPERTY_PREFIX + ".metadataUrl");
	}

	public static String getSecurityToken() {
		return getProperty(PROPERTY_PREFIX + ".stoken");
	}

	public static void logInfo(String message) {
		logger.log(App.class.getCanonicalName(), Level.INFO, message, null);
	}

	public static void logError(String message) {
		logger.log(App.class.getCanonicalName(), Level.ERROR, message, null);
	}

	public static void main(String[] args) throws IOException {
		System.out.println("Hello World!");
	}
}
