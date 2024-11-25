package UtilsHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bson.Document;
import org.json.JSONObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class CommonVariables {

	public static	InitDB						db;
	public static 	String 						baseURL;
	public static	String						environment;
	public static	boolean						headless;
	public static	String						username;
	public static	boolean						newDriver = false;
	public static	String						browserName 	= "";
	public static 	String 						browserVersion 	= "";
	
	public static 	String 						driverVersion 	= "";
	
	public 			boolean						allSimilarities = false;
	public 			MongoCollection<Document>	collection;
	public			MongoCursor<Document> 		cursor;
	public 			WebElement 					element;
	public			WebDriverWait 				wait;
	public			List<Object>				options = new ArrayList<>();
	public			List<String>				displayedRows = new ArrayList<>();
	public			int							counter = -1;
	public 			JSONObject					jsonWebInfo;
	
	public			ResponseBy					response;
	public			SendRequest 				sendRequest		= new SendRequest();
	public			HashMap<String, String> 	headers			= new HashMap<String, String>();
	public			HashMap<Object, Object> 	parameters	 	= new HashMap<Object, Object>();
	public			String 						requestBody 	= "";
}
