@TestScenario @Logs
Feature: Logs Page
  Verify search functionality on Logs page

  Scenario: Open Logs page
    Given the Homepage is opened with user "valid.username16"
    When click "Logs" button on Home page
    Then a "Logs" page is displayed

  Scenario: Open Logs page directly by URL
    Given there is no logged in user
    And the Logs page is opened with user "loggedOutUser"
    When enter "valid.username6" and "valid.password"
    And click on "Login" button
    Then a "Logs" page is displayed
    And an element "Action" is displayed and "Enabled" is not displayed

  Scenario Outline: Search by User Name
    Given there is no logged in user
    And the Logs page is opened with user "valid.username6"
    When enter in "UserName" field on Logs page "<UserName>" text
    And select "<UserName>" option from "User Name" drop down
    And click on "SEARCH" button
    Then a "<UserName>" is displayed on the web table 10 times and "<notDisplayedElement>" is not

    Examples: 
      | UserName  | notDisplayedElement |
      | username1 | username01          |
      | username2 | username1           |

  Scenario Outline: Search by User ID
    Given the Logs page is opened with user "valid.username6"
    When enter in "UserID" field on Logs page "<UserID>" text
    And clear "UserName" field on "Users" page
    And click on "SEARCH" button
    Then a "<UserName>" is displayed on the web table 10 times and "<notDisplayedElement>" is not
    And check "change_user_id" with "whole" string "<UserID>" against "logs" table records

    Examples: 
      | UserID     | UserName  | notDisplayedElement |
      | 1000000001 | username1 | username10          |
      | 1000000002 | username2 | username1           |

  Scenario Outline: Search by part of User Name
    Given the Logs page is opened with user "valid.username6"
    When enter in "UserName" field on Logs page "<UserName>" text
    And click on "SEARCH" button
    Then a string "<UserName>" is contained on the web table 10 times and "admin" is not

    Examples: 
      | UserName |
      | username |
      | usern    |

  Scenario Outline: Search by User Name on second page
    Given the Logs page is opened with user "valid.username6"
    When enter in "UserName" field on Logs page "<UserName>" text
    And clear "UserID" field on "Users" page
    And click on "SEARCH" button
    And click on "nextPaginationPage" button
    Then a string "<UserName>" is contained on the web table 10 times and "<notDisplayedElement>" is not

    Examples: 
      | UserName | notDisplayedElement |
      | user     | admin               |

  Scenario Outline: Search by User Name from second page
    Given the Logs page is opened with user "valid.username6"
    When click on "nextPaginationPage" button
    And enter in "UserName" field on Logs page "<UserName>" text
    And click on "SEARCH" button
    Then a "<UserName>" is displayed on the web table 10 times and "<notDisplayedElement>" is not

    Examples: 
      | UserName  | notDisplayedElement |
      | username2 | admin               |

  Scenario Outline: Search after click on next page
    Given the Logs page is opened with user "valid.username6"
    When click on "Order by Username" button
    And enter in "UserName" field on Logs page "<UserName>" text
    And get value of pagination counter
    And click on "lastPaginationPage" button
    Then a string "<UserName>" is contained on the web table 0 times and "" is not

    Examples: 
      | UserName  |
      | username2 |

  Scenario Outline: Search by Element ID
    Given the Logs page is opened with user "valid.username6"
    When enter in "ElementID" field on Logs page "<ElementID>" text
    And click on "SEARCH" button
    Then a "<ElementID>" is displayed on the web table <ManyTimes> times and "<notDisplayedElement>" is not
    And check "element_id" with "whole" string "<ElementID>" against "logs" table records

    Examples: 
      | ElementID | notDisplayedElement | ManyTimes |
      |        80 |                  79 |         4 |
      |        81 |                  80 |         7 |

  Scenario Outline: Search by Collection
    Given the Logs page is opened with user "valid.username6"
    When select "<Collection>" option from combo box "Collection"
    And click on "SEARCH" button
    Then a "<Collection>" is displayed on the web table 10 times and "<notDisplayedElement>" is not
    And check "collection" with "underline" string "<Collection>" against "logs" table records

    Examples: 
      | Collection         | notDisplayedElement |
      | Games              | Games Per Operator  |
      | Operators          | Providers           |
      | Providers          | Users               |
      | Users              | Operators           |
      | Games Per Operator | Games               |

  Scenario Outline: Search by date
    Given the Logs page is opened with user "valid.username6"
    When enter in "dateFrom" field on Logs page "<dateFrom>" text
    And enter in "dateTo" field on Logs page "<dateTo>" text
    And enter in "UserName" field on Logs page "Username" text
    And press 1 times the "Enter" key on the keyboard
    And click on "SEARCH" button
    Then a string "username" is contained on the web table <ManyTimes> times and "admin" is not

    Examples: 
      | dateFrom                      | dateTo | ManyTimes |
      | lastInputTestDateMinuteMinus1 |        |        10 |

  Scenario Outline: Search by date
    Given the Logs page is opened with user "valid.username6"
    When enter in "dateFrom" field on Logs page "<dateFrom>" text
    And enter in "dateTo" field on Logs page "<dateTo>" text
    And enter in "UserName" field on Logs page "username1" text
    And press 1 times the "Enter" key on the keyboard
    And click on "SEARCH" button
    Then a "username1" is displayed on the web table <ManyTimes> times and "admin" is not

    Examples: 
      | dateFrom                      | dateTo                        | ManyTimes |
      |                               | lastInputTestDateMinutePlus1  |         2 |
      | lastInputTestDateMinuteMinus1 | lastInputTestDateMinutePlus20 |         6 |

  Scenario Outline: Search with combined Log parameters
    Given the Logs page is opened with user "valid.username6"
    When click on "Order by Date & Time" button
    And enter in "dateFrom" field on Logs page "<dateFrom>" text
    And enter in "dateTo" field on Logs page "<dateTo>" text
    And enter in "UserName" field on Logs page "Username" text
    And select "<UserName>" option from "User Name" drop down
    And enter in "ElementID" field on Logs page "<ElementID>" text
    And select "<Collection>" option from combo box "Collection"
    And click on "SEARCH" button
    Then a "<UserName>" is displayed on the web table <ManyTimes> times and "<notDisplayedElement>" is not

    Examples: 
      | UserName  | ElementID | Collection | dateFrom                      | dateTo                        | notDisplayedElement | ManyTimes |
      | username1 |        80 | Any        |                               |                               | admin               |         4 |
      | username1 |        80 | Games      |                               |                               | admin               |         1 |
      | username1 |           | Operators  |                               |                               | admin               |        10 |
      | username1 |           | Providers  |                               |                               | admin               |        10 |
      | username1 |        80 | Users      |                               |                               | admin               |         0 |
      | username1 |        80 | Any        |                               | lastInputTestDateMinutePlus4  | admin               |         1 |
      | username1 |           | Any        | lastInputTestDateMinutePlus1  | lastInputTestDateMinutePlus11 | admin               |         2 |
      | username1 |        80 | Any        | lastInputTestDateMinutePlus4  | lastInputTestDateDayPlus2     | admin               |         3 |
      | username1 |        80 | Games      | lastInputTestDateMinuteMinus1 | lastInputTestDateMinutePlus2  | admin               |         1 |

  Scenario Outline: Search with wrong data on all fields except date
    Given the Logs page is opened with user "valid.username6"
    When enter in "UserName" field on Logs page "Username" text
    And select "<UserName>" option from "User Name" drop down
    And enter in "ElementID" field on Logs page "<ElementID>" text
    And select "<Collection>" option from combo box "Collection"
    And click on "SEARCH" button
    Then a "<UserName>" is displayed on the web table 0 times and "<UserName>" is not

    Examples: 
      | UserName  | Collection | ElementID  |
      | username2 | Users      | 1000000001 |
      | username1 | Games      | 1000000001 |
      | username1 | Users      |         56 |

  Scenario: Test Clear Filters function from last Pagination Page
    Given the Logs page is opened with user "valid.username6"
    When click on "lastPaginationPage" button
    And get displayed results on the web table
    And click on "ClearFilters" button
    Then compare current with the previous table results
