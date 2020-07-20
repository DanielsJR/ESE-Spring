package nx.ESE.controllers;

import static org.junit.Assert.assertEquals;

import java.util.List;

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
import nx.ESE.dtos.CourseDto;
import nx.ESE.dtos.SubjectDto;
import nx.ESE.services.CourseRestService;
import nx.ESE.services.HttpMatcher;
import nx.ESE.services.RestService;
import nx.ESE.services.SubjectRestService;
import nx.ESE.services.UserRestService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class SubjectControllerIT {

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

	// POST
	@Test
	public void testPostSubject() {
		subjectRestService.postSubject();

		SubjectDto sDto = subjectRestService.getSubjectById(subjectRestService.getSubjectDto().getId());
		assertEquals(sDto, subjectRestService.getSubjectDto());
	}

	@Test
	public void testPostSubjectPreAuthorize() {
		restService.loginTeacher();// PreAuthorize("hasRole('MANAGER')")

		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		subjectRestService.postSubject();

	}

	@Test
	public void testPostSubjectNoBearerAuth() {
		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(SubjectController.SUBJECT).body(subjectRestService.getSubjectDto()).post()
				.build();
	}

	@Test
	public void testPostSubjectFieldInvalidExceptionId() {
		subjectRestService.postSubject();

		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		subjectRestService.postSubject();// it goes with Id

	}

	@Test
	public void testPostSubjectSubjectRepeated() {
		subjectRestService.postSubject();

		subjectRestService.getSubjectDto2().setName(subjectRestService.getSubjectDto().getName());
		subjectRestService.getSubjectDto2().setCourse(subjectRestService.getSubjectDto().getCourse());

		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		subjectRestService.postSubject2();
	}

	@Test
	public void testPostSubjectBodyNull() {
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		restService.restBuilder().path(SubjectController.SUBJECT).body(null)
				.bearerAuth(restService.getAuthToken().getToken()).post().build();
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

	// PUT
	@Test
	public void testPutSubject() {
		subjectRestService.postSubject();

		subjectRestService.getSubjectDto().setTeacher(userRestService.getTeacherDto());

		subjectRestService.putSubject();

		Assert.assertEquals(subjectRestService.getSubjectDto().getTeacher(), userRestService.getTeacherDto());
	}

	@Test
	public void testPutSubjectPreAuthorize() {
		subjectRestService.postSubject();

		subjectRestService.getSubjectDto().setTeacher(userRestService.getTeacherDto());

		restService.loginTeacher();// PreAuthorize("hasRole('MANAGER')")

		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		subjectRestService.putSubject();
	}

	@Test
	public void testPutSubjectNoBearerAuth() {
		subjectRestService.postSubject();

		subjectRestService.getSubjectDto().setTeacher(userRestService.getTeacherDto());

		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(SubjectController.SUBJECT).path(SubjectController.PATH_ID)
				.expand(subjectRestService.getSubjectDto().getId()).body(subjectRestService.getSubjectDto()).put()
				.build();
	}

	@Test
	public void testPutSubjectDocumentAlreadyExistException() {
		subjectRestService.postSubject();
		subjectRestService.postSubject2();

		subjectRestService.getSubjectDto().setName(subjectRestService.getSubjectDto2().getName());
		subjectRestService.getSubjectDto().setCourse(subjectRestService.getSubjectDto2().getCourse());

		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		subjectRestService.putSubject();
	}

	@Test
	public void testPutSubjectFieldNotFoundExceptionId() {
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		restService.restBuilder().path(SubjectController.SUBJECT).path(SubjectController.PATH_ID).expand("xxx")
				.body(subjectRestService.getSubjectDto()).bearerAuth(restService.getAuthToken().getToken()).put()
				.build();

	}

	@Test
	public void testPutSubjectIdNull() {
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		subjectRestService.putSubject();

	}

	@Test
	public void testPutSubjectBodyNull() {
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		restService.restBuilder().path(SubjectController.SUBJECT).path(SubjectController.PATH_ID).expand("xxx")
				.body(null).bearerAuth(restService.getAuthToken().getToken()).put().build();
	}

	@Test
	public void testPutSubjectNameNull() {
		subjectRestService.postSubject();

		subjectRestService.getSubjectDto().setName(null);

		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		subjectRestService.putSubject();
	}

	@Test
	public void testPutSubjectTeacherNull() {
		subjectRestService.postSubject();

		subjectRestService.getSubjectDto().setTeacher(null);

		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		subjectRestService.putSubject();
	}

	@Test
	public void testPutSubjectCourseNull() {
		subjectRestService.postSubject();

		subjectRestService.getSubjectDto().setCourse(null);

		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		subjectRestService.putSubject();
	}

	// DELETE
	@Test
	public void testDeleteSubject() {
		subjectRestService.postSubject();

		subjectRestService.deleteSubject(subjectRestService.getSubjectDto().getId());
	}

	@Test
	public void testDeleteSubjectPreAuthorize() {
		subjectRestService.postSubject();

		restService.loginTeacher();// PreAuthorize("hasRole('MANAGER')")
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		subjectRestService.deleteSubject(subjectRestService.getSubjectDto().getId());
	}

	@Test
	public void testDeleteSubjectNoBearerAuth() {
		subjectRestService.postSubject();

		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(SubjectController.PATH_ID).expand(subjectRestService.getSubjectDto().getId())
				.delete().build();

	}

	@Test
	public void testDeleteSubjectForbiddenDeleteExceptionGrade() {
		CourseDto cDto = courseRestService.getCourseByNameAndYear(CourseName.PRIMERO_A, "2018");
		SubjectDto sDto = subjectRestService.getSubjectByNameAndCourse(SubjectName.MATEMATICAS, cDto.getId());

		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		subjectRestService.deleteSubject(sDto.getId());
	}

	@Test
	public void testDeleteSubjectFieldNotFoundExceptionId() {
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		subjectRestService.deleteSubject("xxx");
	}

	// GET
	@Test
	public void testGetSubjectById() {
		subjectRestService.postSubject();

		SubjectDto sDto = subjectRestService.getSubjectById(subjectRestService.getSubjectDto().getId());

		assertEquals(subjectRestService.getSubjectDto(), sDto);
	}

	@Test
	public void testGetSubjectByIdPreAuthorize() {
		subjectRestService.postSubject();

		// @PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
		restService.loginAdmin();

		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		subjectRestService.getSubjectById(subjectRestService.getSubjectDto().getId());

	}

	@Test
	public void testGetSubjectByIdNoBearerAuth() {
		subjectRestService.postSubject();

		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(SubjectController.SUBJECT).path(SubjectController.PATH_ID)
				.expand(subjectRestService.getSubjectDto().getId()).get().build();
	}

	@Test
	public void testGetSubjectByIdFieldNotFoundExceptionId() {
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		subjectRestService.getSubjectById("xxx");

	}

	//
	@Test
	public void testGetSubjectByNameAndCourse() {
		subjectRestService.postSubject();

		SubjectDto sDto = subjectRestService.getSubjectByNameAndCourse(subjectRestService.getSubjectDto().getName(),
				subjectRestService.getSubjectDto().getCourse().getId());

		assertEquals(subjectRestService.getSubjectDto(), sDto);
		
	}

	@Test
	public void testGetSubjectByNameAndCoursePreAuthorize() {
		subjectRestService.postSubject();

		// @PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
		restService.loginAdmin();

		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		subjectRestService.getSubjectByNameAndCourse(subjectRestService.getSubjectDto().getName(),
				subjectRestService.getSubjectDto().getCourse().getId());

	}

	@Test
	public void testGetSubjectByNameAndCourseNoBearerAuth() {
		subjectRestService.postSubject();

		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(SubjectController.SUBJECT).path(SubjectController.PATH_NAME)
				.expand(subjectRestService.getSubjectDto().getName()).path(SubjectController.PATH_ID)
				.expand(subjectRestService.getSubjectDto().getCourse().getId()).get().build();

	}

	@Test
	public void testGetSubjectByNameAndCourseDocumentNotFoundException() {
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		subjectRestService.getSubjectByNameAndCourse(subjectRestService.getSubjectDto().getName(), "xxx");

	}

	//
	@Test
	public void testGetSubjects() {
		subjectRestService.postSubject();

		List<SubjectDto> sDtos = subjectRestService.getFullSubjects();
		Assert.assertTrue(sDtos.size() > 0);
	}

	@Test
	public void testGetSubjectsPreAuthorize() {
		// @PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
		restService.loginAdmin();

		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		subjectRestService.getFullSubjects();

	}

	@Test
	public void testGetSubjectsNoBearerAuth() {
		subjectRestService.postSubject();

		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(SubjectController.SUBJECT).get().build();

	}

}
