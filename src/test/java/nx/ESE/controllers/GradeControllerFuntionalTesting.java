package nx.ESE.controllers;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import nx.ESE.services.GradeRestService;
import nx.ESE.services.RestService;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class GradeControllerFuntionalTesting {

	@Autowired
	private GradeRestService gradeRestService;

	@Autowired
	private RestService restService;

	@Before
	public void before() {
		gradeRestService.createGradeDto();
	}

	@After
	public void delete() {
		gradeRestService.deleteGrades();
	}

	// POST********************************
	@Test
	public void testPostGrade() {
		restService.loginTeacher();
		gradeRestService.postGrade();
	}
	
	// PUT********************************
	@Test
	public void testPutGrade() {
		restService.loginTeacher();
		gradeRestService.postGrade();
		gradeRestService.getGradeDto().setGrade(1.0);
		gradeRestService.putGrade(gradeRestService.getGradeDto().getId());
	}
	
	// GET********************************
	@Test
	public void testGetGradeById() {
		restService.loginTeacher();
		gradeRestService.getGradeById(gradeRestService.postGrade().getId());
	}
	
	@Test
	public void testGetFullGrades() {
		restService.loginTeacher();
		gradeRestService.postGrade();
		gradeRestService.getFullGrades();
		Assert.assertTrue(gradeRestService.getListGradeDto().size() > 0);
	}

}
