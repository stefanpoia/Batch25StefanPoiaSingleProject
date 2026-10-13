
package utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;

public class CommonMethod extends PageInitializer{

    public static WebDriver driver;

    public void openBrowserAndLaunchApplication() {
        if (driver != null) throw new IllegalStateException("Browser already started");
        boolean headless = Boolean.parseBoolean(ConfigReader.read("headless"));
        switch (ConfigReader.read("browser").toLowerCase(java.util.Locale.ROOT)){

            case "chrome":
                ChromeOptions options = new ChromeOptions();
                 if (headless) options.addArguments("--headless=new");
                options.addArguments("--disable-gpu");
                options.addArguments("--window-size=1920,1080");
                options.addArguments("--disable-dev-shm-usage");
                driver = new ChromeDriver(options);
                break;
            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) firefoxOptions.addArguments("-headless");
                driver=new FirefoxDriver(firefoxOptions);
                break;
            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) edgeOptions.addArguments("--headless=new");
                driver = new EdgeDriver(edgeOptions);
                break;
            case "safari":
                if (headless) throw new IllegalArgumentException("Safari does not support headless mode");
                driver = new SafariDriver();
                break;
            default:
                throw new RuntimeException("Invalid Browser Name");
        }

         driver.manage().window().setSize(new Dimension(1920, 1080));
         driver.manage().timeouts().implicitlyWait(Duration.ZERO);
         driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(Long.parseLong(ConfigReader.read("pageLoadTimeout"))));
        driver.get(ConfigReader.read("url"));
        initializePageObjects();

    }

    public void closeBrowser() {
        if (driver != null) {
            try { driver.quit(); }
            finally { driver = null; loginPage = null; dashboardPage = null; contactDetailsPage = null; }
        }
    }



    public void sendText(String text, WebElement element){
        click(element);
        element.sendKeys((System.getProperty("os.name").toLowerCase(java.util.Locale.ROOT).contains("mac") ? Keys.COMMAND : Keys.CONTROL) + "a");
        element.sendKeys(Keys.DELETE);
        element.sendKeys(text);
    }


    public static void sendText(WebElement element, String text) {
        new CommonMethod().sendText(text, java.util.Objects.requireNonNull(element, "Web element is required"));
    }
    public void selectFromDropDown(WebElement dropDown, String visibleText){
        Select sel =new Select(dropDown);
        sel.selectByVisibleText(visibleText);
    }
    public void selectFromDropDown(String value, WebElement dropDown ){
        Select sel =new Select(dropDown);
        sel.selectByValue(value);
    }
    public void selectFromDropDown( WebElement dropDown,int index ){
        Select sel =new Select(dropDown);
        sel.selectByIndex(index);
    }
    

    public WebDriverWait getwait(){
        WebDriverWait wait= new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(ConfigReader.read("explicitWait"))));
        return  wait;
    }

    public void waitForElementToBeClickAble(WebElement element){
        getwait().until(ExpectedConditions.elementToBeClickable(element));
    }
    public void click(WebElement element){
        waitForElementToBeClickAble(element);
        element.click();
    }

    public JavascriptExecutor getJSExecutor(){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return js;
    }
    public void jsClick(WebElement element) {
        getJSExecutor().executeScript("arguments[0].click();", element);
    }

    public static void acceptAlert() {
        try {
            driver.switchTo().alert().accept();
        } catch (NoAlertPresentException e) {
            throw e;
        }
    }
    public static String getAlertText() {
        try {
            return driver.switchTo().alert().getText();
        } catch (NoAlertPresentException e) {
            throw e;
        }
    }
    public static void switchToFrame(Object identifier) {
        try {
            if (identifier instanceof String) {
                driver.switchTo().frame((String) identifier);
            } else if (identifier instanceof WebElement) {
                driver.switchTo().frame((WebElement) identifier);
            } else if (identifier instanceof Integer) {
                driver.switchTo().frame((int) identifier);
            } else { throw new IllegalArgumentException("Unsupported frame identifier"); }
        } catch (NoSuchFrameException e) {
            throw e;
        }
    }



    public byte[] takeScreenshot(String fileName){
        fileName = fileName.replaceAll("[^a-zA-Z0-9_ /-]", "_");
        TakesScreenshot ts = (TakesScreenshot) driver;
        byte[] picByte = ts.getScreenshotAs(OutputType.BYTES);

        try {
            FileUtils.writeByteArrayToFile(
                    new File(Constants.SCREENSHOT_FILEPATH +
                            fileName+" "+
                            getTimeStamp("yyyy-MM-dd-HH-mm-ss-SSS")+".png"), picByte);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return picByte;
    }

    public String getTimeStamp(String pattern){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static void moveCursor(WebElement element) {
        try {
            if (element.isDisplayed() && element.isEnabled()) {
                Actions action = new Actions(driver);
                action.moveToElement(element).perform();
                System.out.println("Cursor moved to the element: " + element.toString());
            } else {
                throw new IllegalStateException("Element is not displayed or enabled");
            }
        } catch (Exception e) {
            throw new IllegalStateException("Cannot move cursor to element", e);
        }
    }


    public void selectFromDropDown1(String countryName, WebElement dropdown) {
        WebDriverWait wait = getwait();
        wait.until(ExpectedConditions.elementToBeClickable(dropdown));
        dropdown.click();

        List<WebElement> options = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(
                        By.cssSelector(".oxd-select-dropdown .oxd-select-option")
                )
        );

        for (WebElement option : options) {
            if (option.getText().trim().equalsIgnoreCase(countryName)) {
                option.click();
                return;
            }
        }
        throw new NoSuchElementException("Country option not found: " + countryName);
    }



}
