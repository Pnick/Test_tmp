package UtilsHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetJsonValues {

	static String result = null;

	public String getKeyValue(JSONObject jObj, String k, Object kValue, String kTest) throws JSONException 
	{
		Iterator<?> keys = jObj.keys();
		String key = null;
		result=null;

		while( keys.hasNext() ) {
			key = (String)keys.next();
			if (key.equals(k)) {
				//System.out.println(jObj.get(key));
				if(jObj.get(key).toString().equals(kValue.toString()))
				{
					result = findKeyValue(jObj, kTest);
					if(result!=null)
						return result;
				}
			}

			if ( jObj.get(key) instanceof JSONObject ) {
				//System.out.println(jObj.get(key));
				result = getKeyValue((JSONObject)jObj.get(key), k, kValue, kTest);
				if(result!=null)
					return result;
			}

			if ( jObj.get(key) instanceof JSONArray ) {
				JSONArray jar = (JSONArray)jObj.get(key);
				//System.out.println(jar.get(0));

				for (int i = 0; i < jar.length(); i++) {
					//System.out.println(jar.get(i));
					if(jar.get(i) instanceof String) {
						if(jar.get(i).equals(kValue)) {
							return k;
						}
					}
					else if(jar.get(i) instanceof Integer){
						continue;
					}
					else if(jar.get(i) instanceof JSONArray) {
						continue;
					}
					else {
						JSONObject j = jar.getJSONObject(i);
						//System.out.println(j);

						if(findKeyValue(j, k) != null && findKeyValue(j, k).equals(kValue)) {
							result = findKeyValue(j, kTest);
							if(result!=null)
								return result;
						}
						else {
							result = getKeyValue(j, k, kValue, kTest);
							if(result!=null)
								return result;
						}
					}
				}
			}
		}
		return result; 
	}

	public String findKeyValue(JSONObject jsonObj, String testKey) throws JSONException 
	{
		Iterator<?> keys = jsonObj.keys();
		String key = null;
		result=null;

		while( keys.hasNext() ) {
			key = (String)keys.next();
			if (key.equals(testKey)) {
				//System.out.println(jsonObj.get(key));
				return result = jsonObj.get(key) + ""; 
			}

			if ( jsonObj.get(key) instanceof JSONObject ) {
				//System.out.println(jsonObj.get(key));
				result = findKeyValue((JSONObject)jsonObj.get(key), testKey);
				if(result!=null)
					return result;
			}

			if ( jsonObj.get(key) instanceof JSONArray ) {
				JSONArray jar = (JSONArray)jsonObj.get(key);

				for (int i = 0; i < jar.length(); i++) {
					//System.out.println(jar.get(i));
					if(jar.get(i) instanceof String) {
						if(jar.get(i).equals(testKey)) {
							return testKey;
						}
					}
					else if(jar.get(i) instanceof Integer){
						if(jar.get(i).equals(testKey)) {
							return testKey;
						}
					}
					else if(jar.get(i) instanceof JSONArray){
						continue;
					}
					else {
						JSONObject j = jar.getJSONObject(i);
						result = findKeyValue(j, testKey);
						if(result!=null)
							return result;
					}
				}
			}
		}
		return result; 
	}

	public boolean checkKeyValue(JSONObject jObj, String k, Object kValue) throws JSONException 
	{
		boolean valueExists = false;

		Iterator<?> keys = jObj.keys();
		String key = null;

		while( keys.hasNext() ) {
			key = (String)keys.next();
			if (key.equals(k)) {
				System.out.println(jObj.get(key));
				if(jObj.get(key).equals(kValue))
				{
					valueExists = true;
					return valueExists;
				}
			}

			if ( jObj.get(key) instanceof JSONObject ) {
				boolean tmpobject= false;
				System.out.println(jObj.get(key));
				tmpobject = checkKeyValue((JSONObject)jObj.get(key), k, kValue);
				if(tmpobject)
					return tmpobject;
			}

			if ( jObj.get(key) instanceof JSONArray ) {
				JSONArray jar = (JSONArray)jObj.get(key);
				System.out.println(jar.get(0));
				boolean tmparr= false;

				for (int i = 0; i < jar.length(); i++) {
					JSONObject j = jar.getJSONObject(i);
					tmparr = checkKeyValue(j, k, kValue);
					if(tmparr)
						return tmparr;
				}
			}
		}
		return valueExists;
	}

	public ArrayList<String> filteredJsonStrings= new ArrayList<String>();
	public ArrayList<String> filteredKeys = new ArrayList<String>();
	public int i = 0;
	public String regex = "(\\d{4}-\\d{2}-\\d{2})";
	//public static String regex = "(\\d{2}/\\d{2}/\\d{2} (([0-1]?[0-9])|(2[0-3])):[0-5][0-9]:[0-5][0-9])";
	static Matcher matcher;
	static JSONObject newjObj=null;
	static Object firstKey ;
	static Object valueForFirstKey ;

	public void getSameKeys(JSONObject jObj, LinkedHashMap<?, ?> keysValues, String sameKey, int eventsNum) throws JSONException, ParseException {
		getSameKeysAfter(jObj, keysValues, sameKey, eventsNum);
	}
	
	public void getSameKeysAfter(JSONObject jObj, LinkedHashMap<?, ?> keysValues, String sameKey, int eventsNum) throws JSONException, ParseException {
		getSameKeysBeforeOrAfter(jObj, keysValues, sameKey, eventsNum, false);
	}
	
	public void getSameKeysBefore(JSONObject jObj, LinkedHashMap<?, ?> keysValues, String sameKey, int eventsNum) throws JSONException, ParseException {
		getSameKeysBeforeOrAfter(jObj, keysValues, sameKey, eventsNum, true);
	}
	
	public void getSameKeysBeforeOrAfter(JSONObject jObj, LinkedHashMap<?, ?> keysValues, String sameKey, int eventsNum, boolean isBefore) throws JSONException, ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateNow =  LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		firstKey = keysValues.keySet().toArray()[0];
		valueForFirstKey = keysValues.get(firstKey);

		String jsonDate;

		Iterator<?> keys = jObj.keys();
		String key = null;
		result=null;
		boolean strExist= false;

		while( keys.hasNext() && i<eventsNum) {
			key = (String)keys.next();
			if (key.equals(firstKey)) {
				//System.out.println(jObj.get(key));
				matcher = Pattern.compile(regex).matcher(jObj.get(key).toString());
				if(jObj.get(key).equals(valueForFirstKey) || matcher.find())
				{
					for (Entry<?, ?> entry : keysValues.entrySet()) {
						if(!entry.getKey().equals(firstKey)) {
							strExist= false;
							matcher = Pattern.compile(regex).matcher(entry.getValue().toString());
							if(matcher.find())
							{
								jsonDate = jObj.get(entry.getKey().toString()).toString();
								Date dateValue = sdf.parse(jsonDate);
								String dateFromJSON = sdf.format(dateValue);

								if (isBefore)
								{
									if(sdf.parse(dateFromJSON).before(sdf.parse(dateNow)))
									{
										filteredJsonStrings.add(i++, jObj.toString() + "\r\n");
										filteredKeys.add(jObj.get(sameKey).toString());
									}
									else {
										break;
									}
								}
								else {
									if(sdf.parse(dateNow).before(sdf.parse(dateFromJSON)) && sdf.parse(dateFromJSON).before(sdf.parse(entry.getValue().toString())))
									{
										filteredJsonStrings.add(i++, jObj.toString() + "\r\n");
										filteredKeys.add(jObj.get(sameKey).toString());
									}
									else {
										break;
									}
								}
							}
							else {
								String str1 =  entry.getValue().toString();
								String str2 =  (jObj.get(entry.getKey().toString())).toString();

								if(str1.equals(str2)) {
									if(filteredJsonStrings.size()>=1) {
										for (Iterator<String> iterator = filteredJsonStrings.iterator(); iterator.hasNext();)
										{
											String str = iterator.next();
											if(str.contains(jObj.toString())) {
												strExist = false;
												break;
											}
											else {
												strExist = true;
											}
										}
									}
									else {
										strExist = true;
									}
									if(strExist)
										filteredJsonStrings.add(i++, jObj.toString());
									filteredKeys.add(jObj.get(sameKey).toString());
								}
								else {
									for (Iterator<String> iterator = filteredJsonStrings.iterator(); iterator.hasNext();) {
										String str = iterator.next();
										if(str.contains(jObj.toString())) {
											iterator.remove();
											i--;
										}
									}
								}
							}

						}
					}
				}
			}

			if ( jObj.get(key) instanceof JSONObject ) {
				//System.out.println(jObj.get(key));
				getSameKeysBeforeOrAfter((JSONObject)jObj.get(key), keysValues, sameKey, eventsNum, isBefore);
			}

			if ( jObj.get(key) instanceof JSONArray ) {
				JSONArray jar = (JSONArray)jObj.get(key);
				//System.out.println(jar.get(0));

				for (int i = 0; i < jar.length(); i++) {
					//System.out.println(jar.get(i));
					if(jar.get(i) instanceof String) {

					}
					else {
						JSONObject j = jar.getJSONObject(i);
						getSameKeysBeforeOrAfter(j, keysValues, sameKey, eventsNum, isBefore);
					}
				}
			}
		}
	}

	public static String dateFrom = null;
	public static String dateTo = null;
	static String regexx = "([0-3]?[0-9][/][0-3]?[0-9][/](?:[0-9]{2})?[0-9]{2} (([0-1]?[0-9])|(2[0-3])):[0-5][0-9]:[0-5][0-9])";
	//static String regexx = "(\\d{2}\\/\\d{2}\\/\\d{2} (([0-1]?[0-9])|(2[0-3])):[0-5][0-9]:[0-5][0-9])";
	public JSONArray jArray = new JSONArray();

	public void filterOdds(JSONObject jObj, LinkedHashMap<?, ?> keysValues, String sameKey, int eventsNum) throws JSONException, ParseException, NoSuchFieldException, IllegalAccessException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

		firstKey = keysValues.keySet().toArray()[0];
		valueForFirstKey = keysValues.get(firstKey);

		String jsonDate;

		Iterator<?> keys = jObj.keys();
		String key = null;
		result=null;
		boolean strExist= false;

		while( keys.hasNext() && i<eventsNum) {
			key = (String)keys.next();
			if (key.equals(firstKey)) {
				//System.out.println(jObj.get(key));
				matcher = Pattern.compile(regexx).matcher(jObj.get(key).toString());
				if(jObj.get(key).toString().equals(valueForFirstKey.toString()) || matcher.find())
				{
					for (Entry<?, ?> entry : keysValues.entrySet()) {
						//						if(!entry.getKey().equals(firstKey))
						//						{
						strExist= false;
						matcher = Pattern.compile(regexx).matcher(entry.getValue().toString());
						if(matcher.find())
						{
							jsonDate = jObj.get(entry.getKey().toString()).toString();
							Date dateValue = sdf.parse(jsonDate);
							String dateFromJSON = sdf.format(dateValue);

							if(dateFrom != null && dateTo == null && sdf.parse(dateFromJSON).after(sdf.parse(entry.getValue().toString())))
							{
								jArray.put(jObj);

								filteredJsonStrings.add(i++, jObj.toString());
								filteredKeys.add(jObj.get(sameKey).toString());
							}
							else if(dateFrom == null && dateTo != null && sdf.parse(dateFromJSON).before(sdf.parse(entry.getValue().toString())))
							{
								jArray.put(jObj);

								filteredJsonStrings.add(i++, jObj.toString());
								filteredKeys.add(jObj.get(sameKey).toString());
							}
							else {
								break;
							}
						}
						else {
							String str1 =  entry.getValue().toString();
							String str2 =  (jObj.get(entry.getKey().toString())).toString();

							if(str1.equals(str2)) {
								if(filteredJsonStrings.size()>=1) {
									for (Iterator<String> iterator = filteredJsonStrings.iterator(); iterator.hasNext();)
									{
										String str = iterator.next();
										if(str.contains(jObj.toString())) {
											strExist = false;
											break;
										}
										else {
											strExist = true;
										}
									}
								}
								else {
									strExist = true;
								}
								if(strExist) {
									jArray.put(jObj);
									filteredJsonStrings.add(i++, jObj.toString());

									findKeyValues(jObj, sameKey);
								}
								filteredKeys.add(jObj.get(sameKey).toString());

								//String id = findKeyValue(jObj, sameKey);
								//System.out.println(id);
							}
							else {
								for (Iterator<String> iterator = filteredJsonStrings.iterator(); iterator.hasNext();) {
									String str = iterator.next();
									if(str.contains(jObj.toString())) {
										iterator.remove();
										i--;
									}
								}
							}
						}

						//}
					}
				}
			}

			if ( jObj.get(key) instanceof JSONObject ) {
				//System.out.println(jObj.get(key));
				filterOdds((JSONObject)jObj.get(key), keysValues, sameKey, eventsNum);
			}

			if ( jObj.get(key) instanceof JSONArray ) {
				JSONArray jar = (JSONArray)jObj.get(key);
				//System.out.println(jar.get(0));

				for (int i = 0; i < jar.length(); i++) {
					//System.out.println(jar.get(i));
					if(jar.get(i) instanceof String) {
						continue;
					}
					else if(jar.get(i) instanceof Integer){
						continue;
					}
					else if(jar.get(i) instanceof JSONArray) {
						continue;
					}
					else {
						JSONObject j = jar.getJSONObject(i);
						//System.out.println(j.toString(4));
						filterOdds(j, keysValues, sameKey, eventsNum);
					}
				}
			}
		}
	}

	public ArrayList<String> sameKeyValues = new ArrayList<String>();
	public HashMap<String, JSONObject> keyJsonMap  = new HashMap<String, JSONObject>();
	public static float sum = 0;
	public float getSameKeyValues(JSONObject jsonObj, String keySearch) throws JSONException 
	{
		Iterator<?> keys = jsonObj.keys();
		String key = null;
		String regexNum ="[-+]?[0-9]*\\.?[0-9]+";
		String keyValue = null;

		while( keys.hasNext() ) {
			key = (String)keys.next();
			if (key.equals(keySearch)) {
				//System.out.println(jsonObj.get(key));

				matcher = Pattern.compile(regexNum).matcher(jsonObj.get(key).toString());
				if(matcher.find()) {
					keyValue = matcher.group();
					sum = sum + Float.valueOf(keyValue);
				}
				else {
					keyValue = jsonObj.get(key).toString();
				}
				keyValue = jsonObj.get(key).toString();
				sameKeyValues.add(keyValue);
				keyJsonMap.put(jsonObj.get(key).toString(), jsonObj);
			}

			if ( jsonObj.get(key) instanceof JSONObject ) {
				//System.out.println(jsonObj.get(key));
				getSameKeyValues((JSONObject)jsonObj.get(key), keySearch);
			}

			if ( jsonObj.get(key) instanceof JSONArray ) {
				JSONArray jar = (JSONArray)jsonObj.get(key);

				for (int i = 0; i < jar.length(); i++) {
					//System.out.println(jar.get(i));
					if(jar.get(i) instanceof String) {
						if(jar.get(i).equals(keySearch)) {
							//return keySearch;
						}
					}
					else if(jar.get(i) instanceof Integer) {
						continue;
					}
					else if(jar.get(i) instanceof JSONArray) {
						continue;
					}
					else {
						JSONObject j = jar.getJSONObject(i);
						getSameKeyValues(j, keySearch);
					}
				}
			}
		}
		return sum; 
	}

	//				List<String> getValuesForGivenKey = IntStream.range(0, jsonArray.length())
	//						.mapToObj(index -> ((JSONObject)jsonArray.get(index)).optString(keySearch))
	//						.collect(Collectors.toList());
	//
	//				System.out.println(getValuesForGivenKey);

	public String findKeyValues(JSONObject jsonObj, String testKey) throws JSONException 
	{
		Iterator<?> keys = jsonObj.keys();
		String key = null;

		while( keys.hasNext() ) {
			key = (String)keys.next();
			if (key.equals(testKey)) {
				//System.out.println(jsonObj.get(key));
				result = result + jsonObj.get(key) + ""; 
			}

			if ( jsonObj.get(key) instanceof JSONObject ) {
				//System.out.println(jsonObj.get(key));
				findKeyValue((JSONObject)jsonObj.get(key), testKey);
			}

			if ( jsonObj.get(key) instanceof JSONArray ) {
				JSONArray jar = (JSONArray)jsonObj.get(key);

				for (int i = 0; i < jar.length(); i++) {
					//System.out.println(jar.get(i));
					if(jar.get(i) instanceof String) {
						if(jar.get(i).equals(testKey)) {
							return testKey;
						}
					}
					else {
						JSONObject j = jar.getJSONObject(i);
						result = findKeyValue(j, testKey);
						sameKeyValues.add(result);
						//if(result!=null)
						//sameKeyValues.add(i++, Float.valueOf(result));
					}
				}
			}
		}
		return result; 
	}

	public JSONObject extractSubJson(JSONObject jsonObj, String Key)
	{
		Iterator<?> keys = jsonObj.keys();
		String key = null;
		JSONObject JsonResult = new JSONObject();

		while( keys.hasNext() ) {
			key = (String)keys.next();
			if (key.equals(Key)) {
				System.out.println( Key + " key exists.");
			}

			if ( jsonObj.get(key) instanceof JSONObject ) {
				if (key.equals(Key)) {
					System.out.println(jsonObj.get(key));
					JsonResult = (JSONObject) jsonObj.get(key);
					break;
				}
			}

			if ( jsonObj.get(key) instanceof JSONArray ) {
				if (key.equals(Key)) {

					JsonResult.put(Key,(Object)jsonObj.get(key));

					break; 
				}
			}
		}
		return JsonResult;
	}
	
	
	public Object findKeyValueGet(JSONObject jsonObj, String testKey) throws JSONException 
	{
		Iterator<?> keys = jsonObj.keys();
		String key = null;
		Object result = null;

		while( keys.hasNext() ) {
			key = (String)keys.next();
			if (key.equals(testKey)) {
				//System.out.println(jsonObj.get(key));
				return result = jsonObj.get(key); 
			}

			if ( jsonObj.get(key) instanceof JSONObject ) {
				//System.out.println(jsonObj.get(key));
				result = findKeyValueGet((JSONObject)jsonObj.get(key), testKey);
				if(result!=null)
					return result;
			}

			if ( jsonObj.get(key) instanceof JSONArray ) {
				JSONArray jar = (JSONArray)jsonObj.get(key);

				for (int i = 0; i < jar.length(); i++) {
					//System.out.println(jar.get(i));
					if(jar.get(i) instanceof String) {
						if(jar.get(i).equals(testKey)) {
							return testKey;
						}
					}
					else if(jar.get(i) instanceof Integer){
						if(jar.get(i).equals(testKey)) {
							return testKey;
						}
					}
					else if(jar.get(i) instanceof JSONArray){
						continue;
					}
					else {
						JSONObject j = jar.getJSONObject(i);
						result = findKeyValueGet(j, testKey);
						if(result!=null)
							return result;
					}
				}
			}
		}
		return result; 
	}
}