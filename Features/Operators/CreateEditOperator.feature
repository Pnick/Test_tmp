@TestScenario @OperatorManagement
Feature: Create and Edit new Operator
  Verify that a new Operator can be created and edited

  Scenario: Create new Operator with correct data
    Given the Operator Management page is opened with user "valid.username4"
    When click on "Add" button
    And enter new operator data
    And click on "Create" button
    And enter in "OperatorName" field on Operator Management page "lastOperator" text
    And click on "SEARCH" button
    Then a string "OperatorTest" is contained on the web table 1 times and "" is not

  Scenario: Edit existing Operator's Info
    Given the Operator Management page is opened with user "valid.username4"
    When enter in "OperatorName" field on Operator Management page "randomOperator" text
    And click on "SEARCH" button
    And open operator info with ID number "random" and name "random"
    And click on "Edit" button
    And change operator data
    And click on "Save" button
    Then check that Operator Info is changed

  Scenario Outline: Operator Info cannot be edit with wrong data
    Given the Operator Management page is opened with user "valid.username4"
    When enter in "OperatorName" field on Operator Management page "randomOperator" text
    And click on "SEARCH" button
    And open operator info with ID number "random" and name "random"
    And click on "Edit" button
    And enter in "<field>" field on Operator Management page "<value>" text
    And click on "Save" button
    Then check that "Operator" Info is NOT changed

    Examples: 
      | field                | value                             |
      | newOperator          | random                            |
      | newOperatorShortName | random                            |
      | newOperatorURL       | https://newoperatorTestDomain.com |
