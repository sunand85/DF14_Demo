package com.dreamforce.demo.webaction;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.dreamforce.demo.util.Report;

/**
 * HTTP Action on Web. Currently Cookies and proxy connections are not handled it could be implemented on need basis.
 * @author Sunand
 *
 */
public class WebAction {

	/**
	 * Does a post request with given parameters. 
	 * This methods takes a rawbody which is a String Payload.
	 * 
	 * @param uri
	 * @param rawBody
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	public HttpResponseObj doPost(String uri, String rawBody, List<Header> headers) throws Exception {
		
		HttpPost post = new HttpPost(uri);
		if(headers.size() > 0) {
			for(Header h : headers) {
				post.addHeader(h.getName(), h.getValue());
			}
		}
			
		StringEntity entity = null;
		try {
			entity = new StringEntity(rawBody);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		post.setEntity(entity);
		return httpResponse(post);
	}
	
	/**
	 * Generic HTTP post method which takes respective url, headers and any of the entity like String, File etc.
	 * @param uri
	 * @param headers
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public HttpResponseObj doPost(String uri, List<Header> headers, HttpEntity entity) throws Exception {
		HttpPost post = new HttpPost(uri);
		if(headers.size() > 0) {
			for(Header h : headers) {
				post.addHeader(h.getName(), h.getValue());
			}
		}
			
		post.setEntity(entity);
		return httpResponse(post);
	}
	
	/**
	 * Reads the response and puts in a wrapper class HttpResponseObj for easy access of the contents.
	 * @param request
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public HttpResponseObj httpResponse(HttpUriRequest request) throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();
		//For Burp Purpose
		/*HttpHost target = new HttpHost("127.0.0.1", 8080, "http");
		HttpResponse response = client.execute(target, request);*/
		HttpResponse response = client.execute(request);

		// Reading Response
		HttpResponseObj obj = new HttpResponseObj();
		String result;
		HttpEntity resEntity = response.getEntity();
		if (resEntity != null) {
			InputStream instream = resEntity.getContent();
			try {
				// do something useful
				result = IOUtils.toString(instream);
				Report.logInfo("Status Code: " + response.getStatusLine().getStatusCode());
				obj.setStatusCode(response.getStatusLine().getStatusCode());
				obj.setStatusLine(response.getStatusLine().toString());
				obj.setContent(result);
				obj.setContentType(resEntity.getContentType().getValue());
				obj.setContentLength(resEntity.getContentLength());
				obj.setHeaders(response.getAllHeaders());
			} finally {
				instream.close();
			}
		}

		EntityUtils.consume(resEntity);
		
		return obj;
	}
	
	/**
	 * Generic HTTP get method for which takes URL and its respective headers to fetch the response.
	 * @param uri
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	public HttpResponseObj doGet(String uri, List<Header> headers) throws Exception {
		HttpGet get = new HttpGet(uri);
		if(headers.size() > 0) {
			for(Header h : headers) {
				get.addHeader(h.getName(), h.getValue());
			}
		}
		return httpResponse(get);
	}
}
