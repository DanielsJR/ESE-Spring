package nx.ESE.controllers;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	TokenControllerFunctionalTesting.class,
	
	UserManagerControllerFuntionalTesting.class,
	UserTeacherControllerFuntionalTesting.class,
	UserStudentControllerFuntionalTesting.class,
	
	CourseControllerFuntionalTesting.class,
	SubjectControllerFuntionalTesting.class,
	EvaluationControllerFuntionalTesting.class,
	GradeControllerFuntionalTesting.class,
	
	
})
public class AllControllersFunctionalTests {

}
