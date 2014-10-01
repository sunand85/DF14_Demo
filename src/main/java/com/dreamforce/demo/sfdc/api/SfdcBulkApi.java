package com.dreamforce.demo.sfdc.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.dreamforce.demo.App;
import com.dreamforce.demo.sfdc.QueryBuilder;
import com.dreamforce.demo.sfdc.bean.BulkJobInfo;
import com.dreamforce.demo.sfdc.bean.SFDCInfo;
import com.dreamforce.demo.sfdc.bean.SFDCOperationType;
import com.dreamforce.demo.util.Report;

/**
 * Standalone class for SFDC push/pull mechanism
 * 
 * @author Sunand
 * 
 */
public class SfdcBulkApi {

	private String api_version = "29.0";
	private String async_url = "/services/async/";
	private String async_job_url = "";
	private final int BULK_INSERT_LIMIT = 10000;
	private String basedir = System.getProperty("user.dir");

	private SfdcBulkOperationImpl op;
	private SFDCInfo info;

	public SfdcBulkApi(SFDCInfo info) {
		this.info = info;
		this.op = new SfdcBulkOperationImpl(info.getSessionId());
		async_job_url = info.getEndpoint() + async_url + api_version + "/job";
	}
	
	/**
	 * Push Data to SFDC with External ID field
	 * 
	 * @param sObject
	 * @param operation
	 * @param csvFile
	 * @param externalIDField
	 *            - for upsert operation, a field should be specified.
	 * @throws IOException
	 */
	public BulkJobInfo pushDataToSfdc(String sObject, String operation,	File csvFile, String externalIDField) throws IOException {
		// Create Job with parameters and change the last parameter to csv or
		// xml for the 5th parameter to change the response content type while
		// registering a job
		String job_id;
		if (externalIDField != null && !externalIDField.isEmpty())
			job_id = op.createJob(async_job_url, operation, sObject,
					"Parallel", "CSV", externalIDField);
		else
			job_id = op.createJob(async_job_url, operation, sObject,
					"Parallel", "CSV");

		if (job_id == null)
			throw new RuntimeException("Failed to create bulk JOB. Check the logs for more info.");

		// Framing the url's for batch from resulting job url
		String async_job_status_url = async_job_url + "/" + job_id;
		String async_batch_url = async_job_status_url + "/batch";

		// Splitting files based on job_id and deleting them once batch is
		// created.
		List<String> batchUrls = splitFilesToCreateBatch(job_id, async_batch_url, csvFile);

		op.setJobState(async_job_status_url, "Closed");
		
		// Wait until all batch completes
		boolean waitResult = waitUntilAllBatchJobCompletes(job_id, batchUrls, 10, 600);
		/*if (waitResult)
			op.setJobState(async_job_status_url, "Closed");
		else {
			App.logInfo("Please Check the batch Result in Logs. It could be either a Partial or Full Failure.");
			op.setJobState(async_job_status_url, "Aborted");
		}*/
		if(!waitResult)
			App.logInfo("Please Check the batch Result in Logs. It could be either a Partial or Full Failure.");
		
		//Fetching Job Status
		String result = op.getJobStatus(async_job_status_url);
		return fetchJobInfo(result);
	}

	/**
	 * Push data to SFDC
	 * 
	 * @param sObject
	 * @param operation
	 * @param csvFile
	 * @throws IOException
	 */
	public BulkJobInfo pushDataToSfdc(String sObject, String operation,	File csvFile) throws IOException {
		return pushDataToSfdc(sObject, operation, csvFile , "");
	}

/*	public void pushDataToSfdc(String sObject, String operation, File csvFile, int recordCount)
			throws IOException {
		// Create Job with parameters and change the last parameter to csv or
		// xml for the 5th parameter to change the response content type while
		// registering a job
		String job_id = op.createJob(async_job_url, operation.toString(), sObject, "Parallel", "CSV");
		if (job_id == null)
			throw new RuntimeException("Failed to create bulk JOB. Check the logs for more info.");

		// Framing the url's for batch from resulting job url
		String async_job_status_url = async_job_url + "/" + job_id;
		String async_batch_url = async_job_status_url + "/batch";

		// Splitting files based on job_id and deleting them once batch is
		// created.
		List<String> batchUrls = splitFilesToCreateBatch(job_id, async_batch_url, csvFile, recordCount);
				
		// Wait until all batch completes
		boolean waitResult = waitUntilAllBatchJobCompletes(job_id, batchUrls);
		if (waitResult) {
			// Fetching result from batch
			String output = op.fetchResult(async_batch_status_url);
			Report.logInfo("OUTPUT:\n" + output);
			op.setJobState(async_job_status_url, "Closed");
		} else
			op.setJobState(async_job_status_url, "Aborted");
	}*/
	
