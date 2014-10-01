package com.dreamforce.demo.sfdc;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.dreamforce.demo.sfdc.bean.JobInfo;

public class TestJobInfoMapping {

	/**
	 * @param args
	 * @throws Exception 
	 * @throws FileNotFoundException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public static void main(String[] args) throws JsonParseException, JsonMappingException, FileNotFoundException, Exception {
		// TODO Auto-generated method stub

		ObjectMapper mapper = new ObjectMapper();
		JobInfo info = mapper.readValue(new FileReader("./resources/template/JobInfo.txt"), JobInfo.class);
		if(info.getExtractionRule() == null || info.getExtractionRule().toString().contains("null")) System.out.println("I have nothing to do here");
		System.out.println(info.getExtractionRule().toString());
		System.out.println(info.getTransformationRule().toString());
		System.out.println(info.getLoadRule().toString());
		
		ArrayList<String> b = new ArrayList<String>();
		b.add("asdasd");
		
		LinkedHashMap<String, List<String>> finalFields = new LinkedHashMap<String, List<String>>();
		finalFields.put("sunand1", b);
		finalFields.put("sunand2", b);
		finalFields.put("sunand3", b);
		finalFields.put("sunand4", b);
		finalFields.put("sunand5", b);
		finalFields.put("sunand6", b);
		
		
		String[] temp = finalFields.keySet().toArray(new String[0]);
		for(String st : temp)
			System.out.println(st);
		
	}

}
