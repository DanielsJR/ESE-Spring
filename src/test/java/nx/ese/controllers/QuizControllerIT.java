package nx.ese.controllers;

import static org.junit.Assert.assertEquals;

import lombok.Getter;
import nx.ese.documents.core.CourseName;
import nx.ese.documents.core.SubjectName;
import nx.ese.dtos.CourseDto;
import nx.ese.dtos.EvaluationDto;
import nx.ese.dtos.SubjectDto;
import nx.ese.services.*;
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

import java.time.LocalDate;
import java.util.List;

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
    private SubjectRestService subjectRestService;

    @Autowired
    private CourseRestService courseRestService;

    @Autowired
    private RestService restService;

    @Getter
    private String teacherUsername;

    @Getter
    private String teacherUsername2;

    @Before
    public void before() {
        quizRestService.createQuizesDto();

        teacherUsername = quizRestService.getQuizDto().getAuthor().getUsername();
        teacherUsername2 = quizRestService.getQuizDto2().getAuthor().getUsername();
        restService.loginUser(teacherUsername, teacherUsername + "@ESE1");
    }

    @After
    public void delete() {
        quizRestService.deleteQuizesDto();
    }

    // POST********************************
    @Test
    public void testPostQuiz() {
        quizRestService.postQuiz(teacherUsername);

        QuizDto qDto = quizRestService.getTeacherQuizById(quizRestService.getQuizDto().getId(), teacherUsername);
        assertEquals(qDto, quizRestService.getQuizDto());

        restService.loginUser(teacherUsername2, teacherUsername2 + "@ESE1");
        quizRestService.postQuiz2(teacherUsername2);

        QuizDto gDto2 = quizRestService.getTeacherQuizById(quizRestService.getQuizDto2().getId(), teacherUsername2);
        assertEquals(gDto2.getId(), quizRestService.getQuizDto2().getId());
    }

    @Test
    public void testPostQuizPreAuthorizeRole() {
        restService.loginManager();// PreAuthorize("hasRole('TEACHER')")

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        quizRestService.postQuiz(teacherUsername);
    }

    @Test
    public void testPostQuizPreAuthorizeUsername() {
        restService.loginTeacher();//#username == authentication.principal.username

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        quizRestService.postQuiz(teacherUsername);
    }

    @Test
    public void testPostQuizNoBearerAuth() {
        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(QuizController.QUIZ)
                .path(QuizController.TEACHER)
                .path(QuizController.PATH_USERNAME).expand(teacherUsername)
                //.bearerAuth(restService.getAuthToken().getToken())
                .body(quizRestService.getQuizDto())
                .post()
                .build();
    }

    @Test
    public void testPostQuizBodyNull() {
        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        restService.restBuilder()
                .path(QuizController.QUIZ)
                .path(QuizController.TEACHER)
                .path(QuizController.PATH_USERNAME).expand(teacherUsername)
                .bearerAuth(restService.getAuthToken().getToken())
                .body(null)//@NotNull
                .post()
                .build();
    }

    @Test
    public void testPostQuizTitleNull() {
        quizRestService.getQuizDto().setTitle(null);//@NotNull

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        quizRestService.postQuiz(teacherUsername);
    }

    @Test
    public void testPostQuizAuthorNull() {
        quizRestService.getQuizDto().setAuthor(null);//@NotNull

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        quizRestService.postQuiz(teacherUsername);
    }

    @Test
    public void testPostQuizSubjectNameNull() {
        quizRestService.getQuizDto().setSubjectName(null);//@NotNull

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        quizRestService.postQuiz(teacherUsername);
    }

    @Test
    public void testPostQuizQuizLevelNull() {
        quizRestService.getQuizDto().setQuizLevel(null);//@NotNull

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        quizRestService.postQuiz(teacherUsername);
    }

    @Test
    public void testPostQuizFieldInvalidExceptionId() {
        quizRestService.postQuiz(teacherUsername);

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        quizRestService.postQuiz(teacherUsername);// it goes with Id
    }

    @Test
    public void testPostQuizDocumentAlreadyExistException() {
        quizRestService.postQuiz(teacherUsername);

        quizRestService.getQuizDto2().setTitle(quizRestService.getQuizDto().getTitle());
        quizRestService.getQuizDto2().setAuthor(quizRestService.getQuizDto().getAuthor());
        quizRestService.getQuizDto2().setSubjectName(quizRestService.getQuizDto().getSubjectName());
        quizRestService.getQuizDto2().setQuizLevel(quizRestService.getQuizDto().getQuizLevel());

        quizRestService.getQuizDto2().setCorrespondItems(quizRestService.getQuizDto().getCorrespondItems());
        quizRestService.getQuizDto2().setTrueFalseItems(quizRestService.getQuizDto().getTrueFalseItems());
        quizRestService.getQuizDto2().setMultipleSelectionItems(quizRestService.getQuizDto().getMultipleSelectionItems());
        quizRestService.getQuizDto2().setIncompleteTextItems(quizRestService.getQuizDto().getIncompleteTextItems());

        restService.loginUser(teacherUsername2, teacherUsername2 + "@ESE1");
        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        quizRestService.postQuiz2(teacherUsername2);

    }

    @Test
    public void testPostQuizIsTeacherInQuiz() {
        restService.loginUser(teacherUsername2, teacherUsername2 + "@ESE1");

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        quizRestService.postQuiz(teacherUsername2);
    }

    // PUT********************************
    @Test
    public void testPutQuiz() {
        quizRestService.postQuiz(teacherUsername);

        quizRestService.getQuizDto().setTitle("new title");
        quizRestService.putQuiz(teacherUsername);

        QuizDto gDto = quizRestService.getTeacherQuizById(quizRestService.getQuizDto().getId(), teacherUsername);
        Assert.assertEquals("new title", gDto.getTitle());
    }

    @Test
    public void testPutQuizPreAuthorizeRole() {
        quizRestService.postQuiz(teacherUsername);

        quizRestService.getQuizDto().setTitle("new title");

        restService.loginManager();// PreAuthorize("hasRole('TEACHER')")

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        quizRestService.putQuiz(teacherUsername);
    }

    @Test
    public void testPutQuizPreAuthorizeUsername() {
        quizRestService.postQuiz(teacherUsername);

        quizRestService.getQuizDto().setTitle("new title");
        quizRestService.putQuiz(teacherUsername);

        restService.loginTeacher();//#username == authentication.principal.username

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        quizRestService.getTeacherQuizById(quizRestService.getQuizDto().getId(), teacherUsername);

    }

    @Test
    public void testPutQuizNoBearerAuth() {
        quizRestService.postQuiz(teacherUsername);

        quizRestService.getQuizDto().setTitle("new title");

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(QuizController.QUIZ)
                .path(QuizController.PATH_ID).expand(quizRestService.getQuizDto().getId())
                .path(QuizController.TEACHER)
                .path(QuizController.PATH_USERNAME).expand(teacherUsername)
                //.bearerAuth(restService.getAuthToken().getToken())
                .body(quizRestService.getQuizDto())
                .put()
                .build();
    }

    @Test
    public void testPutQuizBodyNull() {
        quizRestService.postQuiz(teacherUsername);

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        restService.restBuilder().path(QuizController.QUIZ)
                .path(QuizController.PATH_ID).expand(quizRestService.getQuizDto().getId())
                .path(QuizController.TEACHER)
                .path(QuizController.PATH_USERNAME).expand(teacherUsername)
                .bearerAuth(restService.getAuthToken().getToken())
                .body(null)//@NotNull
                .put()
                .build();
    }

    @Test
    public void testPutQuizTitleNull() {
        quizRestService.postQuiz(teacherUsername);

        quizRestService.getQuizDto().setTitle(null);//@NotNull

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        quizRestService.putQuiz(teacherUsername);
    }

    @Test
    public void testPutQuizAuthorNull() {
        quizRestService.postQuiz(teacherUsername);

        quizRestService.getQuizDto().setAuthor(null);//@NotNull

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        quizRestService.putQuiz(teacherUsername);
    }

    @Test
    public void testPutQuizSubjectNameNull() {
        quizRestService.postQuiz(teacherUsername);

        quizRestService.getQuizDto().setSubjectName(null);//@NotNull

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        quizRestService.putQuiz(teacherUsername);
    }

    @Test
    public void testPutQuizQuizLevelNull() {
        quizRestService.postQuiz(teacherUsername);

        quizRestService.getQuizDto().setQuizLevel(null);//@NotNull

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        quizRestService.putQuiz(teacherUsername);
    }

    @Test
    public void testPutQuizDocumentAlreadyExistException() {
        quizRestService.postQuiz(teacherUsername);

        restService.loginUser(teacherUsername2, teacherUsername2 + "@ESE1");
        quizRestService.postQuiz2(teacherUsername2);

        quizRestService.getQuizDto().setTitle(quizRestService.getQuizDto2().getTitle());
        quizRestService.getQuizDto().setAuthor(quizRestService.getQuizDto2().getAuthor());
        quizRestService.getQuizDto().setSubjectName(quizRestService.getQuizDto2().getSubjectName());
        quizRestService.getQuizDto().setQuizLevel(quizRestService.getQuizDto2().getQuizLevel());

        quizRestService.getQuizDto().setCorrespondItems(quizRestService.getQuizDto2().getCorrespondItems());
        quizRestService.getQuizDto().setTrueFalseItems(quizRestService.getQuizDto2().getTrueFalseItems());
        quizRestService.getQuizDto().setMultipleSelectionItems(quizRestService.getQuizDto2().getMultipleSelectionItems());
        quizRestService.getQuizDto().setIncompleteTextItems(quizRestService.getQuizDto2().getIncompleteTextItems());

        restService.loginUser(teacherUsername, teacherUsername + "@ESE1");
        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        quizRestService.putQuiz(teacherUsername);
    }

    @Test
    public void testPutQuizIsTeacherInQuiz() {
        quizRestService.postQuiz(teacherUsername);

        quizRestService.getQuizDto().setTitle("new title");

        restService.loginUser(teacherUsername2, teacherUsername2 + "@ESE1");

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        quizRestService.putQuiz(teacherUsername2);

    }

    @Test
    public void testPutQuizFieldNotFoundExceptionId() {
        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        restService.restBuilder()
                .path(QuizController.QUIZ)
                .path(QuizController.PATH_ID).expand("xxx")
                .path(QuizController.TEACHER)
                .path(QuizController.PATH_USERNAME).expand(teacherUsername)
                .bearerAuth(restService.getAuthToken().getToken())
                .body(quizRestService.getQuizDto())
                .put()
                .build();
    }


    // DELETE********************************
    @Test
    public void testDeleteQuiz() {
        quizRestService.postQuiz(teacherUsername);

        quizRestService.deleteQuiz(quizRestService.getQuizDto().getId(), teacherUsername);

        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        quizRestService.getTeacherQuizById(quizRestService.getQuizDto().getId(), teacherUsername);
    }

    @Test
    public void testDeleteQuizPreAuthorizeRole() {
        quizRestService.postQuiz(teacherUsername);

        restService.loginManager();// PreAuthorize("hasRole('TEACHER')")

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        quizRestService.deleteQuiz(quizRestService.getQuizDto().getId(), teacherUsername);
    }

    @Test
    public void testDeleteQuizPreAuthorizeUsername() {
        quizRestService.postQuiz(teacherUsername);

        restService.loginTeacher();//#username == authentication.principal.username

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        quizRestService.deleteQuiz(quizRestService.getQuizDto().getId(), teacherUsername);
    }

    @Test
    public void testDeleteQuizNoBearerAuth() {
        quizRestService.postQuiz(teacherUsername);

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(QuizController.QUIZ)
                .path(QuizController.PATH_ID).expand(quizRestService.getQuizDto().getId())
                //.bearerAuth(restService.getAuthToken().getToken())
                .delete()
                .build();
    }

    @Test
    public void testDeleteQuizFieldNotFoundExceptionId() {
        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        quizRestService.deleteQuiz("xxx", teacherUsername);
    }

    @Test
    public void testDeleteQuizForbiddenDeleteExceptionEvaluation() {
        restService.loginManager();
        CourseDto cDto = courseRestService.getCourseByNameAndYear(CourseName.OCTAVO_C, "2018");
        SubjectDto sDto = subjectRestService.getSubjectByNameAndCourse(SubjectName.MATEMATICAS, cDto.getId());
        EvaluationDto eDto = evaluationRestService.getEvaluationBySubjectAndDate(sDto.getId(), LocalDate.of(2018, 6, 11));

        restService.loginUser("u010", "p010@ESE1");
        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        quizRestService.deleteQuiz(eDto.getQuiz().getId(), "u010");
    }

    @Test
    public void testDeleteQuizIsTeacherInQuiz() {
        quizRestService.postQuiz(teacherUsername);

        restService.loginUser(teacherUsername2, teacherUsername2 + "@ESE1");

        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        quizRestService.deleteQuiz(quizRestService.getQuizDto().getId(), teacherUsername2);

    }

    // GET********************************
    @Test
    public void testGetQuizById() {
        quizRestService.postQuiz(teacherUsername);

        restService.loginManager();
        QuizDto qDto = quizRestService.getQuizById(quizRestService.getQuizDto().getId());
        Assert.assertEquals(qDto, quizRestService.getQuizDto());
    }

    @Test
    public void testGetQuizByIdPreAuthorizeRole() {
        quizRestService.postQuiz(teacherUsername);

        restService.loginTeacher();// PreAuthorize("hasRole("MANAGER")

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        quizRestService.getQuizById(quizRestService.getQuizDto().getId());

    }

    @Test
    public void testGetQuizByIdNoBearerAuth() {
        quizRestService.postQuiz(teacherUsername);

        restService.loginManager();
        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(QuizController.QUIZ)
                .path(QuizController.PATH_ID).expand(quizRestService.getQuizDto().getId())
                //.bearerAuth(restService.getAuthToken().getToken())
                .body(quizRestService.getQuizDto().getId())
                .get()
                .build();
    }

    @Test
    public void testGetQuizByIdFieldNotFoundExceptionId() {
        restService.loginManager();
        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        quizRestService.getQuizById("xxx");

    }

    //
    @Test
    public void testGetTeacherQuizById() {
        quizRestService.postQuiz(teacherUsername);

        QuizDto qDto = quizRestService.getTeacherQuizById(quizRestService.getQuizDto().getId(), teacherUsername);
        Assert.assertEquals(qDto, quizRestService.getQuizDto());
    }

    @Test
    public void testGetTeacherQuizByIdPreAuthorizeRole() {
        quizRestService.postQuiz(teacherUsername);

        restService.loginManager();// PreAuthorize("hasRole("TEACHER")
        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        QuizDto qDto = quizRestService.getTeacherQuizById(quizRestService.getQuizDto().getId(), teacherUsername);
    }

    @Test
    public void testGetTeacherQuizByIdPreAuthorizeUsername() {
        quizRestService.postQuiz(teacherUsername);

        restService.loginTeacher();//#username == authentication.principal.username
        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        QuizDto qDto = quizRestService.getTeacherQuizById(quizRestService.getQuizDto().getId(), teacherUsername);
    }

    @Test
    public void testGetTeacherQuizByIdNoBearerAuth() {
        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(QuizController.QUIZ)
                .path(QuizController.PATH_ID).expand(quizRestService.getQuizDto().getId())
                .path(QuizController.TEACHER)
                .path(QuizController.PATH_USERNAME).expand(teacherUsername)
                //.bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }

    @Test
    public void testGetTeacherQuizByIdIsTeacherAuthor() {
        quizRestService.postQuiz(teacherUsername);

        restService.loginUser(teacherUsername2, teacherUsername2 + "@ESE1");
        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        quizRestService.getTeacherQuizById(quizRestService.getQuizDto().getId(), teacherUsername2);
    }

    @Test
    public void testGetTeacherQuizByIdDocumentNotFoundException() {
        quizRestService.postQuiz(teacherUsername);

        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        quizRestService.getTeacherQuizById("xxx", teacherUsername);
    }

    //
    @Test
    public void testGetQuizes() {
        restService.loginManager();
        List<QuizDto> list = quizRestService.getQuizes();
        Assert.assertTrue(list.size() > 0);
    }

    @Test
    public void testGetQuizesPreAuthorizeRole() {
        restService.loginTeacher();// PreAuthorize("hasRole MANAGER")

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        quizRestService.getQuizes();
    }

    @Test
    public void testGetQuizesNoBearerAuth() {
        restService.loginManager();

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(QuizController.QUIZ)
                //.bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }

    //
    @Test
    public void testGetTeacherQuizes() {
        quizRestService.postQuiz(teacherUsername);

        List<QuizDto> list = quizRestService.getTeacherQuizes(teacherUsername);
        Assert.assertTrue(list.size() > 0);
    }

    @Test
    public void testGetTeacherQuizesPreAuthorizeRole() {
        quizRestService.postQuiz(teacherUsername);

        restService.loginManager();// PreAuthorize("hasRole("TEACHER")
        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        quizRestService.getTeacherQuizes(teacherUsername);

    }

    @Test
    public void testGetTeacherQuizesPreAuthorizeUsername() {
        quizRestService.postQuiz(teacherUsername);

        restService.loginTeacher();//#username == authentication.principal.username
        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        quizRestService.getTeacherQuizes(teacherUsername);
    }

    @Test
    public void testGetTeacherQuizesNoBearerAuth() {
        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(QuizController.QUIZ)
                .path(QuizController.TEACHER)
                .path(QuizController.PATH_USERNAME).expand(teacherUsername)
                //.bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }

    @Test
    public void testGetTeacherQuizesIsTeacherAuthor() {
        quizRestService.postQuiz(teacherUsername);

        restService.loginUser(teacherUsername2, teacherUsername2 + "@ESE1");
        List<QuizDto> list = quizRestService.getTeacherQuizes(teacherUsername2);
        assertEquals(0, list.size());
    }

    //
    @Test
    public void testGetQuizesByAuthor() {
        quizRestService.postQuiz(teacherUsername);
        String aId = quizRestService.getQuizDto().getAuthor().getId();

        restService.loginManager();
        List<QuizDto> list = quizRestService.getQuizesByAuthor(aId);
        Assert.assertTrue(list.size() > 0);
    }

    @Test
    public void testGetQuizesByAuthorPreAuthorizeRole() {
        quizRestService.postQuiz(teacherUsername);
        String aId = quizRestService.getQuizDto().getAuthor().getId();

        restService.loginTeacher();// PreAuthorize("hasRole MANAGER")

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        quizRestService.getQuizesByAuthor(aId);
    }

    @Test
    public void testGetQuizesByAuthorNoBearerAuth() {
        quizRestService.postQuiz(teacherUsername);
        String aId = quizRestService.getQuizDto().getAuthor().getId();

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(QuizController.QUIZ)
                .path(QuizController.AUTHOR)
                .path(QuizController.PATH_ID).expand(aId)
                //.bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }

    //
    @Test
    public void testGetTeacherQuizesByAuthor() {
        quizRestService.postQuiz(teacherUsername);
        String aId = quizRestService.getQuizDto().getAuthor().getId();

        List<QuizDto> list = quizRestService.getTeacherQuizesByAuthor(aId, teacherUsername);
        Assert.assertTrue(list.size() > 0);
    }

    @Test
    public void testGetTeacherQuizesByAuthorPreAuthorizeRole() {
        quizRestService.postQuiz(teacherUsername);
        String aId = quizRestService.getQuizDto().getAuthor().getId();

        restService.loginManager();// PreAuthorize("hasRole("TEACHER")
        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        quizRestService.getTeacherQuizesByAuthor(aId, teacherUsername);

    }

    @Test
    public void testGetTeacherQuizesByAuthorPreAuthorizeUsername() {
        quizRestService.postQuiz(teacherUsername);
        String aId = quizRestService.getQuizDto().getAuthor().getId();

        restService.loginTeacher();//#username == authentication.principal.username
        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        quizRestService.getTeacherQuizesByAuthor(aId, teacherUsername);
    }

    @Test
    public void testGetTeacherQuizesByAuthorNoBearerAuth() {
        quizRestService.postQuiz(teacherUsername);
        String aId = quizRestService.getQuizDto().getAuthor().getId();

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(QuizController.QUIZ)
                .path(QuizController.AUTHOR)
                .path(QuizController.PATH_ID).expand(aId)
                .path(QuizController.TEACHER)
                .path(QuizController.PATH_USERNAME).expand(teacherUsername)
                //.bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }

    @Test
    public void testGetTeacherQuizesByAuthorisTeacherAuthor() {
        quizRestService.postQuiz(teacherUsername);
        String aId = quizRestService.getQuizDto().getAuthor().getId();

        restService.loginUser(teacherUsername2, teacherUsername2 + "@ESE1");
        List<QuizDto> list = quizRestService.getTeacherQuizesByAuthor(aId, teacherUsername2);
        assertEquals(0, list.size());
    }

    @Test
    public void testGetTeacherQuizesByAuthorisQuizShared() {
        quizRestService.getQuizDto().setShared(true);
        quizRestService.postQuiz(teacherUsername);
        String aId = quizRestService.getQuizDto().getAuthor().getId();

        restService.loginUser(teacherUsername2, teacherUsername2 + "@ESE1");
        List<QuizDto> list = quizRestService.getTeacherQuizesByAuthor(aId, teacherUsername2);
        Assert.assertTrue(list.size() > 0);


        quizRestService.getQuizDto2().setShared(false);
        quizRestService.postQuiz2(teacherUsername2);
        String aId2 = quizRestService.getQuizDto2().getAuthor().getId();

        restService.loginUser(teacherUsername, teacherUsername + "@ESE1");
        List<QuizDto> list2 = quizRestService.getTeacherQuizesByAuthor(aId2, teacherUsername);
        assertEquals(0, list2.size());
    }


}
