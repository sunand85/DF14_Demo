package com.dreamforce.demo.sfdc.bean;

import java.lang.reflect.Field;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "jobInfo")
public class BulkJobInfo {

	@Element
	String id;

	@Element
	String operation;

	@Element
	String object;

	@Element
	String state;
	
	@Element(required = false)
	String externalIdFieldName;

	@Element(type = Integer.class)
	int numberBatchesTotal;

	@Element(type = Integer.class)
	int numberBatchesFailed;

	@Element(type = Integer.class)
	int numberBatchesCompleted;

	@Element(type = Integer.class)
	int numberBatchesInProgress;

	@Element(type = Integer.class)
	int numberRecordsProcessed;
	
	@Element(type = Integer.class, required = false)
	int numberRecordsFailed;

	// If required use these params
	@Element
	String createdById;
	@Element
	String createdDate;
	@Element
	String systemModstamp;
	@Element
	String concurrencyMode;
	@Element
	String contentType;

	@Element(type = Integer.class)
	int numberBatchesQueued;
	@Element(type = Integer.class)
	int numberRetries;
	@Element
	String apiVersion;
	@Element
	String totalProcessingTime;
	@Element
	String apiActiveProcessingTime;
	@Element
	String apexProcessingTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getExternalIdFieldName() {
		return externalIdFieldName;
	}

	public void setExternalIdFieldName(String externalIdFieldName) {
		this.externalIdFieldName = externalIdFieldName;
	}

	public int getNumberBatchesTotal() {
		return numberBatchesTotal;
	}

	public void setNumberBatchesTotal(int numberBatchesTotal) {
		this.numberBatchesTotal = numberBatchesTotal;
	}

	public int getNumberBatchesFailed() {
		return numberBatchesFailed;
	}

	public void setNumberBatchesFailed(int numberBatchesFailed) {
		this.numberBatchesFailed = numberBatchesFailed;
	}

	public int getNumberBatchesCompleted() {
		return numberBatchesCompleted;
	}

	public void setNumberBatchesCompleted(int numberBatchesCompleted) {
		this.numberBatchesCompleted = numberBatchesCompleted;
	}

	public int getNumberBatchesInProgress() {
		return numberBatchesInProgress;
	}

	public void setNumberBatchesInProgress(int numberBatchesInProgress) {
		this.numberBatchesInProgress = numberBatchesInProgress;
	}

	public int getNumberRecordsProcessed() {
		return numberRecordsProcessed;
	}

	public void setNumberRecordsProcessed(int numberRecordsProcessed) {
		this.numberRecordsProcessed = numberRecordsProcessed;
	}

	public int getNumberRecordsFailed() {
		return numberRecordsFailed;
	}

	public void setNumberRecordsFailed(int numberRecordsFailed) {
		this.numberRecordsFailed = numberRecordsFailed;
	}
	
	public String getCreatedById() {
		return createdById;
	}

	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getSystemModstamp() {
		return systemModstamp;
	}

	public void setSystemModstamp(String systemModstamp) {
		this.systemModstamp = systemModstamp;
	}

	public String getConcurrencyMode() {
		return concurrencyMode;
	}

	public void setConcurrencyMode(String concurrencyMode) {
		this.concurrencyMode = concurrencyMode;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public int getNumberBatchesQueued() {
		return numberBatchesQueued;
	}

	public void setNumberBatchesQueued(int numberBatchesQueued) {
		this.numberBatchesQueued = numberBatchesQueued;
	}

	public int getNumberRetries() {
		return numberRetries;
	}

	public void setNumberRetries(int numberRetries) {
		this.numberRetries = numberRetries;
	}

	public String getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	public String getTotalProcessingTime() {
		return totalProcessingTime;
	}

	public void setTotalProcessingTime(String totalProcessingTime) {
		this.totalProcessingTime = totalProcessingTime;
	}

	public String getApiActiveProcessingTime() {
		return apiActiveProcessingTime;
	}

	public void setApiActiveProcessingTime(String apiActiveProcessingTime) {
		this.apiActiveProcessingTime = apiActiveProcessingTime;
	}

	public String getApexProcessingTime() {
		return apexProcessingTime;
	}

	public void setApexProcessingTime(String apexProcessingTime) {
		this.apexProcessingTime = apexProcessingTime;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer summary = new StringBuffer();
		Field[] fList = this.getClass().getDeclaredFields();
		for (Field f : fList) {
			try {
				summary.append(f.getName() + " : " + f.get(this) + " | ");
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return summary.toString();
	}
}
