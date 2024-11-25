@TestScenario @Login
Feature: Login Functional Verification

  Scenario Outline: Verify that the user is able to login by entering valid credentials and pressing Enter key.
    Given the Login screen is opened
    When enter "<username>" and "<password>"
    And press 1 times the "Enter" key on the keyboard
    Then home page is displayed

    Examples: 
      | username        | password       |
      | valid.username1 | valid.password |

  Scenario Outline: Verify that User is able to Login with Valid Credentials and pressing Login button.
    Given the Login screen is opened
    When enter "<username>" and "<password>"
    And click on "Login" button
    Then home page is displayed

    Examples: 
      | username        | password       |
      | valid.username1 | valid.password |

  Scenario Outline: Verify that User is not able to Login with invalid Username and invalid Password
    Given the Login screen is opened
    When enter "<username>" and "<password>"
    And click on "Login" button
    Then the user is NOT logged in
    And validation message for "incorrect" fields is displayed

    Examples: 
      | username          | password         |
      | invalid.username1 | invalid.password |
      | valid.username1   | invalid.password |
      | invalid.username1 | valid.password   |
      | inactive.username | valid.password   |

  Scenario Outline: Verify that 'Log In' button is disabled in case the user leaves the username or password field as blank.
    Given the Login screen is opened
    When enter "<username>" and "<password>"
    And click on "Login" button
    Then the user is NOT logged in
    And the button "Login" is "inactive"

    Examples: 
      | username        | password       |
      |                 | valid.password |
      | valid.username1 |                |

  Scenario Outline: Verify if the password can be copy-pasted or not.
    Given the Login screen is opened
    When enter "<username>" and "<password>"
    Then the password can NOT be copy-pasted

    Examples: 
      | username        | password       |
      | valid.username1 | valid.password |

  Scenario: Verify that the logout link is redirected to login/home page
    Given the Homepage is opened with user "valid.username1"
    When open user menu
    And click on "Logout" button
    Then the User is redirected to login/home page

  Scenario: Verify that User is redirected to Forgot password page when clicking on Forgot Password link
    Given the Login screen is opened
    When click on "Forgot Password" button
    Then the User is redirected to Forgot password page

  Scenario: Verify that whether User is still logged in after series of actions such as sign in, close browser and reopen the application.
    Given the Logs page is opened with user "valid.username1"
    When the User closes the browser
    And the User reopens the application
    Then home page is displayed
