package Runner;

import org.testng.annotations.DataProvider;

import io.cucumber.testng.AbstractTestNGCucumberTests;


public abstract class AbstractTestNGCucumberParallelTests extends AbstractTestNGCucumberTests {

//	@Override
//	@DataProvider(parallel = true)
//	public Object[][] features() {
//		return super.features();
//	}

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}