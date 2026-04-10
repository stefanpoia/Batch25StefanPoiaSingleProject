package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.CommonMethod;

public class LoginPage extends CommonMethod {

    @FindBy(xpath = "//*[@name='username']")
    public WebElement usernameField;

    @FindBy(xpath = "//*[@name='password']")
    public WebElement passwordField;

    @FindBy(xpath = "//*[@type='submit']")
    public WebElement loginButton;

    @FindBy(xpath = "//*[text()='Invalid credentials']")
    public WebElement errorMessageLoc;

    @FindBy(xpath = "(//*[text()='Required'])[1]")
    public WebElement errorMessageRequired;

    public LoginPage(){
        PageFactory.initElements(driver,this);
    }


}
