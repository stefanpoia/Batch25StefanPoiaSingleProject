# HRMS automated tests

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

## Modul API SyntaxHRM (REST Assured / Cucumber)

Modulul API foloseste rutele v2 ale aplicatiei configurate, conform [rutelor oficiale OrangeHRM](https://github.com/orangehrm/orangehrm/wiki/5.x-advanced-guide). Nu foloseste endpointurile legacy v1 sau un token Bearer inventat.

Structura:
- `src/test/resources/api/EmployeesApi.feature`: 6 scenarii pentru acces anonim, lista angajatilor, paginare, detalii si offset invalid.
- `src/test/java/api/steps/ApiSteps.java`: toti pasii implementati, autentificare si asertiuni JSON/status.
- `src/test/java/api/client/HrmsApiClient.java`: client REST Assured separat pentru fiecare scenariu, timeout si cookie-uri de sesiune.
- `src/test/java/runners/ApiRunnerClass.java` si `ApiFailedRunner.java`: executie si rerun API, rapoarte separate.
- `src/test/java/api/client/HrmsApiClientTest.java`: teste locale cu server HTTP pentru rute, query, izolarea sesiunii si redirecturi.

Autentificarea API deschide browserul, foloseste LoginPage, asteapta confirmarea autentificarii, preia cookie-urile si inchide browserul. Cererile ulterioare sunt HTTP GET prin REST Assured. Verificarea originii impiedica trimiterea cookie-urilor spre alt server. Scenariile anonime nu deschid browserul; hook-urile UI sunt in alt pachet glue.

Locatorii formularului au fost verificati in DOM-ul live pe 12 octombrie 2026: `input[name='username']`, `input[name='password']`, `button[type='submit']`; formularul trimite catre `/web/index.php/auth/validate` si contine `_token`. API-ul nu foloseste locatori; acestia sunt necesari doar pentru login-ul UI. Confirmarea login-ului reutilizeaza locatorul DashboardPage existent.

Configuratie: `apiBaseUrl` (implicit `https://www.syntaxhrm.com/web/index.php`) si `apiTimeout` (milisecunde, implicit 20000), suprascrise prin `-DapiBaseUrl`, `-DapiTimeout`, `HRMS_API_BASE_URL`, `HRMS_API_TIMEOUT`. Foloseste credentialele locale `userName` / `password` deja configurate si un cont cu acces de citire PIM. Scenariul de detalii necesita cel putin un angajat vizibil.

```powershell
# Toate scenariile API
.\mvnw.cmd '-Dtest=ApiRunnerClass' test
# API smoke (anonim + lista autentificata)
.\mvnw.cmd '-Dtest=ApiRunnerClass' '-Dcucumber.filter.tags=@apiSmoke' test
# Fara credentiale sau browser
.\mvnw.cmd '-Dtest=ApiRunnerClass' '-Dcucumber.filter.tags=@apiAnonymous' test
# Verificarea tuturor pasilor API fara acces la server
.\mvnw.cmd '-Dtest=ApiRunnerClass' '-Dcucumber.execution.dry-run=true' test
# Teste locale ale clientului API
.\mvnw.cmd '-Dtest=HrmsApiClientTest' test
# Rerun API
.\mvnw.cmd '-Dtest=ApiFailedRunner' test
# Doar suita UI
.\mvnw.cmd '-Dtest=RunnerClass' test
```

Executia Maven implicita include acum ambele suite si testele locale. Rapoarte API: `target/api-cucumber.html`, `target/api-cucumber.json`, `target/api-failed.txt`. Nu executa mai multe comenzi Maven simultan. Nu loga cookie-uri, parole sau raspunsuri cu date HR in rapoarte.

Validare pe 12 octombrie 2026: toate cele 6 scenarii API au trecut live pe SyntaxHRM, iar cele 5 teste locale ale clientului au trecut. Au fost confirmate statusurile 200, 401 si 422, contractul JSON, login-ul prin locatori si cookie-urile de sesiune. Detaliile folosesc ruta `/api/v2/pim/employees/{empNumber}/personal-details`; ruta simpla de detalii a returnat 405 pe aceasta instalare. Compilarea si dry-run pentru toate cele 18 scenarii UI/API au trecut, impreuna cu cele 6 teste UtilitiesTest. Sandboxul a blocat initial reteaua si serverul loopback; validarile au reusit la executia autorizata in afara sandboxului. Scenariile API citesc date; nu creeaza, modifica sau sterg angajati.

