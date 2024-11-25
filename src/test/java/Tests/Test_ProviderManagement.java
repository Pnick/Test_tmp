package Tests;

import TestObjects.ProviderManagementPO;
import UtilsHelper.CommonVariables;
import UtilsHelper.PropertyUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import Driver.SharedDriver;

public class Test_ProviderManagement {

	public ProviderManagementPO providerManagement;

	public Test_ProviderManagement(SharedDriver driver, ProviderManagementPO providerManagement) {
		this.providerManagement = providerManagement;
	}

	@Given("^the Provider Management page is opened with user \"([^\"]*)\"$")
	public void openProviderManagementPage(String user) throws Throwable {

		providerManagement.openProviderManagementPageFrom(
				CommonVariables.baseURL + PropertyUtils.getProperty("provider.management.page.URL"), user);
	}
	
	@When("^enter in \"([^\"]*)\" field on Provider Management page \"([^\"]*)\" text$")
	public void enterInput(String inputName, String inputText) throws Throwable {

		providerManagement.enterText(inputName, inputText);
	}
	
	@When("^open provider info with ID number \"([^\"]*)\" and name \"([^\"]*)\"$")
	public void openProviderInfo(String idNumber, String name) throws Throwable {

		providerManagement.openProviderInfo(idNumber, name);
	}
	
	@When("^enter new provider data$")
	public void enterNewProviderData() throws Throwable {

		providerManagement.enterNewProviderData();
	}
	
	@When("^change provider data$")
	public void changeProviderData() throws Throwable {

		providerManagement.changeProviderData();
	}
	
	@Then("^check provider info data filtered by \"([^\"]*)\" and (\\d+)$")
	public void checkWebDataAgainstDB(String idNumber , int value) throws Throwable {

		providerManagement.checkWebDataAgainstDB(idNumber , value);
	}
	
	@Then("^check that Provider Info is changed$")
	public void checkProviderInfoIsChanged() throws Throwable {

		providerManagement.checkProviderInfoIsChanged();
	}
}

