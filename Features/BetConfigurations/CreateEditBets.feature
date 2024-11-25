@TestScenario @BetConfigurations
Feature: Create and Edit new Bet
  Verify that a new Bet can be created and edited

  Scenario Outline: Create new bet with correct data
    Given there is no logged in user
    And the Bet Configurations "<type>" page is opened with user "valid.username8"
    And remove "<currency>" Bet Configuration for "<type>" game type
    When click element "AddNewCofig" on Bet Configurations page
    And select "<currency>" option from combo box "Currency"
    And switch "On" "Max Payout Cap" checkbox
    And enter in "Max Payout Cap" field on Bet Configurations page "<maxPayout>" text
    And enter in "New Bet Increment" field on Bet Configurations page "<bet1>" text
    And click on "AddBetIncrement" button
    And enter in "New Bet Increment" field on Bet Configurations page "<bet2>" text
    And click on "AddBetIncrement" button
    And click on "Save" button
    Then check that "<currency>" Bet is added

    Examples: 
      | currency | type        | maxPayout | bet1 | bet2      |
      | CHF      | SLOT        |         0 | 0.09 |   1000000 |
      | CHF      | SLOT        |         1 | 0.01 | 999999.99 |
      | CHF      | SLOT        |  10000000 | 0.09 |   1000000 |
      | CHF      | INSTANT WIN |         0 | 0.09 |   1000000 |
      | CHF      | INSTANT WIN |         1 | 0.01 | 999999.99 |
      | CHF      | INSTANT WIN |  10000000 | 0.09 |   1000000 |

  Scenario Outline: Try to create new Bet with wrong data
    Given the Bet Configurations "<type>" page is opened with user "valid.username8"
    And remove "<currency>" Bet Configuration for "<type>" game type
    When click element "AddNewCofig" on Bet Configurations page
    And select "<currency>" option from combo box "Currency"
    And switch "On" "Max Payout Cap" checkbox
    And enter in "Max Payout Cap" field on Bet Configurations page "<maxPayout>" text
    And enter in "New Bet Increment" field on Bet Configurations page "<bet1>" text
    And click on "AddBetIncrement" button
    And enter in "New Bet Increment" field on Bet Configurations page "<bet2>" text
    And click on "AddBetIncrement" button
    And click on "Save" button
    Then check that "<currency>" Bet is NOT added

    Examples: 
      | currency | type        | maxPayout | bet1 | bet2       |
      | CHF      | SLOT        | 1.01      |    1 |          2 |
      | CHF      | SLOT        |  10000001 |    1 |          2 |
      | CHF      | SLOT        |  10000000 |      |          2 |
      | CHF      | SLOT        |        10 | 0.01 | 1000000.01 |
      | CHF      | SLOT        |       100 |    2 | 0.009      |
      | CHF      | INSTANT WIN | 1.01      |    1 |          2 |
      | CHF      | INSTANT WIN |  10000001 |    1 |          2 |
      | CHF      | INSTANT WIN |  10000000 |      |          2 |
      | CHF      | INSTANT WIN |        10 | 0.01 | 1000000.01 |
      | CHF      | INSTANT WIN |       100 |    2 | 0.009      |

  Scenario Outline: Edit existing Bet Configuration
    Given the Bet Configurations "<type>" page is opened with user "valid.username8"
    And init "<currency>" Bet Configuration for "<type>" game type
    And get "<currency>" Bet Configuration
    When click element "EditCHF" on Bet Configurations page
    And switch "On" "Max Payout Cap" checkbox
    And enter in "Max Payout Cap" field on Bet Configurations page "<maxPayout>" text
    And enter in "New Bet Increment" field on Bet Configurations page "<betIncrement>" text
    And click element "AddBetIncrement" on Bet Configurations page
    And click on "Save" button
    Then check that "<currency>" Bet is changed

    Examples: 
      | currency | type        | maxPayout | betIncrement |
      | CHF      | SLOT        |       169 | 45.67        |
      | CHF      | INSTANT WIN |        36 | 0.39         |

  Scenario Outline: Remove 'Max Payout Cap' from existing Bet Configuration
    Given the Bet Configurations "<type>" page is opened with user "valid.username8"
    And init "<currency>" Bet Configuration for "<type>" game type
    And get "<currency>" Bet Configuration
    When click element "EditCHF" on Bet Configurations page
    And switch "<OnOff>" "Max Payout Cap" checkbox
    And click on "Save" button
    Then check that "<currency>" Bet is changed

    Examples: 
      | currency | type        | OnOff |
      | CHF      | SLOT        | Off   |
      | CHF      | INSTANT WIN | Off   |

  Scenario Outline: Edit/Remove random 'Bet Increment' from existing Bet Configuration
    Given the Bet Configurations "<type>" page is opened with user "valid.username8"
    And init "<currency>" Bet Configuration for "<type>" game type
    And get "<currency>" Bet Configuration
    When click element "EditCHF" on Bet Configurations page
    And remove "<Bet Increment>" Bet Increments from "<currency>" currency
    And click on "Save" button
    Then check that "<currency>" Bet is changed

    Examples: 
      | currency | type        | Bet Increment |
      | CHF      | SLOT        | random        |
      | CHF      | INSTANT WIN | random        |

  Scenario Outline: Edit/Remove all the 'Bet Increment' from existing Bet Configuration
    Given the Bet Configurations "<type>" page is opened with user "valid.username8"
    And init "<currency>" Bet Configuration for "<type>" game type
    And get "<currency>" Bet Configuration
    When click element "EditCHF" on Bet Configurations page
    And remove "<Bet Increment>" Bet Increments from "<currency>" currency
    And click on "Save" button
    Then check that "<currency>" Bet is NOT added

    Examples: 
      | currency | type        | Bet Increment |
      | CHF      | SLOT        | all           |
      | CHF      | INSTANT WIN | all           |
