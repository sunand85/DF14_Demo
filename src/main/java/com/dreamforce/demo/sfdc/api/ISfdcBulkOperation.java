package com.dreamforce.demo.sfdc.api;

import java.io.File;
import java.io.IOException;

/**
 * SFDC Operation's Data Contract
 * @author Sunand
 *
 */
public interface ISfdcBulkOperation {
	
	/**
	 * Create a Job in SFDC using Bulk API.  
	 * 
	 * @param uri Rest URL for creating JOB
	 * @param operation insert, query, upsert, delete.
	 * @param sObject Sales Force Object Name
	 * @param concurrencyMode sequential or parallel
	 * @param contentType csv or xml
	 * @return Returns Job ID
	 * @throws IOException
	 */
	public String createJob(String uri, String operation, String sObject,
			String concurrencyMode, String contentType) throws IOException;
	
	/**
	 * Adding batch to job. This method is for querying an sObject. Make sure that you frame the URL after running the createJob method.
	 * 
	 * @param uri Rest URL for adding Batch to Job
	 * @param query Build a query using Query Builder
	 * @return Returns Batch ID
	 * @throws IOException
	 */
	public String addBatchToJob(String uri, String query) throws IOException;
	
	/**
	 * Adding batch to job. This method is for insert/upsert an sObject. Make sure that you frame the URL after running the createJob method.
	 * 
	 * @param uri Rest URL for adding Batch to Job
	 * @param csvFile Frame the data required in CSV format with proper headers.
	 * @return Returns Batch ID
	 * @throws IOException
	 */
	public String addBatchToJob(String uri, File csvFile) throws IOException;
	
	/**
	 * Monitor Batch Status for progress/completion
	 * 
	 * @param uri Rest URL for fetching Batch Status
	 * @return Returns batch status
	 */
	public String getBatchStatus(String uri);
	
	/**
	 * Monitor Job Status for progree/completion
	 * 
	 * @param uri Rest URL for fetching Job Status
	 * @return 
	 */
	public String getJobStatus(String uri);
	
	/**
	 * Fetches the output of a Job
	 * 
	 * @param uri Rest URL for fetching Result
	 * @return Returns entire result
	 */
	public String fetchResult(String uri);
	
	/**
	 * Setting Job State either Close or Abort based on the need.
	 * 
	 * @param uri Rest URL for Setting Job State
	 * @param jobState closed or abort
	 * @return Returns Job State
	 * @throws IOException
	 */
	public String setJobState(String uri, String jobState) throws IOException;
	
}
