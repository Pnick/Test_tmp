package TestObjects;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.Document;
import org.json.JSONObject;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

public class GameManagementPO extends LoadableComponent<GameManagementPO> {

	public static CommonVariables var = new CommonVariables();
	BaseMethodsO method = new BaseMethodsO(var);
	public String parentWindow;
	private List<WebElement> listWebElements = new ArrayList<WebElement>();

	public WebDriver driver;
	public WebDriverWait wait;
	ExtentTest reporter = ExtentCucumberAdapter.getCurrentStep();

	@FindBy(xpath = "//*[contains(@id, 'game-name')]")
	public WebElement inputGameName;

	@FindBy(xpath = "//*[contains(@id, 'game-id')]")
	public WebElement inputGameID;

	@FindBy(xpath = "//*[contains(@id, 'game-providers')]")
	public WebElement comboGameProvider;

	@FindBy(xpath = "//*[contains(@id, 'game-types')]")
	public WebElement comboGameType;

	@FindBy(xpath = "//*[contains(@id, 'game-categories')]")
	public WebElement comboGameCategories;

	@FindBy(xpath = "//*[contains(@class, 'pagination')]//following-sibling::li[2]")
	public WebElement secondSearchPage;

	@FindBy(xpath = "//*[contains(@id, 'modal-name')]")
	public WebElement newGameName;

	@FindBy(xpath = "//*[contains(@id, 'modal-short_name')]")
	public WebElement newGameShortName;

	@FindBy(xpath = "//*[contains(@id, 'modal-ui-url')]")
	public WebElement newGameURL;

	@FindBy(xpath = "//*[contains(@id, 'modal-provider')]")
	public WebElement newGameProvider;

	@FindBy(xpath = "//*[contains(@id, 'modal-type')]")
	public WebElement newGameType;

	@FindBy(xpath = "//select[contains(@id, 'modal-category')]")
	public WebElement newGameCategory;

	@FindBy(xpath = "//*[contains(@id, 'modal-rtp')]")
	public WebElement newGameRTP;

	@FindBy(xpath = "//*[contains(text(), 'Enabled')]")
	public WebElement newGameEnable;

