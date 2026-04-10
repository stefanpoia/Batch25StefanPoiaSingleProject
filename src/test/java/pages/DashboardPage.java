package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.CommonMethod;

public class DashboardPage extends CommonMethod {

    @FindBy(xpath="//*[@class='oxd-userdropdown-tab']")
    public WebElement welcomeScreenLoc;

    public DashboardPage(){
        PageFactory.initElements(driver, this);
    }

}
