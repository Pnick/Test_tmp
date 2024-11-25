@TestScenario @GameManagement
Feature: Game Management
  Verify search functionality on All Games page

  Scenario: Open Game Management page
    Given the Homepage is opened with user "valid.username3"
    When click "Management" button on Home page
    And click "Game Management" button on Home page
    Then a "All Games" page is displayed

  Scenario Outline: Search by Game Name
    Given the Game Management page is opened with user "valid.username3"
    When enter in "GameName" field on Game Management page "<GameName>" text
    And select "<GameName>" option from "Game Name" drop down
    And click on "SEARCH" button
    Then a "<GameName>" is displayed on the web table 1 times and "<notDisplayedElement>" is not
    And check "name" with "whole" string "<GameName>" against "games" table records

    Examples: 
      | GameName  | notDisplayedElement |
      | GameName1 | GameName01          |
      | GameName2 | GameName1           |

  Scenario Outline: Search by Game ID
    Given the Game Management page is opened with user "valid.username3"
    When enter in "GameID" field on Game Management page "<GameID>" text
    And clear "GameName" field on "Games" page
    And click on "SEARCH" button
    Then a "<GameName>" is displayed on the web table 1 times and "<notDisplayedElement>" is not
    And check "_id" with "whole" string "<GameID>" against "games" table records

    Examples: 
      | GameID     | GameName  | notDisplayedElement |
      | 1000000001 | GameName1 | GameName10          |
      | 1000000002 | GameName2 | GameName1           |

  Scenario Outline: Search by part of Game Name
    Given the Game Management page is opened with user "valid.username3"
    When enter in "GameID" field on Game Management page "<GameID>" text
    And enter in "GameName" field on Game Management page "GameName" text
    And click on "SEARCH" button
    And click on "Order by Game ID" button
    Then a "<GameName>" is displayed on the web table 1 times and "<notDisplayedElement>" is not
    And check "_id" with "whole" string "<GameID>" against "games" table records

    Examples: 
      | GameID     | GameName  | notDisplayedElement |
      | 1000000013 | GameName3 | GameName13          |
      | 1000000014 | GameName4 | GameName14          |

  Scenario Outline: Search by Game Name on second page
    Given the Game Management page is opened with user "valid.username3"
    When enter in "GameName" field on Game Management page "<GameName>" text
    And click on "SEARCH" button
    And click on "nextPaginationPage" button
    Then a string "<GameName>" is contained on the web table 10 times and "" is not

    Examples: 
      | GameName |
      | GameN    |

  Scenario Outline: Search by Game Name from last page
    Given the Game Management page is opened with user "valid.username3"
    When click on "lastPaginationPage" button
    And enter in "GameName" field on Game Management page "<GameName>" text
    And click on "SEARCH" button
    Then a "<GameName>" is displayed on the web table 1 times and "<notDisplayedElement>" is not
    And check "name" with "whole" string "<GameName>" against "games" table records

    Examples: 
      | GameName  | notDisplayedElement |
      | GameName2 | GameName3           |

  Scenario Outline: Search after click on next page
    Given the Game Management page is opened with user "valid.username3"
    When enter in "GameName" field on Game Management page "<GameName>" text
    And clear "GameID" field on "Games" page
    And get value of pagination counter
    And click on "Order by Game ID" button
    And click on "nextPaginationPage" button
    Then a string "<GameName>" is contained on the web table 0 times and "" is not

    Examples: 
      | GameName  |
      | GameName1 |

  Scenario: Search by Provider
    Given the Game Management page is opened with user "valid.username3"
    When select "Provider3" option from combo box "Game Provider"
    And click on "SEARCH" button
    Then a "Provider3" is displayed on the web table 10 times and "Provider1" is not
    And check "provider_id" with "notVisible" string "100003" against "games" table records

  Scenario: Search by Types
    Given the Game Management page is opened with user "valid.username3"
    When select "Slot" option from combo box "Type"
    And click on "SEARCH" button
    Then a "Slot" is displayed on the web table 10 times and "Instant Win" is not
    And check "type" with "existing" string "SLOT" against "games" table records

  Scenario: Search by Categories
    Given the Game Management page is opened with user "valid.username3"
    When select "Premium" option from combo box "Categories"
    And click on "SEARCH" button
    Then a string "Premium" is contained on the web table 10 times and "" is not
    And check "categories" with "existing" string "PREMIUM" against "games" table records

  Scenario Outline: Search verification with combined parameters
    Given the Game Management page is opened with user "valid.username3"
    When enter in "GameName" field on Game Management page "<GameName>" text
    And select "<GameName>" option from "Game Name" drop down
    And select "<Provider>" option from combo box "Game Provider"
    And select "<Type>" option from combo box "Type"
    And click on "SEARCH" button
    Then a "<GameName>" is displayed on the web table <ManyTimes> times and "<notDisplayedElement>" is not

    Examples: 
      | GameName  | Provider  | Type        | notDisplayedElement | ManyTimes |
      | GameName1 | Provider1 |             | GameName10          |         1 |
      | GameName1 |           | Instant Win | GameName10          |         1 |
      | GameName3 | Provider3 | Slot        | GameName8           |         1 |
      | GameName3 | Provider3 | Instant Win | GameName6           |         0 |
      | GameName3 | Provider2 | Instant Win | GameName4           |         0 |
      | GameName2 | Provider3 | Instant Win | GameName4           |         0 |

  Scenario Outline: Search verification with combined parameters
    Given the Game Management page is opened with user "valid.username3"
    When enter in "GameName" field on Game Management page "<GameName>" text
    And select "<GameName>" option from "Game Name" drop down
    And select "<Provider>" option from combo box "Game Provider"
    And select "<Type>" option from combo box "Type"
    And click on "SEARCH" button
    Then a "<Provider>" is displayed on the web table <ManyTimes> times and "<notDisplayedElement>" is not

    Examples: 
      | GameName | Provider  | Type | notDisplayedElement | ManyTimes |
      |          | Provider3 | Slot | GameName2           |        10 |

  Scenario Outline: Search with similar Game's data
    Given the Game Management page is opened with user "valid.username3"
    When enter in "GameName" field on Game Management page "<GameName>" text
    And click on "SEARCH" button
    Then a string "<GameName>" is contained on the web table <ManyTimes> times and "" is not

    Examples: 
      | GameName  | ManyTimes |
      | GameName  |        20 |
      | GameName1 |        11 |

  Scenario: 'Clear Filters' verification on last Pagination Page
    Given the Game Management page is opened with user "valid.username3"
    When click on "lastPaginationPage" button
    And get displayed results on the web table
    And click on "ClearFilters" button
    Then compare current with the previous table results

  Scenario Outline: Verify Game info through Game Management page
    Given the Game Management page is opened with user "valid.username3"
    When enter in "GameID" field on Game Management page "<GameID>" text
    And click on "SEARCH" button
    And open game info with ID number "<GameID>" and name "<GameName>"
    Then check game info data filtered by "_id" and <GameID>

    Examples: 
      | GameID     | GameName  |
      | 1000000002 | GameName2 |

  Scenario Outline: Verify Provider info through Game Management page
    Given the Game Management page is opened with user "valid.username3"
    When enter in "GameID" field on Game Management page "<GameID>" text
    And click on "SEARCH" button
    And open provider info with ID number "<GameID>" and name "<ProviderName>"
    Then check provider info data filtered by "_id" and <GameID>

    Examples: 
      | GameID     | ProviderName |
      | 1000000002 | Provider2    |
