package Tests;

import Driver.SharedDriver;
import TestObjects.BaseMethodsO;
import TestObjects.HomePagePO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class Test_BaseMethods {

	protected BaseMethodsO method;
	
	public Test_BaseMethods(SharedDriver driver, HomePagePO homePage, BaseMethodsO method) {
		this.method = method;
	}
	
	@When("^click on \"([^\"]*)\" button$")
	public void clickButton(String button) throws Throwable {

		method.clickButton(button);
	}
	
	@When("^clear \"([^\"]*)\" field on \"([^\"]*)\" page$")
	public void clearField(String input, String page) throws Throwable {

		method.clear(page, input);
	}
 
	@When("^select \"([^\"]*)\" option from combo box \"([^\"]*)\"$")
	public void selectComboItem(String option, String comboName) throws Throwable {

		method.selectComboBoxOption(option, comboName);
	}
	
	@When("^select \"([^\"]*)\" option from \"([^\"]*)\" drop down$")
	public void selectDropDownItem(String option, String dropDownName) throws Throwable {

		method.selectDropDownOption(option, dropDownName);
	}
	
	@When("^switch to parent tab$")
	public void switchToParentTab() throws Throwable {
		
		method.switchToParentTab();
	}
	
	@When("^get displayed results on the web table$")
	public void getResultsOnWeb() throws Throwable {
		
		method.getDisplayedResults();
	}
	
	@When("^get value of pagination counter$")
	public void getPaginationValue() throws Throwable {
		
		method.getResultNumberOnWeb(true);
	}
	
	@When("^change data on DB$")
	public void changeValueOnDB(DataTable dbParams) throws Throwable {

		method.changeKeyValueOnDB(dbParams);
	}
	
	@When("^enter data on DB$")
	public void enterDataOnDB(DataTable dbParams) throws Throwable {

		method.enterDataOnDB(dbParams);
	}
	
	@Then("^a \"([^\"]*)\" page is displayed$")
	public void checkPage(String page) throws Throwable {

		method.checkPageOpened(page);
	}
	
	@Then("^an element \"([^\"]*)\" is displayed and \"([^\"]*)\" is not displayed$")
	public void isDisplayedElement(String element, String notDisplayedElement) throws Throwable {

		method.checkExistingOnPage(element, notDisplayedElement);
	}
	
	@Then("^check \"([^\"]*)\" with \"([^\"]*)\" string \"([^\"]*)\" against \"([^\"]*)\" table records$")
	public void checkAgainstDB(String fieldName, String wholeOrPartString, String fieldValue, String tableName) throws Throwable {

		method.checkResultsDB(fieldName, wholeOrPartString, fieldValue, tableName);
	}
	
	@Then("^a \"([^\"]*)\" is NOT displayed$")
	public void isNotDisplayedElement(String element) throws Throwable {

		method.checkNotExistingOnPage(element);
	}
	
	@Then("^a \"([^\"]*)\" is displayed on the web table (\\d+) times and \"([^\"]*)\" is not$")
	public void isElementDisplayedOnWebTable(String element, int elementNumber, String notExistingElement) throws Throwable {

		method.checkMatchOnTable(element, elementNumber, notExistingElement);
	}

	@Then("^a string \"([^\"]*)\" is contained on the web table (\\d+) times and \"([^\"]*)\" is not$")
	public void isElementContainedOnWebTable(String element, int elementNumber, String notExistingElement) throws Throwable {

		method.checkExistOnTable(element, elementNumber, notExistingElement);
	}
	
	@Then("^compare current with the previous table results$")
	public void compareResults() throws Throwable {

		method.compareResults();
	}	
	
	@Then("^the button \"([^\"]*)\" is \"([^\"]*)\"$")
	public void isButtonActive(String button, String buttonState) throws Throwable {

		method.checkButtonIsActive(button, buttonState);
	}
	
	@Then("^check that \"([^\"]*)\" Info is NOT changed$")
	public void checkInfoIsNotChanged(String typeInfo) throws Throwable {

		method.checkInfoIsNotChanged(typeInfo);
	}
	
	@Then("^check that server is updated$")
	public void checkServerUpdated(DataTable checkParams) throws Throwable {

		method.checkServerUpdated(checkParams);
	}
}
