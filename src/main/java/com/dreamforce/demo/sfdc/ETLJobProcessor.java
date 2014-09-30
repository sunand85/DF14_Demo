package com.dreamforce.demo.sfdc;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.map.ObjectMapper;

import com.dreamforce.demo.App;
import com.dreamforce.demo.db.H2Db;
import com.dreamforce.demo.sfdc.JobInfo.PreProcess;
import com.dreamforce.demo.sfdc.JobInfo.SfdcExtract;
import com.dreamforce.demo.sfdc.JobInfo.SfdcLoad;
import com.dreamforce.demo.sfdc.JobInfo.Transform;
import com.dreamforce.demo.sfdc.JobInfo.Transform.TableInfo;
import com.dreamforce.demo.sfdc.JobInfo.Transform.TableInfo.Columns;
import com.dreamforce.demo.sfdc.api.SfdcBulkApi;
import com.dreamforce.demo.sfdc.api.SfdcBulkOperationImpl;
import com.dreamforce.demo.sfdc.bean.BulkJobInfo;
import com.dreamforce.demo.sfdc.bean.SFDCInfo;
import com.dreamforce.demo.sfdc.metadata.Entity;
import com.dreamforce.demo.util.Validator;

/**
 * ETL Happens Here look in to the Job Defintion for more details, which is located
 * in resources/datagen/jobs
 * 
 * @author Sunand
 */
public class ETLJobProcessor implements IJobProcessor {

	private String api_version = "29.0";
	private String async_url = "/services/async/";
	private String async_job_url = "";
	static Map<String, String> pMap;
	
	private String dropTableQuery = "DROP TABLE IF EXISTS ";
	private String resDir = "./resources/datagen/";
	private SfdcBulkOperationImpl op;
	private SFDCInfo info;
	private JobInfo jobInfo;
	private ObjectMapper mapper;
	private H2Db db;
	private SfdcBulkApi bulkApi;
	
	public ETLJobProcessor(SFDCInfo info) {
		// TODO Auto-generated constructor stub
		this.info = info;
		op = new SfdcBulkOperationImpl(info.getSessionId());
		async_job_url = info.getEndpoint() + async_url + api_version + "/job";
		bulkApi = new SfdcBulkApi(info);
		mapper = new ObjectMapper();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	public void getListOfJobs() {
	}

	/**
	 * Execute the Job Defined in the Json
	 */
	public BulkJobInfo execute(JobInfo jobInfo) {
		// TODO Auto-generated method stub
		Entity ent = new Entity();
		try{
			//Creates a db for the particular job
			db = new H2Db("jdbc:h2:~/" + jobInfo.getJobName(),"sa","");
			String metaDataFunction = jobInfo.getMetaDataFunction();
			if(!Validator.isBlank(metaDataFunction)) {				
				Method method = ent.getClass().getMethod(metaDataFunction);
				method.invoke(ent);
			}
			//Pre Process Especially for Usage Data
			PreProcess preProcess = jobInfo.getPreProcessRule();
			if(preProcess != null){
				File inputFile = new File(preProcess.getInputFile());
				File outputFile = new File(preProcess.getOutputFile());
				if(preProcess.isMonthly()) {
					outputFile = FileProcessor.generateMonthlyUsageData(inputFile, preProcess.getFieldName(), outputFile);
				}
				else if(preProcess.isWeekly()) {
					outputFile = FileProcessor.generateWeeklyUsageData(inputFile, preProcess.getFieldName(), outputFile);
				}
				else if(preProcess.isDaysToAdd()){
					outputFile=FileProcessor.generateResponseSubmissionDate(inputFile, preProcess.getFieldName(), outputFile);
				}
				if(FileUtils.sizeOf(outputFile) > 0) {
					App.logInfo("Data is Ready");
				}
				else
					throw new RuntimeException("Sorry Boss Pre Processing Failed");
			}
			
			//Extraction Code
			SfdcExtract pull = jobInfo.getExtractionRule();
			if(pull != null && !pull.toString().contains("null")) {
				String query = QueryBuilder.buildSOQLQuery(pull.getTable(), pull.getFields());
				bulkApi.pullDataFromSfdc(pull.getTable(), query, pull.getOutputFileLoc());
			}
			else
				App.logInfo("Nothing to extract. Check the Job Details");
			
			//Mapping and Transformation Code.
			Transform transform = jobInfo.getTransformationRule();
			File transFile = null;
            File pushFile  = null;
			LinkedHashMap<String, List<Columns>> finalFields= new LinkedHashMap<String, List<Columns>>();
			ArrayList<String> joinColumn = new ArrayList<String>();
            SfdcLoad load = jobInfo.getLoadRule();
			if(transform != null && !transform.toString().contains("null")) {
				if(transform.isJoin()) {
					transFile = new File(transform.getOutputFileLoc());
					ArrayList<TableInfo> tables = transform.getTableInfo();
					for(TableInfo tableInfo : tables) {
						db.executeStmt(dropTableQuery + tableInfo.getTable());
						String createTableFromCSv = "CREATE TABLE " + tableInfo.getTable() + " AS SELECT * FROM CSVREAD('"+ tableInfo.getCsvFile() +"')";
						db.executeStmt(createTableFromCSv);
						finalFields.put(tableInfo.getTable(), tableInfo.getColumns());
						joinColumn.add(tableInfo.getJoinColumnName());
					}
					
					//Building Join Query
					String finalQuery;
					if(!transform.isJoinUsingQuery()) {
                        if(load != null && load.getOperation().equals("upsert")) {
                            finalQuery = QueryBuilder.buildRightJoinQuery(finalFields, joinColumn, "=");
                        } else {
                            finalQuery = QueryBuilder.buildJoinQuery(finalFields, joinColumn, "=");
                        }

						App.logInfo("Final Query by forming join query at run time : " + finalQuery);
					}//Or use Query provided by the user
					else {
						finalQuery = transform.getQuery();
						App.logInfo("Final Query provided by the user : " + finalQuery);
					}
						
					db.executeStmt("call CSVWRITE ( '" + transform.getOutputFileLoc() + "', '" + finalQuery + "' ) ");
					if(transFile.exists())
						App.logInfo("Success");
					else
						App.logInfo("Something went wrong");
				}
			}
			
			//Load the Data back to SFDC
			BulkJobInfo bulkInfo = null;
			if(load != null) {
				if(load.getOperation().equals("upsert")) {
					bulkInfo =  bulkApi.pushDataToSfdc(load.getsObject(), load.getOperation(), (transform !=null && transform.isPicklist()) ? pushFile : new File(load.getFile()), load.getExternalIDField());
				} else{
					bulkInfo =  bulkApi.pushDataToSfdc(load.getsObject(), load.getOperation(), (transform !=null && transform.isPicklist()) ? pushFile : new File(load.getFile()));
				}
			}
			
			return bulkInfo;
		}
		catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} finally {
			//Closing Db Connection
			db.close();
		}
		
		return null;
	}
	
