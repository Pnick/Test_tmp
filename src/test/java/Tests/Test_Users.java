package Tests;

import TestObjects.HomePagePO;
import TestObjects.UsersPO;
import UtilsHelper.CommonVariables;
import UtilsHelper.PropertyUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import Driver.SharedDriver;

public class Test_Users {

	public UsersPO users;
	HomePagePO homePage;

	public Test_Users(SharedDriver driver, HomePagePO homePage, UsersPO users) {
		this.users = users;
		this.homePage = homePage;
	}

	@Given("^the Users page is opened with user \"([^\"]*)\"$")
	public void openUsersPage(String user) throws Throwable {

		users.openUsersPageFrom(this.homePage,
				CommonVariables.baseURL + PropertyUtils.getProperty("users.page.URL"), user);
	}
	
	@When("^enter in \"([^\"]*)\" field on Users page \"([^\"]*)\" text$")
	public void enterInput(String inputName, String inputText) throws Throwable {

		users.enterText(inputName, inputText);
	}
	
	@When("^open user info with ID number \"([^\"]*)\" and name \"([^\"]*)\"$")
	public void openUserInfo(String idNumber, String name) throws Throwable {

		users.openUserInfo(idNumber, name);
	}
	
	@When("^enter new user data$")
	public void enterNewUserData() throws Throwable {

		users.enterNewUserData();
	}
	
	@When("^change user data$")
	public void changeUserData() throws Throwable {

		users.changeUserData();
	}	

	@Then("^check user info data filtered by \"([^\"]*)\" and (\\d+)$")
	public void checkWebDataAgainstDB(String idNumber , int value) throws Throwable {

		users.checkWebDataAgainstDB(idNumber , value);
	}
	
	@Then("^check that User Info is changed$")
	public void checkUserInfoIsChanged() throws Throwable {

		users.checkUserInfoIsChanged();
	}
}

