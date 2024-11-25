package Tests;

import TestObjects.GameManagementPO;
import UtilsHelper.CommonVariables;
import UtilsHelper.PropertyUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import Driver.SharedDriver;

public class Test_GameManagement {

	public GameManagementPO gameManagement;

	public Test_GameManagement(SharedDriver driver, GameManagementPO gameManagement) {
		this.gameManagement = gameManagement;
	}

	@Given("^the Game Management page is opened with user \"([^\"]*)\"$")
	public void openGameManagementPage(String user) throws Throwable {

		gameManagement.openGameManagementPageFrom(
				CommonVariables.baseURL + PropertyUtils.getProperty("game.management.page.URL"), user);
	}
	
	@When("^enter in \"([^\"]*)\" field on Game Management page \"([^\"]*)\" text$")
	public void enterInput(String inputName, String inputText) throws Throwable {

		gameManagement.enterText(inputName, inputText);
	}

	@When("^open game info with ID number \"([^\"]*)\" and name \"([^\"]*)\"$")
	public void openGameInfo(String idNumber, String name) throws Throwable {

		gameManagement.openGameInfo(idNumber, name);
	}
	
	@When("^enter new game data$")
	public void enterNewGameData() throws Throwable {

		gameManagement.enterNewGameData();
	}
	
	@When("^set \"([^\"]*)\" checkbox to \"([^\"]*)\"$")
	public void clickCheckBox(String checkbox, String onOff) throws Throwable {

		gameManagement.clickCheckBox(checkbox, onOff);
	}
	
	@When("^change game data$")
	public void changeGameData() throws Throwable {

		gameManagement.changeGameData();
	}	
	
	@Then("^check game info data filtered by \"([^\"]*)\" and (\\d+)$")
	public void checkWebDataAgainstDB(String idNumber , int value) throws Throwable {

		gameManagement.checkWebDataAgainstDB(idNumber , value);
	}
	
	@Then("^check that Game Info is changed$")
	public void checkGameInfoIsChanged() throws Throwable {

		gameManagement.checkGameInfoIsChanged();
	}
	
}

