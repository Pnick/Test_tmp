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

public class UsersPO extends LoadableComponent<UsersPO>
{

	public static CommonVariables var = new CommonVariables();
	BaseMethodsO method = new BaseMethodsO(var);
	public String parentWindow;
	private List<WebElement> listWebElements = new ArrayList<WebElement>();

	public WebDriver driver;
	public WebDriverWait wait;
	ExtentTest reporter = ExtentCucumberAdapter.getCurrentStep();

	@FindBy(xpath = "//*[@id='user-name']")
	public WebElement inputUserName;

	@FindBy(xpath = "//*[contains(@id, 'user-ac-dd')]")
	public WebElement dropDownUserName;

	@FindBy(xpath = "//*[contains(@id, 'user-id')]")
	public WebElement inputUserID;

	@FindBy(xpath = "//*[contains(@id, 'isEnabled')]")
	public WebElement comboIsEnabled;

	@FindBy(xpath = "//*[@id='company']")
	public WebElement inputCompany;	

	@FindBy(xpath = "//*[contains(@class, 'pagination')]//following-sibling::li[2]")
	public WebElement secondSearchPage;

	@FindBy(xpath = "//*[contains(text(), 'Username')]//preceding-sibling::input")
	public WebElement newUserName;

	@FindBy(xpath = "//*[contains(text(), 'Password')]//preceding-sibling::input")
	public WebElement newUserPass;

	@FindBy(xpath = "//*[contains(text(), 'Repeat Password')]//preceding-sibling::input")
	public WebElement newUserPassRepeat;

	@FindBy(xpath = "//*[contains(text(), 'Company')]//preceding-sibling::input")
	public WebElement newUserCompany;

	@FindBy(xpath = "//*[contains(text(), 'Email')]//preceding-sibling::input")
	public WebElement newUserEmail;

	@FindBy(xpath = "//*[contains(text(), 'Name')]//preceding-sibling::input")
	public WebElement newName;

	@FindBy(xpath = "//*[contains(@id, 'modal-role')]")
	public WebElement newUserRole;

	@FindBy(xpath = "//*[contains(@id, 'operators')]")
	public WebElement newUserOperator;

	@FindBy(xpath = "//*[contains(text(), 'Enabled')]")
	public WebElement newUserEnable;

