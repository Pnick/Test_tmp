package Tests;

import TestObjects.HomePagePO;
import UtilsHelper.CommonVariables;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import Driver.SharedDriver;

/**
 * The Class Test_HomePage.
 */
public class Test_HomePage {

	/** The home page. */
	public HomePagePO homePage;

	/**
	 * Instantiates a new test home page.
	 *
	 * @param driver   the driver
	 * @param homePage the home page
	 */
	public Test_HomePage(SharedDriver driver, HomePagePO homePage) {
		this.homePage = homePage;
	}

	/**
	 * Open home page.
	 *
	 * @return the home page PO
	 * @throws Throwable the throwable
	 */
	@Given("^the Homepage is opened with user \"([^\"]*)\"$")
	public HomePagePO openHomePage(String user) throws Throwable {
		homePage.openHomePage(CommonVariables.baseURL, user);
		return homePage;
	}
	
	@Given("^page via \"([^\"]*)\" is opened with user \"([^\"]*)\"$")
	public void openPageViaLink(String url, String user) throws Throwable {
		homePage.openPageViaLink(url, user);
	}
	
	/**
	 * Click button.
	 *
	 * @param button the button
	 * @throws Throwable the throwable
	 */
	@When("^click \"([^\"]*)\" button on Home page$")
	public void clickButton(String button) throws Throwable {

		homePage.clickButton(button);
	}
	
	/**
	 * Click logo.
	 *
	 * @throws Throwable the throwable
	 */
	@When("^user click logo button$")
	public void clickLogo() throws Throwable {
		homePage.linkLogo.click();
	}
	
	/**
	 * Open user menu.
	 *
	 * @throws Throwable the throwable
	 */
	@When("^open user menu$")
	public void openUserMenu() throws Throwable {
	    
		homePage.openUserMenu();
	}
	
	/**
	 * Verify page.
	 *
	 * @throws Throwable the throwable
	 */
	@Then("^home page is displayed$")
	public void verifyPage() throws Throwable {
		this.homePage.checkPage();
	}
}

