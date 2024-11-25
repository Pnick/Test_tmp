@TestScenario @GameHistory
Feature: Game History
  Verify search functionality on Game History page

  Scenario: Open Game History page
    Given the Homepage is opened with user "valid.username7"
    When click "Game History" button on Home page
    Then a "Game History" page is displayed

  Scenario: Verify that the Game History page can be opened directly by URL
    Given there is no logged in user
    And the Game History page is opened with user "loggedOutUser"
    When enter "valid.username17" and "valid.password"
    And click on "Login" button
    Then a "Game History" page is displayed
    And a string "+" is contained on the web table 10 times and "" is not

  Scenario Outline: Search by Operator Name
  	Given there is no logged in user
    And the Game History page is opened with user "valid.username7"
    When enter in "OperatorName" field on History page "<OperatorName>" text
    And select "<OperatorName>" option from "Operator Name" drop down
    And click on "SEARCH" button
    Then a "<OperatorName>" is displayed on the web table 12 times and "<notDisplayedElement>" is not
    And check "operator_name" with "whole" string "<OperatorName>" against "game_history" table records

    Examples: 
      | OperatorName | notDisplayedElement |
      | Operator1    | Operator10          |
      | Operator2    | Operator1           |

  Scenario Outline: Search by Operator ID
    Given the Game History page is opened with user "valid.username7"
    When enter in "OperatorID" field on History page "<OperatorID>" text
    And clear "OperatorName" field on "History" page
    And click on "SEARCH" button
    Then a "<OperatorName>" is displayed on the web table 12 times and "<notDisplayedElement>" is not
    And check "operator_id" with "notVisible" string "<OperatorID>" against "game_history" table records

    Examples: 
      | OperatorID | OperatorName | notDisplayedElement |
      | 1000000001 | Operator1    | Operator10          |
      | 1000000002 | Operator2    | Operator1           |

  Scenario Outline: Search by part of Operator Name
    Given the Game History page is opened with user "valid.username7"
    When enter in "OperatorID" field on History page "<OperatorID>" text
    And enter in "OperatorName" field on History page "<OperatorName>" text
    And click on "SEARCH" button
    Then an element "<OperatorName>" is displayed and "<notDisplayedElement>" is not displayed

    Examples: 
      | OperatorID | OperatorName | notDisplayedElement |
      | 1000000010 | Operator1    | Operator3           |
      | 1000000011 | Operator1    | Operator7           |

  Scenario Outline: Search by Game Name
    Given the Game History page is opened with user "valid.username7"
    When click on "Order by Game Play ID" button
    And enter in "GameName" field on History page "<GameName>" text
    And select "<GameName>" option from "Game Name" drop down
    And click on "SEARCH" button
    Then a "<GameName>" is displayed on the web table 12 times and "<notDisplayedElement>" is not
    And check "game_name" with "whole" string "<GameName>" against "game_history" table records

    Examples: 
      | GameName  | notDisplayedElement |
      | GameName1 | GameName10          |
      | GameName2 | GameName1           |

  Scenario Outline: Search by Game ID
    Given the Game History page is opened with user "valid.username7"
    When click on "Order by Game Play ID" button
    And enter in "GameID" field on History page "<GameID>" text
    And clear "GameName" field on "History" page
    And click on "SEARCH" button
    Then a "<GameName>" is displayed on the web table 12 times and "<notDisplayedElement>" is not
    And check "game_id" with "notVisible" string "<GameID>" against "game_history" table records

    Examples: 
      | GameID     | GameName  | notDisplayedElement |
      | 1000000001 | GameName1 | GameName10          |
      | 1000000002 | GameName2 | GameName1           |

  Scenario Outline: Search by part of Game Name
    Given the Game History page is opened with user "valid.username7"
    When click on "Order by Game Play ID" button
    And enter in "GameID" field on History page "<GameID>" text
    And enter in "GameName" field on History page "Game" text
    And click on "SEARCH" button
    Then a "<GameName>" is displayed on the web table 1 times and "<notDisplayedElement>" is not

    Examples: 
      | GameID     | GameName  | notDisplayedElement |
      | 1000000001 | GameName1 | GameName11          |
      | 1000000012 | GameName2 | GameName12          |

  Scenario Outline: Search with similar Game Names
    Given the Game History page is opened with user "valid.username7"
    When enter in "GameName" field on History page "<GameName>" text
    And click on "SEARCH" button
    Then a string "<GameName>" is contained on the web table <ManyTimes> times and "" is not

    Examples: 
      | GameName  | ManyTimes |
      | GameName  |       144 |
      | GameName1 |        48 |

  Scenario Outline: Search by Player ID
    Given the Game History page is opened with user "valid.username7"
    When click on "Order by Game Play ID" button
    And enter in "PlayerID" field on History page "<PlayerID>" text
    And click on "SEARCH" button
    Then a "<PlayerID>" is displayed on the web table 12 times and "<notDisplayedElement>" is not
    And check "player_id" with "whole" string "<PlayerID>" against "game_history" table records

    Examples: 
      | PlayerID | notDisplayedElement |
      | Player1  | Player2             |
      | Player2  | Player1             |

  Scenario Outline: Search by Status
    Given the Game History page is opened with user "valid.username7"
    When click on "Order by Game Play ID" button
    And select "<Status>" option from combo box "Status"
    And click on "SEARCH" button
    Then a "<Status>" is displayed on the web table 10 times and "<notDisplayedElement>" is not
    And check "status" with "part" string "<Status>" against "game_history" table records

    Examples: 
      | Status    | notDisplayedElement |
      | OPEN      | CLOSED              |
      | CLOSED    | CANCELLED           |
      | CANCELLED | OPEN                |

  Scenario Outline: Search by date
    Given the Game History page is opened with user "valid.username7"
    When click on "Order by Game" button
    And enter in "dateFrom" field on History page "<dateFrom>" text
    And enter in "dateTo" field on History page "<dateTo>" text
    And enter in "GameName" field on History page "GameName" text
    And press 1 times the "Enter" key on the keyboard
    And click on "SEARCH" button
    Then a string "GameName" is contained on the web table <ManyTimes> times and "Wild Wheels" is not

    Examples: 
      | dateFrom                      | dateTo | ManyTimes |
      | lastInputTestDateMinuteMinus1 |        |         10|

  Scenario Outline: Search by date
    Given the Game History page is opened with user "valid.username7"
    When click on "Order by Game" button
    And enter in "dateFrom" field on History page "<dateFrom>" text
    And enter in "dateTo" field on History page "<dateTo>" text
    And enter in "GameName" field on History page "GameName" text
    And press 1 times the "Enter" key on the keyboard
    And click on "SEARCH" button
    Then a "GameName1" is displayed on the web table <ManyTimes> times and "Wild Wheels" is not

    Examples: 
      | dateFrom                   | dateTo                       | ManyTimes |
      |                            | lastInputTestDateMinutePlus1 |         6 |
      | lastInputTestDateDayMinus1 | lastInputTestDateMinutePlus1 |         6 |

  Scenario Outline: Search by Game Name on second page
    Given the Game History page is opened with user "valid.username7"
    When enter in "GameName" field on History page "<GameName>" text
    And clear "GameID" field on "History" page
    And click on "SEARCH" button
    And click on "nextPaginationPage" button
    And click on "Order by Game Play ID" button
    Then an element "<GameName>" is displayed and "<notDisplayedElement>" is not displayed
    And check "game_name" with "part" string "<GameName>" against "game_history" table records

    Examples: 
      | GameName  | notDisplayedElement |
      | GameName1 | GameName14          |

  Scenario Outline: Search by Game Name from second page
    Given the Game History page is opened with user "valid.username7"
    When click on "Order by Game Play ID" button
    And click on "lastPaginationPage" button
    And enter in "GameName" field on History page "<GameName>" text
    And click on "SEARCH" button
    Then a "<GameName>" is displayed on the web table 12 times and "<notDisplayedElement>" is not
    And check "game_name" with "whole" string "<GameName>" against "game_history" table records

    Examples: 
      | GameName  | notDisplayedElement |
      | GameName2 | GameName12          |

  Scenario Outline: Search after click on next page
    Given the Game History page is opened with user "valid.username7"
    When click on "Order by Game" button
    And click on "Order by Game" button
    And enter in "GameName" field on History page "<GameName>" text
    And clear "GameID" field on "History" page
    And get value of pagination counter
    And click on "lastPaginationPage" button
    Then a string "<GameName>" is contained on the web table 0 times and "" is not

    Examples: 
      | GameName  |
      | GameName2 |

  Scenario Outline: Search verification with combined parameters
    Given the Game History page is opened with user "valid.username7"
    When click on "Order by Game Play ID" button
    And enter in "dateFrom" field on History page "<dateFrom>" text
    And enter in "dateTo" field on History page "<dateTo>" text
    And enter in "OperatorName" field on History page "<OperatorName>" text
    And select "<OperatorName>" option from "Operator Name" drop down
    And enter in "GameName" field on History page "<GameName>" text
    And select "<GameName>" option from "Game Name" drop down
    And enter in "PlayerID" field on History page "<PlayerID>" text
    And select "<Status>" option from combo box "Status"
    And click on "SEARCH" button
    Then a "<GameName>" is displayed on the web table <ManyTimes> times and "<notDisplayedElement>" is not

    Examples: 
      | OperatorName | GameName  | PlayerID | Status    | dateFrom                      | dateTo                       | notDisplayedElement | ManyTimes |
      | Operator1    | GameName1 | Player1  | Any       |                               |                              | GameName10          |        12 |
      |              | GameName1 | Player1  | OPEN      |                               |                              | GameName10          |         3 |
      | Operator1    | GameName1 |          | CLOSED    |                               |                              | GameName10          |         5 |
      |              | GameName1 |          | CANCELLED |                               |                              | GameName10          |         4 |
      | Operator1    | GameName1 | Player1  | CLOSED    |                               |                              | GameName10          |         5 |
      |              | GameName1 | Player1  | Any       |                               | lastInputTestDateMinutePlus4 | GameName10          |         8 |
      |              | GameName1 |          | Any       | lastInputTestDateMinutePlus1  | lastInputTestDateMinutePlus4 | GameName10          |         2 |
      | Operator1    | GameName1 | Player1  | Any       | lastInputTestDateMinutePlus4  | lastInputTestDateMinutePlus6 | GameName10          |         4 |
      | Operator1    | GameName1 | Player1  | OPEN      | lastInputTestDateMinuteMinus1 | lastInputTestDateMinutePlus2 | GameName10          |         3 |

  Scenario Outline: Search with wrong data on all fields except date
    Given the Game History page is opened with user "valid.username7"
    When click on "Order by Game Play ID" button
    And enter in "OperatorName" field on History page "<OperatorName>" text
    And select "<OperatorName>" option from "Operator Name" drop down
    And enter in "GameName" field on History page "<GameName>" text
    And select "<GameName>" option from "Game Name" drop down
    And enter in "PlayerID" field on History page "<PlayerID>" text
    And click on "SEARCH" button
    Then a "<GameName>" is displayed on the web table 0 times and "<GameName>" is not

    Examples: 
      | OperatorName | GameName  | PlayerID |
      | Operator2    | GameName1 | Player1  |
      | Operator1    | GameName2 | Player1  |
      | Operator1    | GameName1 | Player10 |

  Scenario: 'Clear Filters' verification on last Pagination Page
    Given the Game History page is opened with user "valid.username7"
    When click on "lastPaginationPage" button
    And get displayed results on the web table
    And click on "ClearFilters" button
    Then compare current with the previous table results

  Scenario Outline: Verify Game Round info
    Given the Game History page is opened with user "valid.username7"
    When enter in "GameID" field on History page "<GameNameID>" text
    And click on "SEARCH" button
    And open game round info with ID number "<GameID>"
    Then check game round info data filtered by "_id" and <GameID>

    Examples: 
      |GameNameID| GameID     |
      |1000000001| 1000000037 |
      |1000000001| 1000000097 |

  Scenario Outline: Verify Game info
    Given the Game History page is opened with user "valid.username7"
    When enter in "GameID" field on History page "<GameID>" text
    And click on "SEARCH" button
    And open game info with ID number "<GameID>" and name "<GameName>"
    Then check game info data filtered by "_id" and <GameID>

    Examples: 
      | GameID     | GameName  |
      | 1000000001 | GameName1 |

  Scenario Outline: Verify Operator info
    Given the Game History page is opened with user "valid.username7"
    When enter in "GameID" field on History page "<GameID>" text
    And click on "SEARCH" button
    And open operator info with ID number "<GameID>" and name "<OperatorName>"
    Then check operator info data filtered by "_id" and <GameID>

    Examples: 
      | GameID     | OperatorName |
      | 1000000001 | Operator1    |

  Scenario Outline: Verify Provider info
    Given the Game History page is opened with user "valid.username7"
    When enter in "GameID" field on History page "<GameID>" text
    And click on "SEARCH" button
    And open provider info with ID number "<GameID>" and name "<ProviderName>"
    Then check provider info data filtered by "_id" and <GameID>

    Examples: 
      | GameID     | ProviderName |
      | 1000000001 | Provider1    |
