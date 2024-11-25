package Tests;

import TestObjects.OperatorManagementPO;
import UtilsHelper.CommonVariables;
import UtilsHelper.PropertyUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import Driver.SharedDriver;

public class Test_OperatorManagement {

	public OperatorManagementPO operatorManagement;

	public Test_OperatorManagement(SharedDriver driver, OperatorManagementPO operatorManagement) {
		this.operatorManagement = operatorManagement;
	}

	@Given("^the Operator Management page is opened with user \"([^\"]*)\"$")
	public void openOperatorManagementPage(String user) throws Throwable {

		operatorManagement.openOperatorManagementPageFrom(
				CommonVariables.baseURL + PropertyUtils.getProperty("operator.management.page.URL"), user);
	}
	
	@When("^enter in \"([^\"]*)\" field on Operator Management page \"([^\"]*)\" text$")
	public void enterInput(String inputName, String inputText) throws Throwable {

		operatorManagement.enterText(inputName, inputText);
	}
	
	@When("^open operator info with ID number \"([^\"]*)\" and name \"([^\"]*)\"$")
	public void openOperatorInfo(String idNumber, String name) throws Throwable {

		operatorManagement.openOperatorInfo(idNumber, name);
	}
	
	@When("^enter new operator data$")
	public void enterNewOperatorData() throws Throwable {

		operatorManagement.enterNewOperatorData();
	}
	
	@When("^change operator data$")
	public void changeOperatorData() throws Throwable {

		operatorManagement.changeOperatorData();
	}	
	
	@Then("^check operator info data filtered by \"([^\"]*)\" and (\\d+)$")
	public void checkWebDataAgainstDB(String idNumber , int value) throws Throwable {

		operatorManagement.checkWebDataAgainstDB(idNumber , value);
	}
	
	@Then("^check that Operator Info is changed$")
	public void checkOperatorInfoIsChanged() throws Throwable {

		operatorManagement.checkOperatorInfoIsChanged();
	}
}

