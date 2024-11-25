@TestScenario @HomePage
Feature: Open Home Page
  Verify if user is able to open Home page

  Scenario: Open Home Page
    Given the Homepage is opened with user "valid.username"
    When user click logo button
    Then home page is displayed


