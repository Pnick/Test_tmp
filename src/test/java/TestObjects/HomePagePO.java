package TestObjects;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

import UtilsHelper.AttachFile;
import UtilsHelper.CommonVariables;
import Driver.DriverFactory;

public class HomePagePO extends LoadableComponent<HomePagePO>
{

	CommonVariables var = new CommonVariables();
	BaseMethodsO method = new BaseMethodsO(var);
	public String parentWindow;

	public WebDriver driver;
	public WebDriverWait wait;
	ExtentTest reporter = ExtentCucumberAdapter.getCurrentStep();
	
	@FindBy(xpath = "//*[contains(text(), 'Overview')]")
	public WebElement buttonOverview;

	@FindBy(xpath = "//*[contains(text(), 'Game History')]")
	public List<WebElement> buttonGameHistory;

	@FindBy(xpath = "//*[contains(text(), 'Others')]")
	public WebElement buttonOthers;

	@FindBy(xpath = "//*[contains(text(), 'RTP')]")
	public WebElement buttonRTP;

	@FindBy(xpath = "//*[contains(text(), 'Management')]")
	public WebElement buttonManagement;

	@FindBy(xpath = "//*[contains(text(), 'Game Management')]")
	public WebElement buttonGameManagement;
	
	@FindBy(xpath = "//*[contains(text(), 'Operator Management')]")
	public WebElement buttonOperatorManagement;
	
	@FindBy(xpath = "//*[contains(text(), 'Provider Management')]")
	public WebElement buttonProviderManagement;

	@FindBy(xpath = "//*[contains(text(), 'Bet Configurations')]")
	public WebElement buttonBetConfigurations;
		
	@FindBy(xpath = "//*[contains(text(), 'Users')]")
	public WebElement buttonUsers;
	
	@FindBy(xpath = "//*[contains(text(), 'Logs')]")
	public WebElement buttonLogs;
		
	@FindBy(xpath = "//*[contains(@src, 'logo')]")
	public WebElement linkLogo;

	@FindBy(xpath = "//*[contains(text(), 'Welcome')]//following-sibling::li")
	public WebElement buttonUserMenu;
	
	@FindBy(xpath = "//*[contains(@href, '/logout')]")
	public WebElement buttonLogout;
	
	public HomePagePO() {
		driver = DriverFactory.getDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4)); // set implicit wait
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(4)); // set explicit wait
		PageFactory.initElements(driver, this);
	}

	//open Home page
	public HomePagePO openHomePage(String clientURL, String user)
	{
		method.setDBRecords("users");
		
		method.closeAllWindowsExceptParent(driver);
		
		parentWindow = method.openPage(driver, clientURL, user, buttonGameHistory);
		return this;
	}

	public void openPageViaLink(String URL, String user)
	{
		method.setDBRecords("users");
		
		method.closeAllWindowsExceptParent(driver);
		
		if(!URL.contains(CommonVariables.baseURL)) {
			
			URL = CommonVariables.baseURL + URL;
		}
		
		method.loginClient(driver, URL, user);
	}
	
	public void clickButton(String button) {

		if(button.contains("Overview")) {
			buttonOverview.click();
		}
		else if(button.contains("Game History")) {
			buttonGameHistory.get(0).click();
		}
		else if(button.contains("Others")) {
			buttonOthers.click();
		}
		else if(button.contains("RTP")) {
			buttonRTP.click();
		}
		else if(button.equals("Management")) {
			driver.navigate().refresh();
			method.isloadComplete(driver);
			buttonManagement.click();
		}
		else if(button.equals("Game Management")) {
			buttonGameManagement.click();
		}
		else if(button.equals("Operator Management")) {
			buttonOperatorManagement.click();
		}
		else if(button.equals("Provider Management")) {
			buttonProviderManagement.click();
		}
		else if(button.contains("Users")) {
			buttonUsers.click();
		}
		else if(button.contains("Bet Configurations")) {
			buttonBetConfigurations.click();
		}
		else if(button.contains("Logs")) {
			buttonLogs.click();
		}
	}

	public void checkPage() throws IOException {

		method.isloadComplete(driver);

		int count = 0 , containCount = 0;

		for(int i = 0 ; i< buttonGameHistory.size(); i++) {

			//System.out.println(buttonGameHistory.get(i).getAttribute("innerText"));

			if(buttonGameHistory.get(i).getAttribute("innerText").toLowerCase().equals("game history") && buttonGameHistory.get(i).isDisplayed())
			{
				count++;
			}
			else if(buttonGameHistory.get(i).getAttribute("innerText").toLowerCase().contains("game history") && buttonGameHistory.get(i).isDisplayed()) {
				containCount++;
			}
		}
		
		if (count > 0 || containCount > 0) {
			reporter.info("Home page is opened.");
			Assert.assertTrue("[EXPECTED] Home page is opened.", true);
		} else {
			reporter.info("Home page is NOT opened.");
			Assert.fail("[ACTUAL] Home page is NOT opened.");
		}
		new AttachFile().Screenshot();
		//method.takeScreenShot(driver, "my_account_page");
	}

	public void openUserMenu() {
		method.isloadComplete(driver);
		buttonUserMenu.click();
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
