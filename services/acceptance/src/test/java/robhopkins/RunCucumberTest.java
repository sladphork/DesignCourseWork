package robhopkins;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = {"src/test/resources/robhopkins/iam"},
    plugin = { "pretty", "html:target/iam-reports.html" }
)
public class RunCucumberTest {
}
