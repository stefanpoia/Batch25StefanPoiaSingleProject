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
        openBrowserAndLaunchApplication();
    }

    @When("user clicks on login button")
    public void user_clicks_on_login_button() {
        click(loginPage.loginButton);
    }

    @Then("user is able to login successfully")
    public void user_is_able_to_login_successfully() {
        Assert.assertTrue(dashboardPage.welcomeScreenLoc.isDisplayed());
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
        Assert.assertEquals("Not correct ,error message is displayed",errorMessage,loginPage.errorMessageLoc.getText());
    }
    @Then("user will see {string}")
    public void error_required(String errorMessage1) {
        Assert.assertEquals("Not correct ,error message is displayed",errorMessage1,loginPage.errorMessageRequired.getText());
    }

}
