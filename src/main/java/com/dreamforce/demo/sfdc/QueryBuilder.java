package com.dreamforce.demo.sfdc;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.dreamforce.demo.sfdc.JobInfo.Transform.TableInfo.Columns;

/**
 * SQL + SOQL Query Builder
 * @author Sunand
 *
 */
public class QueryBuilder {

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Building join queries. Currently only 2 joins are supported.
	 * 
	 * @param finalFields final columns required from respective tables.
	 * @param joinColumn columns upon which join has to happen
	 * @param operation operation can be equals (=)
	 * @return
	 */
	public static String buildJoinQuery(LinkedHashMap<String, List<Columns>> finalFields, List<String> joinColumn, String operation){
		StringBuffer finalQuery = new StringBuffer();
		finalQuery.append("SELECT").append(" ");
		//Just framing the fields
		Set<String> f = finalFields.keySet();
		String[] tables = f.toArray(new String[0]);
		if(tables.length != 2 || joinColumn.size() != 2) 
			throw new RuntimeException("Only 2 Column Joins are Supported. Please provide appropriate Info");
		for(String table : tables) {
			List<Columns> cols = finalFields.get(table);
			for(Columns col : cols) {
				finalQuery.append(table).append(".").append(col.getName()).append(" ")
					.append("AS").append(" ").append("\"").append(col.getAlias()).append("\"").append(",");
			}
		}   
		//Removing last comma and adding a space
		finalQuery.deleteCharAt(finalQuery.length() -1).append(" ");
		
		finalQuery.append("FROM").append(" ").append(tables[0]);

		//Adding join to query
		finalQuery.append(" ").append("JOIN").append(" ").append(tables[1]).append(" ").append("ON").append(" ");
		//defining the join operator
		finalQuery.append(tables[0]).append(".").append(joinColumn.get(0))
			.append(operation)
			.append(tables[1]).append(".").append(joinColumn.get(1));
		
		//Printing the final Query after join
		System.out.println(finalQuery);
		return finalQuery.toString();
	}

    public static String buildRightJoinQuery(LinkedHashMap<String, List<Columns>> finalFields, List<String> joinColumn, String operation){
        StringBuffer finalQuery = new StringBuffer();
        finalQuery.append("SELECT").append(" ");
        //Just framing the fields
        Set<String> f = finalFields.keySet();
        String[] tables = f.toArray(new String[0]);
        if(tables.length != 2 || joinColumn.size() != 2)
            throw new RuntimeException("Only 2 Column Joins are Supported. Please provide appropriate Info");
        for(String table : tables) {
            List<Columns> cols = finalFields.get(table);
            for(Columns col : cols) {
                finalQuery.append(table).append(".").append(col.getName()).append(" ")
                        .append("AS").append(" ").append("\"").append(col.getAlias()).append("\"").append(",");
            }
        }
        //Removing last comma and adding a space
        finalQuery.deleteCharAt(finalQuery.length() -1).append(" ");

        finalQuery.append("FROM").append(" ").append(tables[0]);

        //Adding join to query
        finalQuery.append(" ").append("RIGHT JOIN").append(" ").append(tables[1]).append(" ").append("ON").append(" ");
        //defining the join operator
        finalQuery.append(tables[0]).append(".").append(joinColumn.get(0))
                .append(operation)
                .append(tables[1]).append(".").append(joinColumn.get(1));

        //Printing the final Query after join
        System.out.println(finalQuery);
        return finalQuery.toString();
    }
	/**
	 * Generic way to build Query to fetch the data from SOQL DB. Example,
	 * object = Account ; limit = 5, field 1, field 2 ... field n
	 * 
	 * @param object
	 * @param limit
	 * @param val
	 * @return
	 */
	public static String buildSOQLQuery(String object, int limit, String... val) {
		// TODO Auto-generated method stub
		StringBuffer query = new StringBuffer("SELECT");
		query.append(" ");
		for (int i = 0; i < val.length; i++) {
			if (i != (val.length - 1))
				query.append(val[i]).append(",");
			else
				query.append(val[i]).append(" ");
		}
		query.append("FROM").append(" ").append(object).append(" ")
				.append("LIMIT").append(" ").append(limit);
		return query.toString();
	}
	
	/**
	 * Generic way to build Query to fetch the data from SOQL DB. Example,
	 * object = Account ;  val = field 1, field 2 ... field n
	 * 
	 * @param object
	 * @param val
	 * @return
	 */
	public static String buildSOQLQuery(String object, String... val) {
		// TODO Auto-generated method stub
		StringBuffer query = new StringBuffer("SELECT");
		query.append(" ");
		for (int i = 0; i < val.length; i++) {
			if (i != (val.length - 1))
				query.append(val[i]).append(",");
			else
				query.append(val[i]).append(" ");
		}
		query.append("FROM").append(" ").append(object);
		return query.toString();
	}
	
	public static String buildSQLQuery() {
		return null;
	}

	/**
	 * Building SOQL Query with object name considered as table name and its column list.
	 * @param sObject
	 * @param fields
	 * @return
	 */
	public static String buildSOQLQuery(String sObject, List<String> fields) {
		// TODO Auto-generated method stub
		StringBuffer query = new StringBuffer("SELECT");
		query.append(" ");
		for (int i = 0; i < fields.size(); i++) {
			if (i != (fields.size() - 1))
				query.append(fields.get(i)).append(",");
			else
				query.append(fields.get(i)).append(" ");
		}
		query.append("FROM").append(" ").append(sObject);
		return query.toString();
	}

}

/**
 * To Store the Source - Destination table/column mapping
 * @author Sunand
 *
 */
class Mapping {
	String source;
	String sColumn;
	String sAlias;
	String dest;
	String dAlias;
	String dColumn;
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getsColumn() {
		return sColumn;
	}
	public void setsColumn(String sColumn) {
		this.sColumn = sColumn;
	}
	public String getDest() {
		return dest;
	}
	public void setDest(String dest) {
		this.dest = dest;
	}
	public String getdColumn() {
		return dColumn;
	}
	public void setdColumn(String dColumn) {
		this.dColumn = dColumn;
	}
	public String getsAlias() {
		return sAlias;
	}
	public void setsAlias(String sAlias) {
		this.sAlias = sAlias;
	}
	public String getdAlias() {
		return dAlias;
	}
	public void setdAlias(String dAlias) {
		this.dAlias = dAlias;
	}
}