	/**
	 * Old Code needs modification Currently marking deprecated. Works fine for pushing records less than 10K
	 * @param sObject
	 * @param operation
	 * @param csvFile
	 * @param recordCount
	 * @throws IOException
	 */
	@Deprecated
	public void pushDataToSfdc(String sObject, SFDCOperationType operation, File csvFile, int recordCount)
			throws IOException {
		// Create Job with parameters and change the last parameter to csv or
		// xml for the 5th parameter to change the response content type while
		// registering a job
		String job_id = op.createJob(async_job_url, operation.toString(),
				sObject, "Parallel", "CSV");
		if (job_id == null)
			throw new RuntimeException(
					"Failed to create bulk JOB. Check the logs for more info.");

		// Framing the url's for batch from resulting job url
		String async_job_status_url = async_job_url + "/" + job_id;
		String async_batch_url = async_job_status_url + "/batch";

		// Here I need to split the files and give it as multiple batch inputs
		// Yet to COMPLETE - SUNAND

		String batch_id = op.addBatchToJob(async_batch_url, csvFile);
		if (batch_id == null)
			throw new RuntimeException(
					"Failed to add BATCH to JOB. Check the logs for more info.");

		String async_batch_status_url = async_job_url + "/" + job_id
				+ "/batch/" + batch_id;
		// Waiting until the batch job is complete
		boolean waitResult = waitUntilBatchJobCompletes(async_batch_status_url,	10, 60);
		if (waitResult) {
			// Fetching result from batch
			String output = op.fetchResult(async_batch_status_url);
			Report.logInfo("OUTPUT:\n" + output);
			op.setJobState(async_job_status_url, "Closed");
		} else
			op.setJobState(async_job_status_url, "Aborted");
	}

	/**
	 * Pull data from SFDC
	 * 
	 * @param sObject
	 * @param query
	 * @param filePath
	 * @throws IOException
	 */
	public BulkJobInfo pullDataFromSfdc(String sObject, String query, String filePath) throws IOException {
		// Create Job with parameters and change the last parameter to csv or
		// xml for the 5th parameter to change the response content type while
		// registering a job
		String job_id = op.createJob(async_job_url, "query", sObject,
				"Parallel", "CSV");
		if (job_id == null)
			throw new RuntimeException(
					"Failed to create bulk JOB. Check the logs for more info.");

		// Framing the url's for batch from resulting job url
		String async_job_status_url = async_job_url + "/" + job_id;
		String async_batch_url = async_job_status_url + "/batch";
		String batch_id = op.addBatchToJob(async_batch_url, query);
		if (batch_id == null)
			throw new RuntimeException(
					"Failed to add BATCH to JOB. Check the logs for more info.");

		//Setting the job state to closed immediately after adding batch to job
		op.setJobState(async_job_status_url, "Closed");
		
		String async_batch_status_url = async_job_url + "/" + job_id
				+ "/batch/" + batch_id;
		// Waiting until the batch job is complete
		boolean waitResult = waitUntilBatchJobCompletes(async_batch_status_url,	10, 300);
		if (waitResult) {
			// Fetching result from batch
			String output = op.fetchResult(async_batch_status_url);
			try {
				// Save the file to the respective destination
				FileOutputStream fos = new FileOutputStream(filePath);
				fos.write(output.getBytes());
				fos.close();
				//op.setJobState(async_job_status_url, "Closed");
			} catch (FileNotFoundException e) {
				App.logInfo("Mostly File Not Found");
				//op.setJobState(async_job_status_url, "Aborted");
				e.printStackTrace();
			}
		} //else
			//op.setJobState(async_job_status_url, "Aborted");
		
		//Fetching Job Status
		String result = op.getJobStatus(async_job_status_url);
		return fetchJobInfo(result);
	}

	public BulkJobInfo cleanUp(String sObject) throws Exception {
		SfdcRestApi restApi = new SfdcRestApi(info);
		int recordCount = restApi.countOfRecordsForASObject(sObject);
		App.logInfo(sObject + " record count : " + recordCount);
		if(recordCount < 1) {
			App.logError("Nothing to Delete  : " + sObject);
			return null;
		}
		App.logInfo("Pulling all Records of " + sObject);
		String query = QueryBuilder.buildSOQLQuery(sObject, "Id");
		App.logInfo("Pull Query : " + query);
		String path = "./resources/process/" + sObject + "_cleanup.csv";
		App.logInfo("Output File Loc : " + path);
		synchronized (this) {
			pullDataFromSfdc(sObject, query, path);
		}
		File f = new File(path);
		if (f.exists())
			App.logInfo("Pull Completed");
		else
			App.logInfo("Pull Failed");

		// Here i need to split files
		App.logInfo("Now Lets Delete some data");
		BulkJobInfo info;
		synchronized (this) {
			info = pushDataToSfdc(sObject, "delete", f);
		}
		App.logInfo("push done");
		return info;
	}

