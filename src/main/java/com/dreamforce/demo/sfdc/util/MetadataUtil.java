package com.dreamforce.demo.sfdc.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.dreamforce.demo.App;
import com.sforce.soap.metadata.AsyncResult;
import com.sforce.soap.metadata.CustomField;
import com.sforce.soap.metadata.CustomObject;
import com.sforce.soap.metadata.DeploymentStatus;
import com.sforce.soap.metadata.FieldType;
import com.sforce.soap.metadata.Metadata;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.soap.metadata.Picklist;
import com.sforce.soap.metadata.PicklistValue;
import com.sforce.soap.metadata.SharingModel;
import com.sforce.ws.ConnectionException;

/**
 * Sample Metadata Util with different Functions focused on creation of Objects and Fields
 * @author Sunand
 *
 */
public class MetadataUtil {

    static MetadataConnection metadataConnection ;
    
    static {
    	metadataConnection = SFDCConnection.createMetadataConnection();
    }

    public static void createFormulaFields(String objName, List<HashMap<String, String>> formulafieldsList) throws Exception {
        Metadata[] metadata = new Metadata[formulafieldsList.size()];
        int i=0;
        for(HashMap<String, String> testData : formulafieldsList) {
            CustomField custField = new CustomField();
            if(testData.get("Type").equals("CheckBox")) {
                custField.setType(FieldType.Checkbox);
            } else if(testData.get("Type").equals("Currency")) {
                custField.setType(FieldType.Currency);
                custField.setPrecision(2);
                custField.setScale(18);
            } else if(testData.get("Type").equals("Date")) {
                custField.setType(FieldType.Date);
            } else if(testData.get("Type").equals("DateTime")) {
                custField.setType(FieldType.DateTime);
            }  else if(testData.get("Type").equals("Number")) {
                custField.setType(FieldType.Number);
                custField.setPrecision(2);
                custField.setScale(18);
            } else if(testData.get("Type").equals("Percent")) {
                custField.setType(FieldType.Percent);
                custField.setPrecision(2);
                custField.setScale(18);
            } else if(testData.get("Type").equals("Text")) {
                custField.setType(FieldType.Text);
            }
            custField.setFormula(testData.get("Formula"));
            custField.setLabel(testData.get("FieldName"));
            custField.setFullName(objName+"."+testData.get("FieldName").trim().replaceAll(" ", "_")+"__c");
            custField.setDescription(testData.get("Description"));
            custField.setInlineHelpText(testData.get("HelpText"));
            metadata[i]=custField;
            i++;
        }
        createAndCheckStatus(metadata);
    }

    /**
     * Make sure not more than 10 objects are referenced in one call - salesforce limitation.
     * @param objName
     * @param fields
     * @throws com.sforce.ws.ConnectionException
     * @throws InterruptedException
     */
    public static void deletefields(String objName, String[] fields) throws ConnectionException, InterruptedException {
        Metadata[] metadata = new Metadata[fields.length];
        int i=0;
        for(String field : fields) {
            CustomField custField = new CustomField();
            custField.setFullName(objName+"."+field.trim().replaceAll(" ", "_")+"__c");
            metadata[i] = custField;
            i++;
        }
        AsyncResult[] ars = metadataConnection.delete(metadata);
        long waitTimeMilliSecs = 1000;
        for(i =0; i < ars.length; i++) {
            while (!ars[i].isDone()) {
                Thread.sleep(waitTimeMilliSecs);
                // double the wait time for the next iteration
                //waitTimeMilliSecs *= 2;
                ars = metadataConnection.checkStatus(new String[] { (ars[i]).getId() });
                App.logInfo("Status of field : "+ars[i].getMessage()+" & Status is: " + ars[i].getState());
            }
        }
        App.logInfo(" Job Done Boss!!!!!!!");
    }

    public static void createFields(String objName, String[] fields, boolean isCheckBox, boolean isPhone, boolean isUrl) throws Exception {
        Metadata[] metadata = new Metadata[fields.length];
        int i=0;
        for(String field : fields) {
            CustomField custField = new CustomField();
            if(isCheckBox) {
                custField.setType(FieldType.Checkbox);
                custField.setDefaultValue("false");
            } else if(isPhone) {
                custField.setType(FieldType.Phone);
            } else if(isUrl) {
                custField.setType(FieldType.Url);
            }
            custField.setLabel(field);
            custField.setFullName(objName+"."+field.trim().replaceAll(" ", "_")+"__c");
            custField.setDescription("This field is created from metadata api script");

            metadata[i] = custField;
            i++;
        }
        createAndCheckStatus(metadata);
    }

    public static void createPickListField(String objName, HashMap<String, String[]> pickListFields, boolean isMutipickList) throws Exception {
        Metadata[] metadata = new Metadata[pickListFields.size()];
        int i=0;
        Iterator<String> itr = pickListFields.keySet().iterator();
        String[] pkValues = null;
        String fieldName;
        while(itr.hasNext()) {
            fieldName = (String) itr.next();
            pkValues = pickListFields.get(fieldName);
            CustomField custField = new CustomField();
            if(isMutipickList) {
                custField.setType(FieldType.MultiselectPicklist);
                custField.setVisibleLines(4);
            } else {
                custField.setType(FieldType.Picklist);
            }

            custField.setLabel(fieldName);
            custField.setFullName(objName+"."+fieldName.trim().replaceAll(" ", "_")+"__c");
            custField.setDescription("This field is created from meta data script");
            Picklist p = new Picklist();
            PicklistValue[] pkValueArray = new PicklistValue[pkValues.length];
            int j=0;
            for(String value : pkValues) {
                PicklistValue picklistValue = new PicklistValue();
                picklistValue.setFullName(value.trim());
                pkValueArray[j]=picklistValue;
                j++;
            }
            p.setPicklistValues(pkValueArray);
            custField.setPicklist(p);
            metadata[i] = custField;
            i++;
        }
        createAndCheckStatus(metadata);
    }

