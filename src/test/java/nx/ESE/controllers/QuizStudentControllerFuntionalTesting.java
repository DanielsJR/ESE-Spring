package nx.ESE.controllers;

import static org.junit.Assert.assertEquals;

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

import nx.ESE.dtos.QuizStudentDto;
import nx.ESE.services.HttpMatcher;
import nx.ESE.services.QuizStudentRestService;
import nx.ESE.services.RestService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class QuizStudentControllerFuntionalTesting {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Autowired
	private QuizStudentRestService quizStudentRestService;

	@Autowired
	private RestService restService;

	@Before
	public void before() {
		quizStudentRestService.createQuizStudentsDto();
		restService.loginTeacher();
	}

	@After
	public void delete() {
		quizStudentRestService.deleteQuizStudentsDto();
	}

	// POST********************************
	@Test
	public void testPostQuizStudent() {
		quizStudentRestService.postQuizStudent();

		QuizStudentDto qsDto = quizStudentRestService.getQuizStudentById(quizStudentRestService.getQuizStudentDto().getId());
		assertEquals(qsDto, quizStudentRestService.getQuizStudentDto());

		quizStudentRestService.postQuizStudent2();

		QuizStudentDto qsDto2 = quizStudentRestService.getQuizStudentById(quizStudentRestService.getQuizStudentDto2().getId());
		assertEquals(qsDto2.getId(), quizStudentRestService.getQuizStudentDto2().getId());
	}

	@Test
	public void testPostQuizStudentPreAuthorize() {
		restService.loginManager();// PreAuthorize("hasRole('TEACHER')")

		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		quizStudentRestService.postQuizStudent();
	}

	@Test
	public void testPostQuizStudentNoBearerAuth() {
		quizStudentRestService.postQuizStudent();

		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(QuizStudentController.QUIZ_STUDENT).body(quizStudentRestService.getQuizStudentDto()).post().build();
	}

	@Test
	public void testPostQuizStudentFieldInvalidExceptionId() {
		quizStudentRestService.postQuizStudent();

		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		quizStudentRestService.postQuizStudent();// it goes with Id
	}

	@Test
	public void testPostQuizStudentDocumentAlreadyExistException() {
		quizStudentRestService.postQuizStudent();
		
		quizStudentRestService.getQuizStudentDto2().setCorrespondItems(quizStudentRestService.getQuizStudentDto().getCorrespondItems());
		quizStudentRestService.getQuizStudentDto2().setTrueFalseItems(quizStudentRestService.getQuizStudentDto().getTrueFalseItems());
		quizStudentRestService.getQuizStudentDto2().setMultipleSelectionItems(quizStudentRestService.getQuizStudentDto().getMultipleSelectionItems());
		quizStudentRestService.getQuizStudentDto2().setIncompleteTextItems(quizStudentRestService.getQuizStudentDto().getIncompleteTextItems());
		

		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		quizStudentRestService.postQuizStudent2();

	} 

	@Test
	public void testPostQuizStudentBodyNull() {
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		restService.restBuilder().path(QuizStudentController.QUIZ_STUDENT).bearerAuth(restService.getAuthToken().getToken())
				.body(null).post().build();
	}



	// PUT********************************
	@Test
	public void testPutQuizStudent() {
		quizStudentRestService.postQuizStudent();

		quizStudentRestService.getQuizStudentDto().setCorrespondItems(null);
		quizStudentRestService.putQuizStudent();

		QuizStudentDto qsDto = quizStudentRestService.getQuizStudentById(quizStudentRestService.getQuizStudentDto().getId());
		Assert.assertEquals(null, qsDto.getCorrespondItems());
	}

	@Test
	public void testPutQuizStudentPreAuthorize() {
		quizStudentRestService.postQuizStudent();

		quizStudentRestService.getQuizStudentDto().setCorrespondItems(null);

		restService.loginManager();// PreAuthorize("hasRole('TEACHER')")

		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		quizStudentRestService.putQuizStudent();
	}

	@Test
	public void testPutQuizStudentNoBearerAuth() {
		quizStudentRestService.postQuizStudent();

		quizStudentRestService.getQuizStudentDto().setCorrespondItems(null);

		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(QuizStudentController.QUIZ_STUDENT).path(QuizStudentController.PATH_ID)
				.expand(quizStudentRestService.getQuizStudentDto().getId()).body(quizStudentRestService.getQuizStudentDto()).put().build();

	}

    @Test
	public void testPutQuizStudentDocumentAlreadyExistException() {
		quizStudentRestService.postQuizStudent();
		quizStudentRestService.postQuizStudent2();
		
		quizStudentRestService.getQuizStudentDto().setCorrespondItems(quizStudentRestService.getQuizStudentDto2().getCorrespondItems());
		quizStudentRestService.getQuizStudentDto().setTrueFalseItems(quizStudentRestService.getQuizStudentDto2().getTrueFalseItems());
		quizStudentRestService.getQuizStudentDto().setMultipleSelectionItems(quizStudentRestService.getQuizStudentDto2().getMultipleSelectionItems());
		quizStudentRestService.getQuizStudentDto().setIncompleteTextItems(quizStudentRestService.getQuizStudentDto2().getIncompleteTextItems());
		

		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		quizStudentRestService.putQuizStudent();

	}

	@Test
	public void testPutQuizStudentFieldNotFoundExceptionId() {
		quizStudentRestService.getQuizStudentDto().setCorrespondItems(null);

		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		restService.restBuilder().path(QuizStudentController.QUIZ_STUDENT).path(QuizStudentController.PATH_ID).expand("xxx")
				.bearerAuth(restService.getAuthToken().getToken()).body(quizStudentRestService.getQuizStudentDto()).put().build();
	}

	@Test
	public void testPutQuizStudentBodyNull() {
		quizStudentRestService.postQuizStudent();

		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		restService.restBuilder().path(QuizStudentController.QUIZ_STUDENT).path(QuizStudentController.PATH_ID)
				.expand(quizStudentRestService.getQuizStudentDto().getId()).bearerAuth(restService.getAuthToken().getToken())
				.body(null).put().build();

	}


	// DELETE
	@Test
	public void testDeleteQuizStudent() {
		quizStudentRestService.postQuizStudent();

		quizStudentRestService.deleteQuizStudent(quizStudentRestService.getQuizStudentDto().getId());
	}

	@Test
	public void testDeleteQuizStudentPreAuthorize() {
		quizStudentRestService.postQuizStudent();

		restService.loginManager();// PreAuthorize("hasRole('TEACHER')")

		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		quizStudentRestService.deleteQuizStudent(quizStudentRestService.getQuizStudentDto().getId());
	}

	@Test
	public void testDeleteQuizStudentNoBearerAuth() {
		quizStudentRestService.postQuizStudent();

		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(QuizStudentController.QUIZ_STUDENT).path(QuizStudentController.PATH_ID)
				.expand(quizStudentRestService.getQuizStudentDto().getId()).delete().build();
	}

	@Test
	public void testDeleteQuizStudentFieldNotFoundExceptionId() {
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		quizStudentRestService.deleteQuizStudent("xxx");
	}
	
    /* TODO public void testDeleteQuizForbiddenDeleteExceptionEvaluation() {
		
		QuizStudentDto qsDto;

		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		quizStudentRestService.deleteQuizStudent(qsDto.getId()); */

	// GET********************************
	@Test
	public void testGetQuizStudentById() {
		quizStudentRestService.postQuizStudent();
		QuizStudentDto Gdto = quizStudentRestService.getQuizStudentById(quizStudentRestService.getQuizStudentDto().getId());
		Assert.assertEquals(Gdto, quizStudentRestService.getQuizStudentDto());
	}

	@Test
	public void testGetQuizStudentByIdPreAuthorize() {
		quizStudentRestService.postQuizStudent();

		restService.loginAdmin();// PreAuthorize("hasRole('TEACHER') or
									// MANAGER")

		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		quizStudentRestService.getQuizStudentById(quizStudentRestService.getQuizStudentDto().getId());

	}

	@Test
	public void testGetQuizStudentByIdNoBearerAuth() {
		quizStudentRestService.postQuizStudent();

		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(QuizStudentController.QUIZ_STUDENT).path(QuizStudentController.PATH_ID)
				.expand(quizStudentRestService.getQuizStudentDto().getId()).body(quizStudentRestService.getQuizStudentDto().getId()).get()
				.build();
	}

	@Test
	public void testGetQuizStudentByIdFieldNotFoundExceptionId() {
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		quizStudentRestService.getQuizStudentById("xxx");

	}

	//
	@Test
	public void testGetFullQuizStudents() {
		quizStudentRestService.getFullQuizStudents();

		Assert.assertTrue(quizStudentRestService.getListQuizStudentDto().size() > 0);
	}

	@Test
	public void testGetFullQuizStudentsPreAuthorize() {
		restService.loginAdmin();// PreAuthorize("hasRole('TEACHER') or
									// MANAGER")

		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		quizStudentRestService.getFullQuizStudents();

	}

	@Test
	public void testGetFullQuizStudentsNoBearerAuth() {
		quizStudentRestService.getFullQuizStudents();

		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(QuizStudentController.QUIZ_STUDENT).get().build();

	}

}
