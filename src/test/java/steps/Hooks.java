package steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import utils.CommonMethod;

public class Hooks extends CommonMethod {
    @Before("not @api")
    public void start() {
        openBrowserAndLaunchApplication();
    }

    @After(value = "not @api", order = 20)
    public void end(Scenario scenario){
        try {
        if (driver == null) return;
        byte[] pic;
        if(scenario.isFailed()) {
            pic = takeScreenshot("failed/"+scenario.getName());
        }else{
            pic = takeScreenshot("passed/"+scenario.getName());
        }
        scenario.attach(pic,"image/png",scenario.getName());

        } catch (org.openqa.selenium.WebDriverException e) {
            scenario.log("Screenshot unavailable: " + e.getClass().getSimpleName());
        }

    }

    @After(value = "not @api", order = 0)
    public void stop() { closeBrowser(); }

}