	/**
	 * Here serializing Job Object from XML to POJO
	 * @param xmlResponse
	 * @return
	 */
	private BulkJobInfo fetchJobInfo(String xmlResponse) {
		Serializer serializer = new Persister();
		BulkJobInfo info = null;
		try {
			info = serializer.read(BulkJobInfo.class, xmlResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}

	protected List<String> splitFilesToCreateBatch(String job_id, String asyncBatchUrl, File csvFile)
			throws UnsupportedEncodingException, IOException {
		List<String> batchUrls = new ArrayList<String>();
		BufferedReader rdr = new BufferedReader(new FileReader(csvFile));
		File tmpFile = new File(basedir + "/resources/process/"
				+ job_id + "_tempFile.csv");
		// read the CSV header row
		byte[] headerBytes = (rdr.readLine() + "\n").getBytes("UTF-8");
		int headerBytesLength = headerBytes.length;
		FileOutputStream tmpOut = null;
		try {
			tmpOut = new FileOutputStream(tmpFile);
			int maxBytesPerBatch = 10000000; // 10 million bytes per batch
			int maxRowsPerBatch = BULK_INSERT_LIMIT; // 10 thousand rows per batch
			int currentBytes = 0;
			int currentLines = 0;
			String nextLine;
			while ((nextLine = rdr.readLine()) != null) {
				byte[] bytes = (nextLine + "\n").getBytes("UTF-8");
				// Create a new batch when our batch size limit is reached
				if (currentBytes + bytes.length > maxBytesPerBatch
						|| currentLines > maxRowsPerBatch) {
					String batch_id = op.addBatchToJob(asyncBatchUrl, tmpFile);
					if (batch_id == null)
						throw new RuntimeException(
								"Failed to add BATCH to JOB. Check the logs for more info.");
					batchUrls.add(asyncBatchUrl + "/" + batch_id);
					currentBytes = 0;
					currentLines = 0;
				}
				if (currentBytes == 0) {
					tmpOut = new FileOutputStream(tmpFile);
					tmpOut.write(headerBytes);
					currentBytes = headerBytesLength;
					currentLines = 1;
				}
				tmpOut.write(bytes);
				currentBytes += bytes.length;
				currentLines++;
			}
			// Finished processing all rows
			// Create a final batch for any remaining data
			if (currentLines > 1) {
				String batch_id = op.addBatchToJob(asyncBatchUrl, tmpFile);
				if (batch_id == null)
					throw new RuntimeException(
							"Failed to add BATCH to JOB. Check the logs for more info.");
				batchUrls.add(asyncBatchUrl + "/" + batch_id);
			}
		} finally {
			tmpOut.close();
			rdr.close();
			tmpFile.deleteOnExit();
		}

		return batchUrls;
	}
	
	
	/*private List<String> splitFilesToCreateBatch(String job_id,	String async_batch_url, File csvFile, int recordCount) {
		// TODO Auto-generated method stub
		return null;
	}*/

	/**
	 * Wait Logic To wait until the batch job status is returned as "Completed"
	 * 
	 * @param url
	 *            batch status url
	 * @param interval
	 *            time in seconds
	 * @param maxTimeout
	 *            time in seconds
	 * @throws IOException
	 */
	private boolean waitUntilBatchJobCompletes(String url, int interval, int maxTimeout) throws IOException {
		int timeCounter = interval;
		while (true) {
			String status = op.getBatchStatus(url);
			App.logInfo("Current State: " + status);
			if (status.equalsIgnoreCase("Completed"))
				return true;
			else if (status.equalsIgnoreCase("Records Failed"))
				return false;
			else if (status.equalsIgnoreCase("Failed"))
				return false;
			else if (timeCounter > maxTimeout)
				return false;
			else {
				try {
					Thread.sleep(interval * 1000);
					timeCounter += interval;
				} catch (InterruptedException e) {
					e.printStackTrace();
					App.logError("Batch Status Polling got Interrupted Mostly by a Timeout. Please Check!");
				}
			}
		}
	}

	private boolean waitUntilAllBatchJobCompletes(String job_id, List<String> batchUrls, int interval, int maxTimeOut) {
		boolean waitResult = true;
		int timeCounter = interval;
		//It should have been do while
		while (!batchUrls.isEmpty() && (timeCounter < maxTimeOut)) {
			try {
				Thread.sleep(interval * 1000);
				timeCounter += interval;
			} catch (InterruptedException e) {
			}
			//for (String batchUrl : batchUrls) {
			//Switch to Iterator To Avoid ConcurrentModificationException
			Iterator<String> iter = batchUrls.iterator();
			while(iter.hasNext()) {
				String batchUrl = iter.next();
				String status = op.getBatchStatus(batchUrl);
				App.logInfo("Current State: " + status);
				if (status.equalsIgnoreCase("Completed")) {
					App.logInfo("Batch : " + batchUrl);
					App.logInfo("Status : " + status);
					iter.remove();
					waitResult = true;
				} else if (status.equalsIgnoreCase("Records Failed")
						|| status.equalsIgnoreCase("Failed")) {
					App.logInfo("Batch : " + batchUrl);
					App.logInfo("Status : " + status);
					iter.remove();
					waitResult = false;
				}
			}
			App.logInfo("No of Batches Under Process : " + batchUrls.size());
		}

		return waitResult;
	}
	
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// pushDataToSfdc("JBCXM__UsageData__c", "insert", new
		// File("./resources/datagen/process/db_Month11UD_3.csv"));
		//pullDataFromSfdc("Account", "Select id, name from Account", "./temp.csv");
	}
}
