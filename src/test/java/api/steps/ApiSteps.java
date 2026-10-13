package api.steps;

import api.client.HrmsApiClient;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.junit.Assert;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.CommonMethod;
import utils.ConfigReader;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ApiSteps extends CommonMethod {
    private HrmsApiClient client;
    private Response response;
    private int employeeNumber;

    @Given("an anonymous HRMS API session")
    public void anonymousSession() { client = newClient(Map.of()); }

    @Given("an authenticated HRMS API session")
    public void authenticatedSession() {
        // SyntaxHRM v5 uses a web session, not the legacy v1 bearer-token API.
        // Login through the real form so the application handles CSRF correctly.
        Map<String, String> cookies = new LinkedHashMap<>();
        try {
            openBrowserAndLaunchApplication();
            sendText(ConfigReader.read("userName"), loginPage.usernameField);
            sendText(ConfigReader.read("password"), loginPage.passwordField);
            click(loginPage.loginButton);
            getwait().until(ExpectedConditions.visibilityOf(dashboardPage.welcomeScreenLoc));
            URI api = URI.create(ConfigReader.read("apiBaseUrl"));
            URI ui = URI.create(driver.getCurrentUrl());
            Assert.assertEquals("API and UI must share the same origin", ui.getScheme(), api.getScheme());
            Assert.assertEquals("API and UI must share the same host", ui.getHost(), api.getHost());
            Assert.assertEquals("API and UI must share the same port", ui.getPort(), api.getPort());
            for (Cookie cookie : driver.manage().getCookies()) cookies.put(cookie.getName(), cookie.getValue());
            Assert.assertFalse("Login did not provide session cookies", cookies.isEmpty());
        } finally { closeBrowser(); }
        client = newClient(cookies);
    }

    private HrmsApiClient newClient(Map<String, String> cookies) {
        return new HrmsApiClient(ConfigReader.read("apiBaseUrl"), cookies,
                Integer.parseInt(ConfigReader.read("apiTimeout")));
    }

    @When("I request employees with limit {int} and offset {int}")
    public void employees(int limit, int offset) { response = client.employees(limit, offset); }

    @Then("the API status is {int}")
    public void status(int expected) {
        Assert.assertNotNull("No API response", response);
        Assert.assertEquals("Unexpected API status", expected, response.statusCode());
    }

    @Then("the API response is JSON")
    public void jsonResponse() {
        Assert.assertTrue("Expected application/json", response.contentType().toLowerCase(java.util.Locale.ROOT)
                .startsWith("application/json"));
        Assert.assertNotNull("Empty JSON response", response.jsonPath().get());
    }

    @Then("the employee collection contains at most {int} records")
    public void collection(int limit) {
        List<Map<String, Object>> data = response.jsonPath().getList("data");
        Assert.assertNotNull("Missing data array", data);
        Assert.assertTrue("Pagination limit exceeded", data.size() <= limit);
        Number total = response.jsonPath().get("meta.total");
        Assert.assertNotNull("Missing meta.total", total);
        Assert.assertTrue("Total smaller than returned collection", total.longValue() >= data.size());
        for (Map<String, Object> employee : data) {
            Assert.assertTrue("Missing employee number", employee.get("empNumber") instanceof Number);
            Assert.assertTrue("Missing firstName", employee.containsKey("firstName"));
            Assert.assertTrue("Missing lastName", employee.containsKey("lastName"));
        }
    }

    @When("I request the first employee from the collection")
    public void firstEmployee() {
        status(200);
        jsonResponse();
        collection(1);
        List<Map<String, Object>> data = response.jsonPath().getList("data");
        Assert.assertFalse("The test account needs at least one visible employee", data.isEmpty());
        employeeNumber = ((Number) data.get(0).get("empNumber")).intValue();
        response = client.employee(employeeNumber);
    }

    @Then("the employee number matches the requested employee")
    public void employeeMatches() {
        Assert.assertEquals(employeeNumber, response.jsonPath().getInt("data.empNumber"));
        Assert.assertNotNull(response.jsonPath().getString("data.firstName"));
        Assert.assertNotNull(response.jsonPath().getString("data.lastName"));
    }

    @Then("the API response contains an error")
    public void error() { Assert.assertNotNull("Missing error object", response.jsonPath().get("error")); }
}
