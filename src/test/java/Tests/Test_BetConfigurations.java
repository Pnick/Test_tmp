package Tests;

import TestObjects.BetConfigurationsPO;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import Driver.SharedDriver;

public class Test_BetConfigurations {

	public BetConfigurationsPO betConfigurations;

	public Test_BetConfigurations(SharedDriver driver, BetConfigurationsPO betConfigurations) {
		this.betConfigurations = betConfigurations;
	}

	@Given("^the Bet Configurations \"([^\"]*)\" page is opened with user \"([^\"]*)\"$")
	public void openBetConfigurationsPage(String page, String user) throws Throwable {

		betConfigurations.openBetConfigurationsPageFrom(page, user);
	}
	
	@Given("^get \"([^\"]*)\" Bet Configuration$")
	public void getConfiguration(String currency) throws Throwable {

		betConfigurations.getConfiguration(currency);
	}

	@Given("^remove \"([^\"]*)\" Bet Configuration for \"([^\"]*)\" game type$")
	public void removeConfiguration(String currency, String gameType) throws Throwable {

		betConfigurations.removeConfiguration(currency,gameType);
	}

	@Given("^init \"([^\"]*)\" Bet Configuration for \"([^\"]*)\" game type$")
	public void initConfiguration(String currency, String gameType) throws Throwable {

		betConfigurations.initConfiguration(currency,gameType);
	}
	
	@When("^click on \"([^\"]*)\" tab$")
	public void clickTab(String tab) throws Throwable {

		betConfigurations.clickTab(tab);
	}
	
	@When("^click element \"([^\"]*)\" on Bet Configurations page$")
	public void clickButton(String elementName) throws Throwable {

		betConfigurations.clickElement(elementName);
	}
	
	@When("^switch \"([^\"]*)\" \"([^\"]*)\" checkbox$")
	public void clickCheckbox(String onOff, String checkbox) throws Throwable {

		betConfigurations.clickCheckBox(onOff, checkbox);
	}
	
	@When("^enter in \"([^\"]*)\" field on Bet Configurations page \"([^\"]*)\" text$")
	public void enterInput(String inputName, String inputText) throws Throwable {

		betConfigurations.enterText(inputName, inputText);
	}

	@When("^remove \"([^\"]*)\" Bet Increments from \"([^\"]*)\" currency$")
	public void removeBetIncrements(String bet, String currency) throws Throwable {

		betConfigurations.removeBet(bet,currency);
	}
	
	@Then("^check that \"([^\"]*)\" Bet is changed$")
	public void checkBet(String currency) throws Throwable {

		betConfigurations.checkBetIsChanged(currency, true);
	}
	
	@Then("^check that \"([^\"]*)\" Bet is added$")
	public void checkBetIsAdded(String currency) throws Throwable {
		
		betConfigurations.checkBetIsAdded(currency);
	}
	
	@Then("^check that \"([^\"]*)\" Bet is NOT added$")
	public void checkBetIsNotAdded(String currency) throws Throwable {
		
		betConfigurations.checkBetIsNotAdded(currency);
	}
}

