# HRMS Test Automation Framework

**End-to-End QA Automation Project | Java | Selenium | Cucumber | REST Assured | Postman | Newman | Jenkins**

## Project Overview

This project demonstrates practical Software Quality Assurance (QA) and Test Automation skills through automated testing of the Syntax HRMS application.

The framework covers UI testing, API testing, employee CRUD operations, positive and negative test scenarios, data persistence validation, and continuous integration.

### Technologies Used

- **Programming:** Java, JavaScript, SQL
- **UI Automation:** Selenium WebDriver, Cucumber BDD
- **API Testing:** Postman, REST Assured, Newman
- **Build & Dependency Management:** Maven
- **CI/CD:** Jenkins
- **Database Testing:** MySQL, JDBC
- **Version Control:** Git, GitHub
- **Project Management:** Jira
- **Test Reporting:** Cucumber HTML Reports, Newman HTML Extra Reporter

## API Automation Results – SCRUM-6

The Syntax HRMS Postman API test suite was executed using Newman and integrated with Jenkins.

| Metric | Result |
|---|---|
| API Requests | 19 |
| Assertions | 84 |
| Passed Assertions | 84 |
| Failed Assertions | 0 |
| Jenkins Build | #12 – SUCCESS |

The automated API tests cover authentication, employee creation, retrieval, updates, deletion, response validation, and negative testing.

## UI Automation – SCRUM-5

Improved employee contact details validation using Selenium WebDriver and Cucumber BDD.

Key testing activities include:

- Automated UI validation
- Data persistence verification
- Positive and negative test scenarios
- Database verification using MySQL
- Automated test execution and reporting
- GitHub pull request and code review workflow

---

## Selenium UI Automation – Technical Documentation

Selenium / Cucumber / JUnit tests for login and employee contact details, with optional MySQL verification.

## Requirements and configuration

Use JDK 17 or newer, Chrome (or Firefox/Edge), and network access to the test HRMS. Maven Wrapper pins Maven 3.9.12; its first run downloads Maven. Safari requires macOS and `headless=false`.

Copy `src/test/resources/config/config.example.properties` to `config.local.properties` in the same directory and enter your test credentials. The local file is ignored by Git. Existing credentials were preserved locally during the repair. If credentials have already been pushed to a remote repository, rotate them; removing them from the current file does not remove Git history.

Configuration precedence: Java `-D` property, environment variable, local properties, default properties. Environment keys include `HRMS_USER_NAME`, `HRMS_PASSWORD`, `HRMS_DB_URL`, `HRMS_DB_USER_NAME`, `HRMS_DB_PASSWORD`, `HRMS_BROWSER`, `HRMS_HEADLESS`, `HRMS_EXPLICIT_WAIT`, `HRMS_PAGE_LOAD_TIMEOUT`, and `HRMS_DB_TIMEOUT`. Missing required settings fail immediately. Use JDBC `connectTimeout` and `socketTimeout` parameters to bound connection/network waits.

## Run on Windows (PowerShell)

```powershell
# Full suite, including database verification
.\mvnw.cmd verify
# Login only
.\mvnw.cmd '-Dcucumber.filter.tags=@smoke' test
# Contact validation and persistence, without MySQL
.\mvnw.cmd '-Dcucumber.filter.tags=@contact' test
# Local utility tests, no browser or database
.\mvnw.cmd '-Dtest=UtilitiesTest' test
# Validate all feature steps without executing hooks or browser actions
.\mvnw.cmd '-Dcucumber.execution.dry-run=true' test
# Visible browser
.\mvnw.cmd '-Dheadless=false' '-Dcucumber.filter.tags=@smoke' test
# Rerun scenarios listed by the last regular execution
.\mvnw.cmd '-Dtest=FailedRunner' test
```

On Linux/macOS replace `.\mvnw.cmd` with `bash mvnw`. Do not run multiple Maven executions simultaneously in the same checkout. The driver and page objects are shared; this suite intentionally runs scenarios sequentially.

## Test data and reports

Contact tests read the current profile's internal employee number from its URL rather than using a fixed employee ID. Original values are captured before edits, backed up in `target/contact-backups/`, and restored through the UI after each save attempt, including failures. A restoration failure fails the scenario and attaches a screenshot; inspect the account against the backup. Use a dedicated test account; concurrent users editing that account can invalidate snapshot restoration. MySQL access only needs SELECT permission, and must point to the database serving the configured HRMS.

Saving is confirmed with the success notification, then values are checked after a reload. The database scenario additionally checks the current employee row. Negative scenarios cover invalid emails/phone numbers, email length, and clearing optional values.

Reports: `target/cucumber.html`, `target/cucumber.json`, `target/surefire-reports/`; screenshots: `screenshots/`. `verify` also generates the Masterthought report. Generated files and local secrets are ignored by Git. CI runs local utility tests and Cucumber dry-run checks; it does not log into the shared HRMS. Live integration tests require credentials and access to the external application/database.

ExcelReader is optional and does not require a bundled workbook. Set `excelPath` or `HRMS_EXCEL_PATH` to your own file when using it; sheet/header validation, sparse rows, blank cells, formulas and formatted values are supported.

## SyntaxHRM API Testing Module (REST Assured / Cucumber)

