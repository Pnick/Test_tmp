package UtilsHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.protocol.BasicHttpContext;
import org.apache.hc.core5.http.protocol.HttpContext;

import org.json.JSONObject;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

public class SendRequest {

	public static List<org.apache.hc.client5.http.cookie.Cookie> cookies;
	BasicCookieStore cookieStore=new BasicCookieStore();
	HttpContext localContext=new BasicHttpContext();
	ExtentTest reporter = ExtentCucumberAdapter.getCurrentStep();

	public HttpResponse Get(String endpointURL, Map<?, ?> headers) throws IOException
	{
		HttpClient httpClient = HttpClients.createDefault();

		HttpGet get = new HttpGet(endpointURL);

		if(headers!=null) {
			//reporter.info("INFO : Request Headers are : ");
			for (Object key : headers.keySet()) {
				Object header = headers.get(key);
				String headerValue = "";
				
				if (header != null) {
					headerValue = header.toString();
				}
				get.setHeader(key.toString(), headerValue);
				System.out.println("Key = " + key + ", Value = " + headers.get(key));
				//reporter.info("INFO : Key = " + key + ", Value = " + headers.get(key));
			}
			if(headers.isEmpty()) {
				System.out.println("Empty Headers !");
			}
		}
		else {
			System.out.println("Headers are null : " + headers);
		}

		localContext.setAttribute(HttpClientContext.COOKIE_STORE,cookieStore);

		HttpResponse httpResponse = httpClient.execute(get,localContext);

		cookies = cookieStore.getCookies();

		return httpResponse;
	}

	public HttpResponse Post(String endpointURL, HashMap<?, ?> headers, HashMap<?, ?> parameters, String body) throws IOException
	{
		JSONObject params = new JSONObject();
		HttpClient httpClient = HttpClients.createDefault();
		HttpPost post = null;
		HttpResponse httpResponse = null;
		String contentType = null;

		if(!endpointURL.equals("")) {
			post = new HttpPost(endpointURL);
		}
		else {
			System.out.println("Endpoint URL is empty or null : " + endpointURL);
		}

		if(headers!=null) {
			reporter.info("INFO : Request Headers are : ");
			for (Object key : headers.keySet()) {
				Object header = headers.get(key);
				String headerValue = "";
				
				if (header != null) {
					headerValue = header.toString();
				}
				post.setHeader(key.toString(), headerValue);
				System.out.println("Key = " + key + ", Value = " + headers.get(key));
				reporter.info("INFO : Key = " + key + ", Value = " + headers.get(key));
				if(key.toString().equals("Content-Type") || key.toString().equals("content-type")) {
					contentType = headerValue;
				}
			}
			if(headers.isEmpty()) {
				System.out.println("Empty Headers !");
			}
		}
		else {
			System.out.println("Headers are null : " + headers);
		}

		if(parameters!=null) {
			reporter.info("INFO : Request Parameters are : ");
			for (Entry<?, ?> entry : parameters.entrySet()) {
				System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
				params.put(entry.getKey().toString(), entry.getValue());
				reporter.info("INFO : Key = " + entry.getKey() + ", Value = " + entry.getValue());
			}
			post.setEntity(new StringEntity(params.toString(), ContentType.APPLICATION_JSON));

			if(parameters.isEmpty()) {
				System.out.println("Empty Parameters !");
			}
		}
		else {
			System.out.println("Parameters are null : " + parameters);
		}

		if(body!=null && (!body.equals(""))) {
			reporter.info("INFO : Request Body is : " + body);
			System.out.println("Request Body is : " + body);

			if(contentType!= null && !contentType.contains("json"))
				post.setEntity(new StringEntity(body, ContentType.APPLICATION_FORM_URLENCODED));
			else {
				post.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
			}

			if(body.isEmpty()) {
				System.out.println("Empty body !");
			}
		}
		else {
			System.out.println("Body is empty : '' or null ");
		}

		localContext.setAttribute(HttpClientContext.COOKIE_STORE,cookieStore);

		if(post!=null) {
			httpResponse = httpClient.execute(post,localContext);
		}
		cookies = cookieStore.getCookies();

		return httpResponse;
	}

	public HttpResponse Delete(String endpointURL, HashMap<?, ?> headers) throws IOException
	{
		HttpClient httpClient = HttpClients.createDefault();

		HttpDelete delete = new HttpDelete(endpointURL);

		if(headers!=null) {
			reporter.info("INFO : Request Headers are : ");
			for (Object key : headers.keySet()) {
				delete.setHeader(key.toString(),(headers.get(key)).toString());
				System.out.println("Key = " + key + ", Value = " + headers.get(key));
				reporter.info("INFO : Key = " + key + ", Value = " + headers.get(key));
			}
			if(headers.isEmpty()) {
				System.out.println("Empty Headers !");
			}
		}
		else {
			System.out.println("Headers are null : " + headers);
		}

		HttpResponse httpResponse = httpClient.execute(delete);

		return httpResponse;
	}
}
