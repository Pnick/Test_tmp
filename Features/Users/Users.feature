@TestScenario @Users
Feature: Users
  Verify search functionality on Users page

  Scenario: Open Users page
    Given the Homepage is opened with user "valid.username1"
    When click "Users" button on Home page
    Then a "Users" page is displayed

  Scenario Outline: Search by User Name
    Given the Users page is opened with user "valid.username1"
    When enter in "UserName" field on Users page "<UserName>" text
    And select "<UserName>" option from "User Name" drop down
    And click on "SEARCH" button
    Then a "<UserName>" is displayed on the web table 1 times and "<notDisplayedElement>" is not
    And check "user_name" with "whole" string "<UserName>" against "users" table records

    Examples: 
      | UserName  | notDisplayedElement |
      | username1 | username01          |
      | username3 | username1           |

  Scenario Outline: Search by User ID
    Given the Users page is opened with user "valid.username1"
    When enter in "UserID" field on Users page "<UserID>" text
    And clear "UserName" field on "Users" page
    And click on "SEARCH" button
    Then a "<UserName>" is displayed on the web table 1 times and "<notDisplayedElement>" is not
    And check "_id" with "whole" string "<UserID>" against "users" table records

    Examples: 
      | UserID     | UserName  | notDisplayedElement |
      | 1000000001 | username1 | username10          |
      | 1000000003 | username3 | username1           |

  Scenario Outline: Search by part of User Name
    Given the Users page is opened with user "valid.username1"
    When enter in "UserID" field on Users page "<UserID>" text
    And enter in "UserName" field on Users page "<UserName>" text
    And click on "SEARCH" button
    Then a string "<UserName>" is contained on the web table 10 times and "" is not

    Examples: 
      | UserID     | UserName |
      | 1000000010 | username |
      | 1000000001 | usern    |

  Scenario Outline: Search by User Name on second page
    Given the Users page is opened with user "valid.username1"
    When enter in "UserName" field on Users page "<UserName>" text
    And clear "UserID" field on "Users" page
    And click on "SEARCH" button
    And click on "nextPaginationPage" button
    Then check "user_name" with "part" string "<UserName>" against "users" table records

    Examples: 
      | UserName | notDisplayedElement |
      | user     | admin               |

  Scenario Outline: Search by User Name from second page
    Given the Users page is opened with user "valid.username1"
    When click on "nextPaginationPage" button
    And enter in "UserName" field on Users page "<UserName>" text
    And click on "SEARCH" button
    Then a "<UserName>" is displayed on the web table 1 times and "<notDisplayedElement>" is not
    And check "user_name" with "whole" string "<UserName>" against "users" table records

    Examples: 
      | UserName  | notDisplayedElement |
      | username3 | admin               |

  Scenario Outline: Search after click on next page
    Given the Users page is opened with user "valid.username1"
    When enter in "UserName" field on Users page "<UserName>" text
    And clear "Company" field on "Users" page
    And get value of pagination counter
    And click on "lastPaginationPage" button
    Then a string "<UserName>" is contained on the web table 0 times and "" is not

    Examples: 
      | UserName  |
      | username2 |

  Scenario Outline: Search by Enabled
    Given the Users page is opened with user "valid.username1"
    When select "<Enabled>" option from combo box "Enabled"
    And click on "SEARCH" button
    Then a "<Enabled>" is displayed on the web table <ManyTimes> times and "<notDisplayedElement>" is not
    And check "is_enabled" with "whole" string "<valueDB>" against "users" table records

    Examples: 
      | Enabled | valueDB | notDisplayedElement | ManyTimes |
      | Yes     | true    | No                  |        10 |
      | No      | false   | Yes                 |        10 |

  Scenario Outline: Search with combined User parameters
    Given the Users page is opened with user "valid.username1"
    When enter in "UserName" field on Users page "<UserName>" text
    And select "<Enabled>" option from combo box "Enabled"
    And click on "SEARCH" button
    Then a "<UserName>" is displayed on the web table <ManyTimes> times and "<notDisplayedElement>" is not

    Examples: 
      | UserName  | Enabled | notDisplayedElement | ManyTimes |
      | username1 | Yes     | username3           |         1 |
      | username2 | No      | username2           |         0 |

  Scenario Outline: Search with similar User's data
    Given the Users page is opened with user "valid.username1"
    When enter in "UserName" field on Users page "<UserName>" text
    And click on "SEARCH" button
    Then a string "<UserName>" is contained on the web table 2 times and "" is not

    Examples: 
      | UserName  |
      | username2 |

  Scenario: 'Clear Filters' verification on last Pagination Page
    Given the Users page is opened with user "valid.username1"
    When click on "lastPaginationPage" button
    And get displayed results on the web table
    And click on "ClearFilters" button
    Then compare current with the previous table results

  Scenario Outline: Verify User info through Users page
    Given the Users page is opened with user "valid.username1"
    When enter in "UserID" field on Users page "<UserID>" text
    And click on "SEARCH" button
    And open user info with ID number "<UserID>" and name "<UserName>"
    Then check user info data filtered by "_id" and <UserID>

    Examples: 
      | UserID     | UserName  |
      | 1000000002 | username2 |
