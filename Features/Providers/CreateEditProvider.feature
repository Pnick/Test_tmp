@TestScenario @ProviderManagement
Feature: Create and Edit new Provider
  Verify that a new Provider can be created and edited

  Scenario: Create new Provider with correct data
    Given the Provider Management page is opened with user "valid.username5"
    When click on "Add" button
    And enter new provider data
    And click on "Create" button
    And enter in "ProviderName" field on Provider Management page "lastProvider" text
    And click on "SEARCH" button
    Then a string "ProviderTest" is contained on the web table 1 times and "" is not

  Scenario: Edit existing Provider's Info
    Given the Provider Management page is opened with user "valid.username5"
    When enter in "ProviderName" field on Provider Management page "randomProvider" text
    And click on "SEARCH" button
    And open provider info with ID number "random" and name "random"
    And click on "Edit" button
    And change provider data
    And click on "Save" button
    Then check that Provider Info is changed
