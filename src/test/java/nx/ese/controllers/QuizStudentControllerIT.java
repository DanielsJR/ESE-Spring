package nx.ese.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nx.ese.documents.core.CourseName;
import nx.ese.documents.core.SubjectName;
import nx.ese.dtos.*;
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

import java.time.LocalDate;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class QuizStudentControllerIT {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private QuizStudentRestService quizStudentRestService;

    @Autowired
    private RestService restService;

    @Autowired
    private GradeRestService gradeRestService;

    @Autowired
    private EvaluationRestService evaluationRestService;

    @Autowired
    private SubjectRestService subjectRestService;

    @Autowired
    private CourseRestService courseRestService;

    @Before
    public void before() {
        quizStudentRestService.createQuizStudentsDto();
    }

    @After
    public void delete() {
        quizStudentRestService.deleteQuizStudentsDto();
    }

    // POST********************************
    @Test
    public void testPostQuizStudent() {
        restService.loginStudent();
        quizStudentRestService.postQuizStudent(restService.getStudentUsername());

        restService.loginTeacher();
        QuizStudentDto qsDto = quizStudentRestService.getQuizStudentById(quizStudentRestService.getQuizStudentDto().getId());
        assertEquals(qsDto, quizStudentRestService.getQuizStudentDto());
        assertEquals(qsDto.getCreatedBy(), restService.getStudentUsername());
    }

    @Test
    public void testPostQuizStudentPreAuthorize() {
        restService.loginTeacher();// PreAuthorize("hasRole('STUDENT')")

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        quizStudentRestService.postQuizStudent(restService.getStudentUsername());
    }

    @Test
    public void testPostQuizStudentPreAuthorizeUsername() {
        restService.loginStudent();// PreAuthorize #username == authentication.principal.username

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        quizStudentRestService.postQuizStudent(restService.getTeacherUsername());
    }

    @Test
    public void testPostQuizStudentNoBearerAuth() {
        restService.loginStudent();

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(QuizStudentController.QUIZ_STUDENT)
                .path(QuizStudentController.STUDENT)
                .path(QuizStudentController.PATH_USERNAME).expand(restService.getStudentUsername())
                //.bearerAuth(restService.getAuthToken().getToken())
                .body(quizStudentRestService.getQuizStudentDto())
                .post()
                .build();
    }

    @Test
    public void testPostQuizStudentFieldInvalidExceptionId() {
        restService.loginStudent();
        quizStudentRestService.postQuizStudent(restService.getStudentUsername());

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        quizStudentRestService.postQuizStudent(restService.getStudentUsername());// it goes with Id
    }

    @Test
    public void testPostQuizStudentDocumentAlreadyExistException() {
        restService.loginStudent();
        quizStudentRestService.postQuizStudent(restService.getStudentUsername());

        quizStudentRestService.getQuizStudentDto2().setCorrespondItems(quizStudentRestService.getQuizStudentDto().getCorrespondItems());
        quizStudentRestService.getQuizStudentDto2().setTrueFalseItems(quizStudentRestService.getQuizStudentDto().getTrueFalseItems());
        quizStudentRestService.getQuizStudentDto2().setMultipleSelectionItems(quizStudentRestService.getQuizStudentDto().getMultipleSelectionItems());
        quizStudentRestService.getQuizStudentDto2().setIncompleteTextItems(quizStudentRestService.getQuizStudentDto().getIncompleteTextItems());

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        quizStudentRestService.postQuizStudent2(restService.getStudentUsername());
    }

    @Test
    public void testPostQuizStudentBodyNull() {
        restService.loginStudent();

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        restService.restBuilder()
                .path(QuizStudentController.QUIZ_STUDENT)
                .path(QuizStudentController.STUDENT)
                .path(QuizStudentController.PATH_USERNAME).expand(restService.getStudentUsername())
                .bearerAuth(restService.getAuthToken().getToken())
                .body(null)//@NotNull
                .post()
                .build();
    }


    // PUT********************************
    @Test
    public void testPutQuizStudent() {
        restService.loginStudent();
        quizStudentRestService.postQuizStudent(restService.getStudentUsername());

        quizStudentRestService.getQuizStudentDto().setCorrespondItems(null);
        restService.loginManager();
        quizStudentRestService.putQuizStudent();

        QuizStudentDto qsDto = quizStudentRestService.getQuizStudentById(quizStudentRestService.getQuizStudentDto().getId());
        assertEquals(qsDto, quizStudentRestService.getQuizStudentDto());
        assertNull(qsDto.getCorrespondItems());

    }

    @Test
    public void testPutQuizStudentPreAuthorizeRole() {
        restService.loginStudent();
        quizStudentRestService.postQuizStudent(restService.getStudentUsername());

        quizStudentRestService.getQuizStudentDto().setCorrespondItems(null);

        restService.loginStudent();// PreAuthorize("hasRole('MANAGER')")
        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        quizStudentRestService.putQuizStudent();
    }

    @Test
    public void testPutQuizStudentNoBearerAuth() {
        restService.loginStudent();
        quizStudentRestService.postQuizStudent(restService.getStudentUsername());

        quizStudentRestService.getQuizStudentDto().setCorrespondItems(null);

        restService.loginManager();
        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(QuizStudentController.QUIZ_STUDENT)
                .path(QuizStudentController.PATH_ID).expand(quizStudentRestService.getQuizStudentDto().getId())
                //.bearerAuth(restService.getAuthToken().getToken())
                .body(quizStudentRestService.getQuizStudentDto())
                .put()
                .build();
    }

    @Test
    public void testPutQuizStudentBodyNull() {
        restService.loginStudent();
        quizStudentRestService.postQuizStudent(restService.getStudentUsername());

        restService.loginManager();
        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        restService.restBuilder()
                .path(QuizStudentController.QUIZ_STUDENT)
                .path(QuizStudentController.PATH_ID).expand(quizStudentRestService.getQuizStudentDto().getId())
                .bearerAuth(restService.getAuthToken().getToken())
                .body(null)//@NotNull
                .put()
                .build();
    }

    @Test
    public void testPutQuizStudentDocumentAlreadyExistException() {
        restService.loginStudent();
        quizStudentRestService.postQuizStudent(restService.getStudentUsername());
        quizStudentRestService.postQuizStudent2(restService.getStudentUsername());

        quizStudentRestService.getQuizStudentDto().setCorrespondItems(quizStudentRestService.getQuizStudentDto2().getCorrespondItems());
        quizStudentRestService.getQuizStudentDto().setTrueFalseItems(quizStudentRestService.getQuizStudentDto2().getTrueFalseItems());
        quizStudentRestService.getQuizStudentDto().setMultipleSelectionItems(quizStudentRestService.getQuizStudentDto2().getMultipleSelectionItems());
        quizStudentRestService.getQuizStudentDto().setIncompleteTextItems(quizStudentRestService.getQuizStudentDto2().getIncompleteTextItems());

        restService.loginManager();
        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        quizStudentRestService.putQuizStudent();
    }

    @Test
    public void testPutQuizStudentFieldNotFoundExceptionId() {
        restService.loginStudent();
        quizStudentRestService.postQuizStudent(restService.getStudentUsername());

        restService.loginManager();
        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        restService.restBuilder()
                .path(QuizStudentController.QUIZ_STUDENT)
                .path(QuizStudentController.PATH_ID).expand("xxx")
                .bearerAuth(restService.getAuthToken().getToken())
                .body(quizStudentRestService.getQuizStudentDto())
                .put()
                .build();

    }


    // DELETE
    @Test
    public void testDeleteQuizStudent() {
        restService.loginStudent();
        quizStudentRestService.postQuizStudent(restService.getStudentUsername());

        restService.loginManager();
        quizStudentRestService.deleteQuizStudent(quizStudentRestService.getQuizStudentDto().getId());

        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        quizStudentRestService.getQuizStudentById(quizStudentRestService.getQuizStudentDto().getId());
    }

    @Test
    public void testDeleteQuizStudentPreAuthorizeRole() {
        restService.loginStudent();
        quizStudentRestService.postQuizStudent(restService.getStudentUsername());

        restService.loginStudent();// PreAuthorize("hasRole('TEACHER')")
        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        quizStudentRestService.deleteQuizStudent(quizStudentRestService.getQuizStudentDto().getId());
    }

    @Test
    public void testDeleteQuizStudentNoBearerAuth() {
        restService.loginStudent();
        quizStudentRestService.postQuizStudent(restService.getStudentUsername());

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(QuizStudentController.QUIZ_STUDENT)
                .path(QuizStudentController.PATH_ID).expand(quizStudentRestService.getQuizStudentDto().getId())
                //.bearerAuth(restService.getAuthToken().getToken())
                .delete()
                .build();
    }

    @Test
    public void testDeleteQuizStudentFieldNotFoundExceptionId() {
        restService.loginManager();
        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        quizStudentRestService.deleteQuizStudent("xxx");
    }

    @Test
    public void testDeleteQuizForbiddenDeleteExceptionEvaluation() {
        restService.loginManager();
        CourseDto cDto = courseRestService.getCourseByNameAndYear(CourseName.OCTAVO_C, "2018");
        SubjectDto sDto = subjectRestService.getSubjectByNameAndCourse(SubjectName.MATEMATICAS, cDto.getId());
        EvaluationDto eDto = evaluationRestService.getEvaluationBySubjectAndDate(sDto.getId(), LocalDate.of(2018, 6, 11));
        List rawList = gradeRestService.getGradesByEvaluation(eDto.getId());

        ObjectMapper mapper = new ObjectMapper();
        List<GradeDto> gList = mapper.convertValue(rawList, new TypeReference<List<GradeDto>>() {
        });

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        quizStudentRestService.deleteQuizStudent(gList.get(0).getQuizStudent().getId());
    }


    // GET********************************
    @Test
    public void testGetQuizStudentById() {
        restService.loginStudent();
        quizStudentRestService.postQuizStudent(restService.getStudentUsername());

        restService.loginTeacher();
        QuizStudentDto gDto = quizStudentRestService.getQuizStudentById(quizStudentRestService.getQuizStudentDto().getId());
        Assert.assertEquals(gDto, quizStudentRestService.getQuizStudentDto());
    }

    @Test
    public void testGetQuizStudentByIdPreAuthorize() {
        restService.loginStudent();
        quizStudentRestService.postQuizStudent(restService.getStudentUsername());

        restService.loginStudent();// PreAuthorize("hasRole('TEACHER') or MANAGER")

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        quizStudentRestService.getQuizStudentById(quizStudentRestService.getQuizStudentDto().getId());

    }

    @Test
    public void testGetQuizStudentByIdNoBearerAuth() {
        restService.loginStudent();
        quizStudentRestService.postQuizStudent(restService.getStudentUsername());

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(QuizStudentController.QUIZ_STUDENT)
                .path(QuizStudentController.PATH_ID).expand(quizStudentRestService.getQuizStudentDto().getId())
                //.bearerAuth(restService.getAuthToken().getToken())
                .body(quizStudentRestService.getQuizStudentDto().getId())
                .get()
                .build();
    }

    @Test
    public void testGetQuizStudentByIdFieldNotFoundExceptionId() {
        restService.loginTeacher();
        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        quizStudentRestService.getQuizStudentById("xxx");
    }

}
