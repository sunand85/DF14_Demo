package com.dreamforce.demo.sfdc.bean;

import java.lang.reflect.Field;

import com.sforce.soap.partner.LoginResult;

public class SFDCInfo {

	private String org;
	private String userId;
	private String sessionId;
	private String endpoint;
	private LoginResult loginResult;
	
	public LoginResult getLoginResult() {
		return loginResult;
	}

	public void setLoginResult(LoginResult loginResult) {
		this.loginResult = loginResult;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	
	@Override
	public String toString() {
		StringBuffer summary = new StringBuffer();
		Field[] fList=this.getClass().getDeclaredFields();
		for (Field f: fList){
			try {
				summary.append(f.getName()+" : "+f.get(this)+" | ");
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return summary.toString();
	}

}
