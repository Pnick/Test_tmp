package TestObjects;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Assume;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

import com.mongodb.BasicDBObject;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;

import Driver.DriverFactory;
import UtilsHelper.AttachFile;
import UtilsHelper.CommonVariables;
import UtilsHelper.PropertyUtils;
import io.cucumber.datatable.DataTable;
import UtilsHelper.ResponseBy;
import UtilsHelper.GetJsonValues;

public class BaseMethodsO extends LoadableComponent<BaseMethodsO>{

	public CommonVariables var;
	public WebDriver driver;
	public WebDriverWait wait;
	WebElement buttonToClick;
	ExtentTest reporter = ExtentCucumberAdapter.getCurrentStep();

	@FindBy(xpath = "//*[contains(@id, 'btn-search')]")
	public WebElement buttonSearch;

	@FindBy(xpath = "//*[contains(@id, 'clear-filters-btn')]")
	public WebElement buttonClearFilters;

	@FindBy(xpath = "//*[contains(@id, 'pagination-info')]")
	public List<WebElement> resultCounter;	

	@FindBy(xpath = "//*[contains(@class, 'pagination')]//following-sibling::li[last()]")
	public WebElement nextPaginationPage;

	@FindBy(xpath = "//*[contains(@class, 'pagination')]//following-sibling::li[last()-1]")
	public WebElement lastPaginationPage;

	@FindBy(xpath = "//*[contains(@id, 'page-size-select')]")
	public WebElement rowsOnPage;

	@FindBy(xpath = "//*[contains(@class, 'modal-trigger pointer')]")
	public WebElement buttonAdd;

	@FindBy(xpath = "//*[contains(@class, 'modal-trigger pointer')]")
	public WebElement buttonEdit;

	@FindBy(xpath = "//button[contains(text(), 'Create')]")
	public WebElement buttonCreate;

	@FindBy(xpath = "//button[contains(text(), 'Save')]")
	public WebElement buttonSave;

