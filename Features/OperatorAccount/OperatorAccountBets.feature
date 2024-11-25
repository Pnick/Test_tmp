@TestScenario @OperatorAccount
Feature: Operator Group Account	- Bet Configurations
  Verify functionalities on Bet Configurations page with Operator Group Account

  Scenario Outline: Operator Group Account - Create/Edit Bet Configuration
    Given the Bet Configurations "<type>" page is opened with user "valid.username.no.admin"
    Then a "Default Bet Configs" page is displayed
    And a string "+" is contained on the web table 0 times and "" is not

    Examples: 
      | type        |
      | SLOT        |
      | INSTANT WIN |