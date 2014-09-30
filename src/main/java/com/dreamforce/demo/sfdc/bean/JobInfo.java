package com.dreamforce.demo.sfdc.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("restriction")
@XmlRootElement
public class JobInfo {
	private String id;
	private String operation;
	private String createdById;
	private String object;
	private String createdDate;
	private String systemModstamp;
	private String state;
	private String concurrencyMode;
	private String contentType;
	private String numberBatchesQueued;
	private String numberBatchesInProgress;
	private String numberBatchesCompleted;
	private String numberBatchesFailed;
	private String numberBatchesTotal;
	private String numberRecordsProcessed;
	private String numberRetries;
	private String apiVersion;
	private String numberRecordsFailed;
	private String totalProcessingTime;
	private String apiActiveProcessingTime;
	private String apexProcessingTime;
	private String jobAttr;

	public String getJobAttr() {
		return jobAttr;
	}

	public void setJobAttr(String jobAttr) {
		this.jobAttr = jobAttr;
	}

	public String getId() {
		return id;
	}

	@XmlElement
	public void setId(String id) {
		this.id = id;
	}

	public String getOperation() {
		return operation;
	}

	@XmlElement
	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getCreatedById() {
		return createdById;
	}

	@XmlElement
	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}

	public String getObject() {
		return object;
	}

	@XmlElement
	public void setObject(String object) {
		this.object = object;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	@XmlElement
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getSystemModstamp() {
		return systemModstamp;
	}

	@XmlElement
	public void setSystemModstamp(String systemModstamp) {
		this.systemModstamp = systemModstamp;
	}

	public String getState() {
		return state;
	}

	@XmlElement
	public void setState(String state) {
		this.state = state;
	}

	public String getConcurrencyMode() {
		return concurrencyMode;
	}

	@XmlElement
	public void setConcurrencyMode(String concurrencyMode) {
		this.concurrencyMode = concurrencyMode;
	}

	public String getContentType() {
		return contentType;
	}

	@XmlElement
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getNumberBatchesQueued() {
		return numberBatchesQueued;
	}

	@XmlElement
	public void setNumberBatchesQueued(String numberBatchesQueued) {
		this.numberBatchesQueued = numberBatchesQueued;
	}

	public String getNumberBatchesInProgress() {
		return numberBatchesInProgress;
	}

	@XmlElement
	public void setNumberBatchesInProgress(String numberBatchesInProgress) {
		this.numberBatchesInProgress = numberBatchesInProgress;
	}

	public String getNumberBatchesCompleted() {
		return numberBatchesCompleted;
	}

	@XmlElement
	public void setNumberBatchesCompleted(String numberBatchesCompleted) {
		this.numberBatchesCompleted = numberBatchesCompleted;
	}

	public String getNumberBatchesFailed() {
		return numberBatchesFailed;
	}

	@XmlElement
	public void setNumberBatchesFailed(String numberBatchesFailed) {
		this.numberBatchesFailed = numberBatchesFailed;
	}

	public String getNumberBatchesTotal() {
		return numberBatchesTotal;
	}

	@XmlElement
	public void setNumberBatchesTotal(String numberBatchesTotal) {
		this.numberBatchesTotal = numberBatchesTotal;
	}

	public String getNumberRecordsProcessed() {
		return numberRecordsProcessed;
	}

	@XmlElement
	public void setNumberRecordsProcessed(String numberRecordsProcessed) {
		this.numberRecordsProcessed = numberRecordsProcessed;
	}

	public String getNumberRetries() {
		return numberRetries;
	}

	@XmlElement
	public void setNumberRetries(String numberRetries) {
		this.numberRetries = numberRetries;
	}

	public String getApiVersion() {
		return apiVersion;
	}

	@XmlElement
	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	public String getNumberRecordsFailed() {
		return numberRecordsFailed;
	}

	@XmlElement
	public void setNumberRecordsFailed(String numberRecordsFailed) {
		this.numberRecordsFailed = numberRecordsFailed;
	}

	public String getTotalProcessingTime() {
		return totalProcessingTime;
	}

	@XmlElement
	public void setTotalProcessingTime(String totalProcessingTime) {
		this.totalProcessingTime = totalProcessingTime;
	}

	public String getApiActiveProcessingTime() {
		return apiActiveProcessingTime;
	}

	@XmlElement
	public void setApiActiveProcessingTime(String apiActiveProcessingTime) {
		this.apiActiveProcessingTime = apiActiveProcessingTime;
	}

	public String getApexProcessingTime() {
		return apexProcessingTime;
	}

	@XmlElement
	public void setApexProcessingTime(String apexProcessingTime) {
		this.apexProcessingTime = apexProcessingTime;
	}
}
