@TestScenario @OperatorAccount
Feature: Operator Group Account	- Game History
  Verify functionalities on Game History page with Operator Group Account

  Scenario: Operator Group Account - Open Game History page
    Given the Homepage is opened with user "valid.username.no.admin"
    When click "Game History" button on Home page
    Then a "Game History" page is displayed

  Scenario: Operator Group Account - Open Game History page directly by URL
    Given there is no logged in user
    And the Game History page is opened with user "loggedOutUser"
    When enter "valid.username.no.admin" and "valid.password"
    And click on "Login" button
    Then a "Game History" page is displayed
    And a string "+" is contained on the web table 10 times and "" is not

  Scenario Outline: Operator Group Account - Search by Operator Name
    Given there is no logged in user
    And the Game History page is opened with user "valid.username.no.admin"
    When enter in "OperatorName" field on History page "<OperatorName>" text
    And select "<OperatorName>" option from "Operator Name" drop down
    And click on "SEARCH" button
    Then a "<OperatorName>" is displayed on the web table 12 times and "<notDisplayedElement>" is not
    And check "operator_name" with "whole" string "<OperatorName>" against "game_history" table records

    Examples: 
      | OperatorName | notDisplayedElement |
      | Operator1    | Operator10          |

  Scenario Outline: Operator Group Account - Search by Operator Name
    Given the Game History page is opened with user "valid.username.no.admin"
    When enter in "OperatorName" field on History page "<OperatorName>" text
    And select "<OperatorName>" option from "Operator Name" drop down
    And click on "SEARCH" button
    Then a string "<OperatorName>" is contained on the web table 0 times and "" is not

    Examples: 
      | OperatorName |
      | Operator2    |

  Scenario Outline: Operator Group Account - Search by Operator ID
    Given the Game History page is opened with user "valid.username.no.admin"
    When enter in "OperatorID" field on History page "<OperatorID>" text
    And clear "OperatorName" field on "History" page
    And click on "SEARCH" button
    Then a "<OperatorName>" is displayed on the web table 12 times and "<notDisplayedElement>" is not
    And check "operator_id" with "notVisible" string "<OperatorID>" against "game_history" table records

    Examples: 
      | OperatorID | OperatorName | notDisplayedElement |
      | 1000000001 | Operator1    | Operator10          |

  Scenario Outline: Operator Group Account - Search by Operator ID
    Given the Game History page is opened with user "valid.username.no.admin"
    When enter in "OperatorID" field on History page "<OperatorID>" text
    And clear "OperatorName" field on "History" page
    And click on "SEARCH" button
    Then a string "<OperatorName>" is contained on the web table 0 times and "" is not

    Examples: 
      | OperatorID | OperatorName |
      | 1000000002 | Operator2    |

  Scenario Outline: Operator Group Account - Search by Game Name
    Given the Game History page is opened with user "valid.username.no.admin"
    When click on "Order by Game Play ID" button
    And enter in "GameName" field on History page "<GameName>" text
    And select "<GameName>" option from "Game Name" drop down
    And click on "SEARCH" button
    Then a "<GameName>" is displayed on the web table 12 times and "<notDisplayedElement>" is not
    And check "game_name" with "whole" string "<GameName>" against "game_history" table records

    Examples: 
      | GameName  | notDisplayedElement |
      | GameName1 | GameName10          |

  Scenario Outline: Operator Group Account - Search by Game Name
    Given the Game History page is opened with user "valid.username.no.admin"
    When click on "Order by Game Play ID" button
    And enter in "GameName" field on History page "<GameName>" text
    And select "<GameName>" option from "Game Name" drop down
    And click on "SEARCH" button
    Then a string "<GameName>" is contained on the web table 0 times and "" is not

    Examples: 
      | GameName  |
      | GameName2 |

  Scenario Outline: Operator Group Account - Search by Game ID
    Given the Game History page is opened with user "valid.username.no.admin"
    When click on "Order by Game Play ID" button
    And enter in "GameID" field on History page "<GameID>" text
    And clear "GameName" field on "History" page
    And click on "SEARCH" button
    Then a "<GameName>" is displayed on the web table 12 times and "<notDisplayedElement>" is not
    And check "game_id" with "notVisible" string "<GameID>" against "game_history" table records

    Examples: 
      | GameID     | GameName  | notDisplayedElement |
      | 1000000001 | GameName1 | GameName10          |

  Scenario Outline: Operator Group Account - Search by Game ID
    Given the Game History page is opened with user "valid.username.no.admin"
    When click on "Order by Game Play ID" button
    And enter in "GameID" field on History page "<GameID>" text
    And clear "GameName" field on "History" page
    And click on "SEARCH" button
    Then a string "<GameName>" is contained on the web table 0 times and "" is not

    Examples: 
      | GameID     | GameName  |
      | 1000000002 | GameName2 |

  Scenario Outline: Operator Group Account - Search by Player ID
    Given the Game History page is opened with user "valid.username.no.admin"
    When click on "Order by Game Play ID" button
    And enter in "PlayerID" field on History page "<PlayerID>" text
    And click on "SEARCH" button
    Then a "<PlayerID>" is displayed on the web table 12 times and "<notDisplayedElement>" is not
    And check "player_id" with "whole" string "<PlayerID>" against "game_history" table records

    Examples: 
      | PlayerID | notDisplayedElement |
      | Player1  | Player2             |

  Scenario Outline: Operator Group Account - Search by Player ID
    Given the Game History page is opened with user "valid.username.no.admin"
    When click on "Order by Game Play ID" button
    And enter in "PlayerID" field on History page "<PlayerID>" text
    And click on "SEARCH" button
    Then a string "<PlayerID>" is contained on the web table 0 times and "" is not

    Examples: 
      | PlayerID | notDisplayedElement |
      | Player2  | Player1             |

  Scenario Outline: Operator Group Account - Search by Status
    Given the Game History page is opened with user "valid.username.no.admin"
    When click on "Order by Game Play ID" button
    And select "<Status>" option from combo box "Status"
    And click on "SEARCH" button
    Then a "<Status>" is displayed on the web table <ManyTimes> times and "<notDisplayedElement>" is not

    Examples: 
      | Status    | notDisplayedElement | ManyTimes |
      | OPEN      | CLOSED              |         3 |
      | CLOSED    | CANCELLED           |         6 |
      | CANCELLED | OPEN                |         3 |

  Scenario Outline: Operator Group Account - Search by date
    Given the Game History page is opened with user "valid.username.no.admin"
    When click on "Order by Game Play ID" button
    And enter in "dateFrom" field on History page "<dateFrom>" text
    And enter in "dateTo" field on History page "<dateTo>" text
    And enter in "GameName" field on History page "GameName" text
    And press 1 times the "Enter" key on the keyboard
    And click on "SEARCH" button
    Then a "GameName1" is displayed on the web table <ManyTimes> times and "Wild Wheels" is not

    Examples: 
      | dateFrom                      | dateTo | ManyTimes |
      | lastInputTestDateMinuteMinus1 |        |        10 |

  Scenario Outline: Operator Group Account - Search by date
    Given the Game History page is opened with user "valid.username.no.admin"
    When click on "Order by Game Play ID" button
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

  Scenario Outline: Operator Group Account - Search by Game Name on second page
    Given the Game History page is opened with user "valid.username.no.admin"
    When enter in "GameName" field on History page "<GameName>" text
    And clear "GameID" field on "History" page
    And click on "SEARCH" button
    And click on "nextPaginationPage" button
    And click on "Order by Game Play ID" button
    Then an element "<GameName>" is displayed and "<notDisplayedElement>" is not displayed

    Examples: 
      | GameName  | notDisplayedElement |
      | GameName1 | GameName14          |

  Scenario Outline: Operator Group Account - Search by Game Name from second page
    Given the Game History page is opened with user "valid.username.no.admin"
    When click on "Order by Game Play ID" button
    And click on "lastPaginationPage" button
    And enter in "GameName" field on History page "<GameName>" text
    And click on "SEARCH" button
    Then a "<GameName>" is displayed on the web table 12 times and "<notDisplayedElement>" is not
    And check "game_name" with "whole" string "<GameName>" against "game_history" table records

    Examples: 
      | GameName  | notDisplayedElement |
      | GameName1 | GameName2           |

  Scenario Outline: Operator Group Account - Search with combined parameters
    Given the Game History page is opened with user "valid.username.no.admin"
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
      | Operator1    | GameName1 | Player1  | OPEN      | lastInputTestDateMinuteMinus1 | lastInputTestDateMinutePlus6 | GameName10          |         1 |

  Scenario Outline: Operator Group Account - Search with wrong data on all fields except date
    Given the Game History page is opened with user "valid.username.no.admin"
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

  Scenario: Operator Group Account - Test Clear Filters function from last Pagination Page
    Given the Game History page is opened with user "valid.username.no.admin"
    When click on "lastPaginationPage" button
    And get displayed results on the web table
    And click on "ClearFilters" button
    Then compare current with the previous table results

  Scenario Outline: Operator Group Account - Verify Game Round info
    Given the Game History page is opened with user "valid.username.no.admin"
    When click on "Order by Game Play ID" button
    And enter in "GameID" field on History page "<GameID>" text
    And click on "SEARCH" button
    And open game round info with ID number "<GameID>"
    Then check game round info data filtered by "_id" and <GameID>

    Examples: 
      | GameID     |
      | 1000000001 |

  Scenario Outline: Operator Group Account - Verify Game info
    Given the Game History page is opened with user "valid.username.no.admin"
    When enter in "GameID" field on History page "<GameID>" text
    And click on "SEARCH" button
    And open game info with ID number "<GameID>" and name "<GameName>"
    Then check game info data filtered by "_id" and <GameID>

    Examples: 
      | GameID     | GameName  |
      | 1000000001 | GameName1 |

  Scenario Outline: Operator Group Account - Verify Operator info
    Given the Game History page is opened with user "valid.username.no.admin"
    When enter in "GameID" field on History page "<GameID>" text
    And click on "SEARCH" button
    And open operator info with ID number "<GameID>" and name "<OperatorName>"
    Then check operator info data filtered by "_id" and <GameID>

    Examples: 
      | GameID     | OperatorName |
      | 1000000001 | Operator1    |

  Scenario Outline: Operator Group Account - Verify Provider info
    Given the Game History page is opened with user "valid.username.no.admin"
    When enter in "GameID" field on History page "<GameID>" text
    And click on "SEARCH" button
    And open provider info with ID number "<GameID>" and name "<ProviderName>"
    Then check provider info data filtered by "_id" and <GameID>

    Examples: 
      | GameID     | ProviderName |
      | 1000000001 | Provider1    |
