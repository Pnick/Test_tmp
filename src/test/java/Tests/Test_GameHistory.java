package Tests;

import TestObjects.GameHistoryPO;
import UtilsHelper.CommonVariables;
import UtilsHelper.PropertyUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import Driver.SharedDriver;


public class Test_GameHistory {

	GameHistoryPO gameHistory;

	public Test_GameHistory(SharedDriver driver, GameHistoryPO gameHistory) {
		this.gameHistory = gameHistory;
	}

	@Given("^the Game History page is opened with user \"([^\"]*)\"$")
	public void openHistoryPage(String user) throws Throwable {

		gameHistory.openHistoryPageFrom(CommonVariables.baseURL + PropertyUtils.getProperty("history.page.URL"), user);
	}
	
	@When("^enter in \"([^\"]*)\" field on History page \"([^\"]*)\" text$")
	public void enterInput(String inputName, String inputText) throws Throwable {

		gameHistory.enterText(inputName, inputText);
	}

	@When("^open game round info with ID number \"([^\"]*)\"$")
	public void openGameRoundInfo(String idNumber) throws Throwable {

		gameHistory.openGameRoundInfo(idNumber);
	}
	
	@Then("^check game round info data filtered by \"([^\"]*)\" and (\\d+)$")
	public void checkWebDataAgainstDB(String idNumber , int value) throws Throwable {

		gameHistory.checkWebDataAgainstDB(idNumber , value);
	}
	

}

