@TestScenario @OperatorAccount
Feature: Operator Group Account	- Operator Management
  Verify functionalities on Operator Management page with Operator Group Account

  Scenario Outline: Operator Group Account - Search by Operator Name on Operator Management page
    Given the Operator Management page is opened with user "valid.username.no.admin"
    When enter in "OperatorName" field on Operator Management page "<OperatorName>" text
    And select "<OperatorName>" option from "Operator Name" drop down
    And click on "SEARCH" button
    Then a "<OperatorName>" is displayed on the web table 1 times and "<notDisplayedElement>" is not
    And check "name" with "whole" string "<OperatorName>" against "operators" table records

    Examples: 
      | OperatorName | notDisplayedElement |
      | Operator1    | Operator01          |

  Scenario Outline: Operator Group Account - Search by Operator Name on Operator Management page
    Given the Operator Management page is opened with user "valid.username.no.admin"
    When enter in "OperatorName" field on Operator Management page "<OperatorName>" text
    And select "<OperatorName>" option from "Operator Name" drop down
    And click on "SEARCH" button
    Then a string "<OperatorName>" is contained on the web table 0 times and "" is not

    Examples: 
      | OperatorName |
      | Operator2    |

  Scenario Outline: Operator Group Account - Search by Operator ID on Operator Management page
    Given the Operator Management page is opened with user "valid.username.no.admin"
    When enter in "OperatorID" field on Operator Management page "<OperatorID>" text
    And clear "OperatorName" field on "Operators" page
    And click on "SEARCH" button
    Then a "<OperatorName>" is displayed on the web table 1 times and "<notDisplayedElement>" is not
    And check "_id" with "whole" string "<OperatorID>" against "operators" table records

    Examples: 
      | OperatorID | OperatorName | notDisplayedElement |
      | 1000000001 | Operator1    | Operator10          |

  Scenario Outline: Operator Group Account - Search by Operator ID on Operator Management page
    Given the Operator Management page is opened with user "valid.username.no.admin"
    When enter in "OperatorID" field on Operator Management page "<OperatorID>" text
    And clear "OperatorName" field on "Operators" page
    And click on "SEARCH" button
    Then a string "<OperatorName>" is contained on the web table 0 times and "" is not

    Examples: 
      | OperatorID | OperatorName |
      | 1000000002 | Operator2    |

  Scenario Outline: Operator Group Account - Search by Operator ID and part of Operator Name on Operator Management page
    Given the Operator Management page is opened with user "valid.username.no.admin"
    When enter in "OperatorID" field on Operator Management page "<OperatorID>" text
    And enter in "OperatorName" field on Operator Management page "Operator" text
    And click on "SEARCH" button
    Then an element "<OperatorName>" is displayed and "<notDisplayedElement>" is not displayed
    And check "_id" with "whole" string "<OperatorID>" against "operators" table records

    Examples: 
      | OperatorID | OperatorName | notDisplayedElement |
      | 1000000001 | Operator1    | Operator10          |

  Scenario: Operator Group Account - Search by Integration Type on Operator Management page
    Given the Operator Management page is opened with user "valid.username.no.admin"
    When select "Direct" option from combo box "Integration Type"
    And click on "SEARCH" button
    Then a "Direct" is displayed on the web table 1 times and "Aggregator" is not

  Scenario: Operator Group Account - Search by Integration Platform on Operator Management page
    Given the Operator Management page is opened with user "valid.username.no.admin"
    When select "Integration Platform 1" option from combo box "Integration Platform"
    And click on "SEARCH" button
    Then a "Integration Platform 1" is displayed on the web table 1 times and "NGL Platform" is not

  Scenario Outline: Operator Group Account - Search with wrong Operator's data on Operator Management page
    Given the Operator Management page is opened with user "valid.username.no.admin"
    When enter in "OperatorID" field on Operator Management page "<OperatorID>" text
    And enter in "OperatorName" field on Operator Management page "<OperatorName>" text
    And click on "SEARCH" button
    Then a string "<notDisplayedElement>" is contained on the web table 0 times and "" is not

    Examples: 
      | OperatorName | OperatorID | notDisplayedElement |
      | %            |            | Operator1           |
      | *            |            | Operator1           |
      | (            |            | Operator1           |
      | )            |            | Operator1           |
      | '#'          |            | Operator1           |

  Scenario Outline: Operator Group Account - Verify Operator info on Operator Management page
    Given the Operator Management page is opened with user "valid.username.no.admin"
    When enter in "OperatorID" field on Operator Management page "<OperatorID>" text
    And click on "SEARCH" button
    And open operator info with ID number "<OperatorID>" and name "<OperatorName>"
    Then check operator info data filtered by "_id" and <OperatorID>

    Examples: 
      | OperatorID | OperatorName |
      | 1000000001 | Operator1    |
