package TestObjects;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import com.google.common.collect.ImmutableMap;
import com.mongodb.client.model.Filters;

import UtilsHelper.AttachFile;
import UtilsHelper.CommonVariables;
import Driver.DriverFactory;

public class GameHistoryPO extends LoadableComponent<GameHistoryPO>
{
	public CommonVariables var = new CommonVariables();
	HomePagePO homePage = new HomePagePO();
	BaseMethodsO method = new BaseMethodsO(var);
	public String parentWindow;

	public WebDriver driver;
	public WebDriverWait wait;
	ExtentTest reporter = ExtentCucumberAdapter.getCurrentStep();
	public List<WebElement> listWebElements = new ArrayList<WebElement>();

	@FindBy(xpath = "//*[contains(@id, 'operator-name')]")
	public WebElement inputOperatorName;

	@FindBy(xpath = "//*[contains(@id, 'operator-id')]")
	public WebElement inputOperatorID;

	@FindBy(xpath = "//*[contains(@id, 'game-name')]")
	public WebElement inputGameName;

	@FindBy(xpath = "//*[contains(@id, 'game-id')]")
	public WebElement inputGameID;

	@FindBy(xpath = "//*[contains(@id, 'player-id')]")
	public WebElement inputPlayerID;

	@FindBy(xpath = "//*[contains(@id, 'search-status')]")
	public WebElement comboStatus;

	@FindBy(xpath = "//*[contains(@id, 'operator-ac-dd')]")
	public WebElement dropDownHistoryOperName;

	@FindBy(xpath = "//*[contains(@id, 'game-ac-dd')]")
	public WebElement dropDownHistoryGameName;

	@FindBy(xpath = "//*[contains(@id, 'from-date')]")
	public WebElement dateFrom;

	@FindBy(xpath = "//*[contains(@id, 'to-date')]")
	public WebElement dateTo;


