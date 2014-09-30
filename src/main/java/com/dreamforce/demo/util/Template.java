package com.dreamforce.demo.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.commons.io.FileUtils;

public class Template {
	private Hashtable<String, String> _values = new Hashtable<String, String>();
	private String _templateStr = "";
	
	public Template(File templateFile) throws IOException {
		// TODO Auto-generated constructor stub
		_templateStr = FileUtils.readFileToString(templateFile);
	}

	public void setValue(String name, String value) {
		_values.put(name, value);
	}

	public void export(OutputStream s) {
		Enumeration<String> keys = _values.keys();
		StringBuffer result = new StringBuffer(_templateStr);
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			String value = _values.get(key);
			replaceParams(result, key, value);
		}
		try {
			OutputStreamWriter osw = new OutputStreamWriter(s);
			osw.write(result.toString());
			osw.flush();
			osw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Replaces all the occurrence of @{paramName} with value.
	 */
	private void replaceParams(StringBuffer buffer, String paramName,
			String value) {
		int lastFound = 0;
		String paramString = "@{" + paramName + "}";
		for (lastFound = buffer.indexOf(paramString); lastFound > -1; lastFound = buffer
				.indexOf(paramString, lastFound + 1)) {
			buffer.replace(lastFound, lastFound + paramString.length(), value);
		}
	}

	public static void main(String[] args) throws IOException {
		File templateFile = new File("./template/CreateJobTemplate.xml");
		Template t = new Template(templateFile);
		t.setValue("operation", "query");
		t.setValue("object", "Account");
		t.setValue("concurrencyMode", "Parallel");
		t.setValue("contentType", "CSV");
		t.export(new FileOutputStream(new File("./template/CreateJob.xml")));
	}

}
