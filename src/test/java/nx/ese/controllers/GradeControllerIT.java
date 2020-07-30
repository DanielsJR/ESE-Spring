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

import nx.ese.dtos.GradeDto;
import nx.ese.services.GradeRestService;
import nx.ese.services.HttpMatcher;
import nx.ese.services.RestService;
import nx.ese.services.SubjectRestService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class GradeControllerIT {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private GradeRestService gradeRestService;

    @Autowired
    private SubjectRestService subjectRestService;

    @Autowired
    private RestService restService;

    @Before
    public void before() {
        gradeRestService.createGradesDto();
        restService.loginTeacher();
    }

    @After
    public void delete() {
        gradeRestService.deleteGrades();
    }

    // POST********************************
    @Test
    public void testPostGrade() {
        gradeRestService.postGrade();

        GradeDto gDto = gradeRestService.getGradeById(gradeRestService.getGradeDto().getId());
        assertEquals(gDto, gradeRestService.getGradeDto());

        gradeRestService.postGrade2();

        GradeDto gDto2 = gradeRestService.getGradeById(gradeRestService.getGradeDto2().getId());
        assertEquals(gDto2.getId(), gradeRestService.getGradeDto2().getId());
    }

    @Test
    public void testPostGradePreAuthorize() {
        restService.loginManager();// PreAuthorize("hasRole('TEACHER')")

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        gradeRestService.postGrade();
    }

    @Test
    public void testPostGradeNoBearerAuth() {
        gradeRestService.postGrade();

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder().path(GradeController.GRADE).body(gradeRestService.getGradeDto()).post().build();
    }

    @Test
    public void testPostGradeFieldInvalidExceptionId() {
        gradeRestService.postGrade();

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        gradeRestService.postGrade();// it goes with Id
    }

    @Test
    public void testPostGradeDocumentAlreadyExistException() {
        gradeRestService.postGrade();

        gradeRestService.getGradeDto2().setStudent(gradeRestService.getGradeDto().getStudent());
        gradeRestService.getGradeDto2().setEvaluation(gradeRestService.getGradeDto().getEvaluation());

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        gradeRestService.postGrade2();

    }

    @Test
    public void testPostGradeBodyNull() {
        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        restService.restBuilder().path(GradeController.GRADE).bearerAuth(restService.getAuthToken().getToken())
                .body(null).post().build();
    }


    @Test
    public void testPostGradeStudentNull() {
        gradeRestService.getGradeDto().setStudent(null);

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        gradeRestService.postGrade();
    }

    @Test
    public void testPostGradeEvaluationNull() {
        gradeRestService.getGradeDto().setEvaluation(null);

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        gradeRestService.postGrade();
    }


    // PUT********************************
    @Test
    public void testPutGrade() {
        gradeRestService.postGrade();

        gradeRestService.getGradeDto().setGrade(1.0);
        gradeRestService.putGrade();

        GradeDto gDto = gradeRestService.getGradeById(gradeRestService.getGradeDto().getId());
        Assert.assertEquals(0, Double.compare(gDto.getGrade(), 1.0));
    }

    @Test
    public void testPutGradePreAuthorize() {
        gradeRestService.postGrade();

        gradeRestService.getGradeDto().setGrade(1.0);

        restService.loginManager();// PreAuthorize("hasRole('TEACHER')")

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        gradeRestService.putGrade();
    }

    @Test
    public void testPutGradeNoBearerAuth() {
        gradeRestService.postGrade();

        gradeRestService.getGradeDto().setGrade(1.0);

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder().path(GradeController.GRADE).path(GradeController.PATH_ID)
                .expand(gradeRestService.getGradeDto().getId()).body(gradeRestService.getGradeDto()).put().build();

    }

    @Test
    public void testPutGradeDocumentAlreadyExistException() {
        gradeRestService.postGrade();
        gradeRestService.postGrade2();

        gradeRestService.getGradeDto().setStudent(gradeRestService.getGradeDto2().getStudent());
        gradeRestService.getGradeDto().setEvaluation(gradeRestService.getGradeDto2().getEvaluation());

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        gradeRestService.putGrade();

    }

    @Test
    public void testPutGradeFieldNotFoundExceptionId() {
        gradeRestService.getGradeDto().setGrade(1.0);

        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        restService.restBuilder().path(GradeController.GRADE).path(GradeController.PATH_ID).expand("xxx")
                .bearerAuth(restService.getAuthToken().getToken()).body(gradeRestService.getGradeDto()).put().build();
    }

    @Test
    public void testPutGradeBodyNull() {
        gradeRestService.postGrade();

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        restService.restBuilder().path(GradeController.GRADE).path(GradeController.PATH_ID)
                .expand(gradeRestService.getGradeDto().getId()).bearerAuth(restService.getAuthToken().getToken())
                .body(null).put().build();

    }


    @Test
    public void testPutGradeStudentNull() {
        gradeRestService.postGrade();

        gradeRestService.getGradeDto().setStudent(null);

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        gradeRestService.putGrade();
    }

    @Test
    public void testPutGradeEvaluationNull() {
        gradeRestService.postGrade();

        gradeRestService.getGradeDto().setEvaluation(null);

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        gradeRestService.putGrade();
    }


    // DELETE
    @Test
    public void testDeleteGrade() {
        gradeRestService.postGrade();

        gradeRestService.deleteGrade(gradeRestService.getGradeDto().getId());

        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        gradeRestService.getGradeById(gradeRestService.getGradeDto().getId());
    }

    @Test
    public void testDeleteGradePreAuthorize() {
        gradeRestService.postGrade();

        restService.loginManager();// PreAuthorize("hasRole('TEACHER')")

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        gradeRestService.deleteGrade(gradeRestService.getGradeDto().getId());
    }

    @Test
    public void testDeleteGradeNoBearerAuth() {
        gradeRestService.postGrade();

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder().path(GradeController.GRADE).path(GradeController.PATH_ID)
                .expand(gradeRestService.getGradeDto().getId()).delete().build();
    }

    @Test
    public void testDeleteGradeFieldNotFoundExceptionId() {
        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        gradeRestService.deleteGrade("xxx");
    }

    // GET********************************
    @Test
    public void testGetGradeById() {
        gradeRestService.postGrade();
        GradeDto Gdto = gradeRestService.getGradeById(gradeRestService.getGradeDto().getId());
        Assert.assertEquals(Gdto, gradeRestService.getGradeDto());
    }

    @Test
    public void testGetGradeByIdPreAuthorize() {
        gradeRestService.postGrade();

        restService.loginAdmin();// PreAuthorize("hasRole('TEACHER') or
        // MANAGER")

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        gradeRestService.getGradeById(gradeRestService.getGradeDto().getId());

    }

    @Test
    public void testGetGradeByIdNoBearerAuth() {
        gradeRestService.postGrade();

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder().path(GradeController.GRADE).path(GradeController.PATH_ID)
                .expand(gradeRestService.getGradeDto().getId()).body(gradeRestService.getGradeDto().getId()).get()
                .build();
    }

    @Test
    public void testGetGradeByIdFieldNotFoundExceptionId() {
        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        gradeRestService.getGradeById("xxx");
    }

    //
    @Test
    public void testGetFullGrades() {
        gradeRestService.getFullGrades();

        Assert.assertTrue(gradeRestService.getListGradeDto().size() > 0);
    }

    @Test
    public void testGetFullGradesPreAuthorize() {
        restService.loginAdmin();// PreAuthorize("hasRole('TEACHER') or
        // MANAGER")

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        gradeRestService.getFullGrades();

    }

    @Test
    public void testGetFullGradesNoBearerAuth() {
        gradeRestService.getFullGrades();

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder().path(GradeController.GRADE).get().build();

    }

    @Test
    public void testGetGradesBySubject() {
        gradeRestService.postGrade();
        String sjcId = subjectRestService.getSubjectDto().getId();
        gradeRestService.getGradesBySubject(sjcId);

        Assert.assertTrue(gradeRestService.getListGradeDto().size() > 0);
    }

}