	public GameHistoryPO() {
		driver = DriverFactory.getDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4)); // set implicit wait
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(4)); // set explicit wait
		PageFactory.initElements(driver, this);
	}

	//open History page
	public GameHistoryPO openHistoryPageFrom(String clientURL, String user)
	{
		if(!user.equals("loggedOutUser")) {
			parentWindow = method.openPage(driver, clientURL, user, homePage.buttonGameHistory);
			method.buttonClearFilters.click();
			method.deleteAllRecords("game_history", 144);
		}
		else {
			method.navigateToURL(driver, clientURL);
			method.deleteAllRecords("game_history", 0);
		}

		method.setDBRecords("games");
		method.setDBRecords("providers");
		method.setDBRecords("operators");
		method.setDBRecords("game_history");

		List<WebElement> allCells = null;
		int eleCounter = 0;
		method.isloadComplete(driver);

		if(!user.equals("loggedOutUser")) {
			while(eleCounter<3) {
				//method.needToSleep(5000);
				allCells = driver.findElements(By.cssSelector("table.grid-table tr td"));
				if(allCells.size()>3) {
					break;
				}
				else {
					eleCounter++;
				}
			}
		}

		return this;
	}

	public void enterText(String inputName, String inputText)	{

		if(inputText.contains("lastInputTestDate")) {

			Bson filterHistory = Filters.and(Filters.eq("game_id", 1000000001),Filters.eq("status", "CLOSED"));

			String value = CommonVariables.db.getValue(var, "game_history" , filterHistory , "_id").toString();

			List<LocalDateTime> outputValues = method.getValueFromDB("game_history", "date_time_ended", "_id", value);
			LocalDateTime date = outputValues.get(0);
			System.out.println("Get 'date_time_ended' of _id = " + value + " : " + date);

			inputText = method.clarifyDate(inputText, date);
		}
		method.writeText(driver, setActiveHistoryElement(inputName), inputText);
	}

	public WebElement setActiveHistoryElement(String inputName) {

		WebElement element = null;
		if(inputName.contains("GameName")) {
			element = inputGameName;
		}
		else if(inputName.contains("GameID")) {
			element = inputGameID;
			element.click();
		}
		else if(inputName.contains("OperatorName")) {
			element = inputOperatorName;
		}
		else if(inputName.contains("OperatorID")) {
			element = inputOperatorID;
		}
		else if(inputName.contains("PlayerID")) {
			element = inputPlayerID;
		}
		else if(inputName.contains("dateFrom")) {
			element = dateFrom;
		}
		else if(inputName.contains("dateTo")) {
			element = dateTo;
		}

		return element;
	}

	public void openGameRoundInfo(String idNumber) {

		List<WebElement> listWebElements = driver.findElements(By.xpath("//table//*[contains(@href, '/game-round/" + idNumber + "')]"));

		for(WebElement element : listWebElements) {

			if(element.getText().equals(idNumber)) {
				element.click();
				break;
			}
		}
	}

	public JSONObject getGameRoundInfoWeb() {

		method.isloadComplete(driver);

		Map<String, String> gameHistoryMap = ImmutableMap.<String, String>builder()
				.put("Status"				, "status"				) 				
				.put("Game Short Name"		, "game_short_name"		)		
				.put("Provider Name"		, "provider_name"		) 		
				.put("Total Bet"			, "total_bet"			) 			
				.put("Player ID"			, "player_id"			) 			
				.put("Game ID"				, "game_id"				)				
				.put("Game Name"			, "game_name"			) 			
				.put("Operator ID"			, "operator_id"			) 		
				.put("Date & Time"			, "date_time_started"	) 	
				.put("Operator Short Name"	, "operator_short_name"	) 
				.put("Game Type"			, "game_type"			) 			
				.put("Total Win"			, "total_win"			) 			
				.put("Provider ID"			, "provider_id"			) 		
				.put("Operator Name"		, "operator_name"		)		
				.put("Currency"				, "player_currency"		) 	
				.put("Game Round ID"		, "_id"					) 				
				.build();

		Map<String, Object> tableNameValues	= new HashMap<String, Object>();
		Pattern floatNum = Pattern.compile("^([0-9]+[,.][0-9]+)(.*)$"); // only float
		Pattern intNum = Pattern.compile("^([0-9]+)$"); // only int

		Matcher floatNumMatcher, intNumMatcher;

		List<WebElement> allRowsNames = driver.findElements(By.cssSelector("table.game-round-info-table tr th"));
		List<WebElement> allRowsValues = driver.findElements(By.cssSelector("table.game-round-info-table tr td"));

		if(allRowsValues.size() < allRowsNames.size()) {
			WebElement element = null;
			while(allRowsValues.size() < allRowsNames.size()) {
				allRowsValues.add(element);
			}
		}

		for(int i = 0 ; i < allRowsNames.size(); i++){

			if(!allRowsNames.get(i).getText().isEmpty()) {

				if(gameHistoryMap.containsKey(allRowsNames.get(i).getText())){

					if(allRowsValues.get(i)!=null) {

						floatNumMatcher = floatNum.matcher(allRowsValues.get(i).getText());
						intNumMatcher = intNum.matcher(allRowsValues.get(i).getText());

						if(floatNumMatcher.find()) {
							tableNameValues.put(gameHistoryMap.get(allRowsNames.get(i).getText()), Double.parseDouble(floatNumMatcher.group(1)));
						}
						else if(intNumMatcher.find()) {
							tableNameValues.put(gameHistoryMap.get(allRowsNames.get(i).getText()), Integer.parseInt(intNumMatcher.group()));
						}
						else {
							tableNameValues.put(gameHistoryMap.get(allRowsNames.get(i).getText()), allRowsValues.get(i).getText());
						}
					}
					else {
						tableNameValues.put(gameHistoryMap.get(allRowsNames.get(i).getText()), "null");
					}
				}
			}
		}

		if(tableNameValues.containsValue("closed")) {
			tableNameValues.put("date_time_ended", tableNameValues.remove( "date_time_started" ) );
		}

		JSONObject jsonObject = new JSONObject(tableNameValues);
		System.out.printf(jsonObject.toString(4));

		return jsonObject;
	}

	public JSONObject getGameRoundInfoDB(String fieldName , Object fieldValue) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		sdf.setTimeZone( TimeZone.getTimeZone( "UTC" ) );

		CommonVariables.db.setCollection("game_history",var);
		Document myDoc = var.collection.find(Filters.eq(fieldName, fieldValue)).first();

		JSONObject jsonObjectDB = new JSONObject(myDoc.toJson());
		if(myDoc.get("date_time_ended")!=null) {
			jsonObjectDB.put("date_time_ended", sdf.format(myDoc.get("date_time_ended")));
		}
		if(myDoc.get("date_time_started")!=null) {
			jsonObjectDB.put("date_time_started", sdf.format(myDoc.get("date_time_started")));
		}
		System.out.println(jsonObjectDB.toString(4));

		return jsonObjectDB;
	}

	public void checkWebDataAgainstDB(String idNumber , Object value) throws IOException {

		JSONObject jsonWeb = getGameRoundInfoWeb();

		JSONObject jsonDB = getGameRoundInfoDB(idNumber, value);

		boolean testPass = true;
		Iterator<?> keysWeb = jsonWeb.keys();
		String key = null;

		while( keysWeb.hasNext()) {
			key = (String)keysWeb.next();
			if (jsonDB.has(key)) {
				if(jsonWeb.get(key).toString().toLowerCase().equals(jsonDB.get(key).toString().toLowerCase().replaceAll("_", " "))
						|| ( key.contains("date") && jsonWeb.get(key).toString().contains(jsonDB.get(key).toString())))
				{
					System.out.println("WEB " + key + " = " + jsonWeb.get(key) + " vs DB " + key + " = " + jsonDB.get(key));
					reporter.info("WEB " + key + " = " + jsonWeb.get(key) + " vs DB " + key + " = " + jsonDB.get(key));
				}
				else if((Double)jsonWeb.get(key)== Double.valueOf(jsonDB.get(key).toString())/100) {
					System.out.println("WEB " + key + " = " + jsonWeb.get(key) + " vs DB " + key + " = " + jsonDB.get(key) + "/100 (" + Double.valueOf(jsonDB.get(key).toString())/100 + ")");
					reporter.info("WEB " + key + " = " + jsonWeb.get(key) + " vs DB " + key + " = " + jsonDB.get(key) + "/100 (" + Double.valueOf(jsonDB.get(key).toString())/100 + ")");
				}
				else {
					reporter.info("INCORRECT : WEB " + key + " = " + jsonWeb.get(key) + " vs DB " + key + " = " + jsonDB.get(key));
					System.out.println("INCORRECT : WEB " + key + " = " + jsonWeb.get(key) + " vs DB " + key + " = " + jsonDB.get(key));
					testPass = false;
				}
			}
			else {
				System.out.println("WARNING : Key on Web '" + key + "' is missing on DB.");
			}
		}

		if(testPass) {
			Assert.assertTrue("[EXPECTED] Data on Game round info page is correct.", true);
		}
		else {
			Assert.fail("[ACTUAL] Data on Game round info page is incorrect!");
		}
		new AttachFile().Screenshot();
		//method.takeScreenShot(driver, "roundInfo_page");
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
