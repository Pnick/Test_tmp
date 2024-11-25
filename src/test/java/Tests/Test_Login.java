package Tests;

import Driver.SharedDriver;
import TestObjects.LoginPO;
import TestObjects.LogsPO;
import UtilsHelper.CommonVariables;
import UtilsHelper.PropertyUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class Test_Login {

	public LoginPO login;

	public Test_Login(SharedDriver driver, LoginPO login) {
		this.login = login;
	}
	
	
	@Given("^the Login screen is opened$")
	public void the_Login_screen_is_opened() throws Throwable {
	    
		login.openLoginPage(CommonVariables.baseURL);
	}

	@Given("^there is no logged in user$")
	public void noLoggedInUser() throws Throwable {
	    
		login.LoggedOut();
	}
		
	@When("^enter \"([^\"]*)\" and \"([^\"]*)\"$")
	public void enter_and(String username, String password) throws Throwable {
	    
		login.enterCredentials(username, password);
	}

	@When("^press (\\d+) times the \"([^\"]*)\" key on the keyboard$")
	public void press_times_the_key_on_the_keyboard(int counter, String key) throws Throwable {
	    
	    login.pressKeyboardKey(counter, key);
	}

	@Then("^the user is NOT logged in$")
	public void the_user_is_NOT_logged_in() throws Throwable {
	    
	    login.checkNotLoggedIn();
	}

	@Then("^validation message for \"([^\"]*)\" fields is displayed$")
	public void validation_message_for_fields_is_displayed(String errorType) throws Throwable {
	    
		login.checkLoggedInMessage(errorType);
	}

	@Then("^the password can NOT be copy-pasted$")
	public void the_password_can_NOT_be_copy_pasted() throws Throwable {
	    
		login.checkPasswordIfCopy();
	}

	@Then("^the User is redirected to Forgot password page$")
	public void the_User_is_redirected_to_Forgot_password_page() throws Throwable {
	    
		login.checkForgotPasswordPage();
	}

	@When("^the User closes the browser$")
	public void the_User_closes_the_browser() throws Throwable {
	    
		login.closeBrowser();
	}

	@When("^the User reopens the application$")
	public void the_User_reopens_the_application() throws Throwable {
	    
		login.reopenBrowser();
	}

	@Then("^the User is redirected to login/home page$")
	public void the_User_is_redirected_to_login_home_page() throws Throwable {
	    
		login.checkNotLoggedIn();
	}

	@Then("^all the labels and controls are present on the Login page$")
	public void all_the_labels_and_controls_are_present_on_the_Login_page(DataTable testedFields) throws Throwable {
	    
		login.checkLoginControls(testedFields);
	}

	@Then("^by default the cursor is focused on “Username” text box\\.$")
	public void by_default_the_cursor_is_focused_on_Username_text_box() throws Throwable {
	    
		login.checkCursor();
	}

	@Then("^user is able to navigate or access the different controls by pressing the ‘Tab’ key$")
	public void user_is_able_to_navigate_or_access_the_different_controls_by_pressing_the_Tab_key() throws Throwable {
	    
		login.checkNavigationByKeyboard();
	}

	@When("^open the Logs page with user \"([^\"]*)\"$")
	public void openTheLogsPage(String user) throws Throwable {
	    
		new LogsPO().openLogsPageFrom(CommonVariables.baseURL + PropertyUtils.getProperty("logs.page.URL"), user);
	}

	@Then("^check that \"([^\"]*)\" and \"([^\"]*)\" are viewing on page source$")
	public void check_that_username_and_password_are_viewing_on_page_source(String username, String password) throws Throwable {
	    
		login.checkPassInPageSource(username, password); 
	}

	@When("^try to open second tab with login screen$")
	public void openSecondLoginScreen() throws Throwable {
	    
		login.openSecondLogin();
	}
}
