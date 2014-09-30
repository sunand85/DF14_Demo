package com.dreamforce.demo.sfdc.util;

import com.dreamforce.demo.App;
import com.dreamforce.demo.sfdc.bean.SFDCInfo;
import com.sforce.soap.partner.Connector;
import com.sforce.soap.partner.GetUserInfoResult;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.QueryResult;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

public class SFDCUtil {

	static PartnerConnection partnerConnection;
	
	public static void main(String[] args) {
		SFDCInfo info = fetchSFDCinfo(App.getUserName(),App.getUserPassword(), App.getSecurityToken());
		System.out.println(info.toString());
	}
	
	/**
	 * Fetching the Salesforce UserInfo along with session id.
	 * @return
	 */
	public static SFDCInfo fetchSFDCinfo() {
		App.logInfo("Fetching SalesForce Data");
		if(App.sfdcInfo.getSessionId() != null) return App.sfdcInfo;
		try {
			ConnectorConfig config = new ConnectorConfig();
			config.setUsername(App.getUserName());
			config.setPassword(App.getUserPassword() + App.getSecurityToken());
			config.setAuthEndpoint(App.getPartnerUrl());
			partnerConnection = Connector.newConnection(config);
			GetUserInfoResult userInfo = partnerConnection.getUserInfo();
			
			App.sfdcInfo.setOrg(userInfo.getOrganizationId());
			App.sfdcInfo.setUserId(userInfo.getUserId());
			App.sfdcInfo.setSessionId(config.getSessionId());
			String sept = config.getServiceEndpoint();
			sept = sept.substring(0, sept.indexOf(".com") + 4);
			App.sfdcInfo.setEndpoint(sept);
			
			App.logInfo("SDCF Info:\n" + App.sfdcInfo.toString());
			return App.sfdcInfo;
		} catch (ConnectionException ce) {
			ce.printStackTrace();
			return null;
		}
	}
	
	public static SFDCInfo fetchSFDCinfo(String userName, String password, String token) {
		App.logInfo("Fetching SalesForce Data");
		if(App.sfdcInfo.getSessionId() != null) return App.sfdcInfo;
		try {
			ConnectorConfig config = new ConnectorConfig();
			config.setUsername(userName);
			config.setPassword(password + token);
			config.setAuthEndpoint(App.getPartnerUrl());
			partnerConnection = Connector.newConnection(config);
			GetUserInfoResult userInfo = partnerConnection.getUserInfo();
			
			App.sfdcInfo.setOrg(userInfo.getOrganizationId());
			App.sfdcInfo.setUserId(userInfo.getUserId());
			App.sfdcInfo.setSessionId(config.getSessionId());
			String sept = config.getServiceEndpoint();
			sept = sept.substring(0, sept.indexOf(".com") + 4);
			App.sfdcInfo.setEndpoint(sept);
			
			App.logInfo("SDCF Info:\n" + App.sfdcInfo.toString());
			return App.sfdcInfo;
		} catch (ConnectionException ce) {
			ce.printStackTrace();
			return null;
		}
	}

    
    
    /**
	 * Method to return the number of records that the query has returned.
	 * @param query - SOQL query
	 * @return count - Returns the number of records that the query as queried.
	 */
	public static int getRecordCount(String query) {
		int count = 0;
		if(fetchSFDCinfo() != null) {
			try{
				QueryResult result = partnerConnection.queryAll(query);
				count = result.getSize();
			} catch(Exception e) {
				App.logInfo(e.getLocalizedMessage());
			}
			App.logInfo("No of records : " +count);
		}			
				
		return count;
	}

    
}
