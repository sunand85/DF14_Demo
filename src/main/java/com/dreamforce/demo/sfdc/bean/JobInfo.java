package com.dreamforce.demo.sfdc.bean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Job Definition Pojo Template
 * @author Sunand
 *
 */
public class JobInfo {

	String jobName;
	String jobType;
	String namespacePrefix;
	boolean useNameSpace;
	@JsonProperty("entityFunction")
	String metaDataFunction;
	boolean runAggregation;
	
	@JsonProperty("preprocess")
	PreProcess preProcess;
	@JsonProperty("extract")
	SfdcExtract extract;
	@JsonProperty("transform")
	Transform transform;
	@JsonProperty("load")
	SfdcLoad load;
	
	public boolean isRunAggregation() {
		return runAggregation;
	}

	public void setRunAggregation(boolean runAggregation) {
		this.runAggregation = runAggregation;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	
	public String getJobType() {
		return jobType;
	}
	
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getNamespacePrefix() {
		return namespacePrefix;
	}

	public void setNamespacePrefix(String namespacePrefix) {
		this.namespacePrefix = namespacePrefix;
	}

	public boolean isUseNameSpace() {
		return useNameSpace;
	}

	public void setUseNameSpace(boolean useNameSpace) {
		this.useNameSpace = useNameSpace;
	}
	
	public String getMetaDataFunction() {
		return metaDataFunction;
	}

	public void setMetaDataFunction(String metaDataFunction) {
		this.metaDataFunction = metaDataFunction;
	}

	public PreProcess getPreProcessRule() {
		return preProcess;
	}
	
	public void setPreProcess(PreProcess preProcess) {
		this.preProcess = preProcess;
	}

	public SfdcExtract getExtractionRule() {
		return extract;
	}

	public void setExtract(SfdcExtract extract) {
		this.extract = extract;
	}

	public Transform getTransformationRule() {
		return transform;
	}

	public void setTransform(Transform transform) {
		this.transform = transform;
	}

	public SfdcLoad getLoadRule() {
		return load;
	}

	public void setLoad(SfdcLoad load) {
		this.load = load;
	}

	/**
	 * To Store any pre processing of data required before ETL process
	 * Currently only file operations are supported and also only for Usage Data
	 * @author Sunand
	 *
	 */
	public class PreProcess {
		String inputFile;
		boolean monthly;
		boolean weekly;
		String fieldName;
		String outputFile;
		boolean daysToAdd;
		
		public String getInputFile() {
			return inputFile;
		}
		public void setInputFile(String inputFile) {
			this.inputFile = inputFile;
		}
		
		public boolean isMonthly() {
			return monthly;
		}
		public void setMonthly(boolean monthly) {
			this.monthly = monthly;
		}
		
		public boolean isDaysToAdd(){
			return daysToAdd;
		}
		
		public void setDaysToAdd(boolean daysToAdd){
			this.daysToAdd = daysToAdd;
		}
		
		public boolean isWeekly() {
			return weekly;
		}
		public void setWeekly(boolean weekly) {
			this.weekly = weekly;
		}
		
		public String getFieldName() {
			return fieldName;
		}
		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}
		
		public String getOutputFile() {
			return outputFile;
		}
		public void setOutputFile(String outputFile) {
			this.outputFile = outputFile;
		}
	}
	
	/**
	 * Storing the Extraction Rules for SFDC
	 * @author Sunand
	 *
	 */
	public class SfdcExtract {
		String source;
		String connection;
		String table;
		List<String> fields;
		@JsonProperty("output")
		String output;

		public SfdcExtract() {
			// TODO Auto-generated constructor stub
		}
		
		public String getSource() {
			return source;
		}

		public void setSource(String source) {
			this.source = source;
		}

		public String getConnection() {
			return connection;
		}

		public void setConnection(String connection) {
			this.connection = connection;
		}

		public String getTable() {
			return table;
		}

		public void setTable(String table) {
			this.table = table;
		}

		public List<String> getFields() {
			return fields;
		}

		public void setFields(List<String> fields) {
			this.fields = fields;
		}

		public String getOutputFileLoc() {
			return output;
		}

		public void setOutputFileLoc(String output) {
			this.output = output;
		}
		
		@Override
		public String toString() {
			return toStringFormat(this);
		}
	}
	
	/**
	 * Storing the Transformation (mapping, join etc)
	 * @author Sunand
	 *
	 */
	public static class Transform {
	
