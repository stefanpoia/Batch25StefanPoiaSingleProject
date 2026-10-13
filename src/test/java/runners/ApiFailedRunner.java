package runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "@target/api-failed.txt", glue = "api.steps", plugin = {"pretty"})
public class ApiFailedRunner { }
