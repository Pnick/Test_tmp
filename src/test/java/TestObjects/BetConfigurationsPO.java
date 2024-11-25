package TestObjects;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.bson.Document;
import org.bson.conversions.Bson;
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
import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;

import UtilsHelper.AttachFile;
import UtilsHelper.CommonVariables;
import UtilsHelper.PropertyUtils;
import Driver.DriverFactory;

public class BetConfigurationsPO extends LoadableComponent<BetConfigurationsPO> {

	public CommonVariables var = new CommonVariables();
	BaseMethodsO method = new BaseMethodsO(var);
	public String parentWindow;
	private List<WebElement> listWebElements = new ArrayList<WebElement>();
	Map<String, String> betData;

	public WebDriver driver;
	public WebDriverWait wait;
	ExtentTest reporter = ExtentCucumberAdapter.getCurrentStep();

	@FindBy(xpath = "//*[contains(@class, 'tab') and contains(text(), 'Default Bet')]")
	public WebElement tabDefaultBet;

	@FindBy(xpath = "//*[contains(@class, 'tab') and contains(text(), 'Operators Bet')]")
	public WebElement tabOperatorBet;

	@FindBy(xpath = "//*[contains(@class, 'tab') and contains(text(), 'Games Bet')]")
	public WebElement tabGamesBet;

	@FindBy(xpath = "//*[contains(@class, 'tab') and contains(text(), 'Slot')]")
	public WebElement tabSlot;

	@FindBy(xpath = "//*[contains(@class, 'tab') and contains(text(), 'Instant Win')]")
	public WebElement tabInstantWin;

	@FindBy(xpath = "//*[contains(@id, 'ac-games-name')]")
	public WebElement inputGameName;

	@FindBy(xpath = "//*[contains(@id, 'ac-games-id')]")
	public WebElement inputGameID;

	@FindBy(xpath = "//*[@id='ac-operators-name']")
	public WebElement inputOperatorName;

	@FindBy(xpath = "//*[contains(@id, 'ac-operators-id')]")
	public WebElement inputOperatorID;

	@FindBy(xpath = "//*[contains(@id, 'ac-games-ac-dd')]")
	public WebElement dropDownBetGameName;

	@FindBy(xpath = "//*[contains(@id, 'ac-operators-ac-dd')]")
	public WebElement dropDownBetOperatorName;

	@FindBy(xpath = "//*[contains(@id, 'add-default-bet-config-btn')]")
	public WebElement buttonAddNewCofig;

	@FindBy(xpath = "//*[contains(@id, 'add-bet-incr-btn')]")
	public WebElement buttonAddBetIncrement;

	@FindBy(xpath = "//input[contains(@id, 'bet-incr-input')]")
	public WebElement inputBetIncrement;

	@FindBy(xpath = "//*[contains(@id, 'max-payout-cap-input')]")
	public WebElement inputMaxPayoutCap;

	@FindBy(xpath = "//*[contains(@class, 'mpc-checkbox')]")
	public WebElement checkMaxPayoutCap;

	@FindBy(xpath = "//*[contains(@id, 'curr-select')]")
	public WebElement dropDownCurrency;

	@FindBy(xpath = "//*[contains(@class, 'clear-bi-btn')]")
	public List<WebElement> buttonClearBetIncrement;

