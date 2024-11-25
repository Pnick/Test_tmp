package TestObjects;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Assume;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

import Driver.DriverFactory;
import Driver.SharedDriver;
import UtilsHelper.CommonVariables;
import UtilsHelper.PropertyUtils;
import io.cucumber.datatable.DataTable;

public class LoginPO extends LoadableComponent<LoginPO>{

	public WebDriver driver;
	public WebDriverWait wait;
	ExtentTest reporter = ExtentCucumberAdapter.getCurrentStep();
	public CommonVariables var = new CommonVariables();
	BaseMethodsO method = new BaseMethodsO(var);

	@FindBy(xpath = "//*[contains(text(), 'Log In')]")
	public WebElement buttonLogin;

	@FindBy(xpath = "//*[contains(text(), 'Forgot Password')]")
	public WebElement linkForgotPassword;

	@FindBy(xpath = "//*[contains(@id, 'username')]")
	public WebElement inputUsername;

	@FindBy(xpath = "//*[contains(@id, 'password')]")
	public WebElement inputPassword;

	public LoginPO() {
		driver = DriverFactory.getDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4)); // set implicit wait
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(4)); // set explicit wait
		PageFactory.initElements(driver, this);
	}

	public void openLoginPage(String loginURL) {

		driver.manage().deleteAllCookies();
		driver.navigate().refresh();

		method.navigateToURL(driver, loginURL + "login");
	}

	public void enterCredentials(String username, String password) {

		method.isloadComplete(driver);
		method.needToSleep(1000);

		if(username.contains("valid.username") && !username.contains("invalid")) {
			method.writeText(driver, inputUsername, PropertyUtils.getProperty(CommonVariables.environment + "." + username));
		}
		else if(username.equals("invalid.username")) {
			method.writeText(driver, inputUsername, PropertyUtils.getProperty("invalid.username"));
		}
		else if(username.equals("inactive.username")) {
			method.writeText(driver, inputUsername, PropertyUtils.getProperty("inactive.username"));
		}
		else {
			method.writeText(driver, inputUsername, username);
		}

		if(password.contains("valid.password") && !password.contains("invalid")) {
			method.writeText(driver, inputPassword, PropertyUtils.getProperty(CommonVariables.environment + "." + password));
		}
		else if(password.equals("invalid.password")) {
			method.writeText(driver, inputPassword, PropertyUtils.getProperty("invalid.password"));
		}
		else {
			method.writeText(driver, inputPassword, password);
		}
	}

	public void pressKeyboardKey(int counter, String key){

		method.pressKey(counter, key);
	}

	public void LoggedOut() {

		driver.manage().deleteAllCookies();
		driver.navigate().refresh();
	}

	public void checkNotLoggedIn() throws IOException {

		method.checkExistingOnPage("Forgot Password?", "Welcome");
	}

	public void checkLoggedInMessage(String errorType) throws IOException {

		String message = null ;
		if(errorType.equals("incorrect")) {
			message = "Username or Password is Invalid";
		}
		else if(errorType.equals("blank")) {
			message = "Username or Password is Invalid";
		}
		else {
			message = "Missing errorType for verificatiion";
		}

		method.checkExistingOnPage(message, "Welcome");
	}

	public void checkPasswordIfCopy() {

		if(CommonVariables.headless) {
			Assume.assumeTrue("WARNING : In headless mode, cannot be copy." , false);
		}
		else {
			StringSelection stringSelection = new StringSelection("");
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);

			inputPassword.sendKeys(Keys.CONTROL, "a");
			inputPassword.sendKeys(Keys.CONTROL + "c");

			String textClipboard = null;
			try {
				textClipboard = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
			} catch (HeadlessException e) {
				e.printStackTrace();
				Assume.assumeTrue("WARNING : In headless mode, cannot be copy." , false);
			} catch (UnsupportedFlavorException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if(textClipboard==null || textClipboard.isEmpty()) {
				System.out.println("The content of password field cannot be copy.");
				reporter.info("The content of password field cannot be copy.");
				Assert.assertTrue("[EXPECTED] The content of password field cannot be copy.", true);
			} else {
				System.out.println("The content of password field is copied. Value = " + textClipboard);
				reporter.info("The content of password field is copied. Value = " + textClipboard);
				Assert.fail("[ACTUAL] The content of password field is copied. Value = " + textClipboard);
			}
		}
	}

	public void checkForgotPasswordPage() throws IOException {

		method.checkExistingOnPage("Email", "Welcome");
	}

	public void checkLoginControls(DataTable testedFields){

		WebElement element;
		method.isloadComplete(driver);
		List<String> loginControls = testedFields.asList(String.class);

		for(int i=0; i<loginControls.size(); i++) {

			element = driver.findElement(By.xpath("//*[contains(text(), '" + loginControls.get(i) + "')]"));

			if(element.isDisplayed() &&
					element.getSize().height>15 &&
					element.getSize().width>57)
			{
				System.out.println("The element " + element.getText() + " is displayed.");
				reporter.info("The element " + element.getText() + " is displayed.");
				Assert.assertTrue("[EXPECTED] The element " + element.getText() + " is displayed.", true);
			}
			else {
				System.out.println("The element " + element.getText() + " is NOT displayed.");
				reporter.info("The element " + element.getText() + " is NOT displayed.");
				Assert.fail("[ACTUAL] The element " + element.getText() + " is NOT displayed.");
			}
		}
	}

	public void checkCursor() {

		String textElement = null;
		WebElement currentElement = driver.switchTo().activeElement();
		if(!currentElement.getAttribute("id").isEmpty()) {
			textElement = currentElement.getAttribute("id");
		}
		else if(!currentElement.getText().isEmpty()) {
			textElement = currentElement.getText();
		}

		if(driver.findElement(By.id("username")).equals(currentElement)) {
			System.out.println("The cursor is focused on “Username” text box.");
			reporter.info("The cursor is focused on “Username” text box.");
			Assert.assertTrue("[EXPECTED] The cursor is focused on “Username” text box.", true);
		}
		else {
			System.out.println("The cursor is NOT focused on “Username” text box. It's focused on " + textElement + " element.");
			reporter.info("The cursor is NOT focused on “Username” text box. It's focused on " + textElement + " element.");
			Assert.fail("[ACTUAL] The cursor is NOT focused on “Username” text box. It's focused on " + textElement + " element.");
		}
	}

	public void checkNavigationByKeyboard() {

		String textElement = null;
		WebElement currentElement;
		List<String> loginControls = new ArrayList<String>(Arrays.asList("username", "password", "Forgot Password?", "LOG IN", "logo"));
		int counter = loginControls.size()+2;
		boolean exist = false;

		for(int i=0; i<counter; i++) {

			currentElement = driver.switchTo().activeElement();

			if(!currentElement.getAttribute("id").isEmpty()) {
				textElement = currentElement.getAttribute("id");
			}
			else if(!currentElement.getText().isEmpty()) {
				textElement = currentElement.getText();
			}

			for(String str : loginControls) {

				if(textElement.equals(str)) {
					exist = true;
					loginControls.remove(str);
					break;
				}
				else if(textElement.contains(new SimpleDateFormat("dd-MM-yyyy").format(new Date()))){
					exist = true;
					break;
				}
			}

			if(exist) {
				System.out.println("Successful navigation through element " + textElement + ".");
				reporter.info("Successful navigation through element " + textElement + ".");
				Assert.assertTrue("[EXPECTED] Successful navigation through element " + textElement + ".", true);
			}

			method.pressKey(1, "Tab");
			exist = false;
		}

		if (!loginControls.isEmpty()){
			for(String strRest : loginControls) {
				if(strRest.equals("LOG IN") && (!method.checkButtonState("Login"))) {
					System.out.println("The cursor is NOT focused on " + strRest + " element, because the element is not enabled!");
					reporter.info("The cursor is NOT focused on " + strRest + " element, because the element is not enabled!");
				}
				else {
					System.out.println("The cursor is NOT focused on " + strRest + " element.");
					reporter.info("The cursor is NOT focused on " + strRest + " element.");
					Assert.fail("[ACTUAL] The cursor is NOT focused on " + strRest + " element.");
				}
			}
		}
	}

	Set<Cookie> allCookies;
	public void closeBrowser() {

		allCookies = driver.manage().getCookies();
		driver.close();
	}

	public void reopenBrowser() {

		CommonVariables.newDriver = true;
		new SharedDriver();
		driver = DriverFactory.getDriver();
		PageFactory.initElements(driver, this);

		method.navigateToURL(driver, CommonVariables.baseURL + PropertyUtils.getProperty("history.page.URL"));

		for(Cookie cookies : allCookies)
		{
			cookies = new Cookie.Builder(cookies.getName(), cookies.getValue()).path("/").build();
			try {
				driver.manage().addCookie(cookies);
			}catch(Exception e) {}
		}

		driver.navigate().refresh();
		allCookies = driver.manage().getCookies();
		allCookies.clear();
		CommonVariables.newDriver = false;
	}

	public void checkPassInPageSource(String username, String password) {

		if(!driver.getPageSource().contains(password)) {
			System.out.println("The password is NOT viewing on page source.");
			reporter.info("The password is NOT viewing on page source.");
			Assert.assertTrue("[EXPECTED] The password is NOT viewing on page source.", true);
		}
		else {
			System.out.println("The password is viewing on page source.");
			reporter.info("The password is viewing on page source.");
			Assert.fail("[ACTUAL] The password is viewing on page source.");
		}
	}

	public void openSecondLogin() {
		try{
			//			Actions action= new Actions(driver);
			//			action.keyDown(Keys.CONTROL).sendKeys("t").keyUp(Keys.CONTROL).build().perform();
			//			driver.findElement(By.tagName("html")).sendKeys(Keys.CONTROL + "t" + Keys.CONTROL);
			WebElement element = driver.findElement(By.tagName("a"));  
			String keyString = Keys.CONTROL+Keys.SHIFT.toString()+Keys.ENTER.toString();
			element.sendKeys(keyString);
		}
		catch(Exception e) {
			JavascriptExecutor js = (JavascriptExecutor)driver;
			js.executeScript("window.open();");
		}
		System.out.println("A new window(tab) is opened.");
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(tabs.size()-1));
		method.navigateToURL(driver, CommonVariables.baseURL + "login");
	}

	public void loginDialogNotDisplayed() throws IOException {

		method.checkExistingOnPage("Welcome", "Forgot Password?");
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
