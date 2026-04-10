Feature: Edit Contact Information
  @db
  Scenario: Successfully update contact details
    Given user is able to access HRMS application
    When user enters valid admin username and password
    And user clicks on login button
    Then user is able to login successfully
    When user clicks on My Info option
    And the employee navigates to the contact details page
    And updates the address from the first street "Windsteiner Weg 36"
    And updates the address from the second street "Eiserfelder Ring 25"
    And updates the city "Berlin"
    And updates the state "Brandenburg"
    And updates the zip code "14165"
    And updates the country "Germany"
    And updates home phone number "345-345-678"
    And updates mobile phone number "004917677387986"
    And updates work phone number "455-334-455"
    And updates work email address "admin1@yahoo.com"
    And updates other email address "admin6@yahoo.com"
    And user clicks on save button
    Then the contact details should be updated in the database