@TestScenario @GameManagement
Feature: Create and Edit new Game
  Verify that a new Game can be created and edited

  Scenario: Create new Game with correct data
    Given the Game Management page is opened with user "valid.username3"
    When click on "Add" button
    And enter new game data
    And click on "Create" button
    And enter in "GameName" field on Game Management page "lastGame" text
    And click on "SEARCH" button
    Then a string "GameTest" is contained on the web table 1 times and "" is not

  Scenario: Edit existing Game Info
    Given the Game Management page is opened with user "valid.username3"
    When enter in "GameName" field on Game Management page "randomGame" text
    And click on "SEARCH" button
    And open game info with ID number "random" and name "random"
    And click on "Edit" button
    And change game data
    And click on "Save" button
    Then check that Game Info is changed

  Scenario Outline: Game Info cannot be edit with wrong data
    Given the Game Management page is opened with user "valid.username3"
    When enter in "GameName" field on Game Management page "randomGame" text
    And click on "SEARCH" button
    And open game info with ID number "random" and name "random"
    And click on "Edit" button
    And enter in "<field>" field on Game Management page "<value>" text
    And click on "Save" button
    Then check that "Game" Info is NOT changed

    Examples: 
      | field            | value                  |
      | newGameName      | random                 |
      | newGameShortName | random                 |
      | newGameRTP       |                    101 |
      | newGameRTP       | 0.00000001             |
      | newGameRTP       | 12.34.56               |
      | newGameRTP       | 78+                    |
      | newGameRTP       |                 100000 |
      | newGameURL       | https://someDomain.com |
