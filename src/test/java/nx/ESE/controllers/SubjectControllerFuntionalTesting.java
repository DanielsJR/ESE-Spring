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
	private SubjectRestService subjectRestService;

	@Before
	public void before() {
		subjectRestService.createSubjectDto();
	}

	@After
	public void delete() {
		subjectRestService.deleteSubjects();
	}
	
	
	// POST******************************************
	@Test
	public void testPostSubject() {
		subjectRestService.postSubject();
	}
	
	// PUT******************************************
	@Test
	public void testPutSubject() {
		subjectRestService.postSubject();
		subjectRestService.getSubjectDto().setTeacher(userRestService.getTeacherDto());
		subjectRestService.putSubject(subjectRestService.getSubjectDto().getId());
		Assert.assertEquals(subjectRestService.getSubjectDto().getTeacher().getId(),userRestService.getTeacherDto().getId());
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
