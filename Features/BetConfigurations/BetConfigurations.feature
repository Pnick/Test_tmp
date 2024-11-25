@TestScenario @BetConfigurations
Feature: Bet Configurations tabs
	Verify Bet Configuration tabs functionalities

  Scenario: Open Bet Configurations page
    Given the Homepage is opened with user "valid.username8"
    When click "Bet Configurations" button on Home page
    Then a "Default Bet Configs" page is displayed

  Scenario: Verify that the Bet Configurations page can be opened directly by URL
    Given there is no logged in user
    And the Bet Configurations "SLOT" page is opened with user "loggedOutUser"
    When enter "valid.username18" and "valid.password"
    And click on "Login" button
    Then a "Default Bet Configs" page is displayed
    And a "Bet Increments" is displayed on the web table 1 times and "Clear Filter" is not

  Scenario: Verify that Operators Bet Config page is opening
    Given there is no logged in user
    And the Bet Configurations "SLOT" page is opened with user "valid.username8"
    When click on "Operators Bet Configs" tab
    Then an element "Operator Name" is displayed and "Bet Increments" is not displayed

  Scenario: Verify that Games Bet Config page is opening
    Given the Bet Configurations "SLOT" page is opened with user "valid.username8"
    When click on "Games Bet Configs" tab
    Then an element "Game Name" is displayed and "Bet Increments" is not displayed

  Scenario: Verify search functionality on Games Bet Configuration page
    Given the Bet Configurations "Games Bet" page is opened with user "valid.username8"
    When click element "GameName" on Bet Configurations page
    #And select "random" option from "Game Name Bet" drop down
    #And click element "Search" on Bet Configurations page
    And enter in "GameName" field on Bet Configurations page "GameName12" text
    And select "GameName12" option from "Game Name Bet" drop down
    And click element "Search" on Bet Configurations page
    And click on "Go to Game Info " button
    And click on "Operator12" button
    Then an element "Yes" is displayed and "Go to Game Info " is not displayed
 
  Scenario: Verify search functionality on Operators Bet Configuration page
    Given the Bet Configurations "Operators Bet" page is opened with user "valid.username8"
    When click element "OperatorName" on Bet Configurations page
    #And select "random" option from "Operator Bet" drop down
    #And click element "Search" on Bet Configurations page
    And enter in "OperatorName" field on Bet Configurations page "Operator1" text
    And select "Operator1" option from "Operator Bet" drop down
    And click element "Search" on Bet Configurations page
    And click on "Go To Operator Info " button
    Then an element "Operator Info" is displayed and "Go To Operator Info " is not displayed