	public BetConfigurationsPO() {
		driver = DriverFactory.getDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4)); // set implicit wait
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(4)); // set explicit wait
		PageFactory.initElements(driver, this);
	}

	// open Bet Configurations page
	public BetConfigurationsPO openHomePage(HomePagePO homePage, String clientURL, String user) {
		listWebElements.add(homePage.buttonManagement);
		parentWindow = method.openPage(driver, clientURL, user, listWebElements);
		method.buttonClearFilters.click();
		return this;
	}

	// open Bet Configurations page via URL
	public BetConfigurationsPO openBetConfigurationsPageFrom(String targetPage, String user) {

		String clientURL = null;
		if(targetPage.equals("SLOT")) {
			clientURL = CommonVariables.baseURL + PropertyUtils.getProperty("bet.configuration.slot.page.URL");
			listWebElements.add(tabSlot);
		}
		else if(targetPage.equals("INSTANT WIN")) {
			clientURL = CommonVariables.baseURL + PropertyUtils.getProperty("bet.configuration.instant.page.URL");
			listWebElements.add(tabInstantWin);
		}
		else if(targetPage.equals("Games Bet")) {
			clientURL = CommonVariables.baseURL + PropertyUtils.getProperty("games.bet.configuration.page.URL");
			listWebElements.add(tabGamesBet);
		}
		else if(targetPage.equals("Operators Bet")) {
			clientURL = CommonVariables.baseURL + PropertyUtils.getProperty("operators.bet.configuration.page.URL");
			listWebElements.add(tabOperatorBet);
		}

		if(!user.equals("loggedOutUser")) {
			parentWindow = method.openPage(driver, clientURL, user, listWebElements);
		}
		else {
			method.navigateToURL(driver, clientURL);
		}

		List<WebElement> allCells = null;
		int eleCounter = 0;
		method.isloadComplete(driver);

		if(!user.equals("loggedOutUser") && targetPage.equals("Bet Configurations")) {
			while(eleCounter<3) {
				//method.needToSleep(5000);
				allCells = driver.findElements(By.cssSelector("table.bet-config-table tr td"));
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

	public void clickTab(String tab) {

		if(tab.equals("Default Bet Configs")) {
			tabDefaultBet.click();
		}
		else if(tab.equals("Operators Bet Configs")) {
			tabOperatorBet.click();
		}
		else if(tab.equals("Games Bet Configs")) {
			tabGamesBet.click();
		}
	}

	public void enterText(String inputName, String inputText) {

		Random rnd = new Random();
		int num = 200;

		if(inputText.contains("random")) {
			if(num>1) {
				inputText = "" + (rnd.nextInt((int) num - 1 + 1) + 1);
			}
			else {
				inputText = "" + 1;
			}
		}

		method.writeText(driver, setActiveElement(inputName), inputText);
	}

	public WebElement setActiveElement(String elementName) {

		WebElement element = null;
		if (elementName.equals("Max Payout Cap")) {
			element = inputMaxPayoutCap;
			if(!element.isEnabled()) {
				clickCheckBox("On", "Max Payout Cap");
			}
		}
		else if (elementName.contains("New Bet Increment")) {
			element = driver.findElement(By.xpath("//label[contains(@for, 'bet-incr-input')]"));
			element.click();
			element = inputBetIncrement;
		}
		else if(elementName.contains("Game") && elementName.contains("Name")){
			element = inputGameName;
		}
		else if(elementName.contains("Operator") && elementName.contains("Name")){
			element = inputOperatorName;
		}
		else if(elementName.equals("AddNewCofig")) {
			element = buttonAddNewCofig;
			
			try {
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true)", element);
			}catch(Exception e) {}
		}
		else if(elementName.equals("AddBetIncrement")) {
			element = buttonAddBetIncrement;
		}
		else if(elementName.contains("Edit")) {
			String currency = elementName.substring(elementName.length()-3, elementName.length());
			element = driver.findElement(By.xpath("//*[@id='config-incr-"+currency+"']//following-sibling::td"));
		}
		else {
			element = driver.findElement(By.xpath("//*[contains(text(), '" + elementName + "')]"));
		}
		
		return element;
	}

	public void clickElement(String elementName) {

		setActiveElement(elementName).click();
	}

	public void clickCheckBox(String onOff, String checkbox) {

		method.isloadComplete(driver);

		if(checkbox.equals("Max Payout Cap")) {
			if(checkMaxPayoutCap.getAttribute("class").contains("dotted")) {
				if(onOff.equals("On")) {
					checkMaxPayoutCap.click();
				}
			}
			else {
				if(onOff.equals("Off")) {
					checkMaxPayoutCap.click();
				}
			}
		}
	}

	public void removeConfiguration(String currency, String gameType) {

		CommonVariables.db.setCollection("bet_configurations",var);

		Document doc = null;
		Bson filter = Filters.eq("_id", gameType.replaceAll(" ", "_"));

		try {
			var.cursor = var.collection.find(filter).iterator();

			while (var.cursor.hasNext()) {
				doc = var.cursor.next();

				Document doc2 = (Document) ((Document) doc.get("per_currency")).get(currency);

				if(((Document) doc.get("per_currency")).containsKey(currency)) {

					String values = doc2.toJson();
					System.out.println("Remove currency " + currency + " values : " + values);

					Bson filter2 = Filters.eq("per_currency." + currency ,  values);

					var.collection.updateOne(filter, new BasicDBObject("$unset", filter2));

					var.cursor = var.collection.find(filter).iterator();
					doc = var.cursor.next();
					break;
				}

			}
		}
		catch(Exception e) {
			System.out.println("WARNING : Cannot connect to " + var.collection.getNamespace() + " OR cursor is null.");
		}
		finally {
			if(var.cursor != null) {
				var.cursor.close();
			}
		}

		System.out.println("Currency " + currency + " missing from document : " + doc.toJson());
		method.needToSleep(1000);
		driver.navigate().refresh();
	}

	public void removeBet(String bet, String currency) {

		Random rnd = new Random();
		method.isloadComplete(driver);
		
		if(bet.equals("random")) {
			if(buttonClearBetIncrement.size()>0) {
				buttonClearBetIncrement.get(rnd.nextInt((buttonClearBetIncrement.size()-1) - 0 + 1) + 0).click();
			}
		}
		if(bet.equals("all")) {
			while(buttonClearBetIncrement.size()>0) {
				buttonClearBetIncrement.get(rnd.nextInt((buttonClearBetIncrement.size()-1) - 0 + 1) + 0).click();
			}
		}
	}

	public void initConfiguration(String currency, String gameType) {

		CommonVariables.db.setCollection("bet_configurations",var);

		Bson filter = Filters.eq("_id",  gameType.replaceAll(" ", "_"));

		BasicDBObject allBets = new BasicDBObject("per_currency." + currency + ".bet_values", new int[]{69,96,2700,1,677099});
		var.collection.updateOne(filter, new BasicDBObject("$set", allBets));

		BasicDBObject maxPayoutCap = new BasicDBObject("per_currency." + currency + ".max_payout_cap", 33*100);
		var.collection.updateOne(filter, new BasicDBObject("$set", maxPayoutCap));

		method.needToSleep(1000);
		driver.navigate().refresh();
		//remove element from array
		//		var.collection.updateOne(filter,
		//				new BasicDBObject("$pull", 
		//				new BasicDBObject("per_currency.CAD.bet_values",
		//				new BasicDBObject("$in", new int[]{1}))));
	}

	public void getConfiguration(String currency) {

		method.isloadComplete(driver);
		betData = new HashMap<String, String>();
		betData.put("minBet", driver.findElement(By.xpath("//*[@id='config-min-"+currency+"']")).getText());
		betData.put("maxBet", driver.findElement(By.xpath("//*[@id='config-max-"+currency+"']")).getText());
		betData.put("payoutCap", driver.findElement(By.xpath("//*[@id='config-cap-"+currency+"']")).getText());
		betData.put("allBets", driver.findElement(By.xpath("//*[@id='config-incr-"+currency+"']")).getText());

	}

	public void checkBetIsChanged(String currency, boolean changed) throws IOException {

		boolean existDiff = false;
		Map<String, String> betDataBefore = new HashMap<String, String>();
		betDataBefore = betData;

		getConfiguration(currency);

		for(String key : betData.keySet()) {
			if(!betDataBefore.get(key).equals(betData.get(key))) {
				existDiff=true;
			}
		}

		if(changed) {
			if(existDiff) {
				reporter.info("There are Bet configuration changes.");
				System.out.println("There are Bet configuration changes.");
				Assert.assertTrue("[EXPECTED] There are Bet configuration changes.", true);
			}
			else {
				reporter.info("There are NOT Bet configuration changes!");
				System.out.println("There are NOT Bet configuration changes!");
				Assert.fail("[ACTUAL] There are NOT Bet configuration changes.!");
			}
		}
		else {
			if(!existDiff) {
				reporter.info("There are NOT Bet configuration changes.");
				System.out.println("There are NOT Bet configuration changes.");
				Assert.assertTrue("[EXPECTED] There are NOT Bet configuration changes.", true);
			}
			else {
				reporter.info("There are Bet configuration changes!");
				System.out.println("There are Bet configuration changes!");
				Assert.fail("[ACTUAL] There are Bet configuration changes.!");
			}
		}
		new AttachFile().Screenshot();
		//method.takeScreenShot(driver, "betConfig");
	}

	public void checkBetIsAdded(String currency) throws IOException {

		boolean existConfig = true;

		getConfiguration(currency);

		for(String key : betData.keySet()) {
			if(betData.get(key).isEmpty()) {
				existConfig=false;
			}
		}

		if(!existOnFoot(currency) && existConfig) {
			reporter.info("Bet configuration for currency " + currency + " is added.");
			Assert.assertTrue("[EXPECTED] Bet configuration for currency " + currency + " is added.", true);
		}
		else {
			reporter.info("Bet configuration for currency " + currency + " is NOT added.!");
			Assert.fail("[ACTUAL] Bet configuration for currency " + currency + " is NOT added.!");
		}
		new AttachFile().Screenshot();
		//method.takeScreenShot(driver, "betConfig");
	}

	boolean existOnFoot(String currency) {

		boolean exist = false;
		List<WebElement> elements =  driver.findElements(By.xpath("//tfoot/tr/td/span[contains(@class, 'red-text')]"));

		if(elements.size()>0) {
			for(WebElement element : elements) {
				System.out.println(element.getAttribute("innerText"));
				if(element.getAttribute("innerText").contains(currency)) {
					exist =  true;
				}
			}
		}
		return exist;
	}

	public void checkBetIsNotAdded(String currency) throws IOException {

		boolean existConfig = false;

		if(!method.checkButtonState("Save") || method.checkForExistingError()) {

			reporter.info("Bet configuration for currency " + currency + " is NOT added.Error message or inactive button 'Save' is displayed.");
			Assert.assertTrue("[EXPECTED] Bet configuration for currency " + currency + " is NOT added.", true);
		}
		else {

			method.navigateToURL(driver, CommonVariables.baseURL + PropertyUtils.getProperty("bet.configuration.page.URL"));

			getConfiguration(currency);

			for(String key : betData.keySet()) {
				if(!betData.get(key).isEmpty()) {
					existConfig=true;
				}
			}

			if(existOnFoot(currency) && !existConfig) {
				reporter.info("Bet configuration for currency " + currency + " is NOT added.");
				Assert.assertTrue("[EXPECTED] Bet configuration for currency " + currency + " is NOT added.", true);
			}
			else {
				reporter.info("Bet configuration for currency " + currency + " is added.!");
				Assert.fail("[ACTUAL] Bet configuration for currency " + currency + " is added.!");
			}
		}
		//method.takeScreenShot(driver, "betConfig");
		new AttachFile().Screenshot();
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
