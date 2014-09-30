package com.dreamforce.demo.sfdc.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.dreamforce.demo.App;
import com.sforce.soap.apex.ExecuteAnonymousResult;
import com.sforce.soap.apex.SoapConnection;
import com.sforce.soap.partner.Connector;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

public class ApexUtil {

	static SoapConnection soapConnection;

	public static void runApexCodeFromFile(String fileName) {
		String code = null;
		try {
			code = FileUtils.readFileToString(new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}

		runApex(code);
	}

	public static void runApex(String apexCode) {
		try {
			if (login()) {
				App.logInfo("Running Apex Code : " + apexCode);
				ExecuteAnonymousResult result = soapConnection
						.executeAnonymous(apexCode);
				if (result.isCompiled()) {
					if (result.isSuccess()) {
						App.logInfo("Apex code excuted sucessfully");
					} else {
						throw new RuntimeException(
								"Apex code execution failed :"
										+ result.getExceptionMessage());
					}
				} else {
					throw new RuntimeException("Apex code compilition failed :"
							+ result.getCompileProblem());
				}
			}
		} catch (ConnectionException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	private static boolean login() {
		boolean success = false;
		if(soapConnection != null) return true;
		try {
			ConnectorConfig config = new ConnectorConfig();
			ConnectorConfig soapConfig = new ConnectorConfig();

			config.setUsername(App.getUserName());
			config.setPassword(App.getUserPassword() + App.getSecurityToken());
			App.logInfo("AuthEndPoint: " + App.getPartnerUrl());
			config.setAuthEndpoint(App.getPartnerUrl());
			Connector.newConnection(config);
			soapConfig.setAuthEndpoint(config.getAuthEndpoint());
			soapConfig.setServiceEndpoint(config.getServiceEndpoint().replace(
					"/u/", "/s/"));
			soapConfig.setSessionId(config.getSessionId());
			soapConnection = new SoapConnection(soapConfig);
			success = true;

		} catch (ConnectionException ce) {
			throw new RuntimeException("Error connecting to salesforce from Apex Util", ce);
		}
		return success;
	}
}