	/**
	 * Simple Clean Up Operation, It queries the ID by pull mechanism and uses the same to delete the id with push mechanism
	 * @param sObject - Name of the object where data should be deleted.
     * @param condition - Where condition need to be supplied - {Ex: JBCXM__Stage__r.Name = 'New Business' (or) JBCXM__ASV__c > 2000}
	 * @throws IOException
	 */
	public void cleanUp(String sObject, String condition) throws IOException {
		App.logInfo("Pulling " + sObject);
		String query = QueryBuilder.buildSOQLQuery(sObject, "Id");
		App.logInfo("Pull Query : " + query);
        if(condition !=null) {
            query = query+" Where "+condition;
            App.logInfo("Where Attached Pull Query : " + query);
        }
		String path = "./resources/datagen/process/" + sObject + "_cleanup.csv";
		App.logInfo("Output File Loc : " + path);
		bulkApi.pullDataFromSfdc(sObject, query, path);
		File f = new File(path);
		if(f.exists())
			App.logInfo("Pull Completed");
		else
			App.logInfo("Pull Failed");
		
		App.logInfo("Now Lets Delete some data");
		bulkApi.pushDataToSfdc(sObject, "delete", f);
		App.logInfo("push done");
		
	}
	
	/**
	 * Simple Clean Up Operation, It queries the ID by pull mechanism and uses the same to delete the id with push mechanism
	 * @param sObject
	 * @throws IOException
	 */
	public void cleanUp(String sObject, int limit) throws IOException {
		//I need to write logic considering governor limits
		App.logInfo("Pulling " + sObject);
		String query = QueryBuilder.buildSOQLQuery(sObject, limit, "Id");
		App.logInfo("Pull Query : " + query);
		String path = "./resources/datagen/process/" + sObject + "_cleanup.csv";
		App.logInfo("Output File Loc : " + path);
		bulkApi.pullDataFromSfdc(sObject, query, path);
		File f = new File(path);
		if(f.exists())
			App.logInfo("Pull Completed");
		else
			App.logInfo("Pull Failed");
		
		App.logInfo("Now Lets Delete some data");
		bulkApi.pushDataToSfdc(sObject, "delete", f);
		App.logInfo("push done");
		
	}

	public void init() throws IOException {
		// TODO Auto-generated method stub
	}
}
