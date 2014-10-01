package com.dreamforce.demo.sfdc.metadata;

import com.dreamforce.demo.App;
import com.dreamforce.demo.sfdc.util.MetadataUtil;
import com.dreamforce.demo.util.Report;

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
}
