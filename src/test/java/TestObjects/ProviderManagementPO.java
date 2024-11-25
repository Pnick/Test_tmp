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

public class ProviderManagementPO extends LoadableComponent<ProviderManagementPO>
{

	public static CommonVariables var = new CommonVariables();
	BaseMethodsO method = new BaseMethodsO(var);
	public String parentWindow;
	private List<WebElement> listWebElements = new ArrayList<WebElement>();

	public WebDriver driver;
	public WebDriverWait wait;
	ExtentTest reporter = ExtentCucumberAdapter.getCurrentStep();

	@FindBy(xpath = "//*[@id='provider-name']")
	public WebElement inputProviderName;

	@FindBy(xpath = "//*[contains(@id, 'provider-ac-dd')]")
	public WebElement dropDownProviderName;

	@FindBy(xpath = "//*[contains(@id, 'provider-id')]")
	public WebElement inputProviderID;

	@FindBy(xpath = "//*[@id='type']")
	public WebElement comboProviderType;

	@FindBy(xpath = "//*[@id='modal-type']")
	public WebElement comboProviderAddType;

	@FindBy(xpath = "//*[contains(@class, 'pagination')]//following-sibling::li[2]")
	public WebElement secondSearchPage;

	@FindBy(xpath = "//*[contains(@id, 'modal-name')]")
	public WebElement newProvider;
	
	@FindBy(xpath = "//*[contains(@id, 'modal-type')]")
	public WebElement newProviderType;
	
	@FindBy(xpath = "//*[contains(text(), 'Enabled')]")
	public WebElement newProviderEnable;
	