	public GameManagementPO() {
		driver = DriverFactory.getDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4)); // set implicit wait
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(4)); // set explicit wait
		PageFactory.initElements(driver, this);
	}

	// open Game Management page
	public GameManagementPO openHomePage(HomePagePO homePage, String clientURL, String user) {
		listWebElements.add(homePage.buttonManagement);
		parentWindow = method.openPage(driver, clientURL, user, listWebElements);
		method.buttonClearFilters.click();
		return this;
	}

	// open Game Management page via URL
	public GameManagementPO openGameManagementPageFrom(String clientURL, String user) {

		method.setDBRecords("games");
		method.setDBRecords("providers");

		listWebElements.add(inputGameName);
		parentWindow = method.openPage(driver, clientURL, user, listWebElements);
		method.buttonClearFilters.click();
		return this;
	}

	public void enterText(String inputName, String inputText) {

		Random rnd = new Random();
		long num = method.getResultOnDB( "name", "part", "GameTest", "games");

		if(inputText.contains("random")) {
			if(num>1) {
				inputText = "GameTest" + (rnd.nextInt((int) num - 1 + 1) + 1);
			}
			else {
				inputText = "GameTest" + 1;
			}
			if(inputName.contains("hort")) {
				inputText = "newshortname#" + rnd.nextInt(100);
			}
			if(inputName.equals("newGameName")) {
				inputText = "newGameTestname^" + rnd.nextInt(100);
			}
		}
		else if(inputText.contains("last")) {
			inputText = "GameTest" + num;
		}

		method.writeText(driver, setActiveGamesElement(inputName), inputText);
	}

	public WebElement setActiveGamesElement(String inputName) {

		WebElement element = null;
		if (inputName.equals("GameName")) {
			element = inputGameName;
		}
		else if (inputName.contains("GameID")) {
			element = inputGameID;
			element.click();
		}
		else if (inputName.equals("newGameName")) {
			element = newGameName;
		}
		else if (inputName.contains("newGameShortName")) {
			element = newGameShortName;
		}
		else if (inputName.contains("newGameURL")) {
			element = newGameURL;
		}
		else if (inputName.contains("newGameRTP")) {
			element = newGameRTP;
		}

		return element;
	}

	public void clickCheckBox(String checkbox, String onOff) {

		method.isloadComplete(driver);

		List<WebElement> operatorCheckBox = driver.findElements(By.xpath("//*[contains(text(), '" + checkbox  + "')]"));
		String elementArg = operatorCheckBox.get(0).getAttribute("class");
		//parent class of operatorCheckBox - operator-availability-check
		WebElement parentElement = operatorCheckBox.get(0).findElement(By.xpath("../../.."));
		String parentArg = parentElement.getAttribute("class");

		if(operatorCheckBox.size()>0) {
			if(elementArg.contains("bold") && parentArg.contains("active")) {
				if(onOff.equals("Off")) {
					//operatorCheckBox.get(0).click();
					((JavascriptExecutor) driver).executeScript("arguments[0].click();", operatorCheckBox.get(0));
				}
			}
			else {
				if(onOff.equals("On")) {
					//operatorCheckBox.get(0).click();
					((JavascriptExecutor) driver).executeScript("arguments[0].click();", operatorCheckBox.get(0));
				}
			}
			List<WebElement> confirmCheckBox = driver.findElements(By.xpath("//button[contains(text(), 'Yes')]"));
			if(confirmCheckBox.size()>0) {
				confirmCheckBox.get(0).click();
			}
		}
	}

	public void openGameInfo(String idNumber, String name) {

		Random rnd = new Random();
		Pattern num = Pattern.compile("[0-9]+$");
		Matcher mNum;
		List<WebElement> listWebElements = null;

		if(idNumber.equals("random")) {
			listWebElements = driver.findElements(By.xpath("(//table[1]/tbody/tr)"));
			int randomElementIndex = rnd.nextInt(listWebElements.size());
			idNumber = listWebElements.get(randomElementIndex).getAttribute("id");

			mNum = num.matcher(idNumber);

			if(mNum.find()) {
				idNumber = mNum.group();
			}

			name = driver.findElement(By.xpath("//table//*[contains(@href, '/game/" + idNumber + "')]")).getText();

			System.out.println("Get random row element with ID = " + idNumber + " and name = " + name);
		}

		listWebElements = driver.findElements(By.xpath("//table//*[contains(@href, '/game/" + idNumber + "')]"));

		for(WebElement element : listWebElements) {

			if(element.getText().equals(name)) {
				element.click();
				break;
			}
		}

		var.jsonWebInfo = getGameInfoWeb();
	}

	public JSONObject getGameInfoWeb() {

		method.isloadComplete(driver);

		Map<String, Object> tableNameValues	= new HashMap<String, Object>();
		Pattern floatNum = Pattern.compile("^([0-9]+[,.][0-9]+)(.*)$"); // only float
		Pattern intNum = Pattern.compile("^([0-9]+)$"); // only int

		Matcher floatNumMatcher, intNumMatcher;

		Map<String, String> gameMap = ImmutableMap.<String, String>builder()

				.put("Game ID"		, "_id"				)				
				.put("Game Name"	, "name"			) 
				.put("Short Name"	, "short_name"		)	
				.put("Type"			, "type"			) 	
				.put("Provider"		, "provider"		) 
				.put("RTP"			, "rtp"				) 			
				.put("Category"		, "categories"		) 			
				.put("Enabled"		, "is_enabled"		)
				.put("UI URL"		, "game_ui_url"		) 
				.build();

		List<WebElement> allRowsNames = driver.findElements(By.cssSelector("table.game-info-table tr th"));
		List<WebElement> allRowsValues = driver.findElements(By.cssSelector("table.game-info-table tr td"));

		if(allRowsValues.size() < allRowsNames.size()) {
			WebElement element = null;
			while(allRowsValues.size() < allRowsNames.size()) {
				allRowsValues.add(element);
			}
		}

		for(int i = 0 ; i < allRowsNames.size(); i++){

			if(!allRowsNames.get(i).getText().isEmpty()) {

				if(gameMap.containsKey(allRowsNames.get(i).getText())){

					if(allRowsValues.get(i)!=null) {

						floatNumMatcher = floatNum.matcher(allRowsValues.get(i).getText());
						intNumMatcher = intNum.matcher(allRowsValues.get(i).getText());

						if(floatNumMatcher.find()) {
							tableNameValues.put(gameMap.get(allRowsNames.get(i).getText()), Double.parseDouble(floatNumMatcher.group(1)));
						}
						else if(intNumMatcher.find()) {
							tableNameValues.put(gameMap.get(allRowsNames.get(i).getText()), Integer.parseInt(intNumMatcher.group()));
						}
						else {
							tableNameValues.put(gameMap.get(allRowsNames.get(i).getText()), allRowsValues.get(i).getText());
						}
					}
					else {
						tableNameValues.put(gameMap.get(allRowsNames.get(i).getText()), "null");
					}
				}
			}
		}

		JSONObject jsonObject = new JSONObject(tableNameValues);
		//System.out.printf(jsonObject.toString(4));

		return jsonObject;
	}

	public JSONObject getGameInfoDB(String fieldName , Object fieldValue) {

		CommonVariables.db.setCollection("games",var);
		Document games = var.collection.find(Filters.eq(fieldName, fieldValue)).first();

		JSONObject jsonObjectDB = new JSONObject(games.toJson());

		if(games.get("is_enabled").equals(true)) {
			jsonObjectDB.put("is_enabled", "Yes");
		}
		else if(games.get("is_enabled").equals(false)) {
			jsonObjectDB.put("is_enabled", "No");
		}

		String categories = games.get("categories").toString();
		if(categories.contains("[")) {
			categories = categories.replaceAll("\\[|\\]", "");
			jsonObjectDB.put("categories", categories);
		}

		CommonVariables.db.setCollection("providers",var);
		Document provider = var.collection.find(Filters.eq(fieldName, fieldValue)).first();

		jsonObjectDB.remove("provider_id");
		jsonObjectDB.put("provider", provider.get("name"));

		System.out.println(jsonObjectDB.toString(4));

		return jsonObjectDB;
	}

	public void checkWebDataAgainstDB(String idNumber , Object value) throws IOException {

		JSONObject jsonWeb = getGameInfoWeb();

		JSONObject jsonDB = getGameInfoDB(idNumber, value);

		boolean testPass = true;
		Iterator<?> keysWeb = jsonWeb.keys();
		String key = null;

		while( keysWeb.hasNext()) {
			key = (String)keysWeb.next();
			if (jsonDB.has(key)) {
				try {
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
				}catch(Exception e) {
					System.out.println("WARNING : There is Key type format, that cannot be cast!");
					reporter.info("WARNING : There is Key type format, that cannot be cast!");
				};
			}
			else {
				System.out.println("WARNING : Key on Web '" + key + "' is missing on DB.");
			}
		}

		if(testPass) {
			Assert.assertTrue("[EXPECTED] Data on Game info page is correct.", true);
		}
		else {
			Assert.fail("[ACTUAL] Data on Game info page is incorrect!");
		}
		new AttachFile().Screenshot();
		//method.takeScreenShot(driver, "gameInfo_page");
	}

	public void checkGameInfoIsChanged() throws IOException {

		method.isloadComplete(driver);

		JSONObject webInfo = getGameInfoWeb();

		System.out.printf("Game info before changes : " +  System.lineSeparator() + var.jsonWebInfo.toString(4));
		reporter.info("Game info before changes : " + System.lineSeparator() + var.jsonWebInfo.toString(4));

		System.out.printf(System.lineSeparator() + "Game info after changes : " +  System.lineSeparator() + webInfo.toString(4));
		reporter.info("Game info after changes : " + System.lineSeparator() + webInfo.toString(4));

		if(!webInfo.toString().equals(var.jsonWebInfo.toString())) {
			Assert.assertTrue("[EXPECTED] Data on Game info page is changed.", true);
		}
		else {
			Assert.fail("[ACTUAL] Data on Game info page is not changed!");
		}
		new AttachFile().Screenshot();
		//method.takeScreenShot(driver, "gameInfo_page");
	}

	public void enterNewGameData() {

		long num = method.getResultOnDB( "name", "part", "GameTest", "games");
		Random rnd = new Random();
		method.writeText(driver, newGameName, "GameTest" + (num+1));
		method.writeText(driver, newGameShortName, "tg" + (num+1));
		method.writeText(driver, newGameURL, "http://test" + (num+1) + ".com");
		method.writeText(driver, newGameRTP, ""  + rnd.nextInt(100));
		method.selectComboBoxOption( "exceptEmpty", newGameProvider);
		if(!newGameEnable.isSelected()) newGameEnable.click();
		method.selectComboBoxOption( "random", newGameCategory);
		method.selectComboBoxOption( "exceptEmpty", newGameType);
	}

	public void changeGameData() {

		Random rnd = new Random();
		method.writeText(driver, newGameShortName, "changedshortname" + (char) (rnd.nextInt(26) + 'a') + rnd.nextInt(200));
		method.selectComboBoxOption( "exceptEmpty", newGameProvider);
		method.writeText(driver, newGameURL, "http://changedurl"  + (char) (rnd.nextInt(26) + 'a') + rnd.nextInt(200) + ".com");
		method.selectComboBoxOption( "exceptEmpty", newGameType);	
		method.writeText(driver, newGameRTP, ""  + rnd.nextInt(100));
		newGameEnable.click();
		method.needToSleep(1000);
		method.selectComboBoxOption( "random", newGameCategory);
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
