package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.CommonMethod;

public class ContactDetailsPage extends CommonMethod {


    @FindBy(css = "input[placeholder='Search']")
    public WebElement Search_Field;

    @FindBy(xpath = "//*[text()='My Info']")
    public WebElement myInfo;

    @FindBy(xpath = "(//*[text()='Contact Details'])[1]")
    public WebElement contactDetails;

    @FindBy(xpath = "//div[contains(@class,'oxd-input-group')][.//label[normalize-space()='Street 1']]//input")
    public WebElement addressStreet1_Field;

    @FindBy(xpath = "//div[contains(@class,'oxd-input-group')][.//label[normalize-space()='Street 2']]//input")
    public WebElement addressStreet2_Field;

    @FindBy(xpath = "//div[contains(@class,'oxd-input-group')][.//label[normalize-space()='City']]//input")
    public WebElement city_Field;

    @FindBy(xpath = "//div[contains(@class,'oxd-input-group')][.//label[normalize-space()='State/Province']]//input")
    public WebElement state_province_Field;

    @FindBy(xpath = "//div[contains(@class,'oxd-input-group')][.//label[normalize-space()='Zip/Postal Code']]//input")
    public WebElement zipcode_Field;

    @FindBy(xpath = "//div[contains(@class,'oxd-input-group')][.//label[normalize-space()='Country']]//div[contains(@class,'oxd-select-text')]")
    public WebElement getCountry_DDF_button_Field;


    @FindBy(xpath = "//div[contains(@class,'oxd-input-group')][.//label[normalize-space()='Home']]//input")
    public WebElement home_telephone_Field;

    @FindBy(xpath = "//div[contains(@class,'oxd-input-group')][.//label[normalize-space()='Mobile']]//input")
    public WebElement mobile_Field;

    @FindBy(xpath = "//div[contains(@class,'oxd-input-group')][.//label[normalize-space()='Work']]//input")
    public WebElement work_telephone_Field;

    @FindBy(xpath = "//div[contains(@class,'oxd-input-group')][.//label[normalize-space()='Work Email']]//input")
    public WebElement work_email_Field;

    @FindBy(xpath = "//div[contains(@class,'oxd-input-group')][.//label[normalize-space()='Other Email']]//input")
    public WebElement other_email_Field;

    @FindBy(xpath = "//form[.//label[normalize-space()='Street 1']]//button[@type='submit']")
    public WebElement saveButton_Field;

    @FindBy(xpath = "//div[contains(@class,'oxd-input-group')][.//label[normalize-space()='Country']]//div[contains(@class,'oxd-select-text')]")
    public WebElement countryDropdown;

    public ContactDetailsPage() {
        PageFactory.initElements(driver, this);
    }

}