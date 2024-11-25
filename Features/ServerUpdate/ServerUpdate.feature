@TestScenario @ServerUpdate
Feature: Update Server
  Verify that server is updated correctly

  Scenario Outline: Verify server update after change Bet Configuration by the Backend
    Given page via "<PageLink>" is opened with user "valid.username8"
    And init "<currency>" Bet Configuration for "<type>" game type
    And get "<currency>" Bet Configuration
    When click element "Edit<currency>" on Bet Configurations page
    And switch "<OnOff>" "Max Payout Cap" checkbox
    And click on "Save" button
    Then check that server is updated
      | ServerCheckLink                                    | basicKey | basicKeyValue | subKey     | key            | keyValue |
      | https://dev-rgs.mobilewaves.com/back_office/config | _id      | <type>        | <currency> | max_payout_cap |        0 |

    Examples: 
      | PageLink                               | currency | type        | OnOff |
      | default-bet-configurations/slot        | CHF      | SLOT        | Off   |
      | default-bet-configurations/instant-win | NOK      | INSTANT_WIN | Off   |

  Scenario Outline: Verify server update after change Bet Configuration by the DataBase
    Given init "<currency>" Bet Configuration for "<type>" game type
    When change data on DB
      | collection         | filterByKey | filterKeyValue | nest                     | key   | value   |
      | bet_configurations | _id         | <type>         | per_currency.<currency>. | <key> | <value> |
    Then check that server is updated
      | ServerCheckLink                                    | basicKey | basicKeyValue | subKey     | key   | keyValue |
      | https://dev-rgs.mobilewaves.com/back_office/config | _id      | <type>        | <currency> | <key> | <value>  |

    Examples: 
      | currency | type        | key            | value |
      | CHF      | SLOT        | max_payout_cap |     0 |
      | NOK      | INSTANT_WIN | max_payout_cap |     0 |

  Scenario Outline: Verify server update after Operator is disabled by the Backend
    Given page via "<PageLink>" is opened with user "valid.username8"
    When set "<Operator>" checkbox to "<InitState>"
    And set "<Operator>" checkbox to "<SetState>"
    Then check that server is updated
      | ServerCheckLink                                    | basicKey | basicKeyValue | subKey | key     | keyValue        |
      | https://dev-rgs.mobilewaves.com/back_office/config | _id      | <OperatorID>  | games  | <Games> | <expectedValue> |

    Examples: 
      | PageLink | Operator      | InitState | SetState | OperatorID | Games               | expectedValue |
      | game/37  | Roller Games  | Off       | On       |         31 | egyptian_gems       | {}            |
      | game/108 | Norse Tipping | On        | Off      |        208 | wizards_magic_wheel | null          |

  Scenario Outline: Verify server update after Operator is disabled by the DataBase
    When enter data on DB
      | collection         | _id            | operator_idFilter | game_idFilter | bet_config_id | state       |
      | games_per_operator | new ObjectId() | <OperatorID>      | <GameID>      |               | <InitState> |
    And enter data on DB
      | collection         | _id            | operator_idFilter | game_idFilter | bet_config_id | state      |
      | games_per_operator | new ObjectId() | <OperatorID>      | <GameID>      |               | <SetState> |
    Then check that server is updated
      | ServerCheckLink                                    | basicKey | basicKeyValue | subKey | key     | keyValue        |
      | https://dev-rgs.mobilewaves.com/back_office/config | _id      | <OperatorID>  | games  | <Games> | <expectedValue> |

    Examples: 
      | PageLink | InitState | SetState | OperatorID | Games               | GameID | expectedValue |
      | game/37  | On        | Off      |         31 | egyptian_gems       |     37 | null          |
      | game/108 | Off       | On       |        208 | wizards_magic_wheel |    108 | {}            |
