package Runner;

import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import com.vimalselvam.cucumber.listener.Reporter;

import UtilsHelper.CommonVariables;
import UtilsHelper.InitDB;
import UtilsHelper.Initial;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
		features 	= 	{ "Features" },
		glue		= 	{ "Tests" },
		tags		= 	{ "~@manual", "~@notSupported", "~@notImplemented", "~@CannotBeTested","@Login, @Users, @Logs"},
		plugin 		= 	{"com.vimalselvam.cucumber.listener.ExtentCucumberFormatter:"})

public class RunnerUsersIT extends AbstractTestNGCucumberParallelTests{

	public static String configPropertiesRun = "Configuration.properties";

	@BeforeSuite
	public static void initTestNG() {
		Initial set = new Initial();
		set.environment();
		set.initURL();
		set.reportPathName();
		if(CommonVariables.db == null) {
			CommonVariables.db = new InitDB(CommonVariables.environment);
		}
	}

	@AfterMethod
	public static void writeExtentReport() throws IOException, InterruptedException {

		//load report configuration
		Reporter.loadXMLConfig(new File("configs/report.xml"));

		//get system info and write on report page
		Reporter.setSystemInfo("User Name", System.getProperty("user.name"));
		Reporter.setSystemInfo("Time Zone", System.getProperty("user.timezone"));
		Reporter.setSystemInfo("Machine", System.getProperty("os.name") + " " + System.getProperty("os.version"));
		//		Reporter.setSystemInfo("Selenium", "3.7.0");
		Reporter.setSystemInfo("Java Version", System.getProperty("java.version"));
		Reporter.setSystemInfo("Browser", CommonVariables.browserName);
		Reporter.setSystemInfo("Driver Version", CommonVariables.driverVersion);
	}
	
	@BeforeClass
	public static void initJUnit() {
		Initial set = new Initial();
		set.environment();
		set.initURL();
		set.reportPathName();
		if(CommonVariables.db == null) {
			CommonVariables.db = new InitDB(CommonVariables.environment);
		}
	}
	
	@AfterClass
	public static void writeExtentReportJUnit() throws IOException, InterruptedException {

		//load report configuration
		Reporter.loadXMLConfig(new File("configs/report.xml"));

		//get system info and write on report page
		Reporter.setSystemInfo("User Name", System.getProperty("user.name"));
		Reporter.setSystemInfo("Time Zone", System.getProperty("user.timezone"));
		Reporter.setSystemInfo("Machine", System.getProperty("os.name") + " " + System.getProperty("os.version"));
		//		Reporter.setSystemInfo("Selenium", "3.7.0");
		Reporter.setSystemInfo("Java Version", System.getProperty("java.version"));
		Reporter.setSystemInfo("Browser", CommonVariables.browserName);
		Reporter.setSystemInfo("Driver Version", CommonVariables.driverVersion);
	}
}
