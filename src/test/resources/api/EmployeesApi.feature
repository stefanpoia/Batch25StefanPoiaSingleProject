@api
Feature: SyntaxHRM employee API v2
  API requests use the same authenticated session as the SyntaxHRM web application.
  These scenarios only read data and require an account with PIM read access.

  @apiAnonymous @apiSmoke
  Scenario: Anonymous users cannot read employee records
    Given an anonymous HRMS API session
    When I request employees with limit 1 and offset 0
    Then the API status is 401
    And the API response is JSON
    And the API response contains an error

  @apiAuthenticated @apiSmoke
  Scenario: Authenticated users can read an employee collection
    Given an authenticated HRMS API session
    When I request employees with limit 1 and offset 0
    Then the API status is 200
    And the API response is JSON
    And the employee collection contains at most 1 records

  @apiAuthenticated
  Scenario Outline: Employee collections respect pagination
    Given an authenticated HRMS API session
    When I request employees with limit <limit> and offset <offset>
    Then the API status is 200
    And the API response is JSON
    And the employee collection contains at most <limit> records
    Examples:
      | limit | offset |
      | 2     | 0      |
      | 2     | 2      |

  @apiAuthenticated
  Scenario: Employee details match the employee selected from the collection
    Given an authenticated HRMS API session
    When I request employees with limit 1 and offset 0
    And I request the first employee from the collection
    Then the API status is 200
    And the API response is JSON
    And the employee number matches the requested employee

  @apiAuthenticated @apiNegative
  Scenario: A negative pagination offset is rejected
    Given an authenticated HRMS API session
    When I request employees with limit 1 and offset -1
    Then the API status is 422
    And the API response is JSON
    And the API response contains an error
