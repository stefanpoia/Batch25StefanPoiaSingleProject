
package utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
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

        switch (ConfigReader.read("browser")){

            case "Chrome":
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--headless");
                driver=new ChromeDriver();
                break;
            case "FireFox":
                driver=new FirefoxDriver();
                break;
            case "Edge":
                driver = new EdgeDriver();
                break;
            case "Safari":
                driver = new SafariDriver();
                break;
            default:
                throw new RuntimeException("Invalid Browser Name");
        }
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        driver.get(ConfigReader.read("url"));
        initializePageObjects();

    }

    public void closeBrowser() {
        driver.close();
    }



    public void sendText(String text, WebElement element){
        element.click();
        element.sendKeys(Keys.CONTROL + "a");
        element.sendKeys(Keys.DELETE);
        element.sendKeys(text);
    }


    public static void sendText(WebElement element, String text) {
        if (element != null) {
            element.clear();
            element.sendKeys(text);
        } else {
            System.err.println("Web Element is null. Cannot send text.");
        }
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
        WebDriverWait wait= new WebDriverWait(driver, Duration.ofSeconds(Constants.EXPLICIT_WAIT));
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
            System.err.println("No alert to accept: " + e.getMessage());
        }
    }
    public static String getAlertText() {
        try {
            return driver.switchTo().alert().getText();
        } catch (NoAlertPresentException e) {
            System.err.println("No alert to retrieve text from: " + e.getMessage());
            return null;
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
            }
        } catch (NoSuchFrameException e) {
            System.err.println("Frame not found: " + e.getMessage());
        }
    }



    public byte[] takeScreenshot(String fileName){
        TakesScreenshot ts = (TakesScreenshot) driver;
        byte[] picByte = ts.getScreenshotAs(OutputType.BYTES);
        File sourceFile = ts.getScreenshotAs(OutputType.FILE);

        try {
            FileUtils.copyFile(sourceFile,
                    new File(Constants.SCREENSHOT_FILEPATH +
                            fileName+" "+
                            getTimeStamp("yyyy-MM-dd-HH-mm-ss")+".png"));
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
                System.err.println("Element is not displayed or not enabled: " + element.toString());
            }
        } catch (Exception e) {
            System.err.println("Error moving cursor to element: " + e.getMessage());
        }
    }


    public void selectFromDropDown1(String countryName, WebElement dropdown) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(dropdown));
        dropdown.click();

        List<WebElement> options = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(
                        By.xpath("//div[contains(@class,'option')]")
                )
        );

        for (WebElement option : options) {
            if (option.getText().trim().equalsIgnoreCase(countryName)) {
                option.click();
                break;
            }
        }
    }



}
