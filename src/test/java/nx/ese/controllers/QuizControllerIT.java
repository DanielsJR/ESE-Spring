package nx.ese.controllers;

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

import nx.ese.dtos.QuizDto;
import nx.ese.services.EvaluationRestService;
import nx.ese.services.HttpMatcher;
import nx.ese.services.QuizRestService;
import nx.ese.services.RestService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class QuizControllerIT {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private QuizRestService quizRestService;

    @Autowired
    private EvaluationRestService evaluationRestService;

    @Autowired
    private RestService restService;

    @Before
    public void before() {
        quizRestService.createQuizesDto();
        restService.loginTeacher();
    }

    @After
    public void delete() {
        quizRestService.deleteQuizesDto();
    }

    // POST********************************
    @Test
    public void testPostQuiz() {
        quizRestService.postQuiz();

        QuizDto qDto = quizRestService.getQuizById(quizRestService.getQuizDto().getId());
        assertEquals(qDto, quizRestService.getQuizDto());

        quizRestService.postQuiz2();

        QuizDto gDto2 = quizRestService.getQuizById(quizRestService.getQuizDto2().getId());
        assertEquals(gDto2.getId(), quizRestService.getQuizDto2().getId());
    }

    @Test
    public void testPostQuizPreAuthorize() {
        restService.loginManager();// PreAuthorize("hasRole('TEACHER')")

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        quizRestService.postQuiz();
    }

    @Test
    public void testPostQuizNoBearerAuth() {
        quizRestService.postQuiz();

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder().path(QuizController.QUIZ).body(quizRestService.getQuizDto()).post().build();
    }

    @Test
    public void testPostQuizFieldInvalidExceptionId() {
        quizRestService.postQuiz();

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        quizRestService.postQuiz();// it goes with Id
    }

    @Test
    public void testPostQuizDocumentAlreadyExistException() {
        quizRestService.postQuiz();

        quizRestService.getQuizDto2().setTitle(quizRestService.getQuizDto().getTitle());
        quizRestService.getQuizDto2().setAuthor(quizRestService.getQuizDto().getAuthor());
        quizRestService.getQuizDto2().setSubjectName(quizRestService.getQuizDto().getSubjectName());
        quizRestService.getQuizDto2().setQuizLevel(quizRestService.getQuizDto().getQuizLevel());

        quizRestService.getQuizDto2().setCorrespondItems(quizRestService.getQuizDto().getCorrespondItems());
        quizRestService.getQuizDto2().setTrueFalseItems(quizRestService.getQuizDto().getTrueFalseItems());
        quizRestService.getQuizDto2().setMultipleSelectionItems(quizRestService.getQuizDto().getMultipleSelectionItems());
        quizRestService.getQuizDto2().setIncompleteTextItems(quizRestService.getQuizDto().getIncompleteTextItems());

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        quizRestService.postQuiz2();

    }

    @Test
    public void testPostQuizBodyNull() {
        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        restService.restBuilder().path(QuizController.QUIZ).bearerAuth(restService.getAuthToken().getToken())
                .body(null).post().build();
    }


    @Test
    public void testPostQuizTitleNull() {
        quizRestService.getQuizDto().setTitle(null);

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        quizRestService.postQuiz();
    }

    @Test
    public void testPostQuizAuthorNull() {
        quizRestService.getQuizDto().setAuthor(null);

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        quizRestService.postQuiz();
    }

    @Test
    public void testPostQuizSubjectNameNull() {
        quizRestService.getQuizDto().setSubjectName(null);

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        quizRestService.postQuiz();
    }

    @Test
    public void testPostQuizQuizLevelNull() {
        quizRestService.getQuizDto().setQuizLevel(null);

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        quizRestService.postQuiz();
    }

    // PUT********************************
    @Test
    public void testPutQuiz() {
        quizRestService.postQuiz();

        quizRestService.getQuizDto().setTitle("new title");
        quizRestService.putQuiz();

        QuizDto gDto = quizRestService.getQuizById(quizRestService.getQuizDto().getId());
        Assert.assertEquals("new title", gDto.getTitle());
    }

    @Test
    public void testPutQuizPreAuthorize() {
        quizRestService.postQuiz();

        quizRestService.getQuizDto().setTitle("new title");

        restService.loginManager();// PreAuthorize("hasRole('TEACHER')")

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        quizRestService.putQuiz();
    }

    @Test
    public void testPutQuizNoBearerAuth() {
        quizRestService.postQuiz();

        quizRestService.getQuizDto().setTitle("new title");

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder().path(QuizController.QUIZ).path(QuizController.PATH_ID)
                .expand(quizRestService.getQuizDto().getId()).body(quizRestService.getQuizDto()).put().build();

    }

    @Test
    public void testPutQuizDocumentAlreadyExistException() {
        quizRestService.postQuiz();
        quizRestService.postQuiz2();

        quizRestService.getQuizDto().setTitle(quizRestService.getQuizDto2().getTitle());
        quizRestService.getQuizDto().setAuthor(quizRestService.getQuizDto2().getAuthor());
        quizRestService.getQuizDto().setSubjectName(quizRestService.getQuizDto2().getSubjectName());
        quizRestService.getQuizDto().setQuizLevel(quizRestService.getQuizDto2().getQuizLevel());

        quizRestService.getQuizDto().setCorrespondItems(quizRestService.getQuizDto2().getCorrespondItems());
        quizRestService.getQuizDto().setTrueFalseItems(quizRestService.getQuizDto2().getTrueFalseItems());
        quizRestService.getQuizDto().setMultipleSelectionItems(quizRestService.getQuizDto2().getMultipleSelectionItems());
        quizRestService.getQuizDto().setIncompleteTextItems(quizRestService.getQuizDto2().getIncompleteTextItems());

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        quizRestService.putQuiz();

    }

    @Test
    public void testPutQuizFieldNotFoundExceptionId() {
        quizRestService.getQuizDto().setTitle("new title");

        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        restService.restBuilder().path(QuizController.QUIZ).path(QuizController.PATH_ID).expand("xxx")
                .bearerAuth(restService.getAuthToken().getToken()).body(quizRestService.getQuizDto()).put().build();
    }

    @Test
    public void testPutQuizBodyNull() {
        quizRestService.postQuiz();

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        restService.restBuilder().path(QuizController.QUIZ).path(QuizController.PATH_ID)
                .expand(quizRestService.getQuizDto().getId()).bearerAuth(restService.getAuthToken().getToken())
                .body(null).put().build();

    }


    @Test
    public void testPutQuizTitleNull() {
        quizRestService.postQuiz();

        quizRestService.getQuizDto().setTitle(null);

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        quizRestService.putQuiz();
    }

    @Test
    public void testPutQuizAuthorNull() {
        quizRestService.postQuiz();

        quizRestService.getQuizDto().setAuthor(null);

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        quizRestService.putQuiz();
    }

    @Test
    public void testPutQuizSubjectNameNull() {
        quizRestService.postQuiz();

        quizRestService.getQuizDto().setSubjectName(null);

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        quizRestService.putQuiz();
    }

    @Test
    public void testPutQuizQuizLevelNull() {
        quizRestService.postQuiz();

        quizRestService.getQuizDto().setQuizLevel(null);

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        quizRestService.putQuiz();
    }

    // DELETE
    @Test
    public void testDeleteQuiz() {
        quizRestService.postQuiz();

        quizRestService.deleteQuiz(quizRestService.getQuizDto().getId());

        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        quizRestService.deleteQuiz(quizRestService.getQuizDto().getId());
    }

    @Test
    public void testDeleteQuizPreAuthorize() {
        quizRestService.postQuiz();

        restService.loginManager();// PreAuthorize("hasRole('TEACHER')")

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        quizRestService.deleteQuiz(quizRestService.getQuizDto().getId());
    }

    @Test
    public void testDeleteQuizNoBearerAuth() {
        quizRestService.postQuiz();

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder().path(QuizController.QUIZ).path(QuizController.PATH_ID)
                .expand(quizRestService.getQuizDto().getId()).delete().build();
    }

    @Test
    public void testDeleteQuizFieldNotFoundExceptionId() {
        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        quizRestService.deleteQuiz("xxx");
    }
	
	/* TODO @Test
	public void testDeleteQuizForbiddenDeleteExceptionEvaluation() {
		
		QuizDto qDto = quizRestService.getQuizById(quizRestService.getQuizDto().getId());

		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		quizRestService.deleteQuiz(qDto.getId());
	}*/

    // GET********************************
    @Test
    public void testGetQuizById() {
        quizRestService.postQuiz();
        QuizDto qDto = quizRestService.getQuizById(quizRestService.getQuizDto().getId());
        Assert.assertEquals(qDto, quizRestService.getQuizDto());
    }

    @Test
    public void testGetQuizByIdPreAuthorize() {
        quizRestService.postQuiz();

        restService.loginAdmin();// PreAuthorize("hasRole('TEACHER') or
        // MANAGER")

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        quizRestService.getQuizById(quizRestService.getQuizDto().getId());

    }

    @Test
    public void testGetQuizByIdNoBearerAuth() {
        quizRestService.postQuiz();

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder().path(QuizController.QUIZ).path(QuizController.PATH_ID)
                .expand(quizRestService.getQuizDto().getId()).body(quizRestService.getQuizDto().getId()).get()
                .build();
    }

    @Test
    public void testGetQuizByIdFieldNotFoundExceptionId() {
        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        quizRestService.getQuizById("xxx");

    }

    //
    @Test
    public void testGetFullQuizes() {
        quizRestService.getFullQuizes();

        Assert.assertTrue(quizRestService.getListQuizDto().size() > 0);
    }

    @Test
    public void testGetFullQuizesPreAuthorize() {
        restService.loginAdmin();// PreAuthorize("hasRole('TEACHER') or MANAGER")

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        quizRestService.getFullQuizes();

    }

    @Test
    public void testGetFullQuizsNoBearerAuth() {
        quizRestService.getFullQuizes();

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder().path(QuizController.QUIZ).get().build();

    }

}
