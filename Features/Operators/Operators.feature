@TestScenario @OperatorManagement
Feature: Operator Management
  Verify search functionality on All Operators page

  Scenario: Open Operator Management page
    Given the Homepage is opened with user "valid.username4"
    When click "Management" button on Home page
    And click "Operator Management" button on Home page
    Then a "All Operators" page is displayed

  Scenario Outline: Search by Operator Name
    Given the Operator Management page is opened with user "valid.username4"
    When enter in "OperatorName" field on Operator Management page "<OperatorName>" text
    And select "<OperatorName>" option from "Operator Name" drop down
    And click on "SEARCH" button
    Then a "<OperatorName>" is displayed on the web table 1 times and "<notDisplayedElement>" is not
    And check "name" with "whole" string "<OperatorName>" against "operators" table records

    Examples: 
      | OperatorName | notDisplayedElement |
      | Operator1    | Operator01          |
      | Operator2    | Operator1           |

  Scenario Outline: Search by Operator ID
    Given the Operator Management page is opened with user "valid.username4"
    When enter in "OperatorID" field on Operator Management page "<OperatorID>" text
    And clear "OperatorName" field on "Operators" page
    And click on "SEARCH" button
    Then a "<OperatorName>" is displayed on the web table 1 times and "<notDisplayedElement>" is not
    And check "_id" with "whole" string "<OperatorID>" against "operators" table records

    Examples: 
      | OperatorID | OperatorName | notDisplayedElement |
      | 1000000001 | Operator1    | Operator10          |
      | 1000000002 | Operator2    | Operator1           |

  Scenario Outline: Search by part of Operator Name
    Given the Operator Management page is opened with user "valid.username4"
    When click on "Order by Operator ID" button
    And enter in "OperatorID" field on Operator Management page "<OperatorID>" text
    And enter in "OperatorName" field on Operator Management page "<OperatorName>" text
    And click on "SEARCH" button
    Then an element "<OperatorName>" is displayed and "<notDisplayedElement>" is not displayed
    And a string "<OperatorName>" is contained on the web table 2 times and "" is not

    Examples: 
      | OperatorID | OperatorName | notDisplayedElement |
      | 1000000020 | Operator2    | Operator12          |

  Scenario Outline: Search by Operator Name on second page
    Given the Operator Management page is opened with user "valid.username4"
    When enter in "OperatorName" field on Operator Management page "<OperatorName>" text
    And clear "OperatorID" field on "Operators" page
    And click on "SEARCH" button
    And click on "nextPaginationPage" button
    Then an element "<OperatorName>" is displayed and "<notDisplayedElement>" is not displayed
    And check "name" with "part" string "<OperatorName>" against "operators" table records

    Examples: 
      | OperatorName | notDisplayedElement |
      | Operator     | Roler Gaming        |

  Scenario Outline: Search by Operator Name from second page
    Given the Operator Management page is opened with user "valid.username4"
    When click on "nextPaginationPage" button
    And enter in "OperatorName" field on Operator Management page "<OperatorName>" text
    And click on "SEARCH" button
    Then a "<OperatorName>" is displayed on the web table 1 times and "<notDisplayedElement>" is not
    And check "name" with "whole" string "<OperatorName>" against "operators" table records

    Examples: 
      | OperatorName | notDisplayedElement |
      | Operator2    | Nest sea            |

  Scenario Outline: Search after click on next page
    Given the Operator Management page is opened with user "valid.username4"
    When click on "Order by Operator ID" button
    And enter in "OperatorName" field on Operator Management page "<OperatorName>" text
    And clear "OperatorID" field on "Operators" page
    And get value of pagination counter
    And click on "nextPaginationPage" button
    Then a string "<OperatorName>" is contained on the web table 0 times and "" is not

    Examples: 
      | OperatorName |
      | Operator1    |

  Scenario: Search by Integration Type
    Given the Operator Management page is opened with user "valid.username4"
    When select "Direct" option from combo box "Integration Type"
    And click on "SEARCH" button
    Then a "Direct" is displayed on the web table 10 times and "Aggregator" is not
    And check "integration_type" with "existing" string "DIRECT" against "operators" table records

  Scenario: Search by Integration Platform
    Given the Operator Management page is opened with user "valid.username4"
    When select "Integration Platform 1" option from combo box "Integration Platform"
    And click on "SEARCH" button
    Then a "Integration Platform 1" is displayed on the web table 10 times and "NGL Platform" is not
    And check "integration_platform" with "existing" string "INTEGRATION_PLATFORM_1" against "operators" table records

  Scenario Outline: Search with combined Operator parameters
    Given the Operator Management page is opened with user "valid.username4"
    When enter in "OperatorName" field on Operator Management page "<OperatorName>" text
    And select "<Type>" option from combo box "Integration Type"
    And select "<Platform>" option from combo box "Integration Platform"
    And click on "SEARCH" button
    Then a "<OperatorName>" is displayed on the web table <ManyTimes> times and "<notDisplayedElement>" is not

    Examples: 
      | OperatorName | Platform               | Type       | notDisplayedElement | ManyTimes |
      | Operator1    |                        | Direct     | Operator10          |         1 |
      | Operator1    | Integration Platform 1 |            | Operator10          |         1 |
      | Operator1    | Integration Platform 1 | Direct     | Operator10          |         1 |
      | Operator1    | NGL Platform           | Direct     | Operator1           |         0 |
      | Operator11   | NGL Platform           | Direct     | Operator1           |         0 |
      | Operator2    | Integration Platform 1 | Aggregator | Operator4           |         0 |

  Scenario Outline: Search with similar Operator's data
    Given the Operator Management page is opened with user "valid.username4"
    When enter in "OperatorName" field on Operator Management page "<OperatorName>" text
    And click on "SEARCH" button
    Then a string "<OperatorName>" is contained on the web table <ManyTimes> times and "" is not

    Examples: 
      | OperatorName | ManyTimes |
      | Operator1    |        11 |

  Scenario Outline: Search with wrong Operator's data
    Given the Operator Management page is opened with user "valid.username4"
    When enter in "OperatorID" field on Operator Management page "<OperatorID>" text
    And enter in "OperatorName" field on Operator Management page "<OperatorName>" text
    And click on "SEARCH" button
    Then a string "<notDisplayedElement>" is contained on the web table 0 times and "" is not

    Examples: 
      | OperatorName | OperatorID | notDisplayedElement |
      | %            |            | Operator1           |
      | *            |            | Operator1           |
      | (            |            | Operator2           |
      | )            |            | Operator3           |
      | '#'          |            | Operator4           |

  Scenario: 'Clear Filters' verification on last Pagination Page
    Given the Operator Management page is opened with user "valid.username4"
    When click on "lastPaginationPage" button
    And get displayed results on the web table
    And click on "ClearFilters" button
    Then compare current with the previous table results

  Scenario Outline: Verify Operator info through Operator Management page
    Given the Operator Management page is opened with user "valid.username4"
    When enter in "OperatorID" field on Operator Management page "<OperatorID>" text
    And click on "SEARCH" button
    And open operator info with ID number "<OperatorID>" and name "<OperatorName>"
    Then check operator info data filtered by "_id" and <OperatorID>

    Examples: 
      | OperatorID | OperatorName |
      | 1000000001 | Operator1    |
