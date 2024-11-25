package UtilsHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ResponseBy {

	public int 				status;
	public JSONObject 		json;
	public HttpResponse 	httpResponse;

	public ResponseBy(HttpResponse httpResp) throws UnsupportedOperationException, IOException 
	{
		httpResponse					= httpResp;
		status							= httpResponse.getCode();
		ClassicHttpResponse response 	= (ClassicHttpResponse) httpResp;

		String jsonText = null;
		
		HttpEntity entity = response.getEntity();
		InputStream inputStream = entity.getContent();
		StringBuilder sb = new StringBuilder();
		int cp;

		try {
			if (inputStream != null) {
				BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
				while ((cp = rd.read()) != -1) {
					sb.append((char) cp);
				}
				jsonText = sb.toString();
			}
			else {
				System.out.println("WARNING : Input Stream is null.");
			}
		}
		finally {
			if(inputStream!=null)
				inputStream.close();
		}

		if (!jsonText.isEmpty())
		{
			if (Character.toString(jsonText.charAt(0)).equals("<")) {

				if(jsonText.contains("html") || jsonText.contains("div")) {

					Document document = Jsoup.parse(jsonText);
					document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
					jsonText = document.html();
				}
				try {
					json = XML.toJSONObject(jsonText);
				} catch (JSONException je) {
					System.out.println(je.toString());
				}
				return;
			}
			else if (Character.toString(jsonText.charAt(0)).equals("[")) {
				jsonText = "{\"data\":" + jsonText + "}";
			}
			else if (!Character.toString(jsonText.charAt(0)).equals("{")) {
				jsonText = "{\"data\":" + jsonText + "}";
			}
			json = new JSONObject(jsonText);
		}
		else{
			json = new JSONObject("{}");
		}
	}
}