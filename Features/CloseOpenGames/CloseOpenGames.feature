@TestScenario @CloseOpenGames
Feature: Close open games forcibly
  Verify that all games with "OPEN" status can be closed forcibly

  Scenario: Verify that all games with "OPEN" status are forcibly closed
    Given the Game is opened
      | gameName      | operator     | currency | session               | language | game |
      | Egyptian Gems | Roller Games | GBP      | SESSION_47_GBP_100.00 | EN       | opww |
    When enter cheat "MATH:{'tier_id': 10,'scenario_id': 4}" and preserve "no"
    And click on "bet_button"
    Then the game status is "OPEN"
