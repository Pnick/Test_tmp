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

public class OperatorManagementPO extends LoadableComponent<OperatorManagementPO>
{

	public static CommonVariables var = new CommonVariables();
	BaseMethodsO method = new BaseMethodsO(var);
	public String parentWindow;
	private List<WebElement> listWebElements = new ArrayList<WebElement>();

	public WebDriver driver;
	public WebDriverWait wait;
	ExtentTest reporter = ExtentCucumberAdapter.getCurrentStep();

	@FindBy(xpath = "//*[@id='operator-name']")
	public WebElement inputOperatorName;

	@FindBy(xpath = "//*[contains(@id, 'operator-id')]")
	public WebElement inputOperatorID;

	@FindBy(xpath = "//*[contains(@id, 'integration-type')]")
	public WebElement comboOperatorType;

	@FindBy(xpath = "//*[contains(@id, 'integration-platform')]")
	public WebElement comboOperatorPlatform;

	@FindBy(xpath = "//*[contains(@class, 'pagination')]//following-sibling::li[2]")
	public WebElement secondSearchPage;

	@FindBy(xpath = "//*[contains(@id, 'modal-name')]")
	public WebElement newOperator;
	
	@FindBy(xpath = "//*[contains(@id, 'modal-short_name')]")
	public WebElement newOperatorShortName;

	@FindBy(xpath = "//*[contains(@id, 'modal-type')]")
	public WebElement newOperatorType;
	
	@FindBy(xpath = "//*[contains(@id, 'modal-platform')]")
	public WebElement newOperatorPlatform;
	
	@FindBy(xpath = "//*[contains(@id, 'modal-url')]")
	public WebElement newOperatorURL;
	
	@FindBy(xpath = "//*[contains(text(), 'Enabled')]")
	public WebElement newOperatorEnable;
	
