package com.dreamforce.demo.db;

import java.sql.SQLException;

public class H2DbTest {

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub

		H2Db db = new H2Db("jdbc:h2:~/sunand","sa","");
		String createTableFromCSv1 = "CREATE TABLE Account AS SELECT * FROM CSVREAD('./result/Account.csv')";
		String createTableFromCSv2 = "CREATE TABLE Customer AS SELECT * FROM CSVREAD('./result/Customers.csv')";
		db.executeStmt("DROP TABLE IF EXISTS Account");
		db.executeStmt("DROP TABLE IF EXISTS Customer");
		db.executeStmt(createTableFromCSv1);
		db.executeStmt(createTableFromCSv2);
		System.err.println("Done");
	}

}
