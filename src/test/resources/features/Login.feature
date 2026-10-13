Feature: User story for login scenarios

  Background:
    Given user is able to access HRMS application

  @smoke  @sprint1
  Scenario: Valid admin login
    When user enters valid admin username and password
    And user clicks on login button
    Then user is able to login successfully


  @smoke
  Scenario Outline: Error message validation while invalid login
    When user enter "<Username>" and "<Password>"
    And user clicks on login button
    Then user see "<ErrorMessage>"

    Examples:
      | Username   | Password     | ErrorMessage        |
      | hrm_user   | Admin1       | Invalid credentials |
      | hrm_user12 | Hrm_user@123 | Invalid credentials |

  @smoke @sprint2
  Scenario Outline: Error message validation while invalid login
    When user enter "<Username>" and "<Password>"
    And user clicks on login button
    Then user will see "<ErrorMessage1>"

    Examples:
      | Username | Password     | ErrorMessage1 |
      | hrm_user |              | Required      |
      |          | Hrm_user@123 | Required      |
      |          |              | Required      |