		String query;
		@JsonProperty("useQuery")
		boolean useQuery;
		boolean join;
		boolean picklist;
		int limit;
		@JsonProperty("output")
		String output;
		ArrayList<Transform.TableInfo> tableInfo;
		
		public Transform() {
			// TODO Auto-generated constructor stub
		}
		
		public String getQuery() {
			return query;
		}
		public void setQuery(String query) {
			this.query = query;
		}
		public boolean isJoinUsingQuery() {
			return useQuery;
		}
		public void setJoinUsingQuery(boolean useQuery) {
			this.useQuery = useQuery;
		}
		public boolean isJoin() {
			return join;
		}
		public void setJoin(boolean join) {
			this.join = join;
		}
		public boolean isPicklist() {
			return picklist;
		}
		public void setPicklist(boolean picklist) {
			this.picklist = picklist;
		}
		public int getLimit() {
			return limit;
		}
		public void setLimit(int limit) {
			this.limit = limit;
		}
		public String getOutputFileLoc() {
			return output;
		}
		public void setOutputFileLoc(String output) {
			this.output = output;
		}
		public ArrayList<TableInfo> getTableInfo() {
			return tableInfo;
		}
		public void setTableInfo(ArrayList<TableInfo> tableInfo) {
			this.tableInfo = tableInfo;
		}
		
		@Override
		public String toString() {
			return toStringFormat(this);
		}
		
		/**
		 * Table info
		 * @author Sunand
		 *
		 */
		public static class TableInfo {
			@JsonProperty("file")
			String file;
			String table;
			String joinColumnName;
			List<Columns> columns;
			
			public TableInfo() {
				// TODO Auto-generated constructor stub
			}
			
			public String getCsvFile() {
				return file;
			}
			public void setCsvFile(String file) {
				this.file = file;
			}
			public String getTable() {
				return table;
			}
			public void setTable(String table) {
				this.table = table;
			}
			public String getJoinColumnName() {
				return joinColumnName;
			}
			public void setJoinColumnName(String joinColumnName) {
				this.joinColumnName = joinColumnName;
			}
			public List<Columns> getColumns() {
				return columns;
			}
			public void setColumns(List<Columns> columns) {
				this.columns = columns;
			}

			@Override
			public String toString() {
				return toStringFormat(this);
			}
			
			/**
			 * Final Columns required to be outputted by the program with proper alias names.
			 * @author Sunand
			 *
			 */
			public static class Columns {
				String name;
				String alias;
				public String getName() {
					return name;
				}
				public void setName(String name) {
					this.name = name;
				}
				public String getAlias() {
					return alias;
				}
				public void setAlias(String alias) {
					this.alias = alias;
				}
				
				@Override
				public String toString() {
					return toStringFormat(this);
				}
			}
		}
	}
	
	/**
	 * Holding the Load rules required to push the data to SFDC
	 * @author Sunand
	 *
	 */
	public class SfdcLoad {
		String target;
		String sObject;
		String operation;
		String contentType;
		String file;

        String externalIDField;
		boolean cleanUp;
		
		public SfdcLoad() {
			// TODO Auto-generated constructor stub
		}
		
		public String getTarget() {
			return target;
		}
		public void setTarget(String target) {
			this.target = target;
		}
		public String getsObject() {
			return sObject;
		}
		public void setsObject(String sObject) {
			this.sObject = sObject;
		}
		public String getOperation() {
			return operation;
		}
		public void setOperation(String operation) {
			this.operation = operation;
		}
		public String getContentType() {
			return contentType;
		}
		public void setContentType(String contentType) {
			this.contentType = contentType;
		}
		public String getFile() {
			return file;
		}
		public void setFile(String file) {
			this.file = file;
		}
		public boolean isCleanUp() {
			return cleanUp;
		}
		public void setCleanUp(boolean cleanUp) {
			this.cleanUp = cleanUp;
		}
        public String getExternalIDField() {
            return externalIDField;
        }
        public void setExternalIDField(String externalIDField) {
            this.externalIDField = externalIDField;
        }

		@Override
		public String toString() {
			return toStringFormat(this);
		}
	}
	
	/**
	 * Common Reflection code to generate to String format on the fly for all classes
	 * @param obj
	 * @return
	 */
	private static String toStringFormat(Object obj) {
		StringBuffer summary = new StringBuffer();
		Field[] fList=obj.getClass().getDeclaredFields();
		for (Field f: fList){
			try {
				summary.append(f.getName()+" : "+f.get(obj)+" | ");
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
