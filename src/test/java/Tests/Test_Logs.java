package Tests;

import TestObjects.LogsPO;
import UtilsHelper.CommonVariables;
import UtilsHelper.PropertyUtils;
import Driver.SharedDriver;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

public class Test_Logs {

	LogsPO logs;

	public Test_Logs(SharedDriver driver, LogsPO logs) {
		this.logs = logs;
	}

	@Given("^the Logs page is opened with user \"([^\"]*)\"$")
	public void openLogsPage(String user) throws Throwable {

		logs.openLogsPageFrom(CommonVariables.baseURL + PropertyUtils.getProperty("logs.page.URL"), user);
	}
	
	@When("^enter in \"([^\"]*)\" field on Logs page \"([^\"]*)\" text$")
	public void enterInput(String inputName, String inputText) throws Throwable {

		logs.enterText(inputName, inputText);
	}

}

