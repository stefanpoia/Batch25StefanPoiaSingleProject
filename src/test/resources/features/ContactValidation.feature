@contact
Feature: Contact details validation and persistence
  Background:
    Given user is able to access HRMS application
    When user enters valid admin username and password
    And user clicks on login button
    Then user is able to login successfully
    When user clicks on My Info option
    And the employee navigates to the contact details page

  Scenario Outline: Invalid email is rejected
    When updates work email address "<email>"
    And user attempts to save invalid contact details
    Then the contact form should display "Expected format: admin@example.com"
    Examples:
      | email           |
      | invalid-email   |
      | user@           |

  Scenario: Invalid phone number is rejected
    When updates mobile phone number "invalid-phone"
    And user attempts to save invalid contact details
    Then the contact form should display "Allows numbers and only + - / ( )"

  Scenario: Optional contact fields can be cleared
    When updates the address from the second street ""
    And updates home phone number ""
    And updates other email address ""
    And user clicks on save button
    Then the contact details should remain saved after reloading

  Scenario: Email longer than the supported limit is rejected
    When updates work email address "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz@example.com"
    And user attempts to save invalid contact details
    Then the contact form should display "Should not exceed 50 characters"
