package steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import utils.CommonMethod;
import utils.CountryUtils;
import utils.DBUtils;
import java.util.List;
import java.util.Map;

public class UpdateContactDetailsSteps extends CommonMethod {

    String employeeNumber;
    private final Map<org.openqa.selenium.WebElement, String> original = new java.util.LinkedHashMap<>();
    private String originalCountry;
    private boolean saveAttempted;
    private final Map<org.openqa.selenium.WebElement, String> expected = new java.util.LinkedHashMap<>();
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
        getwait().until(d -> d.getCurrentUrl().matches(".*/empNumber/\\d+.*"));
        employeeNumber = driver.getCurrentUrl().replaceFirst(".*/empNumber/(\\d+).*", "$1");
        click(contactDetailsPage.contactDetails);
        waitForContactForm();
        for (org.openqa.selenium.WebElement field : fields()) original.put(field, field.getAttribute("value"));
        originalCountry = contactDetailsPage.countryDropdown.getText().trim();
        saveOriginalSnapshot();
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
        work_email = work_email.replace("{unique}", java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 16));
        sendText(work_email,contactDetailsPage.work_email_Field);
        Assert.assertTrue(contactDetailsPage.work_email_Field.isDisplayed());
        workEmailFE=work_email;
    }
    @When("updates other email address {string}")
    public void updates_other_email_address(String other_email) {
        other_email = other_email.replace("{unique}", java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 16));
        sendText(other_email,contactDetailsPage.other_email_Field);
        Assert.assertTrue(contactDetailsPage.other_email_Field.isDisplayed());
        otherEmailFE=other_email;
    }
    @When("user clicks on save button")
    public void user_clicks_on_save_button() {
        saveAttempted = true;
        click(contactDetailsPage.saveButton_Field);
        waitForSave();
    }


    @Then("the contact details should be updated in the database")
    public void the_contact_details_should_be_updated_in_the_database() {

        verifyPersistedValues();
        List<Map<String, String>> data = DBUtils.fetch("select * from hs_hr_employee where emp_number = ?", employeeNumber);
        Assert.assertEquals("Expected exactly one employee row for the current profile", 1, data.size());
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

    private org.openqa.selenium.WebElement[] fields() {
        return new org.openqa.selenium.WebElement[] {contactDetailsPage.addressStreet1_Field, contactDetailsPage.addressStreet2_Field,
            contactDetailsPage.city_Field, contactDetailsPage.state_province_Field, contactDetailsPage.zipcode_Field,
            contactDetailsPage.home_telephone_Field, contactDetailsPage.mobile_Field, contactDetailsPage.work_telephone_Field,
            contactDetailsPage.work_email_Field, contactDetailsPage.other_email_Field};
    }
    private void waitForContactForm() {
        getwait().until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf(contactDetailsPage.addressStreet1_Field));
        getwait().until(org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated(org.openqa.selenium.By.cssSelector(".oxd-form-loader, .oxd-loading-spinner")));
        getwait().until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(contactDetailsPage.addressStreet1_Field));
    }
    private void waitForContactValues(Map<org.openqa.selenium.WebElement, String> values, String country) {
        // The SPA renders empty inputs before the asynchronous contact request completes.
        // Wait for the persisted values, including legitimate empty optional fields.
        getwait().until(d -> {
            try {
                for (Map.Entry<org.openqa.selenium.WebElement, String> entry : values.entrySet()) {
                    if (!entry.getValue().equals(entry.getKey().getAttribute("value"))) return false;
                }
                return country == null || country.equals(contactDetailsPage.countryDropdown.getText().trim());
            } catch (org.openqa.selenium.StaleElementReferenceException | org.openqa.selenium.NoSuchElementException e) {
                return false;
            }
        });
    }
    private void waitForSave() {
        try {
            getwait().until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(org.openqa.selenium.By.cssSelector(".oxd-toast--success")));
        } catch (org.openqa.selenium.TimeoutException e) {
            List<org.openqa.selenium.WebElement> errors = driver.findElements(org.openqa.selenium.By.cssSelector(".oxd-input-field-error-message, .oxd-toast--error"));
            throw new AssertionError("Contact save was not confirmed: " + errors.stream().map(org.openqa.selenium.WebElement::getText).collect(java.util.stream.Collectors.joining("; ")), e);
        }
        getwait().until(org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated(org.openqa.selenium.By.cssSelector(".oxd-toast--success")));
    }
    private void verifyPersistedValues() {
        String[] values = {street1FE, street2FE, cityFE, stateFE, zipFE, homePhoneNumberFE, mobilePhoneNumberFE, workPhoneNumberFE, workEmailFE, otherEmailFE};
        org.openqa.selenium.WebElement[] inputs = fields();
        for (int i = 0; i < inputs.length; i++) if (values[i] != null) expected.put(inputs[i], values[i]);
        driver.navigate().refresh();
        waitForContactForm();
        waitForContactValues(expected, countryFE);
        for (Map.Entry<org.openqa.selenium.WebElement, String> entry : expected.entrySet()) {
            Assert.assertEquals("Saved contact value", entry.getValue(), entry.getKey().getAttribute("value"));
        }
        if (countryFE != null) Assert.assertEquals(countryFE, contactDetailsPage.countryDropdown.getText().trim());
    }
    @Then("the contact details should remain saved after reloading")
    public void contactDetailsRemainSaved() { verifyPersistedValues(); }

    @Then("the contact form should display {string}")
    public void contactFormError(String message) {
        getwait().until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(
            org.openqa.selenium.By.xpath("//span[contains(@class,'oxd-input-field-error-message') and normalize-space()='" + message + "']")));
    }

    @When("user attempts to save invalid contact details")
    public void attemptInvalidSave() {
        saveAttempted = true;
        click(contactDetailsPage.saveButton_Field);
    }

    @io.cucumber.java.After(value = "not @api", order = 10)
    public void restoreContactDetails(io.cucumber.java.Scenario scenario) {
        try { restoreOriginalValues(); }
        catch (RuntimeException | AssertionError e) {
            scenario.log("Contact restoration failed; the original values are saved in target/contact-backups.");
            try { if (driver != null) scenario.attach(takeScreenshot("failed/restoration-" + scenario.getName()), "image/png", "Restoration failure"); }
            catch (RuntimeException screenshotFailure) { e.addSuppressed(screenshotFailure); }
            throw e;
        }
    }

    private void saveOriginalSnapshot() {
        java.util.Properties snapshot = new java.util.Properties();
        String[] names = {"street1", "street2", "city", "state", "zip", "homePhone", "mobilePhone", "workPhone", "workEmail", "otherEmail"};
        org.openqa.selenium.WebElement[] inputs = fields();
        for (int i = 0; i < inputs.length; i++) snapshot.setProperty(names[i], original.get(inputs[i]));
        snapshot.setProperty("country", originalCountry);
        snapshot.setProperty("employeeNumber", employeeNumber);
        try {
            java.nio.file.Path directory = java.nio.file.Paths.get("target", "contact-backups");
            java.nio.file.Files.createDirectories(directory);
            try (java.io.OutputStream output = java.nio.file.Files.newOutputStream(directory.resolve("employee-" + employeeNumber + "-" + java.util.UUID.randomUUID() + ".properties"))) {
                snapshot.store(output, "Original contact values before test edits");
            }
        } catch (java.io.IOException e) { throw new IllegalStateException("Cannot back up contact details; test edits cancelled", e); }
    }

    private void restoreOriginalValues() {
        if (!saveAttempted || original.isEmpty() || driver == null) return;
        driver.navigate().refresh();
        waitForContactForm();
        boolean changed = !originalCountry.equals(contactDetailsPage.countryDropdown.getText().trim());
        for (Map.Entry<org.openqa.selenium.WebElement, String> entry : original.entrySet()) changed |= !entry.getValue().equals(entry.getKey().getAttribute("value"));
        if (!changed) return;
        for (Map.Entry<org.openqa.selenium.WebElement, String> entry : original.entrySet()) sendText(entry.getValue(), entry.getKey());
        if (!originalCountry.equals(contactDetailsPage.countryDropdown.getText().trim())) selectFromDropDown1(originalCountry, contactDetailsPage.countryDropdown);
        click(contactDetailsPage.saveButton_Field);
        waitForSave();
        driver.navigate().refresh();
        waitForContactForm();
        waitForContactValues(original, originalCountry);
        for (Map.Entry<org.openqa.selenium.WebElement, String> entry : original.entrySet()) Assert.assertEquals("Restored contact value", entry.getValue(), entry.getKey().getAttribute("value"));
        Assert.assertEquals("Restored country", originalCountry, contactDetailsPage.countryDropdown.getText().trim());
    }
}
