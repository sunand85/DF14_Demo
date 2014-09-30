package com.dreamforce.demo.webaction;

import java.lang.reflect.Field;

import org.apache.http.Header;

public class HttpResponseObj {

	private String content;
	private String contentType;
	private long contentLength;
	private int statusCode;
	private String statusLine;
	
	private Header[] headers;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusLine() {
		return statusLine;
	}

	public void setStatusLine(String statusLine) {
		this.statusLine = statusLine;
	}

	public Header[] getAllHeaders() {
		return headers;
	}
	
	public void setHeaders(Header[] headers) {
		this.headers = headers;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer summary = new StringBuffer();
		Field[] fList=this.getClass().getDeclaredFields();
		for (Field f: fList){
			try {
				summary.append(f.getName()+" : "+f.get(this)+" | ");
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
