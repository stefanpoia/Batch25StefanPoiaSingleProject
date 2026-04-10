package steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import utils.CommonMethod;
import utils.CountryUtils;
import utils.DBUtils;
import java.util.List;
import java.util.Map;

public class UpdadesContactDetailsSteps extends CommonMethod {

    String employeeIdFE="000167";
    String street1FE;
    String street2FE;
    String cityFE;
    String stateFE;
    String zipFE;
    String countryFE;
    String homePhoneNumberFE;
    String mobilePhoneNumberFE;
    String workPhoneNumberFE;
    String workEmailFE;
    String otherEmailFE;




    @When("user clicks on My Info option")
    public void user_clicks_on_my_info_option() {
        click(contactDetailsPage.myInfo);
    }

    @When("the employee navigates to the contact details page")
    public void the_employee_navigates_to_the_contact_details_page() {
        click(contactDetailsPage.contactDetails);
    }
    @When("updates the address from the first street {string}")
    public void updates_the_address_from_the_first_street(String street1) {
        sendText(street1,contactDetailsPage.addressStreet1_Field);
        Assert.assertTrue(contactDetailsPage.addressStreet1_Field.isDisplayed());
        street1FE=street1;
    }
    @When("updates the address from the second street {string}")
    public void updates_the_address_from_the_second_street(String street2) {
        sendText(street2,contactDetailsPage.addressStreet2_Field);
        Assert.assertTrue(contactDetailsPage.addressStreet2_Field.isDisplayed());
        street2FE=street2;
    }
    @When("updates the city {string}")
    public void updates_the_city(String city) {
        sendText(city,contactDetailsPage.city_Field);
        Assert.assertTrue(contactDetailsPage.city_Field.isDisplayed());
        cityFE=city;
    }
    @When("updates the state {string}")
    public void updates_the_state(String state) {
        sendText(state,contactDetailsPage.state_province_Field);
        Assert.assertTrue(contactDetailsPage.state_province_Field.isDisplayed());
        stateFE=state;
    }
    @When("updates the zip code {string}")
    public void updates_the_zip_code(String zip_code) {
        sendText(zip_code,contactDetailsPage.zipcode_Field);
        Assert.assertTrue(contactDetailsPage.zipcode_Field.isDisplayed());
        zipFE=zip_code;
    }
    @When("updates the country {string}")
    public void updates_the_country(String country) {
        selectFromDropDown1(country,contactDetailsPage.countryDropdown);
        Assert.assertTrue(contactDetailsPage.getCountry_DDF_button_Field.isDisplayed());
        countryFE=country;
    }
    @When("updates home phone number {string}")
    public void updates_home_phone_number(String home_phone_number) {
        sendText(home_phone_number,contactDetailsPage.home_telephone_Field);
        Assert.assertTrue(contactDetailsPage.home_telephone_Field.isDisplayed());
        homePhoneNumberFE=home_phone_number;
    }
    @When("updates mobile phone number {string}")
    public void updates_mobile_phone_number(String mobile_phone_number) {
        sendText(mobile_phone_number,contactDetailsPage.mobile_Field);
        Assert.assertTrue(contactDetailsPage.mobile_Field.isDisplayed());
        mobilePhoneNumberFE=mobile_phone_number;
    }
    @When("updates work phone number {string}")
    public void updates_work_phone_number(String work_phone_number) {
        sendText(work_phone_number,contactDetailsPage.work_telephone_Field);
        Assert.assertTrue(contactDetailsPage.work_telephone_Field.isDisplayed());
        workPhoneNumberFE=work_phone_number;
    }
    @When("updates work email address {string}")
    public void updates_work_email_address(String work_email) {
        sendText(work_email,contactDetailsPage.work_email_Field);
        Assert.assertTrue(contactDetailsPage.work_email_Field.isDisplayed());
        workEmailFE=work_email;
    }
    @When("updates other email address {string}")
    public void updates_other_email_address(String other_email) {
        sendText(other_email,contactDetailsPage.other_email_Field);
        Assert.assertTrue(contactDetailsPage.other_email_Field.isDisplayed());
        otherEmailFE=other_email;
    }
    @When("user clicks on save button")
    public void user_clicks_on_save_button() {
        waitForElementToBeClickAble(contactDetailsPage.saveButton_Field);
        click(contactDetailsPage.saveButton_Field);
    }


    @Then("the contact details should be updated in the database")
    public void the_contact_details_should_be_updated_in_the_database() {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        String query = "select * from hs_hr_employee where employee_id="+employeeIdFE;
        List<Map<String, String>> data = DBUtils.fetch(query);
        String street1FB = data.get(0).get("emp_street1");
        String street2FB = data.get(0).get("emp_street2");
        String cityFB = data.get(0).get("city_code");
        String stateFB = data.get(0).get("provin_code");
        String zipFB = data.get(0).get("emp_zipcode");
        String countryFB = data.get(0).get("coun_code");
        String homePhoneNumberFB = data.get(0).get("emp_hm_telephone");
        String mobilePhoneNumberFB = data.get(0).get("emp_mobile");
        String workPhoneNumberFB = data.get(0).get("emp_work_telephone");
        String workEmailFB = data.get(0).get("emp_work_email");
        String otherEmailFB = data.get(0).get("emp_oth_email");

        Assert.assertEquals(street1FE,street1FB);
        Assert.assertEquals(street2FE,street2FB);
        Assert.assertEquals(cityFE,cityFB);
        Assert.assertEquals(stateFE,stateFB);
        Assert.assertEquals(zipFE,zipFB);
        Assert.assertEquals(CountryUtils.toCountryCode(countryFE),countryFB);
        Assert.assertEquals(homePhoneNumberFE,homePhoneNumberFB);
        Assert.assertEquals(mobilePhoneNumberFE,mobilePhoneNumberFB);
        Assert.assertEquals(workPhoneNumberFE,workPhoneNumberFB);
        Assert.assertEquals(workEmailFE,workEmailFB);
        Assert.assertEquals(otherEmailFE,otherEmailFB);
    }

}

