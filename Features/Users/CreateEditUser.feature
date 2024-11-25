@TestScenario @Users
Feature: Create and Edit new User
  Verify that a new User can be created and edited

  Scenario: Create new User with correct data
    Given the Users page is opened with user "valid.username1"
    When click on "Add" button
    And enter new user data
    And click on "Create" button
    When enter in "UserName" field on Users page "lastUser" text
    And click on "SEARCH" button
    Then a string "usertest" is contained on the web table 1 times and "" is not

  Scenario: Edit existing User's Info
    Given the Users page is opened with user "valid.username1"
    When enter in "UserName" field on Users page "randomUser" text
    And click on "SEARCH" button
    And open user info with ID number "random" and name "random"
    And click on "Edit" button
    And change user data
    And click on "Save" button
    Then check that User Info is changed

  Scenario Outline: User Info cannot be edit with wrong data
    Given the Users page is opened with user "valid.username1"
    When enter in "UserName" field on Users page "randomUser" text
    And click on "SEARCH" button
    And open user info with ID number "random" and name "random"
    And click on "Edit" button
    And enter in "<field>" field on Users page "<value>" text
    And click on "Save" button
    Then check that "User" Info is NOT changed

    Examples: 
      | field          | value                |
      | newUserCompany | MWS$&@!()            |
      | newName        | name@!$%             |
