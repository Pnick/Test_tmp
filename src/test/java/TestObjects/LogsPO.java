package TestObjects;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

import UtilsHelper.CommonVariables;
import Driver.DriverFactory;

public class LogsPO extends LoadableComponent<LogsPO>
{
	public CommonVariables var = new CommonVariables();
	HomePagePO homePage = new HomePagePO();
	BaseMethodsO method = new BaseMethodsO(var);
	public String parentWindow;

	public WebDriver driver;
	public WebDriverWait wait;
	ExtentTest reporter = ExtentCucumberAdapter.getCurrentStep();
	public List<WebElement> listWebElements = new ArrayList<WebElement>();

	@FindBy(xpath = "//*[@id='user-name']")
	public WebElement inputUserName;

	@FindBy(xpath = "//*[contains(@id, 'user-id')]")
	public WebElement inputUserID;

	@FindBy(xpath = "//*[contains(@id, 'collection-select')]")
	public WebElement comboCollection;
		
	@FindBy(xpath = "//*[contains(@id, 'elementId')]")
	public WebElement inputElementID;
	
	@FindBy(xpath = "//*[contains(@id, 'from-date')]")
	public WebElement dateFrom;

	@FindBy(xpath = "//*[contains(@id, 'to-date')]")
	public WebElement dateTo;

	public LogsPO() {
		driver = DriverFactory.getDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4)); // set implicit wait
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(4)); // set explicit wait
		PageFactory.initElements(driver, this);
	}

	//open Logs page
	public LogsPO openLogsPageFrom(String clientURL, String user)
	{
		if(!user.equals("loggedOutUser")) {
			parentWindow = method.openPage(driver, clientURL, user, homePage.buttonGameHistory);
		}
		else {
			method.navigateToURL(driver, clientURL);
		}
		
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

			List<LocalDateTime> outputValues = method.getValueFromDB("logs", "change_date", "change_user_id", "1000000001");
			
			LocalDateTime date = outputValues.get(0);
			System.out.println("Get from logs collection 'change_date' : " + date);
			
			inputText = method.clarifyDate(inputText, date);
		}
		method.writeText(driver, setActiveLogsElement(inputName), inputText);
	}

	public WebElement setActiveLogsElement(String inputName) {

		WebElement element = null;
		if(inputName.contains("UserName")) {
			element = inputUserName;
		}
		else if(inputName.contains("UserID")) {
			element = inputUserID;
		}
		else if(inputName.equals("ElementID")) {
			element = inputElementID;
		}
		else if(inputName.contains("dateFrom")) {
			element = dateFrom;
		}
		else if(inputName.contains("dateTo")) {
			element = dateTo;
		}
		
		return element;
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
