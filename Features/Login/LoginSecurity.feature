@TestScenario @Login
Feature: Login Security Verification

  Scenario: Verify that once logged in, clicking the back button doesn’t logout the user.
    Given the Homepage is opened with user "valid.username1"
    When press 2 times the "Back" key on the keyboard
    And open the Logs page with user "valid.username1"
    Then a "Logs" page is displayed

  Scenario: Verify that once logged out, clicking the back button doesn’t login the user.
    Given the Homepage is opened with user "valid.username1"
    When open user menu
    And click on "Logout" button
    And press 1 times the "Back" key on the keyboard
    Then the User is redirected to login/home page

  Scenario Outline: Verify whether the login form is revealing any security information by viewing page source
    Given the Login screen is opened
    When enter "<username>" and "<password>"
    Then check that "<username>" and "<password>" are viewing on page source

    Examples: 
      | username        | password       |
      | valid.username1 | valid.password |

  Scenario: Verify if a user should not be allowed to log in with same credentials from the same browser at the same time.
    Given the Homepage is opened with user "valid.username1"
    When try to open second tab with login screen
    Then home page is displayed

  Scenario Outline: Verify if a user should not be allowed to log in with different credentials from the same browser at the same time.
    Given the Homepage is opened with user "valid.username1"
    When try to open second tab with login screen
    And open user menu
    And click on "Logout" button
    And enter "<username>" and "<password>"
    And click on "Login" button
    And switch to parent tab
    Then an element "<username>" is displayed and "valid.username1" is not displayed

    Examples: 
      | username   | password  |
      | username11 | admin123! |
#  Scenario: Verify whether Cross-site scripting (XSS ) vulnerability work on a login page. XSS vulnerability may be used by hackers to bypass access controls.
