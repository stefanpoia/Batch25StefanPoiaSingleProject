package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import utils.CommonMethod;
import utils.ConfigReader;

public class LoginSteps extends CommonMethod {
    @Given("user is able to access HRMS application")
    public void user_is_able_to_access_hrms_application() {
        getwait().until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf(loginPage.usernameField));
    }

    @When("user clicks on login button")
    public void user_clicks_on_login_button() {
        click(loginPage.loginButton);
    }

    @Then("user is able to login successfully")
    public void user_is_able_to_login_successfully() {
        Assert.assertTrue(getwait().until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf(dashboardPage.welcomeScreenLoc)).isDisplayed());
        System.out.println("Test passed");
    }
    @When("user enters valid admin username and password")
    public void user_enters_valid_admin_username_and_password() {

        sendText(ConfigReader.read("userName"),loginPage.usernameField);
        sendText(ConfigReader.read("password"), loginPage.passwordField);
    }

    @When("user enter {string} and {string}")
    public void user_enter_and(String Username, String Password) {
        sendText(Username, loginPage.usernameField);
        sendText(Password, loginPage.passwordField);
    }

    @Then("user see {string}")
    public void user_see(String errorMessage) {
        Assert.assertEquals("Unexpected login error", errorMessage, getwait().until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf(loginPage.errorMessageLoc)).getText());
    }
    @Then("user will see {string}")
    public void error_required(String errorMessage1) {
        int expectedCount = loginPage.usernameField.getAttribute("value").isEmpty() && loginPage.passwordField.getAttribute("value").isEmpty() ? 2 : 1;
        getwait().until(d -> d.findElements(org.openqa.selenium.By.xpath("//span[text()='Required']")).size() == expectedCount);
        for (org.openqa.selenium.WebElement error : driver.findElements(org.openqa.selenium.By.xpath("//span[text()='Required']"))) Assert.assertEquals(errorMessage1, error.getText());
    }

}