	public ProviderManagementPO() {
		driver = DriverFactory.getDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4)); // set implicit wait
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(4)); // set explicit wait
		PageFactory.initElements(driver, this);
	}

	//open Provider Management page via URL
	public ProviderManagementPO openProviderManagementPageFrom(String clientURL, String user)
	{
		method.setDBRecords("providers");

		try {
			inputProviderName.isDisplayed();
			listWebElements.add(inputProviderName);
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
		long num = method.getResultOnDB( "name", "part", "ProviderTest", "providers");

		if(inputText.contains("random")) {
			if(num>1) {
				inputText = "ProviderTest" + (rnd.nextInt((int) num - 1 + 1) + 1);
			}
			else {
				inputText = "ProviderTest" + 1;
			}
		}
		else if(inputText.contains("last")) {
			inputText = "ProviderTest" + num;
		}

		method.writeText(driver, setActiveProvidersElement(inputName), inputText);
	}

	public WebElement setActiveProvidersElement(String inputName) {

		WebElement element = null;
		if(inputName.contains("ProviderName")) {
			element = inputProviderName;
		}
		else if(inputName.contains("ProviderID")) {
			element = inputProviderID;
			element.click();
		}

		return element;
	}

	public void openProviderInfo(String idNumber, String name) {

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

			name = driver.findElement(By.xpath("//table//*[contains(@href, '/provider/" + idNumber + "')]")).getText();

			System.out.println("Get random row element with ID = " + idNumber + " and name = " + name);
		}
		
		listWebElements = driver.findElements(By.xpath("//table//*[contains(@href, '/provider/" + idNumber + "')]"));

		for(WebElement element : listWebElements) {

			if(element.getText().equals(name)) {
				element.click();
				break;
			}
		}
		
		var.jsonWebInfo = getProviderInfoWeb();
	}

	public JSONObject getProviderInfoWeb() {

		method.isloadComplete(driver);

		Map<String, Object> tableNameValues	= new HashMap<String, Object>();
		Pattern intNum = Pattern.compile("^([0-9]+)$"); // only int

		Matcher intNumMatcher;

		Map<String, String> providerMap = ImmutableMap.<String, String>builder()

				.put("Provider ID"		, "_id"				)				
				.put("Provider Name"	, "name"			) 
				.put("Type"				, "type"			) 	
				.put("Games Count"		, "games_counter"	) 
				.put("Enabled"			, "is_enabled"		) 		
				.build();
		
		List<WebElement> allRowsNames = driver.findElements(By.cssSelector("table.provider-info-table tr th"));
		List<WebElement> allRowsValues = driver.findElements(By.cssSelector("table.provider-info-table tr td"));

		if(allRowsValues.size() < allRowsNames.size()) {
			WebElement element = null;
			while(allRowsValues.size() < allRowsNames.size()) {
				allRowsValues.add(element);
			}
		}

		for(int i = 0 ; i < allRowsNames.size(); i++){

			if(!allRowsNames.get(i).getText().isEmpty()) {

				if(providerMap.containsKey(allRowsNames.get(i).getText())){

					if(allRowsValues.get(i)!=null) {

						intNumMatcher = intNum.matcher(allRowsValues.get(i).getText());

						if(intNumMatcher.find()) {
							tableNameValues.put(providerMap.get(allRowsNames.get(i).getText()), Integer.parseInt(intNumMatcher.group()));
						}
						else {
							tableNameValues.put(providerMap.get(allRowsNames.get(i).getText()), allRowsValues.get(i).getText());
						}
					}
					else {
						tableNameValues.put(providerMap.get(allRowsNames.get(i).getText()), "null");
					}
				}
			}
		}

		JSONObject jsonObject = new JSONObject(tableNameValues);
		System.out.printf(jsonObject.toString(4));

		return jsonObject;
	}

	public JSONObject getProviderInfoDB(String fieldName , Object fieldValue) {

		CommonVariables.db.setCollection("providers",var);
		Document providers = var.collection.find(Filters.eq(fieldName, fieldValue)).first();

		JSONObject jsonObjectDB = new JSONObject(providers.toJson());

		if(providers.get("is_enabled").equals(true)) {
			jsonObjectDB.put("is_enabled", "Yes");
		}
		else if(providers.get("is_enabled").equals(false)) {
			jsonObjectDB.put("is_enabled", "No");
		}

		CommonVariables.db.setCollection("games",var);
		long games_counter = -1;
		games_counter = var.collection.countDocuments(Filters.eq("provider_id", fieldValue));
		jsonObjectDB.put("games_counter", games_counter);

		System.out.println(jsonObjectDB.toString(4));

		return jsonObjectDB;
	}

	public void checkWebDataAgainstDB(String idNumber , Object value) throws IOException {

		JSONObject jsonWeb = getProviderInfoWeb();

		JSONObject jsonDB = getProviderInfoDB(idNumber, value);

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
		//method.takeScreenShot(driver, "providerInfo_page");
	}

	public void checkProviderInfoIsChanged() {

		method.isloadComplete(driver);
		
		JSONObject webInfo = getProviderInfoWeb();
		
		System.out.printf("Provider info before changes : " +  System.lineSeparator() + var.jsonWebInfo.toString(4));
		reporter.info("Provider info before changes : " + System.lineSeparator() + var.jsonWebInfo.toString(4));
		
		System.out.printf(System.lineSeparator() + "Provider info after changes : " +  System.lineSeparator() + webInfo.toString(4));
		reporter.info("Provider info after changes : " + System.lineSeparator() + webInfo.toString(4));
		
		if(!webInfo.toString().equals(var.jsonWebInfo.toString())) {
			Assert.assertTrue("[EXPECTED] Data on Provider info page is changed.", true);
		}
		else {
			Assert.fail("[ACTUAL] Data on Provider info page is not changed!");
		}
	}
	
	public void enterNewProviderData() {

		long num = method.getResultOnDB( "name", "part", "ProviderTest", "providers");

		method.writeText(driver, newProvider, "ProviderTest" + (num+1));

		if(!newProviderEnable.isSelected()) newProviderEnable.click();

		method.selectComboBoxOption( "random", newProviderType);
	}

	public void changeProviderData() {

		newProviderEnable.click();

		method.selectComboBoxOption( "random", newProviderType);
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
