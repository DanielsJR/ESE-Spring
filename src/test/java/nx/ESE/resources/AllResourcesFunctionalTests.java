package nx.ESE.resources;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	TokenResourceFunctionalTesting.class,
	UserResourceFuntionalTesting.class
})
public class AllResourcesFunctionalTests {

}
