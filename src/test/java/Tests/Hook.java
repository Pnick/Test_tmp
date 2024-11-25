package Tests;

import java.io.IOException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import Driver.DriverFactory;

public class Hook {

	//public static Map<String, Scenario> scenarios = new HashMap<String, Scenario>();
	public static Scenario scenario;
	
	@Before
	public void before(Scenario scenario) {

		System.out.println("************************************************************************************************************");
		System.out.println("Starting scenario : " + scenario.getName());
		System.out.println("************************************************************************************************************");

		//scenarios.put(scenario.getName(), scenario);
		Hook.scenario=scenario;
//		  public void clearDriverCache(driver) {
//			    driver.getDevTools().createSessionIfThereIsNotOne();
//			    driver.getDevTools().send(Network.clearBrowserCookies());
//			    // you could also use                
//			    // driver.getDevTools().send(Network.clearBrowserCache());
//			}
		  
//		driver.getDevTools().send(Network.clearBrowserCache());
		
//	    chromeDriver.manage().deleteAllCookies();
//	    chromeDriver.get("chrome://settings/clearBrowserData");
//	    chromeDriver.findElementByXPath("//settings-ui").sendKeys(Keys.ENTER);
		

//		js.executeScript(String.format("window.localStorage.removeItem('persist:betslip');"));
		
		/*String date =  LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm"));
		String repName="Test-Report-"+date+".html";

		htmlReporter=new ExtentHtmlReporter("report/"+repName);//specify location of the report
		htmlReporter.loadXMLConfig("configs/report.xml");

		extent=new ExtentReports();

		extent.attachReporter(htmlReporter);

		htmlReporter.config().setDocumentTitle("Test Project"); 
		htmlReporter.config().setReportName("Functional Test Automation Report"); 
		htmlReporter.config().setTheme(Theme.DARK);

		extent.setSystemInfo("Host Name", "Sofia");
		extent.setSystemInfo("User Name", "Admin");
		extent.setSystemInfo("Environment", "QA");	*/

		//DriverFactory.getDriver().get(propertyHelper.getValue("baseUrl")));
		//OperationsHelper.generateRequiredAmountOfSymbols(6);
	}


	//	@After
	//	public void after(Scenario scenario) {
	//
	//		if (DriverFactory.getDriver() != null) {
	//			if (scenario.isFailed()) {
	//				try {
	//					final byte[] screenShot = ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.BYTES);
	//					scenario.embed(screenShot, "image/png");
	//					scenario.write("URL: " + DriverFactory.getDriver().getCurrentUrl());
	//				} catch (WebDriverException exception) {
	//					exception.printStackTrace();
	//				}
	//			}
	//		}
	//	}

	@After
	public void aftercase(Scenario scenario) throws IOException {

		if (scenario != null && scenario.isFailed()) { 
			byte[] screenshot = ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.BYTES);
			scenario.attach(screenshot, "image/png", "Error");
			
//			for (LogEntry entry : DriverFactory.getDriver().manage().logs().get(LogType.BROWSER)) {
//			    //System.out.println(entry.toString());
//			    System.out.println("Console entry message: " + entry.getMessage());
//			}
//			for (LogEntry entry : DriverFactory.getDriver().manage().logs().get(LogType.PERFORMANCE)) {
//			    //System.out.println(entry.toString());
//			    System.out.println("Entry Level: " + entry.getLevel() + " Entry message: " + entry.getMessage());
//			}
		}
//		if (DriverFactory.getDriver() != null) {
//			DriverFactory.getDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
//		}
		System.out.println("============================================================================================================");
		System.out.println("End of scenario : " + scenario.getName() + " -> Status - " + scenario.getStatus());
		System.out.println("============================================================================================================");
	}
	//	@After
	//	public void closeWindows() {
	//		new BaseSeleniumFunc().closeAllWindowsExceptParent(DriverFactory.getDriver());
	//	}
}
