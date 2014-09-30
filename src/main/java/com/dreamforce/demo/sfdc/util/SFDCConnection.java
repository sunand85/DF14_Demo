package com.dreamforce.demo.sfdc.util;

import com.dreamforce.demo.App;
import com.dreamforce.demo.sfdc.bean.SFDCInfo;
import com.sforce.soap.apex.SoapConnection;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.soap.partner.Connector;
import com.sforce.soap.partner.LoginResult;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

public class SFDCConnection {
	
	public static PartnerConnection partnerConnection;
	public static MetadataConnection metadataConnection;
	public static SoapConnection apexConnection;
	
	public static void main(String[] args) throws ConnectionException {
		fetchSFDCInfo();
		metadataConnection = createMetadataConnection();
		apexConnection = createApexConnection();
		System.out.println(metadataConnection.getConfig().getServiceEndpoint());
		System.out.println(apexConnection.getConfig().getServiceEndpoint());
	}
	
	/**
	 * Fetching the Salesforce LoginResult + UserInfo.
	 * @return
	 */
	public static SFDCInfo fetchSFDCInfo() {
		App.logInfo("Creating Partner Connection");
		if(App.sfdcInfo.getLoginResult() != null) return App.sfdcInfo;
		try {
//			System.out.println(App.getUserName() + " " + App.getUserPassword() + App.getSecurityToken() + " " + App.getPartnerUrl());
			LoginResult loginResult = loginToSalesforce();
			
			App.sfdcInfo.setLoginResult(loginResult);
			App.sfdcInfo.setOrg(loginResult.getUserInfo().getOrganizationId());
			App.sfdcInfo.setUserId(loginResult.getUserId());
			App.sfdcInfo.setSessionId(loginResult.getSessionId());
			String sept = loginResult.getServerUrl();
//			System.out.println("Service Endpoint " + sept);
			sept = sept.substring(0, sept.indexOf(".com") + 4);
			App.sfdcInfo.setEndpoint(sept);
			
			App.logInfo("SDCF Info:\n" + App.sfdcInfo.toString());
			return App.sfdcInfo;
		} catch (ConnectionException ce) {
			ce.printStackTrace();
			return null;
		}
	}

	private static SoapConnection createApexConnection() {
		if(apexConnection != null) return apexConnection;
		try {
			LoginResult loginResult = loginToSalesforce();
			ConnectorConfig soapConfig = new ConnectorConfig();
			soapConfig.setAuthEndpoint(App.getPartnerUrl());
			soapConfig.setServiceEndpoint(loginResult.getServerUrl().replace("/u/", "/s/"));
			soapConfig.setSessionId(loginResult.getSessionId());
			apexConnection = new SoapConnection(soapConfig);
			return apexConnection;

		} catch (ConnectionException ce) {
			throw new RuntimeException("Error connecting to salesforce from Apex Util", ce);
		}
	}
	
	/**
	 *  Creating Metadata Connection
	 * @return
	 */
	public static MetadataConnection createMetadataConnection() {
		if(metadataConnection != null) return metadataConnection;
		
		try {
			final LoginResult login = loginToSalesforce();
			metadataConnection = createMetadataConnection(login);
		}
		catch(ConnectionException ce) {
			App.logError("Connection Exception " + ce.getLocalizedMessage());
			ce.printStackTrace();
			return null;
		}
		return metadataConnection;
	}
	
	private static MetadataConnection createMetadataConnection(final LoginResult login) throws ConnectionException {
		ConnectorConfig config = new ConnectorConfig();
		config.setServiceEndpoint(login.getMetadataServerUrl());
		config.setSessionId(login.getSessionId());
		return new MetadataConnection(config);
	}

	private static LoginResult loginToSalesforce() throws ConnectionException {
		if(App.sfdcInfo.getLoginResult() == null) {
			ConnectorConfig config = new ConnectorConfig();
			config.setAuthEndpoint(App.getPartnerUrl());
			config.setUsername(App.getUserName());
			config.setPassword(App.getUserPassword() + App.getSecurityToken());
			partnerConnection = Connector.newConnection(config);
			return partnerConnection.login(App.getUserName(), App.getUserPassword() + App.getSecurityToken());
		}
		else
			return App.sfdcInfo.getLoginResult();
	}

}