The API testing module uses the configured application's v2 endpoints, following the [official OrangeHRM documentation](https://github.com/orangehrm/orangehrm/wiki/5.x-advanced-guide). It does not use legacy v1 endpoints or a fabricated Bearer token.

### Project Structure

- `src/test/resources/api/EmployeesApi.feature`: Contains 6 scenarios covering anonymous access, employee listing, pagination, employee details, and invalid offsets.
- `src/test/java/api/steps/ApiSteps.java`: Implements all API step definitions, authentication, and JSON/status code assertions.
- `src/test/java/api/client/HrmsApiClient.java`: Provides a REST Assured client with per-scenario isolation, timeouts, and session cookie management.
- `src/test/java/runners/ApiRunnerClass.java` and `ApiFailedRunner.java`: Execute API tests, rerun failed scenarios, and generate separate reports.
- `src/test/java/api/client/HrmsApiClientTest.java`: Contains local HTTP server tests for endpoints, query parameters, session isolation, and redirects.
- 
### API Authentication

API authentication is performed through the browser using Selenium WebDriver and the existing LoginPage.

The framework waits for successful authentication, extracts the session cookies, and closes the browser.

Subsequent HTTP GET requests are executed using REST Assured with the authenticated session cookies.

Origin validation prevents session cookies from being sent to a different server.

Anonymous API scenarios do not launch a browser, and UI hooks are isolated in a separate Cucumber glue package.

### Login Form Validation

The login form locators were verified against the live application's DOM on October 12, 2026:

- Username: `input[name='username']`
- Password: `input[name='password']`
- Submit button: `button[type='submit']`

The login form submits authentication requests to `/web/index.php/auth/validate` and includes a `_token` field.

These locators are used exclusively for UI-based authentication and are not part of the REST Assured API requests.

Successful authentication is confirmed using the existing DashboardPage locator.

### API Configuration

The API testing module supports the following configuration properties:

- `apiBaseUrl`: Base URL of the SyntaxHRM application (default: `https://www.syntaxhrm.com/web/index.php`).
- `apiTimeout`: API request timeout in milliseconds (default: `20000`).

These properties can be overridden using Java system properties:

- `-DapiBaseUrl`
- `-DapiTimeout`

Alternatively, environment variables can be used:

- `HRMS_API_BASE_URL`
- `HRMS_API_TIMEOUT`

The module uses the locally configured `userName` and `password` credentials.

An account with PIM read permissions is required. The employee details scenario requires at least one accessible employee record.


```powershell
# Run all API scenarios
.\mvnw.cmd '-Dtest=ApiRunnerClass' test

# API smoke tests (anonymous + authenticated employee list)
.\mvnw.cmd '-Dtest=ApiRunnerClass' '-Dcucumber.filter.tags=@apiSmoke' test

# Run anonymous API tests without credentials or browser
.\mvnw.cmd '-Dtest=ApiRunnerClass' '-Dcucumber.filter.tags=@apiAnonymous' test

# Validate API step definitions without accessing the server
.\mvnw.cmd '-Dtest=ApiRunnerClass' '-Dcucumber.execution.dry-run=true' test

# Run local API client tests
.\mvnw.cmd '-Dtest=HrmsApiClientTest' test

# Rerun failed API scenarios
.\mvnw.cmd '-Dtest=ApiFailedRunner' test

# Run UI tests only
.\mvnw.cmd '-Dtest=RunnerClass' test
```


### Test Execution and Reporting

The default Maven execution includes both UI and API test suites, along with local utility tests.

API test reports are generated at:

- `target/api-cucumber.html`
- `target/api-cucumber.json`
- `target/api-failed.txt`

Do not execute multiple Maven commands simultaneously in the same project directory.

Avoid logging session cookies, passwords, or HR employee data in test reports.

### API Test Validation Results – October 12, 2026

All 6 API scenarios were successfully executed against the live SyntaxHRM application, along with 5 local API client tests.

The following validations were completed:

- HTTP status codes: 200, 401, and 422
- JSON response structure and contract validation
- UI-based authentication and session cookie handling
- Employee details retrieval using `/api/v2/pim/employees/{empNumber}/personal-details`
- Compilation and dry-run validation of all 18 UI/API scenarios
- Successful execution of 6 utility tests

The alternative employee details endpoint returned HTTP 405 in this environment.

Initial sandbox network restrictions prevented external and local server access. All validations succeeded when executed in an authorized environment outside the sandbox.

The REST Assured scenarios are read-only and do not create, modify, or delete employee records.

## Postman, Newman & Jenkins CI/CD

**Jira Task:** SCRUM-6 – Implement API Testing for Syntax HRMS using Postman

### API Test Coverage

The Postman collection includes automated tests for:

- Authentication with valid and invalid credentials
- GET requests for employee information
- POST requests to create employees
- PUT requests to update employee information
- DELETE requests to remove employees
- Positive and negative API testing
- HTTP status code and JSON response validation
- End-to-end employee CRUD workflows

### Automated Execution

The Postman collection was executed using Newman and integrated into a Jenkins CI pipeline.

**Verified execution results:**
- 19 API requests executed
- 84 assertions passed
- 0 assertion failures
- Jenkins Build #12: SUCCESS

### Test Reporting

Newman HTML Extra Reporter was used to generate detailed HTML reports containing API execution results, assertion outcomes, and test statistics.

### Security

Authentication credentials and tokens are managed through environment variables rather than being hardcoded in the Postman collection.