	public OperatorManagementPO() {
		driver = DriverFactory.getDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4)); // set implicit wait
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(4)); // set explicit wait
		PageFactory.initElements(driver, this);
	}

	//open Operator Management page via URL
	public OperatorManagementPO openOperatorManagementPageFrom(String clientURL, String user)
	{
		method.setDBRecords("operators");
		method.setDBRecords("providers");

		try {
			inputOperatorName.isDisplayed();
			listWebElements.add(inputOperatorName);
		}
		catch(Exception e) {
			listWebElements = null;
		}

		parentWindow = method.openPage(driver, clientURL, user, listWebElements);
		method.buttonClearFilters.click();
		return this;
	}

	public void enterText(String inputName, String inputText)	{

		Random rnd = new Random();
		long num = method.getResultOnDB( "name", "part", "OperatorTest", "operators");

		if(inputText.contains("random")) {
			if(num>1) {
				inputText = "OperatorTest" + (rnd.nextInt((int) num - 1 + 1) + 1);
			}
			else {
				inputText = "OperatorTest" + 1;
			}
			if(inputName.contains("hort")) {
				inputText = "newoperatorshortname*" + rnd.nextInt(100);
			}
			if(inputName.equals("newOperator")) {
				inputText = "newOperatorTestname@" + rnd.nextInt(100);
			}
		}
		else if(inputText.contains("last")) {
			inputText = "OperatorTest" + num;
		}
		
		method.writeText(driver, setActiveOperatorsElement(inputName), inputText);
	}

	public WebElement setActiveOperatorsElement(String inputName) {

		WebElement element = null;
		if(inputName.contains("OperatorName")) {
			element = inputOperatorName;
		}
		else if(inputName.contains("OperatorID")) {
			element = inputOperatorID;
			element.click();
		}
		else if (inputName.equals("newOperator")) {
			element = newOperator;
		}
		else if (inputName.contains("newOperatorShortName")) {
			element = newOperatorShortName;
		}
		else if (inputName.contains("newOperatorURL")) {
			element = newOperatorURL;
		}

		return element;
	}

	public void openOperatorInfo(String idNumber, String name) {

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

			name = driver.findElement(By.xpath("//table//*[contains(@href, '/operator/" + idNumber + "')]")).getText();

			System.out.println("Get random row element with ID = " + idNumber + " and name = " + name);
		}
		
		listWebElements = driver.findElements(By.xpath("//table//*[contains(@href, '/operator/" + idNumber + "')]"));

		for(WebElement element : listWebElements) {

			if(element.getText().equals(name)) {
				element.click();
				break;
			}
		}
		
		var.jsonWebInfo = getOperatorInfoWeb();
	}

	public JSONObject getOperatorInfoWeb() {

		method.isloadComplete(driver);

		Map<String, Object> tableNameValues	= new HashMap<String, Object>();
		Pattern intNum = Pattern.compile("^([0-9]+)$"); // only int

		Matcher intNumMatcher;

		Map<String, String> operatorMap = ImmutableMap.<String, String>builder()

				.put("Operator ID"			, "_id"						)				
				.put("Operator Name"		, "name"					) 
				.put("Operator Short Name"	, "short_name"				)	
				.put("Integration Type"		, "integration_type"		) 	
				.put("Integration Platform"	, "integration_platform"	) 
				.put("Enabled"				, "is_enabled"				)
				.put("URL"					, "url"						) 	
				.build();

		List<WebElement> allRowsNames = driver.findElements(By.cssSelector("table.operator-info-table tr th"));
		List<WebElement> allRowsValues = driver.findElements(By.cssSelector("table.operator-info-table tr td"));

		if(allRowsValues.size() < allRowsNames.size()) {
			WebElement element = null;
			while(allRowsValues.size() < allRowsNames.size()) {
				allRowsValues.add(element);
			}
		}

		for(int i = 0 ; i < allRowsNames.size(); i++){

			if(!allRowsNames.get(i).getText().isEmpty()) {

				if(operatorMap.containsKey(allRowsNames.get(i).getText())){

					if(allRowsValues.get(i)!=null) {

						intNumMatcher = intNum.matcher(allRowsValues.get(i).getText());

						if(intNumMatcher.find()) {
							tableNameValues.put(operatorMap.get(allRowsNames.get(i).getText()), Integer.parseInt(intNumMatcher.group()));
						}
						else {
							tableNameValues.put(operatorMap.get(allRowsNames.get(i).getText()), allRowsValues.get(i).getText());
						}
					}
					else {
						tableNameValues.put(operatorMap.get(allRowsNames.get(i).getText()), "null");
					}
				}
			}
		}

		JSONObject jsonObject = new JSONObject(tableNameValues);
		System.out.printf(jsonObject.toString(4));

		return jsonObject;
	}

	public JSONObject getOperatorInfoDB(String fieldName , Object fieldValue) {

		CommonVariables.db.setCollection("operators",var);
		Document operators = var.collection.find(Filters.eq(fieldName, fieldValue)).first();

		JSONObject jsonObjectDB = new JSONObject(operators.toJson());

		if(operators.get("is_enabled").equals(true)) {
			jsonObjectDB.put("is_enabled", "Yes");
		}
		else if(operators.get("is_enabled").equals(false)) {
			jsonObjectDB.put("is_enabled", "No");
		}

		System.out.println(jsonObjectDB.toString(4));

		return jsonObjectDB;
	}

	public void checkWebDataAgainstDB(String idNumber , Object value) throws IOException {

		JSONObject jsonWeb = getOperatorInfoWeb();

		JSONObject jsonDB = getOperatorInfoDB(idNumber, value);

		boolean testPass = true;
		Iterator<?> keysWeb = jsonWeb.keys();
		String key = null;

		while( keysWeb.hasNext()) {
			key = (String)keysWeb.next();
			if (jsonDB.has(key)) {
				try {
					if(jsonWeb.get(key).toString().toLowerCase().equals(jsonDB.get(key).toString().toLowerCase().replaceAll("_", " ")))
					{
						System.out.println("WEB " + key + " = " + jsonWeb.get(key) + " vs DB " + key + " = " + jsonDB.get(key));
						reporter.info("WEB " + key + " = " + jsonWeb.get(key) + " vs DB " + key + " = " + jsonDB.get(key));
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
			Assert.assertTrue("[EXPECTED] Data on Operator info page is correct.", true);
		}
		else {
			Assert.fail("[ACTUAL] Data on Operator info page is incorrect!");
		}
		new AttachFile().Screenshot();
		//method.takeScreenShot(driver, "operatorInfo_page");
	}	

	public void checkOperatorInfoIsChanged() {

		method.isloadComplete(driver);
		
		JSONObject webInfo = getOperatorInfoWeb();
		
		System.out.printf("Operator info before changes : " +  System.lineSeparator() + var.jsonWebInfo.toString(4));
		reporter.info("Operator info before changes : " + System.lineSeparator() + var.jsonWebInfo.toString(4));
		
		System.out.printf(System.lineSeparator() + "Operator info after changes : " +  System.lineSeparator() + webInfo.toString(4));
		reporter.info("Operator info after changes : " + System.lineSeparator() + webInfo.toString(4));
		
		if(!webInfo.toString().equals(var.jsonWebInfo.toString())) {
			Assert.assertTrue("[EXPECTED] Data on Operator info page is changed.", true);
		}
		else {
			Assert.fail("[ACTUAL] Data on Operator info page is not changed!");
		}
	}
	
	public void enterNewOperatorData() {

		long num = method.getResultOnDB( "name", "part", "OperatorTest", "operators");

		method.writeText(driver, newOperator, "OperatorTest" + (num+1));
		method.writeText(driver, newOperatorShortName, "to" + (num+1));
		method.writeText(driver, newOperatorURL, "http://operatortest" + (num+1) + ".com");

		if(!newOperatorEnable.isSelected()) newOperatorEnable.click();

		method.selectComboBoxOption( "random", newOperatorType);
		method.selectComboBoxOption( "random", newOperatorPlatform);
	}

	public void changeOperatorData() {

		Random rnd = new Random();
		
		method.writeText(driver, newOperatorShortName, "chnagedshortname" + (char) (rnd.nextInt(26) + 'a') + rnd.nextInt(100));
		method.writeText(driver, newOperatorURL, "http://changedurl"  + (char) (rnd.nextInt(26) + 'a') + rnd.nextInt(100) + ".com");

		newOperatorEnable.click();

		method.selectComboBoxOption( "random", newOperatorType);
		method.selectComboBoxOption( "random", newOperatorPlatform);
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
