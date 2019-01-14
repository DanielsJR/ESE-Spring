package nx.ESE.controllers;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import nx.ESE.services.GradeRestService;
import nx.ESE.services.HttpMatcher;
import nx.ESE.services.RestService;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class GradeControllerFuntionalTesting {
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Autowired
	private GradeRestService gradeRestService;

	@Autowired
	private RestService restService;

	@Before
	public void before() {
		gradeRestService.createGradesDto();
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
	
	@Test
	public void testPostGradeTitleNull() {
		restService.loginTeacher();
		gradeRestService.getGradeDto().setTitle(null);
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		gradeRestService.postGrade();
	}
	
	@Test
	public void testPostGradeTypeNull() {
		restService.loginTeacher();
		gradeRestService.getGradeDto().setType(null);
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		gradeRestService.postGrade();
	}
	
	@Test
	public void testPostGradeStudentNull() {
		restService.loginTeacher();
		gradeRestService.getGradeDto().setStudent(null);
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		gradeRestService.postGrade();
	}
	
	@Test
	public void testPostGradeSubjectNull() {
		restService.loginTeacher();
		gradeRestService.getGradeDto().setSubject(null);
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
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
	
	@Test
	public void testPutGradeTitleNull() {
		restService.loginTeacher();
		gradeRestService.postGrade();
		gradeRestService.getGradeDto().setTitle(null);
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		gradeRestService.putGrade(gradeRestService.getGradeDto().getId());
	}
	
	@Test
	public void testPutGradeTypeNull() {
		restService.loginTeacher();
		gradeRestService.postGrade();
		gradeRestService.getGradeDto().setType(null);
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		gradeRestService.putGrade(gradeRestService.getGradeDto().getId());
	}
	
	@Test
	public void testPutGradeStudentNull() {
		restService.loginTeacher();
		gradeRestService.postGrade();
		gradeRestService.getGradeDto().setStudent(null);
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		gradeRestService.putGrade(gradeRestService.getGradeDto().getId());
	}
	
	@Test
	public void testPutGradeSubjectNull() {
		restService.loginTeacher();
		gradeRestService.postGrade();
		gradeRestService.getGradeDto().setSubject(null);
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
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
