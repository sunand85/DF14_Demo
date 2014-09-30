package com.dreamforce.demo.sfdc;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import com.dreamforce.demo.util.DateUtil;

/**
 * Modifies the CSV file with dynamic dates either Monthly, Weekly, Daily
 * @author Sunand
 *
 */
public class FileProcessor {

	static String resDir = "./resources/datagen/";
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		File inputFile = new File("./testdata/sfdc/UsageData/Data/XXX.csv");
		File outputFile = new File(resDir + "process/InstanceMonthlyFinal.csv");
		System.out.println("File exists : " + outputFile.exists());
		File f = generateMonthlyUsageData(inputFile, "Monthly", outputFile);
		System.out.println("File exists : " + f.exists());
	}
	
	public static File generateMonthlyUsageData(File inputFile, String fieldName, File outputFile) throws IOException {
		CSVReader reader = new CSVReader(new FileReader(inputFile));
		String[] cols;
		int fieldIndex = -1;
		CSVWriter writer = new CSVWriter(new FileWriter(outputFile), ',', '"', '\\', "\n");
		cols = reader.readNext(); 
		System.out.println(cols.length);
		writer.writeNext(cols);
		writer.flush();
		for(String str : cols) {
			System.out.println(str);
		}
		//TO find the index position of field for which we are going to add Monthly Date Diff.
		for(int i = 0; i < cols.length; i++) {
			if(cols[i].trim().equalsIgnoreCase(fieldName)) {
				fieldIndex	= i;
				cols = reader.readNext();
				break;
			}
		}
		
		if(fieldIndex > -1) {
		
			while(cols != null) {
				int monthDiff = Integer.parseInt(cols[fieldIndex]);
				cols[fieldIndex] = DateUtil.addMonths(new Date(), monthDiff, "yyyy-MM-dd");
				writer.writeNext(cols);
				writer.flush();
				cols = reader.readNext();
			}
			
		}
		else
			throw new RuntimeException("The input file : " + inputFile.getAbsolutePath() + " did not have the required field name : " + fieldName);
		
		return outputFile;
	}
	
	public static File generateWeeklyUsageData(File inputFile, String fieldName, File outputFile) throws IOException {
		CSVReader reader = new CSVReader(new FileReader(inputFile));
		String[] cols;
		int fieldIndex = -1;
		CSVWriter writer = new CSVWriter(new FileWriter(outputFile), ',', '"', '\\', "\n");
		cols = reader.readNext(); 
		System.out.println(cols.length);
		writer.writeNext(cols);
		writer.flush();
		for(String str : cols) {
			System.out.println(str);
		}
		//TO find the index position of field for which we are going to add Monthly Date Diff.
		for(int i = 0; i < cols.length; i++) {
			if(cols[i].trim().equalsIgnoreCase(fieldName)) {
				fieldIndex	= i;
				cols = reader.readNext();
				break;
			}
		}
		
		if(fieldIndex > -1) {
		
			while(cols != null) {
				int weekDiff = Integer.parseInt(cols[fieldIndex]);
				cols[fieldIndex] = DateUtil.addWeeks(new Date(), weekDiff, "yyyy-MM-dd");
				writer.writeNext(cols);
				writer.flush();
				cols = reader.readNext();
			}
			
		}
		else
			throw new RuntimeException("The input file : " + inputFile.getAbsolutePath() + " did not have the required field name : " + fieldName);
		
		return outputFile;
	}
	
	public static File generateResponseSubmissionDate(File inputFile, String fieldName, File outputFile) throws IOException {
		CSVReader reader = new CSVReader(new FileReader(inputFile));
		String[] cols;
		int fieldIndex = -1;
		CSVWriter writer = new CSVWriter(new FileWriter(outputFile), ',', '"', '\\', "\n");
		cols = reader.readNext(); 
		System.out.println(cols.length);
		writer.writeNext(cols);
		writer.flush();
		for(String str : cols) {
			System.out.println(str);
		}
		//TO find the index position of field for which we are going to add Monthly Date Diff.
		for(int i = 0; i < cols.length; i++) {
			if(cols[i].trim().equalsIgnoreCase(fieldName)) {
				fieldIndex	= i;
				cols = reader.readNext();
				break;
			}
		}
		
		if(fieldIndex > -1) {
			while(cols != null) {
				int daysDiff = Integer.parseInt(cols[fieldIndex]);
				cols[fieldIndex] = DateUtil.addDays(new Date(),daysDiff, "yyyy-MM-dd'T'HH:mm:ss");
				writer.writeNext(cols);
				writer.flush();
				cols = reader.readNext();
			}
		}
		else
			throw new RuntimeException("The input file : " + inputFile.getAbsolutePath() + " did not have the required field name : " + fieldName);
		
		return outputFile;
	}
}