    public static void createTextFields(String objName, String[] fields, boolean isExternalID, boolean isUnique, boolean isTextField, boolean isTextArea, boolean isTextRich) throws Exception {
        Metadata[] metadata = new Metadata[fields.length];
        int i=0;
        for(String field : fields) {
            CustomField custField = new CustomField();
            if(isTextField) {
                custField.setType(FieldType.Text);
                custField.setLength(250);
            } else if(isTextArea) {
                custField.setType(FieldType.TextArea);
            } else if(isTextRich) {
                custField.setType(FieldType.Html);
                custField.setLength(32768);
                custField.setVisibleLines(10);
            }
            if(isExternalID) {
                custField.setExternalId(true);
            }
            if(isUnique) {
                custField.setUnique(true);
                custField.setCaseSensitive(false);
            }
            custField.setLabel(field);
            custField.setFullName(objName+"."+field.replaceAll(" ", "_")+"__c");
            metadata[i] = custField;
            i++;
        }
        createAndCheckStatus(metadata);
    }

    public static void createCurrencyField(String objName, String[] fields) throws Exception {
        Metadata[] metadata = new Metadata[fields.length];
        int i=0;
        for(String field : fields) {
            CustomField custField = new CustomField();
            custField.setType(FieldType.Currency);
            custField.setLabel(field);
            custField.setScale(2);
            custField.setPrecision(18);
            custField.setFullName(objName+"."+field.replaceAll(" ", "_")+"__c");
            metadata[i] = custField;
            i++;
        }
        createAndCheckStatus(metadata);
    }

    public static void createDateField(String objName, String[] fields, boolean isDateTime) throws Exception {
        Metadata[] metadata = new Metadata[fields.length];
        int i=0;
        for(String field : fields) {
            CustomField custField = new CustomField();
            if(isDateTime) {
                custField.setType(FieldType.DateTime);
            } else {
                custField.setType(FieldType.Date);
            }
            custField.setLabel(field);
            custField.setFullName(objName+"."+field.replaceAll(" ", "_")+"__c");
            metadata[i] = custField;
            i++;
        }
        createAndCheckStatus(metadata);
    }

    public static void createNumberField(String objName, String[] fields, boolean isPercentage) throws Exception {
        Metadata[] metadata = new Metadata[fields.length];
        int i=0;
        for(String field : fields) {
            CustomField custField = new CustomField();
            if(isPercentage) {
                custField.setType(FieldType.Percent);
            } else {
                custField.setType(FieldType.Number);
            }
            custField.setLabel(field);
            custField.setScale(2);
            custField.setPrecision(18);
            custField.setFullName(objName+"."+field.replaceAll(" ", "_")+"__c");
            metadata[i] = custField;
            i++;
        }
        createAndCheckStatus(metadata);
    }

    public static void createCustomObject(String name) throws ConnectionException, InterruptedException {
        CustomObject co = new CustomObject();
        co.setFullName(name + "__c");
        co.setDeploymentStatus(DeploymentStatus.Deployed);
        co.setDescription("Created by the Metadata API");
        co.setEnableActivities(true);
        co.setLabel(name + " Object");
        co.setPluralLabel(co.getLabel() + "s");
        co.setSharingModel(SharingModel.ReadWrite);
        CustomField nf = new CustomField();
        nf.setType(FieldType.Text);
        nf.setLabel("Name");
        co.setNameField(nf);

        AsyncResult[] ars = metadataConnection.create(new Metadata[]{co});
        AsyncResult asyncResult = ars[0];

        long waitTimeMilliSecs = 1000;
        while (!asyncResult.isDone()) {
            Thread.sleep(waitTimeMilliSecs);
            // double the wait time for the next iteration
            waitTimeMilliSecs *= 2;
            asyncResult = metadataConnection.checkStatus(new String[] {asyncResult.getId()})[0];
            App.logInfo("Status is: " + asyncResult.getState());
        }
    }
    
    public static void deleteCustomObject(String name) throws ConnectionException, InterruptedException {
        CustomObject co = new CustomObject();
        co.setFullName(name + "__c");
        AsyncResult[] ars = metadataConnection.delete(new Metadata[]{co});
        AsyncResult asyncResult = ars[0];

        long waitTimeMilliSecs = 1000;
        while (!asyncResult.isDone()) {
            Thread.sleep(waitTimeMilliSecs);
            // double the wait time for the next iteration
            waitTimeMilliSecs *= 2;
            asyncResult = metadataConnection.checkStatus(new String[] {asyncResult.getId()})[0];
            App.logInfo("Status is: " + asyncResult.getState());
        }
    }
    
    public static void createAndCheckStatus(Metadata[] metadata) throws Exception {
        AsyncResult[] ars = metadataConnection.create(metadata);
        String[] id = new String[ars.length];
        int j =0;
        boolean iserror = false;
        for(AsyncResult ar : ars) {
            id[j]=ar.getId();
            ++j;
        }
        long waitTimeMilliSecs = 1000;
        Thread.sleep(waitTimeMilliSecs);
        for(int i =0; i < id.length; i++) {
            do {
                ars = metadataConnection.checkStatus(new String[] { id[i]});
            }
            while (!ars[0].isDone());

            if(ars[0].getMessage() != null ) {
                App.logInfo("Status of field : "+ars[0].getMessage()+" & Status is: " + ars[0].getState());
                iserror = true;
            }
            if(iserror) {
                App.logError("job Done With Error's !!!!!!!");
            } else {
                App.logInfo("job Done !!!!!!!");
            }
        }
    }
}
