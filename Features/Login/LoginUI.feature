@TestScenario @Login
Feature: Login UI Verification

  Scenario: Verify that all the labels and controls including text-boxes, buttons, and links are present on the Login page.
    Given the Login screen is opened
    Then all the labels and controls are present on the Login page
      | sername | assword | Forgot Password | og In |

  Scenario: Verify that as soon as the login page opens, by default the cursor is focused on “Username” text box.
    Given the Login screen is opened
    Then by default the cursor is focused on “Username” text box.

  Scenario: Verify that the user is able to navigate or access the different controls by pressing the ‘Tab’ key on the keyboard.
    Given the Login screen is opened
    Then user is able to navigate or access the different controls by pressing the ‘Tab’ key

  Scenario Outline: Verify that the user is able to navigate or access the different controls by pressing the ‘Tab’ key on the keyboard.    
    Given the Login screen is opened
    When enter "<username>" and "<password>"
    Then user is able to navigate or access the different controls by pressing the ‘Tab’ key

    Examples: 
      | username        | password       |
      | valid.username1 | valid.password |