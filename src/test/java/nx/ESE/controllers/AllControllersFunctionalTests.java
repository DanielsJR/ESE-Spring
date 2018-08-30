package nx.ESE.controllers;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	TokenControllerFunctionalTesting.class,
	UserControllerFuntionalTesting.class,
	UserManagerControllerFuntionalTesting.class,
	UserTeacherControllerFuntionalTesting.class,
	UserStudentControllerFuntionalTesting.class
})
public class AllControllersFunctionalTests {

}
