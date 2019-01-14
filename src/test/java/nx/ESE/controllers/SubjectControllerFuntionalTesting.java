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

import nx.ESE.documents.core.CourseName;
import nx.ESE.documents.core.SubjectName;
import nx.ESE.services.CourseRestService;
import nx.ESE.services.HttpMatcher;
import nx.ESE.services.RestService;
import nx.ESE.services.SubjectRestService;
import nx.ESE.services.UserRestService;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class SubjectControllerFuntionalTesting {
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Autowired
	private RestService restService;
	
	@Autowired
	private UserRestService userRestService;

	@Autowired
	private CourseRestService courseRestService;
	
	@Autowired
	private SubjectRestService subjectRestService;

	@Before
	public void before() {
		subjectRestService.createSubjectsDto();
	}

	@After
	public void delete() {
		subjectRestService.deleteSubjects();
	}
	
	
	// POST******************************************
	@Test
	public void testPostSubject() {
		subjectRestService.postSubject();
		subjectRestService.postSubject2();
	}
	
	@Test
	public void testPostSubjectNameNull() {
		subjectRestService.getSubjectDto().setName(null);
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		subjectRestService.postSubject();
	}
	
	@Test
	public void testPostSubjectTeacherNull() {
		subjectRestService.getSubjectDto().setTeacher(null);
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		subjectRestService.postSubject();
	}
	
	@Test
	public void testPostSubjectCourseNull() {
		subjectRestService.getSubjectDto().setCourse(null);
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		subjectRestService.postSubject();
	}
	
	@Test
	public void testPostSubjectSubjectRepeated() {
		subjectRestService.postSubject();
		subjectRestService.getSubjectDto2().setName(subjectRestService.getSubjectDto().getName());
		subjectRestService.getSubjectDto2().setCourse(subjectRestService.getSubjectDto().getCourse());
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		subjectRestService.postSubject2();
	}
	
	
	
	// PUT******************************************
	@Test
	public void testPutSubject() {
		subjectRestService.postSubject();
		subjectRestService.getSubjectDto().setTeacher(userRestService.getTeacherDto());
		subjectRestService.putSubject(subjectRestService.getSubjectDto().getId());
		Assert.assertEquals(subjectRestService.getSubjectDto().getTeacher().getId(),userRestService.getTeacherDto().getId());
	}
	
	
	@Test
	public void testPutSubjectNameNull() {
		subjectRestService.postSubject();
		subjectRestService.getSubjectDto().setName(null);
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		subjectRestService.putSubject(subjectRestService.getSubjectDto().getId());
	}
	
	@Test
	public void testPutSubjectTeacherNull() {
		subjectRestService.postSubject();
		subjectRestService.getSubjectDto().setTeacher(null);
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		subjectRestService.putSubject(subjectRestService.getSubjectDto().getId());
	}
	
	@Test
	public void testPutSubjectCourseNull() {
		subjectRestService.postSubject();
		subjectRestService.getSubjectDto().setCourse(null);
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		subjectRestService.putSubject(subjectRestService.getSubjectDto().getId());
	}
	
	@Test
	public void testPutSubjectSubjectRepeated() {
		subjectRestService.postSubject();
		subjectRestService.postSubject2();
		subjectRestService.getSubjectDto().setName(subjectRestService.getSubjectDto2().getName());
		subjectRestService.getSubjectDto().setCourse(subjectRestService.getSubjectDto2().getCourse());
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		subjectRestService.putSubject(subjectRestService.getSubjectDto().getId());
	}
	
	
	
	// PATCH*****************************************
	
	
	// GET******************************************
	@Test
	public void testGetSubjectById() {
		subjectRestService.getSubjectById(subjectRestService.postSubject().getId());
	}
	
	@Test
	public void testGetSubjects() {
		subjectRestService.postSubject();
		subjectRestService.getFullSubjects();
		Assert.assertTrue(subjectRestService.getListSubjectDto().size() > 0);
	}



}
