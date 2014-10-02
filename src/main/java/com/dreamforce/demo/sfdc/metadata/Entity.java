package com.dreamforce.demo.sfdc.metadata;

import com.dreamforce.demo.App;
import com.dreamforce.demo.sfdc.util.ApexUtil;
import com.dreamforce.demo.sfdc.util.MetadataUtil;

/**
 * Entity Class which holds all the metadata creation + running apex scripts.
 * 
 * @author Sunand
 *
 */
public class Entity {
	// private String measureFile = App.basedir +
	// "/testdata/sfdcdata/usageData/scripts/Usage_Measure_Create.txt";

	public static void createExtIdFieldOnAccount() {
		try {
			MetadataUtil.createTextFields("Account",
					new String[] { "Data ExternalId" }, true, true, true,
					false, false);
			MetadataUtil.createNumberField("Account",
					new String[] { "Auto Data Load" }, false);
		} catch (Exception e) {
			App.logError("Failed to create ext id field on account object :"
					+ e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	public static void setupOpportunityFields() {
		try {
			MetadataUtil.createDateField("Opportunity", new String[] {"Subscription_Start_Date", "Subscription_End_Date"}, false);
			MetadataUtil.createCurrencyField("Opportunity", new String[] {"ASV", "OTR"});
			MetadataUtil.createNumberField("Opportunity", new String[] {"Users"}, false);
		}
		catch (Exception e) {
			App.logError("Failed to create fields in Opportunity Standard Object");
			e.printStackTrace();
		}
	}
	
	public static void configureOrgSettings() {
		try {
			//Enabling a Remote Site Setting
			MetadataUtil.createRemoteSiteSetting("DreamForce_ExternalUrl", "https://www.google.com");
			//Creating 2 Custom Objects and its fields
			MetadataUtil.createCustomObject("MyUsageData");
			MetadataUtil.createCustomObject("MyApplicationSettings");
			MetadataUtil.createDateField("MyUsageData__c", new String[] {"Date"}, false);
			MetadataUtil.createMasterDetailRelationField("MyUsageData__c", "Account", "Account");
			MetadataUtil.createNumberField("MyUsageData__c", new String[] {"Downloads", "Units", "Amount"},  false);
			MetadataUtil.createTextFields("MyApplicationSettings__c", 
					new String[] {"AggregationType", "Granularity", "WeekStartsOn"}, 
					false, false, true, false, false);
			MetadataUtil.createTextFields("MyApplicationSettings__c", new String[] {"MeasureColMap"}, 
					false, false, false, true, false);
			//Configuring My App Settings
			ApexUtil.runApexCodeFromFile("./resources/apex_scripts/MyApplicationSettings.apex");
			App.logInfo("Configuration Done Successfully");
		} catch (Exception e) {
			e.printStackTrace();
			App.logError("Configuration Done With Errors");
			throw new RuntimeException("Configuration Done With Errors");
		}
	}
	
	public static void main (String[] args) {
		configureOrgSettings();
	}
}
