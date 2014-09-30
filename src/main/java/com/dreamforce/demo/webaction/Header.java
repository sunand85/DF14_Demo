package com.dreamforce.demo.webaction;

import java.util.ArrayList;
import java.util.List;

public class Header {
	String name;
	String value;
	List<Header> headers = new ArrayList<Header>();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public void addHeader(String name, String value) {
		Header h = new Header();
		h.name = name;
		h.value = value;
		headers.add(h);
	}
	
	public List<Header> getAllHeaders() {
		return headers;
	}
	
	public void clearHeader() {
		headers.clear();
	}
}