	public BaseMethodsO(CommonVariables var) {
		driver = DriverFactory.getDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4)); // set implicit wait
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(4)); // set explicit wait
		PageFactory.initElements(driver, this);
		if (var != null) {
			this.var = var;
		} else {
			this.var = new CommonVariables();
		}
	}
	
	public String openPage(WebDriver driver, String clientURL, String user, List<WebElement> recognizedElement) {

		var.wait = new WebDriverWait(driver, Duration.ofSeconds(3));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

		loginClient(driver, clientURL, user);

		if(recognizedElement == null 
				|| recognizedElement.size()<1
				|| !recognizedElement.get(0).isDisplayed())
		{
			navigateToURL(driver, clientURL);
			//reporter.info("Navigate to " + driver.getTitle() + " page.");
		}

		if(PropertyUtils.getProperty("browser.name").equals("IE"))
			driver.findElement(By.tagName("html")).sendKeys(Keys.chord(Keys.CONTROL, "0"));

		return driver.getWindowHandle();
	}

	public void checkPageOpened(String element) throws IOException {

		isloadComplete(driver);

		if (driver.getPageSource().contains(element)) {
			reporter.info("The " + element + " page is opened.");
			Assert.assertTrue("[EXPECTED] The " + element + " page is opened.", true);
		} else {
			reporter.info("The " + element + " page is NOT opened.");
			Assert.fail("[ACTUAL] The " + element + " page is NOT opened.");
		}

		element = element.replaceAll(" ", "_") + "_page";
		new AttachFile().Screenshot();
		//takeScreenShot(driver, element);

	}

	Pattern str = Pattern.compile("of [0-9]+");
	Pattern num = Pattern.compile("[0-9]+$");
	Matcher mStr, mNum;

	public int getResultNumberOnWeb(boolean saveValue) {
		String result = null;
		int resultOnWeb = 0;

		if(resultCounter.size()>0) {
			mStr = str.matcher(resultCounter.get(0).getText());
			if(mStr.find()) {
				result = mStr.group();
				mNum = num.matcher(result);
				mNum.find();
				result = mNum.group();
			}
		}

		if(result!=null) {
			resultOnWeb = Integer.parseInt(result);
		}

		if(saveValue) {
			var.counter = resultOnWeb;
		}

		return resultOnWeb;
	}

	public void getDisplayedResults() {

		isloadComplete(driver);
		List<WebElement> displayedRows = driver.findElements(By.cssSelector("table.grid-table tr"));

		displayedRows.remove(0);

		System.out.println("Number of displayed rows = " + displayedRows.size());

		for(WebElement row : displayedRows){

			var.displayedRows.add(row.getText());
		}
	}

	public void compareResults() throws IOException {

		isloadComplete(driver);
		List<WebElement> allRows = driver.findElements(By.cssSelector("table.grid-table tr"));

		allRows.remove(0);

		System.out.println("Number of displayed rows = " + allRows.size());

		int i = 0;
		for(WebElement row: allRows){

			System.out.println(row.getText());

			if(var.displayedRows.contains(row.getText())) {
				i++;
			}

			if(i==2) {
				try {
					WebElement lastTableElement = driver.findElement(By.xpath("(//table[1]/tbody/tr)[last()]"));
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true)", lastTableElement);
				}catch(Exception e) {}
			}
		}

		if(var.displayedRows.size()!=i) {
			reporter.info("Elements on the last table page are different by elements on the first one.");
			Assert.assertTrue("[EXPECTED] Elements on the last table page are different by elements on the first one.", true);
		}
		else {
			reporter.info("Elements on the last table page are the same as the elements on the first one OR missing.");
			Assert.fail("[ACTUAL] Elements on the last table page are the same as the elements on the first one OR missing.");
		} 

		String element = "table_page";
		new AttachFile().Screenshot();
		//takeScreenShot(driver, element);
	}

	public void deleteAllRecords(String tableName, int expectedRecordsNumber) {

		long existingRecords = -1;

		if(var.collection==null || !var.collection.getNamespace().toString().contains(tableName)) {
			CommonVariables.db.setCollection(tableName,var);
		}

		existingRecords = var.collection.countDocuments(Filters.and(Filters.gte("_id", 1000000000), Filters.lte("_id", 1000000000+expectedRecordsNumber)));

		if(existingRecords!=-1 && existingRecords < expectedRecordsNumber) {

			var.collection.deleteMany(Filters.and(Filters.gte("_id", 1000000000), Filters.lte("_id", 1000000200)));
			reporter.info("Delete history records");
		}
	}

	public void setDBRecords(String tableName) {

		long existingRecords = -1;
		int expectedRecordsNumber = 0;
		List<Integer> documentIds = new ArrayList<Integer>();

		if(var.collection==null || !var.collection.getNamespace().toString().contains(tableName)) {
			CommonVariables.db.setCollection(tableName,var);
		}

		if(tableName.contains("history")) {
			expectedRecordsNumber = 144;
			//System.out.println(var.collection.estimatedDocumentCount());
		}
		else if(tableName.equals("games") || tableName.equals("operators") || tableName.equals("providers") || tableName.equals("users")) {
			expectedRecordsNumber = 20;
		}

		existingRecords = var.collection.countDocuments(Filters.and(Filters.gte("_id", 1000000000), Filters.lte("_id", 1000000000+expectedRecordsNumber)));

		if(existingRecords!=-1 && existingRecords < expectedRecordsNumber) {

			for(int c=1; c<=expectedRecordsNumber; c++) {
				documentIds.add((1000000000+c));
			}
			DistinctIterable<Integer> documents = 
					var.collection.distinct("_id", Integer.class )
					.filter(Filters.and(Filters.gte("_id", 1000000000), Filters.lte("_id", 1000000000+expectedRecordsNumber)));

			for (Integer document : documents) {
				if(documentIds.contains(document)) {
					documentIds.remove(document);
				}
			}
		}

		//System.out.println(docIds);
		if(documentIds.size() > 0 ) {		
			CommonVariables.db.insertDocAsJSON(tableName, documentIds, var);
		}
	}

	public List<LocalDateTime> getValueFromDB(String tableName, String fieldName, String filterByField, String filterByFieldValue) {

		Object fieldVar = null;
		DistinctIterable<LocalDateTime> documents;
		List<LocalDateTime> values = new ArrayList<LocalDateTime>();
		if(var.collection==null) {
			CommonVariables.db.setCollection(tableName,var);
		}

		if(filterByFieldValue.matches("\\d+")) {

			fieldVar = Integer.parseInt(filterByFieldValue);
		}
		else {
			fieldVar = filterByFieldValue;
		}

		if(filterByFieldValue!=null && !filterByFieldValue.isEmpty()) {
			documents = var.collection.distinct(fieldName, LocalDateTime.class )
					.filter(Filters.eq(filterByField, fieldVar));
		}
		else {
			documents = var.collection.distinct(fieldName, LocalDateTime.class );
		}

		for (LocalDateTime document : documents) {
			values.add(document);
		}
		return values;
	}

	public long getResultOnDB(String fieldName, String wholeOrPartString, String fieldValue, String tableName) {

		Object fieldVar = null;
		Pattern regex;
		long resultOnDB;
		CommonVariables.db.setCollection(tableName,var);

		if(fieldValue.matches("\\d+")) {
			fieldVar = Integer.parseInt(fieldValue);
		}
		else if(fieldValue.matches("true")) {
			fieldVar = true;
		}
		else if(fieldValue.matches("false")) {
			fieldVar = false;
		}
		else {
			fieldVar = fieldValue;

			if(wholeOrPartString.equals("part")) {
				regex = Pattern.compile("^.*" + fieldVar + ".*$", Pattern.CASE_INSENSITIVE);
			}
			else if(wholeOrPartString.equals("underline")) {
				fieldVar = fieldVar.toString().replaceAll(" ", "_");
				regex = Pattern.compile(fieldVar + "$", Pattern.CASE_INSENSITIVE);
			}
			else {
				regex = Pattern.compile(fieldVar + "$", Pattern.CASE_INSENSITIVE);
			}
			fieldVar = regex;
		}
		resultOnDB = CommonVariables.db.printResultsFilter(Filters.eq(fieldName, fieldVar ), var);

		return resultOnDB;

		//MongoCollection<Document> colection = CommonVariables.db.setCollection(tableName);
		//System.out.println(colection.countDocuments(Filters.eq(fieldName, fieldValue)));
		//System.out.println(var.collection.countDocuments(Filters.gte("_id", 1000000000)));
		//CommonVariables.db.printResultsFilter(Filters.and(Filters.eq("categories.0.name", "Premium"), Filters.eq("id", 222)));
		//		Instant from = Instant.parse("2019-01-01T00:00:00.000Z");
		//		Instant to = Instant.parse("2019-03-01T00:00:00.000Z");
		//
		//		FindIterable<Document> documents = var.collection.find(Filters.and("started", gte(from),lt(to)));
	}

	public void enterDataOnDB(DataTable dbParameters) {

		Map<String, String> dataMap = dataTableToMap(dbParameters);
		List<Bson> fieldList = new ArrayList<Bson>();
		Object formatValue;
		Document newDocument = new Document();
		CommonVariables.db.setCollection(dataMap.get("collection"), var);

		for(Entry<?, ?> mapEntry : dataMap.entrySet()) {

			if(mapEntry.getValue().toString().matches("\\d+")) {
				formatValue = Integer.parseInt(mapEntry.getValue().toString());
			}
			else if(mapEntry.getValue().toString().matches("true")) {
				formatValue = true;
			}
			else if(mapEntry.getValue().toString().matches("false")) {
				formatValue = false;
			}
			else {
				formatValue = mapEntry.getValue();
			}

			if(!mapEntry.getKey().equals("collection") && !mapEntry.getKey().toString().equals("state")) {
				if(formatValue.equals("new ObjectId()")) {
					newDocument.append(mapEntry.getKey().toString(), new ObjectId());
				}
				else {
					if(mapEntry.getKey().toString().contains("Filter")) {
					    fieldList.add(Filters.eq(mapEntry.getKey().toString().replace("Filter", ""), formatValue)); 
					}
					newDocument.append(mapEntry.getKey().toString().replace("Filter", ""), formatValue);
				}
			}
		}

	    Bson match = Aggregates.match(Filters.and(fieldList));
	    List<Document> results = var.collection.aggregate(Arrays.asList(match), Document.class).into(new ArrayList<>());
	    
		if(dataMap.containsValue("On") && results.size()<1) {
			CommonVariables.db.insertDoc( var, dataMap.get("collection")
					, newDocument);
		}
		else if(dataMap.containsValue("Off") && results.size()>0) {
			var.collection.deleteOne(Filters.and(fieldList));
		}

	}

	public void changeKeyValueOnDB(DataTable dbParameters){

		Map<String, String> dataMap = dataTableToMap(dbParameters);

		CommonVariables.db.setCollection(dataMap.get("collection"),var);

		Bson filter = Filters.eq(dataMap.get("filterByKey"),  dataMap.get("filterKeyValue"));

		Object formatValue;

		if(dataMap.get("value").matches("\\d+")) {
			formatValue = Integer.parseInt(dataMap.get("value"));
		}
		else if(dataMap.get("value").matches("true")) {
			formatValue = true;
		}
		else if(dataMap.get("value").matches("false")) {
			formatValue = false;
		}
		else {
			formatValue = dataMap.get("value");
		}

		BasicDBObject newValue = new BasicDBObject(dataMap.get("nest") + dataMap.get("key"), formatValue);
		var.collection.updateOne(filter, new BasicDBObject("$set", newValue));
	}

	public void checkResultsDB(String fieldName, String wholeOrPartString, String fieldValue, String tableName) throws IOException {

		int resultOnWeb = 0;
		int resultCounterOnWeb = 0;
		long resultOnDB = getResultOnDB(fieldName, wholeOrPartString, fieldValue, tableName);

		if(wholeOrPartString.equals("whole")) {
			resultOnWeb = getMatchOnTable(fieldValue);
			resultCounterOnWeb = getResultNumberOnWeb(false);
		}
		else {
			resultOnWeb = getResultNumberOnWeb(false);
		}

		if(resultOnWeb == resultOnDB) {
			reporter.info("The number of table rows on page (" + resultOnWeb + ") which contain '" + fieldValue + "' is equal to number on DB (" + resultOnDB + ").");
			System.out.println("The number of table rows on page (" + resultOnWeb + ") which contain '" + fieldValue + "' is equal to number on DB (" + resultOnDB + ").");
			Assert.assertTrue("[EXPECTED] The number of table rows on page (" + resultOnWeb + ") which contain '" + fieldValue + "' is equal to number on DB (" + resultOnDB + ").", true);
		}
		else if(resultCounterOnWeb == resultOnDB) {
			reporter.info("The number of rows (" + resultCounterOnWeb + ") which contain '" + fieldValue + "' on the page is equal to number on DB (" + resultOnDB + ").");
			System.out.println("The number of rows (" + resultCounterOnWeb + ") which contain '" + fieldValue + "' on the page is equal to number on DB (" + resultOnDB + ").");
			Assert.assertTrue("[EXPECTED] The number of rows (" + resultCounterOnWeb + ") which contain '" + fieldValue + "' on the page is equal to number on DB (" + resultOnDB + ").", true);
		}
		else if(resultOnDB>999) {
			reporter.info("The number of rows on DB " + resultOnDB + " is bigger than 999.");
			System.out.println("The number of rows on DB " + resultOnDB + " is bigger than 999.");
			Assert.assertTrue("[EXPECTED] The number of rows on DB " + resultOnDB + " is bigger than 999.", true);
		}
		else {
			reporter.info("The number of rows ( " + resultOnWeb + " ) that contains  '" + fieldValue + "' on the page is different by number on DB (" + resultOnDB + ").");
			System.out.println("The number of rows ( " + resultOnWeb + " ) that contains '" + fieldValue + "' on the page is different by number on DB (" + resultOnDB + ").");
			Assert.fail("[ACTUAL] The number of rows ( " + resultOnWeb + " ) that contains '" + fieldValue + "' on the page is different by number on DB (" + resultOnDB + ").");
		}
	}

	public void checkExistingOnPage(String element, String notDisplayedElement) throws IOException {

		isloadComplete(driver);

		List<WebElement> elements = driver.findElements(By.xpath("//*[contains(text(), '" + element + "')]"));
		int count = 0 , containCount = 0;

		for(int i = 0 ; i< elements.size(); i++) {

			//System.out.println(elements.get(i).getAttribute("innerText"));

			if(elements.get(i).getAttribute("innerText").toLowerCase().equals(element.toLowerCase()) && elements.get(i).isDisplayed())
			{
				count++;
			}
			else if(elements.get(i).getAttribute("innerText").toLowerCase().contains(element.toLowerCase()) && elements.get(i).isDisplayed()) {
				containCount++;
			}
		}
		reporter.info("There are " + count + " visible elements with text = " + element + ".");
		reporter.info("There are " + containCount + " similar elements which contain text " + element + ".");

		if (driver.getPageSource().contains(element)
				&& elements.size()>0
				&& (count>0 || containCount>0)
				&& !driver.getPageSource().contains(notDisplayedElement)) {
			reporter.info("Element '" + element + "' does exists on page.");
			reporter.info("Element '" + notDisplayedElement + "' does NOT exists on page.");
			Assert.assertTrue("[EXPECTED] Element '" + element + "' does exists on page.", true);
		} else {
			reporter.info("Element '" + element + "' does NOT exists on page.");
			if(driver.getPageSource().contains(notDisplayedElement)) {
				reporter.info("Element '" + notDisplayedElement + "' does exists on page.");
			}
			Assert.fail("[ACTUAL] Element '" + element + "' does NOT exists on page.");
		}

		element = element.replaceAll(" ", "_") + "_page";
		new AttachFile().Screenshot();
		//takeScreenShot(driver, element);

	}

	public void checkNotExistingOnPage(String element) throws IOException {

		isloadComplete(driver);

		if (!driver.getPageSource().contains(element)) {
			reporter.info("Element " + element + " does NOT exists on page.");
			Assert.assertTrue("[EXPECTED] Element " + element + " does NOT exists on page.", true);
		} else {
			reporter.info("Element " + element + " exists on page.");
			Assert.fail("[ACTUAL] Element " + element + " exists on page.");
		}

		element = element.replaceAll(" ", "_") + "_page";
		new AttachFile().Screenshot();
		//takeScreenShot(driver, element);
	}

	public void checkMatchOnTable(String element, int elementNumber, String notExistingElement) throws IOException {

		Map<String,Integer> tableCounters = getTableSimilarities(element, notExistingElement);

		int counterOnWeb = getResultNumberOnWeb(false);

		if (elementNumber==0) {
			if(tableCounters.get("matchedCellValues")==0 
					&& (counterOnWeb==0 || counterOnWeb==tableCounters.get("containedCellValues"))
					&& tableCounters.get("notExistingElement")==0) {
				reporter.info("Element " + element + " does NOT exists on the table.");
				Assert.assertTrue("[EXPECTED] Element " + element + " does NOT exists on the table.", true);
			}
			else {
				reporter.info("Element " + element + " exists on the table " + tableCounters.get("matchedCellValues") + " times OR pagination counter is different.");
				Assert.fail("[ACTUAL] Element " + element + " exists on the table " + tableCounters.get("matchedCellValues") + " times OR pagination counter is different.");
			} 
		}
		else {
			if(tableCounters.get("matchedCellValues")>0 
					&& (tableCounters.get("matchedCellValues")==elementNumber
					|| counterOnWeb==elementNumber
					|| counterOnWeb==tableCounters.get("matchedCellValues"))
					&& tableCounters.get("notExistingElement")==0) {
				reporter.info("The table contains "+ tableCounters.get("matchedCellValues") +" matches of element " + element );
				Assert.assertTrue("[EXPECTED] The table contains "+ tableCounters.get("matchedCellValues") +" matches of element " + element, true);
			}
			else {
				reporter.info("Incorrect element existing OR number of '" + element + "' : " + tableCounters.get("matchedCellValues") + " on the table is incorrect.");
				Assert.fail("[ACTUAL] Incorrect element existing OR number of '" + element + "' : " + tableCounters.get("matchedCellValues") + " on the table is incorrect.");
			} 
		}

		element = element.replaceAll(" ", "_") + "_page";
		new AttachFile().Screenshot();
		//takeScreenShot(driver, element);
	}

	public void checkExistOnTable(String element, int elementNumber, String notExistingElement) throws IOException {

		Map<String,Integer> tableCounters = getTableSimilarities(element, notExistingElement);

		int sumMatchedAndContained = 0;
		if(var.allSimilarities) {
			sumMatchedAndContained = tableCounters.get("containedCellValues") + tableCounters.get("matchedCellValues");
		}

		int counterOnWeb = getResultNumberOnWeb(false);

		if (elementNumber==0) {
			if(tableCounters.get("containedCellValues")==0 || counterOnWeb==0 || ( var.counter!=-1 && counterOnWeb==var.counter)) {
				reporter.info("Element " + element + " does NOT exists on the table.");
				Assert.assertTrue("[EXPECTED] Element " + element + " does NOT exists on the table.", true);
			}
			else {
				reporter.info("Element " + element + " exists on the table " + tableCounters.get("containedCellValues") + " times OR pagination counter is different.");
				Assert.fail("[ACTUAL] Element " + element + " exists on the table " + tableCounters.get("containedCellValues") + " times OR pagination counter is different.");
			} 
		}
		else {
			if(sumMatchedAndContained==0) {
				if(tableCounters.get("containedCellValues")>0 
						&& (tableCounters.get("containedCellValues")==elementNumber || counterOnWeb==elementNumber || counterOnWeb==tableCounters.get("containedCellValues"))
						&& tableCounters.get("notExistingElement")==0)
				{
					reporter.info("Element " + element + " exists on the table " + tableCounters.get("containedCellValues") + " times.");
					Assert.assertTrue("[EXPECTED] Element " + element + " exists on the table " + tableCounters.get("containedCellValues") + " times.", true);
				}
				else {
					reporter.info("Incorrect element existing OR number of '" + element + "' : " + tableCounters.get("containedCellValues") + " on the table is incorrect.");
					Assert.fail("[ACTUAL] Incorrect element existing OR number of '" + element + "' : " + tableCounters.get("containedCellValues") + " on the table is incorrect.");
				} 
			}
			else {
				if(sumMatchedAndContained==elementNumber || counterOnWeb==elementNumber || counterOnWeb==sumMatchedAndContained) {
					reporter.info("SUM of matched and contained elements " + element + " on the table is " + sumMatchedAndContained + " .");
					Assert.assertTrue("[EXPECTED] SUM of matched and contained elements " + element + " on the table is " + sumMatchedAndContained + " .", true);
				}
				else {
					reporter.info("Incorrect element existing OR number of '" + element + "' : " + sumMatchedAndContained + " on the table is incorrect.");
					Assert.fail("[ACTUAL] Incorrect element existing OR number of '" + element + "' : " + sumMatchedAndContained + " on the table is incorrect.");
				} 
			}
		}

		var.counter = -1;
		var.allSimilarities = false;
		element = element.replaceAll(" ", "_") + "_page";
		
		new AttachFile().Screenshot();
		//takeScreenShot(driver, element);
	}
	

	public Map<String,Integer> getTableSimilarities(String element, String notExistingElement){

		int rowCounter = 0;
		int matchCounter = 0;
		int conCounter = 0;
		int eleNotExist = 0;
		List<WebElement> allRows = null;
		Map<String,Integer> tableCounters = new HashMap<String,Integer>();
		isloadComplete(driver);

		if(element.equals("true")) {
			element = "Yes";
		}
		else if(element.equals("false")) {
			element = "No";
		}

		while(rowCounter<3) {

			if(driver.findElements(By.cssSelector("table.grid-table")).size()>0) {
				allRows = driver.findElements(By.cssSelector("table.grid-table tr"));
			}
			else if(driver.findElements(By.cssSelector("table.bet-config-table")).size()>0) {
				allRows = driver.findElements(By.cssSelector("table.bet-config-table tr"));
			}

			if(allRows!=null && allRows.size()>3) {
				break;
			}
			else {
				rowCounter++;
			}
		}

		rowCounter=0;

		try {
			//((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", allCells.get(0));
			new Actions(driver).moveToElement(allRows.get(0)).perform();
		}catch(Exception e) {}

		for(WebElement row : allRows){

			List<WebElement> allRowCells;
			allRowCells = row.findElements(By.cssSelector("td"));
			if(allRowCells.isEmpty()) {
				allRowCells = allRows.get(0).findElements(By.cssSelector("th"));
			}

			List<String> cellValues = new ArrayList<String>();
			for(WebElement rowCell : allRowCells) {

				if (!cellValues.contains(rowCell.getText())) { 

					cellValues.add(rowCell.getText()); 
				}
			}
			//			cellValues = cellValues.stream()
			//					.distinct()
			//					.collect(Collectors.toList());
			//			System.out.println(cellValues);

			if(cellValues.contains(element)) {
				matchCounter++;
			}
			else {
				for(String cell: cellValues){
					if(cell.contains(element)) {
						conCounter++;
						break;
					}
					else if(cell.equals(notExistingElement) && !notExistingElement.contains(element)) {
						eleNotExist = 1; //like true
					}
				}
			}

			rowCounter++;

			if(rowCounter%5==0) {
				try {
					if(allRows.size()<12 && rowCounter==5) {
						WebElement lastTableElement = driver.findElement(By.xpath("(//table[1]/tbody/tr)[last()]"));
						((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true)", lastTableElement);
					}
					else {
						WebElement nextTableElement = driver.findElement(By.xpath("(//table[1]/tbody/tr["+rowCounter+"])"));
						((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true)", nextTableElement);
					}
				}catch(Exception e) {}
			}
		}

		tableCounters.put("matchedCellValues", matchCounter);
		tableCounters.put("containedCellValues", conCounter);
		tableCounters.put("notExistingElement", eleNotExist);

		return tableCounters;
	}

	public int getMatchOnTable(String element) throws IOException {

		if(getResultNumberOnWeb(false)>10) {
			selectComboBoxOption("50", rowsOnPage);
			isloadComplete(driver);
		}

		return getTableSimilarities(element, "").get("matchedCellValues");
	}

	public String clarifyDate(String inputText, LocalDateTime date) {

		if( inputText.length()>"lastInputTestDate".length()) {
			String period = null;
			Long offset = null;
			String action = null;

			String pattern = "(Minute||Hour||Day)(Minus||Plus)(\\d+)";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(inputText);
			if (m.find( )) {
				period = m.group(1);
				offset = Long.valueOf(m.group(3));
				action = m.group(2);
				System.out.println("Period : " + period );
				System.out.println("Action : " + action );
				System.out.println("Offset : " + offset );
			}		         

			if(action.equals("Minus")) {
				if(period.equals("Minute")) {
					date = date.minusMinutes(offset);
				}
				else if(period.equals("Hour")) {
					date = date.minusHours(offset);
				}
				else if(period.equals("Day")) {
					date = date.minusDays(offset);
				}
			}
			else if(action.equals("Plus")) {
				if(period.equals("Minute")) {
					date = date.plusMinutes(offset);
				}
				else if(period.equals("Hour")) {
					date = date.plusHours(offset);
				}
				else if(period.equals("Day")) {
					date = date.plusDays(offset);
				}
			}
		}
		inputText = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
		System.out.println("Enter date : " + inputText);

		return inputText;
	}

	public void loginClient(WebDriver driver, String clientURL, String user) {

		navigateToURL(driver, clientURL);

		if(driver.getPageSource().contains("Forgot Password")) {

			LoginPO login = new LoginPO();

			login.enterCredentials(user, "valid.password");

			login.buttonLogin.click();

			isloadComplete(driver);

			if(!driver.getCurrentUrl().equals(clientURL)) {
				navigateToURL(driver, clientURL);
			}
		}
		isloadComplete(driver);
	}

	public void navigateToURL(WebDriver driver, String url) {

		if(!url.endsWith("/")) {
			url = url + "/";
		}

		if(driver.getTitle().isEmpty()) {
			try{
				driver.get(url);
			} catch (TimeoutException ex) {
				driver.get(url);
			}
		}
		else {
			driver.navigate().to(url);
		}

		if(isloadComplete(driver)) {
			System.out.println("The page '" + driver.getTitle() + "' is loaded.");
		}
		else {
			System.out.println("The page is NOT loaded.");
		}
	}

	public boolean isloadComplete(WebDriver driver)
	{
		String result, loader;
		boolean loaded = false;

		for(int i=0; i < 10; i++) {
			result =  String.valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState"));
			loader =  String.valueOf(((JavascriptExecutor) driver).executeScript("return document.getElementsByClassName('loader small-loader').length!=0"));
			if(result.equals("complete") && loader.equals("false")) {
				loaded = true;
				break;
			}
			else {
				needToSleep(1000);
			}
		}
		needToSleep(1000);
		return loaded;
	}

//	public void takeScreenShot(WebDriver driver, String nameShot) throws IOException {
//
//		String newShotName = null;
//		List<Integer> numbers = new ArrayList<Integer>();
//		needToSleep(1500);
//		File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
//
//		String reportPath = ExtentProperties.INSTANCE.getReportPath();
//		reportPath = reportPath.substring(0, reportPath.indexOf("report.html"));
//
//		File dir = new File(reportPath + "screenshots//");
//		if (!dir.exists()) {
//			if (dir.mkdir()) {
//				System.out.println("Directory 'screenshots' is created!");
//			} else {
//				System.out.println("Failed to create directory 'screenshots'!");
//			}
//		}
//		String[] files = dir.list();
//		if (files.length == 0) {
//			System.out.println("The directory is empty");
//			newShotName = nameShot.replaceAll("page[0-9]", "page") + 1;
//		} else {
//			for (String aFile : files)
//			{
//				if(aFile.contains(nameShot))
//				{
//					if(aFile.matches(".*\\d+.*"))
//					{
//						String num = aFile.replaceAll(".*page", "");
//						num = num.replaceAll("[^0-9]", "");
//						numbers.add(Integer.parseInt(num));  //  [^0-9]
//					}
//					else {
//						newShotName = nameShot.replaceAll("page[0-9]", "page") + 1;
//					}
//				}
//			}     
//
//			if(numbers.size() > 0) {
//				Collections.sort(numbers);
//				int number = numbers.get(numbers.size()-1);
//				newShotName = nameShot.replaceAll("page[0-9]", "page") + (number + 1);
//			}
//			else {
//				newShotName = nameShot.replaceAll("page[0-9]", "page") + 1;
//			}
//		}
//
//		String imageRelative  = "screenshots/" + newShotName +".png";
//		//System.out.println("The image name and shot folder are " + imageRelative);
//
//		String relativePath = reportPath + imageRelative;
//		//System.out.println("The shot relative path is " + relativePath);
//
//		//FileUtils.copyFile(screenshot, new File("output//screenshots//" + newShotName +".png"));
//		FileUtils.copyFile(screenshot, new File(relativePath));
//
//		//absolutePath = FileSystems.getDefault().getPath("output//screenshots//screenshot_non_runners.png").normalize().toAbsolutePath().toString();
//		//absolutePath = new File("output//screenshots//" + newShotName +".png").getCanonicalPath();
//
//		reporter.addScreenCaptureFromPath(imageRelative);
//	}

	public WebElement getParentFrom(WebElement element)
	{
		WebElement parent = element.findElement(By.xpath("./.."));

		return parent;
	}

	public WebElement setButton(String button) {
		buttonToClick = null;

		if(button.contains("Login")) {
			buttonToClick = new LoginPO().buttonLogin;
		}
		else if(button.contains("Forgot")) {
			buttonToClick = new LoginPO().linkForgotPassword;
		}
		else if(button.contains("Logout")) {
			buttonToClick = new HomePagePO().buttonLogout;
		}
		else if(button.contains("SEARCH")) {
			buttonToClick = buttonSearch;
		}
		else if(button.contains("ClearFilters")) {
			buttonToClick = buttonClearFilters;
		}
		else if(button.contains("nextPaginationPage")) {
			buttonToClick = nextPaginationPage;
		}
		else if(button.contains("lastPaginationPage")) {
			buttonToClick = lastPaginationPage;
		}
		else if(button.equals("Add")) {
			buttonToClick = buttonAdd;
		}
		else if(button.contains("AddBetIncrement")) {
			buttonToClick = new BetConfigurationsPO().buttonAddBetIncrement;
		}
		else if(button.contains("Edit")) {
			buttonToClick = buttonEdit;
		}
		else if(button.contains("Create")) {
			buttonToClick = buttonCreate;
		}
		else if(button.contains("Save")) {
			buttonToClick = buttonSave;
		}
		else if(button.contains("Order by")) {
			button = button.replaceFirst("Order by ", "");
			buttonToClick = driver.findElement(By.xpath("//th[text() = '" + button + "']"));
		}
		else {
			buttonToClick = driver.findElement(By.xpath("//*[contains(text(), '" + button + "')]"));
		}

		return buttonToClick;
	}

	public void clickButton(String button) {

		if(checkButtonState(button)) {
			buttonToClick.click();
		}
		else {
			if(!buttonToClick.getAttribute("id").isEmpty()) {
				System.out.println("Button '" + buttonToClick.getAttribute("id") + "' is missing or inactive !");
			}
			else {
				System.out.println("Button '" + button + "' is missing or inactive !");
			}
		}
	}

	public boolean checkButtonState(String button){

		isloadComplete(driver);

		setButton(button);

		if(buttonToClick!= null && buttonToClick.isDisplayed() && buttonToClick.isEnabled()) {
			return true;
		} else {
			return false;
		}
	}

	public void checkButtonIsActive(String button, String buttonState){

		if(buttonState.equals("active")) {
			if(checkButtonState(button)) {
				reporter.info("The button '" + buttonToClick.getAttribute("id") + "' is active.");
				Assert.assertTrue("[EXPECTED] The button '" + buttonToClick.getAttribute("id") + "' is active.", true);
			} else {
				reporter.info("The button '" + buttonToClick.getAttribute("id") + "' is missing or inactive !");
				Assert.fail("[ACTUAL]The button '" + buttonToClick.getAttribute("id") + "' is missing or inactive !");
			}
		}
		else {
			if(!checkButtonState(button)) {
				reporter.info("The button '" + buttonToClick.getAttribute("id") + "' is inactive.");
				Assert.assertTrue("[EXPECTED] The button '" + buttonToClick.getAttribute("id") + "' is inactive.", true);
			} else {
				reporter.info("The button '" + buttonToClick.getAttribute("id") + "' is active !");
				Assert.fail("[ACTUAL]The button '" + buttonToClick.getAttribute("id") + "' is active !");
			}
		}
	}

	//clear field
	public void clear(String page, String inputName){

		if(page.equals("History")) {
			new GameHistoryPO().setActiveHistoryElement(inputName).sendKeys(Keys.chord(Keys.CONTROL, "a") , Keys.DELETE);
		}
		else if(page.equals("Games")) {
			new GameManagementPO().setActiveGamesElement(inputName).clear();
			new GameManagementPO().setActiveGamesElement(inputName).sendKeys(Keys.chord(Keys.CONTROL, "a") , Keys.DELETE);
		}
		else if(page.equals("Operators")) {
			new OperatorManagementPO().setActiveOperatorsElement(inputName).sendKeys(Keys.chord(Keys.CONTROL, "a") , Keys.DELETE);
		}
		else if(page.equals("Providers")) {
			new ProviderManagementPO().setActiveProvidersElement(inputName).sendKeys(Keys.chord(Keys.CONTROL, "a") , Keys.DELETE);
		}
		else if(page.equals("Users")) {
			new UsersPO().setActiveUserElement(inputName).sendKeys(Keys.chord(Keys.CONTROL, "a") , Keys.DELETE);
		}
	}

	//Write Text
	public void writeText (WebDriver driver, WebElement element, String text) {

		Actions seriesOfActions = new Actions(driver);
		element.clear();
		seriesOfActions.moveToElement(element).click().sendKeys(element, text).perform();

		if(element.isEnabled() && !element.getAttribute("value").equals(text)) {
			element.click();
			JavascriptExecutor js = (JavascriptExecutor)driver;
			js.executeScript("arguments[0].value='"+ text +"';", element);

			element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
			element.sendKeys(Keys.chord(Keys.CONTROL, "c"));
			js.executeScript("arguments[0].value='';", element);
			element.sendKeys(Keys.ESCAPE);
			element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
			element.sendKeys(Keys.CONTROL + "v");
			element.sendKeys(Keys.ENTER);
		}
		System.out.println("Entered value : " + element.getAttribute("value"));
		reporter.info("Enter : " + text);
	}

	public void selectComboBoxOption(String option, String comboName){
		WebElement combo 	= getComboByName(comboName);
		needToSleep(1000);
		WebElement parent 	= getParentFrom(combo);

		parent.findElement(By.tagName("input")).click();

		parent = parent.findElement(By.tagName("ul"));

		List<WebElement> allChildren = parent.findElements(By.xpath(".//child::li"));

		if(option.equals("random")) {
			allChildren.get(1).click();
			//var.options = getAllComboOptions(combo);
			//parent.findElement(By.xpath("//span[contains(text(), '" + var.options.get(1).toString() + "')]")).click();
		}
		else {
			needToSleep(1000);
			for(WebElement ele : allChildren) {
				if(ele.getAttribute("innerText").equals(option)) {
					//System.out.println(ele.getAttribute("innerText"));
					ele.click();
					break;
				}
			}
		}
	}

	public WebElement getComboByName(String comboName)
	{
		WebElement combo = null;

		if(comboName.contains("Game Provider")) {
			combo = new GameManagementPO().comboGameProvider;
		}
		else if(comboName.equals("Status")) {
			combo = new GameHistoryPO().comboStatus;
		}
		else if(comboName.equals("Type")) {
			combo = new GameManagementPO().comboGameType;
		}
		else if(comboName.contains("Categories")) {
			combo = new GameManagementPO().comboGameCategories;
			var.allSimilarities = true;
		}
		else if(comboName.contains("Integration Type")) {
			combo = new OperatorManagementPO().comboOperatorType;
		}
		else if(comboName.contains("Platform")) {
			combo =  new OperatorManagementPO().comboOperatorPlatform;
		}
		else if(comboName.contains("Provider Type")) {
			combo =  new ProviderManagementPO().comboProviderType;
		}
		else if(comboName.contains("Enabled")) {
			combo =  new UsersPO().comboIsEnabled;
		}
		else if(comboName.equals("Currency")) {
			combo = new BetConfigurationsPO().dropDownCurrency;
		}
		else if(comboName.equals("Collection")) {
			combo = new LogsPO().comboCollection;
		}
		return combo;
	}

	public void selectComboBoxOption(String option,  WebElement comboName){

		Random rnd = new Random();
		isloadComplete(driver);
		WebElement parent = getParentFrom(comboName);

		parent.findElement(By.tagName("input")).click();
		needToSleep(1000);
		parent = parent.findElement(By.tagName("ul"));

		List<WebElement> allChildren = parent.findElements(By.xpath(".//child::li"));

		if(option.equals("random")) {
			for(int c=0;c<allChildren.size(); c++) {
				if(!allChildren.get(c).getAttribute("class").equals("selected")) {
					allChildren.get(c).click();
					break;
				}
				if(allChildren.size()-1==c) {
					allChildren.get(c).click();
				}
			}
			// click random option between 0 and (array size -1 )
			//allChildren.get(rnd.nextInt((allChildren.size()-1) - 0 + 1) + 0).click();
		}
		else if(option.equals("exceptEmpty")) {
			allChildren.get(rnd.nextInt((allChildren.size()-1) - 1 + 1) + 1).click();
		}
		else {
			needToSleep(1000);
			for(WebElement ele : allChildren) {
				if(ele.getAttribute("innerText").equals(option)) {
					//System.out.println(ele.getAttribute("innerText"));
					ele.click();
					break;
				}
			}
		}
	}

	public void selectDropDownOption(String option, String dropDownName){

		WebElement			dropDown 	= getDropDownByName(dropDownName);
		List<WebElement>	allChildren = dropDown.findElements(By.xpath(".//child::*"));
		List<Object>		allOptions 	= new ArrayList<>();
		String				optionStr;
		Random rnd = new Random();

		if(option.equals("random")) {
			try {
				allChildren = dropDown.findElements(By.xpath(".//child::li"));
				// click random option between 0 and (array size -1 )
				allChildren.get(rnd.nextInt((allChildren.size()-1) - 0 + 1) + 0).click(); 
			}catch (Exception e) {};
		}
		else {
			for (WebElement optionEle : allChildren) {
				optionStr = optionEle.getAttribute("innerText");
				if (!optionStr.isEmpty() && option.equals(optionStr)){
					allOptions.add(optionStr);
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", optionEle);
					optionEle.click();
					break;
				}
			}
		}
		System.out.println("Selected option from dropDown '" + dropDownName + "' is : " + allOptions);
	}

	public void selectDropDownOption(String option, WebElement dropDown){

		List<WebElement>	allChildren = dropDown.findElements(By.xpath(".//child::*"));
		List<Object>		allOptions 	= new ArrayList<>();
		String				optionStr;
		Random rnd = new Random();

		if(option.equals("random")) {
			try {
				// click random option between 0 and (array size -1 )
				allChildren.get(rnd.nextInt((allChildren.size()-1) - 0 + 1) + 0).click(); 
			}catch (Exception e) {};
		}
		else {
			for (WebElement optionEle : allChildren) {
				optionStr = optionEle.getAttribute("innerText");
				if (!optionStr.isEmpty() && optionStr.equals(option)) {
					allOptions.add(optionStr);
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", optionEle);
					optionEle.click();
					break;
				}
			}
		}
		System.out.println("Selected option from dropDown '" + dropDown + "' is : " + allOptions);
	}

	public WebElement getDropDownByName(String dropDownName)
	{
		WebElement dropDown = null;

		if(dropDownName.contains("Operator Name")) {
			dropDown = new GameHistoryPO().dropDownHistoryOperName;
		}
		else if(dropDownName.equals("Game Name")) {
			dropDown = new GameHistoryPO().dropDownHistoryGameName;
		}
		else if(dropDownName.contains("Provider Name")) {
			dropDown = new ProviderManagementPO().dropDownProviderName;
		}
		else if(dropDownName.contains("User Name")) {
			dropDown = new UsersPO().dropDownUserName;
		}
		else if(dropDownName.equals("Game Name Bet")) {
			dropDown = new BetConfigurationsPO().dropDownBetGameName;
		}
		else if(dropDownName.equals("Operator Bet")) {
			dropDown = new BetConfigurationsPO().dropDownBetOperatorName;
		}

		return dropDown;
	}

	//Get combo menu ALL options 
	public List<Object> getAllComboOptions(WebElement comboBox) {

		List<Object> allOptions = new ArrayList<>();

		@SuppressWarnings("unused")
		String optionStr=null;
		for (WebElement option : new Select(comboBox).getOptions()) {
			optionStr = option.getText();
			if (option.getAttribute("innerText") != "") allOptions.add(option.getAttribute("innerText"));
		}
		System.out.println("All the combo '" + comboBox.getText() + "' options are : " + allOptions);
		return allOptions;
	}

	public void checkInfoIsNotChanged(String typeInfo) throws IOException {

		JSONObject webInfoBefore = null, webInfoAfter = null;
		isloadComplete(driver);

		if(typeInfo.equals("Game")) {
			webInfoBefore = GameManagementPO.var.jsonWebInfo;
			webInfoAfter = new GameManagementPO().getGameInfoWeb();
		}
		else if(typeInfo.equals("Operator")) {
			webInfoBefore = OperatorManagementPO.var.jsonWebInfo;
			webInfoAfter = new OperatorManagementPO().getOperatorInfoWeb();
		}
		else if(typeInfo.equals("Provider")) {
			webInfoBefore = ProviderManagementPO.var.jsonWebInfo;
			webInfoAfter = new ProviderManagementPO().getProviderInfoWeb();
		}
		else if(typeInfo.equals("User")) {
			webInfoBefore = UsersPO.var.jsonWebInfo;
			webInfoAfter = new UsersPO().getUserInfoWeb();
		}

		System.out.printf(typeInfo + " info before changes : " +  System.lineSeparator() + webInfoBefore.toString(4));
		reporter.info(typeInfo + " info before changes : " + System.lineSeparator() + webInfoBefore.toString(4));

		System.out.printf(System.lineSeparator() + typeInfo + " info after changes : " +  System.lineSeparator() + webInfoAfter.toString(4));
		reporter.info(typeInfo + " info after changes : " + System.lineSeparator() + webInfoAfter.toString(4));

		checkForExistingError();

		if(webInfoAfter.toString().equals(webInfoBefore.toString())) {
			reporter.info("Data on " + typeInfo + " info page is not changed.");
			Assert.assertTrue("[EXPECTED] Error message is displayed. Data on " + typeInfo + " info page is not changed.", true);
		}
		else {
			reporter.info("Data on " + typeInfo + " info page is changed!");
			reporter.info("OR Data is trying to insert infinitely!");
			Assert.fail("[ACTUAL] Data on " + typeInfo + " info page is changed!");
		}

		new AttachFile().Screenshot();
		//takeScreenShot(driver, typeInfo.toLowerCase() + "Info_page");
	}

	public boolean checkForExistingError() {

		String regex = ".*\\S.*";
		String errorMessage = null;
		boolean exist = false;
		try {
			List<WebElement> elements = driver.findElements(By.xpath("//*[@class='helper-text' and @data-error]"));

			for(WebElement ele : elements) {
				errorMessage = ele.getAttribute("data-error");
				if(errorMessage.matches(regex)) {
					System.out.printf(System.lineSeparator() + "Displayed error message: " + errorMessage +  System.lineSeparator());
					exist = true;
					break;
				}
			}
		}catch(Exception e) {}


		try {
			List<WebElement> elementsModal = driver.findElements(By.xpath("//*[contains(@class, 'modal-error')]"));

			for(WebElement elem : elementsModal) {
				errorMessage = elem.getAttribute("innerText");
				if(errorMessage.contains("wrong") || errorMessage.contains("rror")) {
					System.out.printf(System.lineSeparator() + "Displayed error message: " + errorMessage +  System.lineSeparator());
					exist = true;
					break;
				}
			}
		}catch(Exception e) {}

		if(exist) {
			reporter.info("Error message '" + errorMessage + "' is displayed.");
		}
		return exist;
	}

	public void checkServerUpdated(DataTable checkedParameters) throws UnsupportedOperationException, IOException {

		Map<String, String> dataMap = dataTableToMap(checkedParameters);
		String serverConfig = null;
		int time = 1;

		if(!dataMap.get("ServerCheckLink").contains(PropertyUtils.getProperty("server.check.update"))) {

			serverConfig = PropertyUtils.getProperty("server.URL") + PropertyUtils.getProperty("server.config");
		}
		else {
			serverConfig = dataMap.get("ServerCheckLink");
		}

		StringBuilder value  = new StringBuilder();
		StringBuilder subValue = new StringBuilder();

		for(time = 1; time <= 10; time++) {
			var.response = new ResponseBy(var.sendRequest.Get(serverConfig, var.headers));

			subValue.append(new GetJsonValues()
					.getKeyValue(var.response.json, dataMap.get("basicKey"), dataMap.get("basicKeyValue"), dataMap.get("subKey")));

			value.append(new GetJsonValues().findKeyValue(new JSONObject(subValue.toString()), dataMap.get("key")));

			if(value.length()<1) {
				value = subValue;
			}
			System.out.println("Searched value is : " + value);

			if(value.toString().equals(dataMap.get("keyValue"))) {
				break;
			}
			needToSleep(1000);
			value.delete(0, value.length());
			subValue.delete(0, subValue.length());
		}
		if(value.toString().equals(dataMap.get("keyValue"))) {
			System.out.println("Server is up to date around " + time + " seconds");
			reporter.info("Server is up to date around " + time + " seconds");
			Assert.assertTrue("[EXPECTED] Server is up to date.", true);
		}
		else {
			reporter.info("Key value from server " + value + " is different by expected " + dataMap.get("keyValue"));
			Assert.fail("[ACTUAL] Key value from server " + value + " is different by expected " + dataMap.get("keyValue"));
		}
	}

	public Map<String, String> dataTableToMap(DataTable dataTable) {

		List<List<String>> dataList = dataTable.asLists();
		Map<String, String> dataMap = new HashMap<String, String>();

		for(int i=0;i<dataList.get(0).size();i++) {
			dataMap.put(dataList.get(0).get(i), dataList.get(1).get(i));
		}
		return dataMap;
	}

	public void pressKey(int counter, String key) {
		int i = 0;
		Keys keyToPress = null;
		WebElement currentElement = driver.switchTo().activeElement();

		if(key.equals("Tab")) {
			keyToPress = Keys.TAB;
		}
		else if(key.equals("Enter")) {
			keyToPress = Keys.ENTER;
		}

		while(i<counter) {
			if(key.equals("Back")) {
				driver.navigate().back();
			}
			else if(keyToPress!=null) {
				currentElement.sendKeys(keyToPress);
			}
			i++;
		}
	}

	public void needToSleep(int sleepTime){
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void switchToParentTab() {

		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());

		driver.switchTo().window(tabs.get(0));
	}

	public void closeAllWindowsExceptParent(WebDriver driver) {

		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		int tabsNumber = tabs.size();

		if( tabsNumber > 1 ) {

			for(int i = tabsNumber-1 ; i > 0 ; i--)
			{
				String winHandle = tabs.toArray()[i].toString();
				driver.switchTo().window(winHandle);
				driver.close();
			}
			driver.switchTo().window(tabs.get(0));
		}
	}

	@Override
	protected void load() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void isLoaded() throws Error {
		// TODO Auto-generated method stub

	}
}
