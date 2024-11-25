@TestScenario @OperatorAccount
Feature: Operator Group Account	- Game Management
  Verify functionalities on Game Management page with Operator Group Account

  Scenario Outline: Operator Group Account - Search by Game Name on Game Management page
    Given the Game Management page is opened with user "valid.username.no.admin"
    When enter in "GameName" field on Game Management page "<GameName>" text
    And select "<GameName>" option from "Game Name" drop down
    And click on "SEARCH" button
    Then a "<GameName>" is displayed on the web table 1 times and "<notDisplayedElement>" is not
    And check "name" with "whole" string "<GameName>" against "games" table records

    Examples: 
      | GameName  | notDisplayedElement |
      | GameName1 | GameName01          |

  Scenario Outline: Operator Group Account - Search by Game Name on Game Management page
    Given the Game Management page is opened with user "valid.username.no.admin"
    When enter in "GameName" field on Game Management page "<GameName>" text
    And select "<GameName>" option from "Game Name" drop down
    And click on "SEARCH" button
    Then a string "<GameName>" is contained on the web table 0 times and "" is not

    Examples: 
      | GameName  |
      | GameName2 |

  Scenario Outline: Operator Group Account - Search by Game ID on Game Management page
    Given the Game Management page is opened with user "valid.username.no.admin"
    When enter in "GameID" field on Game Management page "<GameID>" text
    And clear "GameName" field on "Games" page
    And click on "SEARCH" button
    Then a "<GameName>" is displayed on the web table 1 times and "<notDisplayedElement>" is not
    And check "_id" with "whole" string "<GameID>" against "games" table records

    Examples: 
      | GameID     | GameName  | notDisplayedElement |
      | 1000000001 | GameName1 | GameName10          |

  Scenario Outline: Operator Group Account - Search by Game ID on Game Management page
    Given the Game Management page is opened with user "valid.username.no.admin"
    When enter in "GameID" field on Game Management page "<GameID>" text
    And clear "GameName" field on "Games" page
    And click on "SEARCH" button
    Then a string "<GameName>" is contained on the web table 0 times and "" is not

    Examples: 
      | GameID     | GameName  |
      | 1000000002 | GameName2 |

  Scenario Outline: Operator Group Account - Search by Game Name on second Game Management page
    Given the Game Management page is opened with user "valid.username.no.admin"
    When enter in "GameName" field on Game Management page "<GameName>" text
    And click on "SEARCH" button
    And click on "nextPaginationPage" button
    Then a string "GameName" is contained on the web table 3 times and "" is not

    Examples: 
      | GameName |
      | Game     |

  Scenario Outline: Operator Group Account - Search by Game Name from the last Game Management page
    Given the Game Management page is opened with user "valid.username.no.admin"
    When click on "lastPaginationPage" button
    And enter in "GameName" field on Game Management page "<GameName>" text
    And click on "SEARCH" button
    Then a "<GameName>" is displayed on the web table 1 times and "<notDisplayedElement>" is not
    And check "name" with "whole" string "<GameName>" against "games" table records

    Examples: 
      | GameName  | notDisplayedElement |
      | GameName1 | GameName2           |

  Scenario: Operator Group Account - Search by Provider on Game Management page
    Given the Game Management page is opened with user "valid.username.no.admin"
    When select "Provider1" option from combo box "Game Provider"
    And click on "SEARCH" button
    Then a "Provider1" is displayed on the web table 4 times and "Provider2" is not
    And check "provider_id" with "notVisible" string "100001" against "games" table records

  Scenario: Operator Group Account - Search by Types on Game Management page
    Given the Game Management page is opened with user "valid.username.no.admin"
    When select "Slot" option from combo box "Type"
    And click on "SEARCH" button
    Then a "Slot" is displayed on the web table 10 times and "Instant Win" is not

  Scenario: Operator Group Account - Search by Categories on Game Management page
    Given the Game Management page is opened with user "valid.username.no.admin"
    When select "Premium" option from combo box "Categories"
    And click on "SEARCH" button
    Then a "Premium" is displayed on the web table 10 times and "" is not

  Scenario Outline: Operator Group Account - Search with similar Game's data on Game Management page
    Given the Game Management page is opened with user "valid.username.no.admin"
    When enter in "GameName" field on Game Management page "<GameName>" text
    And click on "SEARCH" button
    Then a string "<GameName>" is contained on the web table <ManyTimes> times and "" is not

    Examples: 
      | GameName  | ManyTimes |
      | GameName  |        13 |
      | GameName1 |         6 |

  Scenario: Operator Group Account - Test Clear Filters function from last Game Management Pagination page
    Given the Game Management page is opened with user "valid.username.no.admin"
    When click on "lastPaginationPage" button
    And get displayed results on the web table
    And click on "ClearFilters" button
    Then compare current with the previous table results

  Scenario Outline: Operator Group Account - Verify Game info on Game Management page
    Given the Game Management page is opened with user "valid.username.no.admin"
    When enter in "GameID" field on Game Management page "<GameID>" text
    And click on "SEARCH" button
    And open game info with ID number "<GameID>" and name "<GameName>"
    Then check game info data filtered by "_id" and <GameID>

    Examples: 
      | GameID     | GameName  |
      | 1000000001 | GameName1 |

  Scenario Outline: Operator Group Account - Verify Provider info on Game Management page
    Given the Game Management page is opened with user "valid.username.no.admin"
    When enter in "GameID" field on Game Management page "<GameID>" text
    And click on "SEARCH" button
    And open provider info with ID number "<GameID>" and name "<ProviderName>"
    Then check provider info data filtered by "_id" and <GameID>

    Examples: 
      | GameID     | ProviderName |
      | 1000000001 | Provider1    |
