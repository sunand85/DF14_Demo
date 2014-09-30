package com.dreamforce.demo.sfdc.api;

import java.net.URLEncoder;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.dreamforce.demo.App;
import com.dreamforce.demo.sfdc.bean.SFDCInfo;
import com.dreamforce.demo.webaction.Header;
import com.dreamforce.demo.webaction.HttpResponseObj;
import com.dreamforce.demo.webaction.WebAction;

/**
 * SFDC Rest API to query objects
 * @author Sunand
 *
 */
public class SfdcRestApi {

	private String query_url;
	private SFDCInfo info;
	private WebAction wa;
	
	public SfdcRestApi(SFDCInfo info) {
		this.info = info;
		wa = new WebAction();
		query_url = info.getEndpoint() + "/services/data/v29.0/query/?q=";
	}
	
	/**
	 * @param args
	 * @throws Exception
	 */
	public int countOfRecordsForASObject(String sObject) throws Exception {
		String query = "Select COUNT(id) from " + sObject;
		App.logInfo("SFDC Query : " + query);
		query = URLEncoder.encode(query, "UTF-8");
		query_url = query_url + query;
		App.logInfo("SFDC URL : " + query_url);
		
		Header h = new Header();
		h.addHeader("Authorization", "Bearer " + info.getSessionId());
		HttpResponseObj resp = wa.doGet(query_url, h.getAllHeaders());
		String json = resp.getContent();
		App.logInfo("Count of Records of Query : " + json);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(json);
		JsonNode records = node.get("records");
		try {
			//return Integer.parseInt(node.get("expr0").toString());
			for(final JsonNode rec : records) {
				return Integer.parseInt(rec.get("expr0").toString());
			}
			return -1;
		}
		catch (NumberFormatException e) {
			App.logInfo("Response from the SFDC Rest URI - " + json);
			return -1;
		}
	}
}