	public UsersPO() {
		driver = DriverFactory.getDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4)); // set implicit wait
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(4)); // set explicit wait
		PageFactory.initElements(driver, this);
	}

	//open Provider Management page via URL
	public UsersPO openUsersPageFrom(HomePagePO homePage, String clientURL, String user)
	{
		method.setDBRecords("users");

		try {
			inputUserName.isDisplayed();
			listWebElements.add(inputUserName);
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
		long num = method.getResultOnDB( "user_name", "part", "usertest", "users");

		if(inputText.contains("random")) {
			if(num>1) {
				inputText = "usertest" + (rnd.nextInt((int) num - 1 + 1) + 1);
			}
			else {
				inputText = "usertest" + 1;
			}
		}
		else if(inputText.contains("last")) {
			inputText = "usertest" + num;
		}

		method.writeText(driver, setActiveUserElement(inputName), inputText);
	}

	public WebElement setActiveUserElement(String inputName) {

		WebElement element = null;
		if(inputName.contains("UserName")) {
			element = inputUserName;
		}
		else if(inputName.contains("UserID")) {
			element = inputUserID;
		}
		else if(inputName.equals("Company")) {
			element = inputCompany;
			element.click();
		}
		else if(inputName.equals("newUserCompany")) {
			element = newUserCompany;
			element.click();
		}
		else if(inputName.equals("newUserEmail")) {
			element = newUserEmail;
		}
		else if(inputName.equals("newName")) {
			element = newName;
		}
		
		return element;
	}

	public void openUserInfo(String idNumber, String name) {
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

			name = driver.findElement(By.xpath("//table//*[contains(@href, '/user/" + idNumber + "')]")).getText();

			System.out.println("Get random row element with ID = " + idNumber + " and name = " + name);
		}

		listWebElements = driver.findElements(By.xpath("//table//*[contains(@href, '/user/" + idNumber + "')]"));

		for(WebElement element : listWebElements) {

			if(element.getText().equals(name)) {
				element.click();
				break;
			}
		}

		var.jsonWebInfo = getUserInfoWeb();
	}

	public JSONObject getUserInfoWeb() {

		method.isloadComplete(driver);
		method.needToSleep(1000);

		Map<String, Object> tableNameValues	= new HashMap<String, Object>();
		Pattern intNum = Pattern.compile("^([0-9]+)$"); // only int

		Matcher intNumMatcher;

		Map<String, String> gameMap = ImmutableMap.<String, String>builder()

				.put("User Id"			, "_id"					)				
				.put("Username"			, "user_name"			) 
				.put("User Role"		, "user_role"			)	
				.put("Company"			, "company"				) 	
				.put("Email"			, "email"				) 
				.put("Email Confirmed"	, "is_email_confirmed"	) 			
				.put("User Name"		, "name"				) 			
				.put("Enabled"			, "is_enabled"			) 		
				.build();

		List<WebElement> allRowsNames = driver.findElements(By.cssSelector("table.user-info-table tr th"));
		List<WebElement> allRowsValues = driver.findElements(By.cssSelector("table.user-info-table tr td"));

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

						intNumMatcher = intNum.matcher(allRowsValues.get(i).getText());

						if(intNumMatcher.find()) {
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
		System.out.printf(jsonObject.toString(4));

		return jsonObject;
	}

	public JSONObject getUserInfoDB(String fieldName , Object fieldValue) {

		CommonVariables.db.setCollection("users",var);
		Document users = var.collection.find(Filters.eq(fieldName, fieldValue)).first();

		JSONObject jsonObjectDB = new JSONObject(users.toJson());

		if(users.get("is_enabled").equals(true)) {
			jsonObjectDB.put("is_enabled", "Yes");
		}
		else if(users.get("is_enabled").equals(false)) {
			jsonObjectDB.put("is_enabled", "No");
		}

		if(users.get("is_email_confirmed").equals(true)) {
			jsonObjectDB.put("is_email_confirmed", "Yes");
		}
		else if(users.get("is_email_confirmed").equals(false)) {
			jsonObjectDB.put("is_email_confirmed", "No");
		}

		System.out.println(jsonObjectDB.toString(4));

		return jsonObjectDB;
	}

	public void checkWebDataAgainstDB(String idNumber , Object value) throws IOException {

		JSONObject jsonWeb = getUserInfoWeb();

		JSONObject jsonDB = getUserInfoDB(idNumber, value);

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
		//method.takeScreenShot(driver, "userInfo_page");
	}

	public void checkUserInfoIsChanged() {

		method.isloadComplete(driver);

		JSONObject webInfo = getUserInfoWeb();

		System.out.printf("User info before changes : " +  System.lineSeparator() + var.jsonWebInfo.toString(4));
		reporter.info("User info before changes : " + System.lineSeparator() + var.jsonWebInfo.toString(4));

		System.out.printf(System.lineSeparator() + "User info after changes : " +  System.lineSeparator() + webInfo.toString(4));
		reporter.info("User info after changes : " + System.lineSeparator() + webInfo.toString(4));

		if(!webInfo.toString().equals(var.jsonWebInfo.toString())) {
			Assert.assertTrue("[EXPECTED] Data on User info page is changed.", true);
		}
		else {
			Assert.fail("[ACTUAL] Data on User info page is not changed!");
		}
	}

	public void enterNewUserData() {

		long num = method.getResultOnDB( "user_name", "part", "usertest", "users");
		method.writeText(driver, newUserName, "usertest" + (num+1));
		method.writeText(driver, newUserPass, "@adminPass" + (num+1));
		method.writeText(driver, newUserPassRepeat, "@adminPass" + (num+1));
		method.writeText(driver, newUserCompany, "Company" + (num+1));
		method.writeText(driver, newUserEmail, "usertest" + (num+1) + "@mws.com");
		method.writeText(driver, newName, "First Second Name");

		if(!newUserEnable.isSelected()) newUserEnable.click();

		method.selectComboBoxOption( "random", newUserRole);

		String selectedOption = method.getParentFrom(newUserRole).findElement(By.xpath("//*[@class = 'selected']")).getAttribute("innerText");
		System.out.println("Selected option = " + selectedOption);
		if(!selectedOption.equals("Admin")) {
			method.selectComboBoxOption( "random", newUserOperator);
		}
	}

	public void changeUserData() {
		Random rnd = new Random();

		method.writeText(driver, newUserCompany, "Company" + rnd.nextInt(100));
		//method.writeText(driver, newUserEmail, "usertest" + (rnd.nextInt(10500 - 10000 + 1) + 10000) + "@mws.com");
		method.writeText(driver, newName, "First Second Name" + (char) (rnd.nextInt(26) + 'a'));

		if(!newUserEnable.isSelected()) newUserEnable.click();

		method.selectComboBoxOption( "random", newUserRole);

		String selectedOption = method.getParentFrom(newUserRole).findElement(By.xpath("//*[@class = 'selected']")).getAttribute("innerText");
		System.out.println("Selected option = " + selectedOption);
		if(!selectedOption.equals("Admin")) {
			method.selectComboBoxOption( "random", newUserOperator);
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
