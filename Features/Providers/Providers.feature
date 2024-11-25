@TestScenario @ProviderManagement
Feature: Provider Management
  Verify search functionality on All Providers page

  Scenario: Open Provider Management page
    Given the Homepage is opened with user "valid.username5"
    When click "Management" button on Home page
    And click "Provider Management" button on Home page
    Then a "All Providers" page is displayed

  Scenario Outline: Search by Provider Name
    Given the Provider Management page is opened with user "valid.username5"
    When enter in "ProviderName" field on Provider Management page "<ProviderName>" text
    And select "<ProviderName>" option from "Provider Name" drop down
    And click on "SEARCH" button
    Then a "<ProviderName>" is displayed on the web table 1 times and "<notDisplayedElement>" is not
    And check "name" with "whole" string "<ProviderName>" against "providers" table records

    Examples: 
      | ProviderName | notDisplayedElement |
      | Provider1    | Provider01          |
      | Provider2    | Provider1           |

  Scenario Outline: Search by Provider ID
    Given the Provider Management page is opened with user "valid.username5"
    When enter in "ProviderID" field on Provider Management page "<ProviderID>" text
    And clear "ProviderName" field on "Providers" page
    And click on "SEARCH" button
    Then a "<ProviderName>" is displayed on the web table 1 times and "<notDisplayedElement>" is not
    And check "_id" with "whole" string "<ProviderID>" against "providers" table records

    Examples: 
      | ProviderID | ProviderName | notDisplayedElement |
      | 1000000001 | Provider1    | Provider10          |
      | 1000000002 | Provider2    | Provider1           |

  Scenario Outline: Search by part of Provider Name
    Given the Provider Management page is opened with user "valid.username5"
    When click on "Order by Provider ID" button
    And enter in "ProviderID" field on Provider Management page "<ProviderID>" text
    And enter in "ProviderName" field on Provider Management page "<ProviderName>" text
    And click on "SEARCH" button
    Then an element "<ProviderName>" is displayed and "<notDisplayedElement>" is not displayed
    And check "_id" with "whole" string "<ProviderID>" against "providers" table records

    Examples: 
      | ProviderID | ProviderName | notDisplayedElement |
      | 1000000010 | Provider1    | Provider2           |
      | 1000000011 | Provider1    | Provider9           |

  Scenario Outline: Search by Provider Name on second page
    Given the Provider Management page is opened with user "valid.username5"
    When enter in "ProviderName" field on Provider Management page "<ProviderName>" text
    And clear "ProviderID" field on "Providers" page
    And click on "SEARCH" button
    And click on "nextPaginationPage" button
    Then an element "<ProviderName>" is displayed and "<notDisplayedElement>" is not displayed
    And check "name" with "part" string "<ProviderName>" against "providers" table records

    Examples: 
      | ProviderName | notDisplayedElement |
      | Provider     | Father frozen       |

  Scenario Outline: Search by Provider Name from second page
    Given the Provider Management page is opened with user "valid.username5"
    When click on "nextPaginationPage" button
    And enter in "ProviderName" field on Provider Management page "<ProviderName>" text
    And click on "SEARCH" button
    Then a "<ProviderName>" is displayed on the web table 1 times and "<notDisplayedElement>" is not
    And check "name" with "whole" string "<ProviderName>" against "providers" table records

    Examples: 
      | ProviderName | notDisplayedElement |
      | Provider2    | Division touch      |

  Scenario Outline: Search after click on next page
    Given the Provider Management page is opened with user "valid.username5"
    When enter in "ProviderName" field on Provider Management page "<ProviderName>" text
    And clear "ProviderID" field on "Providers" page
    And get value of pagination counter
    And click on "nextPaginationPage" button
    Then a string "<ProviderName>" is contained on the web table 0 times and "" is not

    Examples: 
      | ProviderName |
      | Provider1    |

  Scenario: Search by Type
    Given the Provider Management page is opened with user "valid.username5"
    When select "Game to RGS" option from combo box "Provider Type"
    And click on "SEARCH" button
    Then a "Game to RGS" is displayed on the web table 10 times and "Scratch" is not
    And check "type" with "existing" string "GAME_TO_RGS" against "providers" table records

  Scenario Outline: Search with combined Provider parameters
    Given the Provider Management page is opened with user "valid.username5"
    When enter in "ProviderName" field on Provider Management page "<ProviderName>" text
    And select "<Type>" option from combo box "Provider Type"
    And click on "SEARCH" button
    Then a "<ProviderName>" is displayed on the web table <ManyTimes> times and "<notDisplayedElement>" is not

    Examples: 
      | ProviderName | Type        | notDisplayedElement | ManyTimes |
      | Provider1    | Game to RGS | Provider10          |         1 |
      | Provider1    | RGS to RGS  | Provider1           |         0 |
      | Provider2    | Game to RGS | Provider12          |         1 |

  Scenario Outline: Search with similar Provider's data
    Given the Provider Management page is opened with user "valid.username5"
    When enter in "ProviderName" field on Provider Management page "<ProviderName>" text
    And click on "SEARCH" button
    Then a string "<ProviderName>" is contained on the web table <ManyTimes> times and "" is not

    Examples: 
      | ProviderName | ManyTimes |
      | Provider2    |         2 |

  Scenario: 'Clear Filters' verification on last Pagination Page
    Given the Provider Management page is opened with user "valid.username5"
    When click on "lastPaginationPage" button
    And get displayed results on the web table
    And click on "ClearFilters" button
    Then compare current with the previous table results

  Scenario Outline: Verify Provider info through Provider Management page
    Given the Provider Management page is opened with user "valid.username5"
    When enter in "ProviderID" field on Provider Management page "<ProviderID>" text
    And click on "SEARCH" button
    And open provider info with ID number "<ProviderID>" and name "<ProviderName>"
    Then check provider info data filtered by "_id" and <ProviderID>

    Examples: 
      | ProviderID | ProviderName |
      | 1000000002 | Provider2    |
