package com.dreamforce.demo.sfdc.api;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.dreamforce.demo.App;
import com.dreamforce.demo.sfdc.bean.BulkJobInfo;
import com.dreamforce.demo.util.Template;
import com.dreamforce.demo.webaction.Header;
import com.dreamforce.demo.webaction.HttpResponseObj;
import com.dreamforce.demo.webaction.WebAction;

/**
 * SFDC Operations Implementation
 * @author Sunand
 *
 */
public class SfdcBulkOperationImpl implements ISfdcBulkOperation {

	public static String session_id = "";
	
	public String basedir = System.getProperty("basedir", ".");
	public String templatePath = basedir + "/resources/template";
	public String createJobTemplate = templatePath + "/CreateJobTemplate.xml";
    public String createUpsertJobTemplate = templatePath + "/CreateUpsertJobTemplate.xml";
	public String jobStateTemplate = templatePath + "/JobStateTemplate.xml";
	
	WebAction wa;
	
	public SfdcBulkOperationImpl(String sessionId) {
		wa = new WebAction();
		session_id = sessionId;
		File resultDir = new File(basedir + "/result");
		if(!resultDir.exists()) {
			resultDir.mkdir();
		}
	}
	
	public String createJob(String uri, String operation, String sObject,
			String concurrencyMode, String contentType) throws IOException {
		File templateFile = new File(createJobTemplate);
		File outputFile = new File(basedir + "/result/"+ sObject +"_CreateJob.xml");
		Template t = new Template(templateFile);
		t.setValue("operation", operation);
		t.setValue("object", sObject);
		t.setValue("concurrencyMode", concurrencyMode);
		t.setValue("contentType", contentType);
		t.export(new FileOutputStream(outputFile));

		FileEntity entity = new FileEntity(outputFile, ContentType.create("text/xml", "UTF-8"));
		HttpResponseObj resp = null;
		
		try {
			App.logInfo("Create Job URL: " + uri);
			Header h = new Header();
			h.addHeader("X-SFDC-Session", session_id);
			h.addHeader("Accept", "application/xml");
			h.addHeader("Content-Type", "application/xml");
			resp = wa.doPost(uri, h.getAllHeaders(), entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		App.logInfo("Job Response:\n" + resp.getContent());
		printSFJobInformation(resp.getContent());
		return parseXMLResponse(resp.getContent(), "id");
	}

    /**
     *
     * @param uri
     * @param operation
     * @param sObject
     * @param concurrencyMode
     * @param contentType
     * @param externalIDField
     * @return
     * @throws IOException
     */
    public String createJob(String uri, String operation, String sObject,
                            String concurrencyMode, String contentType, String externalIDField) throws IOException {
        File templateFile = new File(createUpsertJobTemplate);
        File outputFile = new File(basedir + "/result/"+ sObject +"_CreateJob.xml");
        Template t = new Template(templateFile);
        t.setValue("operation", operation);
        t.setValue("object", sObject);
        t.setValue("externalIdFieldName", externalIDField);
       // t.setValue("concurrencyMode", concurrencyMode);
        t.setValue("contentType", contentType);
        t.export(new FileOutputStream(outputFile));

        FileEntity entity = new FileEntity(outputFile, ContentType.create("text/xml", "UTF-8"));
        HttpResponseObj resp = null;

        try {
            App.logInfo("Create Job URL: " + uri);
            Header h = new Header();
            h.addHeader("X-SFDC-Session", session_id);
            h.addHeader("Accept", "application/xml");
            h.addHeader("Content-Type", "application/xml");
            resp = wa.doPost(uri, h.getAllHeaders(), entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        App.logInfo("Job Response:\n" + resp.getContent());
        return parseXMLResponse(resp.getContent(), "id");
    }

	public String addBatchToJob(String uri, String query) throws IOException {
		StringEntity entity = new StringEntity(query, ContentType.create("text/plain", "UTF-8"));
		HttpResponseObj resp = null;
		try {
			App.logInfo("Adding Batch to Job URL: " + uri);
			Header h = new Header();
			h.addHeader("X-SFDC-Session", session_id);
			h.addHeader("Content-Type", "text/csv");
			resp = wa.doPost(uri, h.getAllHeaders(), entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		App.logInfo("Batch Response:\n" + resp.getContent());
		return parseXMLResponse(resp.getContent(), "id");
	}
	
	public String[] addBatchToJob(String uri, String[] queries) throws IOException {
		String[] ids = new String[queries.length];
		int index = 0;
		for(String query : queries){
			ids[index] = addBatchToJob(uri, query);
		}
		return ids;
	}
	
	public String addBatchToJob(String uri, File csvFile) throws IOException {
		FileEntity entity = new FileEntity(csvFile, ContentType.create("text/csv", "UTF-8"));
		HttpResponseObj resp = null;
		try {
			App.logInfo("Adding Batch to Job URL: " + uri);
			Header h = new Header();
			h.addHeader("X-SFDC-Session", session_id);
			h.addHeader("Content-Type", "text/csv");
			resp = wa.doPost(uri, h.getAllHeaders(), entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		App.logInfo("Batch Response:\n" + resp.getContent());
		return parseXMLResponse(resp.getContent(), "id");
	}
	
	public String[] addBatchToJob(String uri, List<File> csvFiles) throws IOException {
		String[] ids = new String[csvFiles.size()];
		int index = 0;
		for(File file : csvFiles){
			ids[index] = addBatchToJob(uri, file);
		}
		return ids;
	}

	public String getBatchStatus(String uri) {
//        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        Calendar cal = Calendar.getInstance();
		HttpResponseObj resp = null;
		try {
			App.logInfo("Getting Batch Status URL: " + uri);
			Header h = new Header();
			h.addHeader("X-SFDC-Session", session_id);
			resp = wa.doGet(uri, h.getAllHeaders());
		} catch (Exception e) {
			e.printStackTrace();
		}
		App.logInfo("Batch Status Response:\n" + resp.getContent());
		String status = parseXMLResponse(resp.getContent(), "state");
		int numberRecordsFailed = Integer.parseInt(parseXMLResponse(resp.getContent(), "numberRecordsFailed"));
		if(status.equalsIgnoreCase("Completed") && numberRecordsFailed == 0 ) 
			return "Completed";
		else if(status.equalsIgnoreCase("Completed") && numberRecordsFailed > 0 ) {
            //https://instance_name—api.salesforce.com/services/async/APIversion/job/jobid/batch/batchId/request
            Header h = new Header();
            h.addHeader("X-SFDC-Session", session_id);
            try {
                resp = wa.doGet(uri+"/result", h.getAllHeaders());
            } catch (Exception e) {
                e.printStackTrace();
            }
            App.logInfo("Batch Status Response:\n" + resp.getContent());
            App.logInfo("Batch completed with failures.");
			App.logInfo("Operation : " + parseXMLResponse(resp.getContent(), "operation"));
			App.logInfo("Number of Records Processed : " + parseXMLResponse(resp.getContent(), "numberRecordsProcessed"));
			App.logInfo("Number of Records Failed : " + numberRecordsFailed);
			return "Records Failed";
		}
		else if(status.equalsIgnoreCase("Failed")) 
			return "Failed";
		else
			return parseXMLResponse(resp.getContent(), "state");
	}
	
	public void getBatchStatus(List<String> uris) {
		
	}

	public String getJobStatus(String uri) {
		// TODO Auto-generated method stub
		HttpResponseObj resp = null;
		try {
			App.logInfo("Getting Job Status URL: " + uri);
			Header h = new Header();
			h.addHeader("X-SFDC-Session", session_id);
			resp = wa.doGet(uri, h.getAllHeaders());
		} catch (Exception e) {
			e.printStackTrace();
		}
		App.logInfo("JOB Status Response:\n" + resp.getContent());
		printSFJobInformation(resp.getContent());
		return resp.getContent();
	}
	
	public void printSFJobInformation(String jobContent) {
		Serializer serializer = new Persister();
		BulkJobInfo info = null;
		try {
			info = serializer.read(BulkJobInfo.class, jobContent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		App.logInfo("SF Job Information\n" + info.toString());
	}

	public String fetchResult(String uri) {
		String resultId = getResultId(uri +"/result");
		HttpResponseObj resp = null;
		try {
			uri = uri +"/result/"+ resultId;
			App.logInfo("Getting Batch Status URL: " + uri);
			Header h = new Header();
			h.addHeader("X-SFDC-Session", session_id);
			resp = wa.doGet(uri, h.getAllHeaders());
		} catch (Exception e) {
			e.printStackTrace();
		}
		App.logInfo("Fetched Result.");
		return resp.getContent();
	}

	public String setJobState(String uri, String jobState) throws IOException {
		File templateFile = new File(jobStateTemplate);
		String job_id = uri.substring(uri.lastIndexOf("/"), uri.length());
		File outputFile = new File(basedir + "/result/" + job_id + "_JobState.xml");
		
		Template t = new Template(templateFile);
		t.setValue("state", jobState);
		t.export(new FileOutputStream(outputFile));

		FileEntity entity = new FileEntity(outputFile, ContentType.create("text/xml", "UTF-8"));
		HttpResponseObj resp = null;
		try {
			App.logInfo("Settting Job State URL: " + uri);
			Header h = new Header();
			h.addHeader("X-SFDC-Session", session_id);
			h.addHeader("Content-Type", "text/csv");
			resp = wa.doPost(uri, h.getAllHeaders(), entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		App.logInfo("Job State Response:\n" + resp.getContent());
		printSFJobInformation(resp.getContent());
		String status = parseXMLResponse(resp.getContent(), "state");
		App.logInfo("Job Status: " + status);
		return status;
	}
	
	private String getResultId(String uri) {
		String resultId = null;
		HttpResponseObj resp = null;
		try {
			App.logInfo("Getting Batch Status URL: " + uri);
			Header h = new Header();
			h.addHeader("X-SFDC-Session", session_id);
			resp = wa.doGet(uri, h.getAllHeaders());
		} catch (Exception e) {
			e.printStackTrace();
		}
		App.logInfo("Result ID Response:\n" + resp.getContent());
		resultId = parseXMLResponse(resp.getContent(), "result");
		App.logInfo("Result ID: " + resultId);
		
		return resultId;
	}

	/**
	 * Parse XML file Using JDOM to get the value of specific Node from Root Node.
	 * Mainly to fetch values of Job ID, batch ID, Result ID etc.
	 * @param content
	 * @param nodeToSearch
	 * @return
	 */
	public static String parseXMLResponse(String content,
			String nodeToSearch) {
		String result = null;
		SAXBuilder builder = new SAXBuilder();
		try {
			Document document = (Document) builder
					.build(new ByteArrayInputStream(content.getBytes()));
			Element rootNode = document.getRootElement();
			List<Element> list = rootNode.getChildren();
			for (int i = 0; i < list.size(); i++) {
				Element node = list.get(i);
				if (node.getName().equals(nodeToSearch)) {
					App.logInfo("Node: " + nodeToSearch + " || value: "	+ node.getText());
					result = node.getText();
					break;
				}
			}
		} catch (IOException io) {
			App.logInfo(io.getMessage());
		} catch (JDOMException jdomex) {
			App.logInfo(jdomex.getMessage());
		}
		return result;
	}
}
