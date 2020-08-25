package nx.ese.controllers;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nx.ese.documents.core.CourseName;
import nx.ese.documents.core.SubjectName;
import nx.ese.dtos.EvaluationDto;
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

import nx.ese.dtos.GradeDto;

import java.util.ArrayList;
import java.util.List;

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
    private CourseRestService courseRestService;

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

    @Test
    public void testPostGradeGradeNull() {
        gradeRestService.getGradeDto().setGrade(null);

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

    @Test
    public void testPutGradeGradeNull() {
        gradeRestService.postGrade();

        gradeRestService.getGradeDto().setGrade(null);

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        gradeRestService.putGrade();
    }

    // DELETE********************************
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
    public void testGetGradesBySubject() {
        gradeRestService.postGrade();
        String sId = subjectRestService.getSubjectDto().getId();
        restService.loginManager();
        gradeRestService.getGradesBySubject(sId);

        Assert.assertTrue(gradeRestService.getListGradeDto().size() > 0);
    }

    @Test
    public void testGetGradesBySubjectPreAuthorize() {
        gradeRestService.postGrade();
        String sId = subjectRestService.getSubjectDto().getId();

        restService.loginAdmin();// PreAuthorize("hasRole MANAGER")

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        gradeRestService.getGradesBySubject(sId);
    }

    @Test
    public void testGetGradesBySubjectNoBearerAuth() {
        gradeRestService.postGrade();
        String sId = subjectRestService.getSubjectDto().getId();

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(GradeController.GRADE)
                .path(GradeController.SUBJECT)
                .path(GradeController.PATH_ID).expand(sId)
                //.bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }

    //
    @Test
    public void testGetTeacherGrades() {
        restService.loginManager();
        String cId = courseRestService.getCourseByNameAndYear(CourseName.OCTAVO_C, "2018").getId();
        String sId = subjectRestService.getSubjectByNameAndCourse(SubjectName.MATEMATICAS, cId).getId();

        restService.loginUser("u010", "p010@ESE1");
        List<GradeDto> rawList = gradeRestService.getTeacherGradesBySubject(sId, "u010");

        ObjectMapper mapper = new ObjectMapper();
        List<GradeDto> gList = mapper.convertValue(rawList, new TypeReference<List<GradeDto>>() {
        });

        Assert.assertTrue(gList.size() > 0);
        Assert.assertEquals("u010", gList.get(0).getEvaluation().getSubject().getTeacher().getUsername());
    }

    @Test
    public void testGetTeacherGradesPreAuthorize() {
        restService.loginManager();
        String cId = courseRestService.getCourseByNameAndYear(CourseName.OCTAVO_C, "2018").getId();
        String sId = subjectRestService.getSubjectByNameAndCourse(SubjectName.MATEMATICAS, cId).getId();

        restService.loginAdmin();// PreAuthorize("hasRole TEACHER")
        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        gradeRestService.getTeacherGradesBySubject(sId, "u010");
    }

    @Test
    public void testGetTeacherGradesNoBearerAuth() {
        restService.loginManager();
        String cId = courseRestService.getCourseByNameAndYear(CourseName.OCTAVO_C, "2018").getId();
        String sId = subjectRestService.getSubjectByNameAndCourse(SubjectName.MATEMATICAS, cId).getId();

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(GradeController.GRADE)
                .path(GradeController.SUBJECT)
                .path(GradeController.PATH_ID).expand(sId)
                .path(GradeController.TEACHER)
                .path(GradeController.PATH_USERNAME).expand("u010")
                //.bearerAuth(restService.getAuthToken().getToken())
                .get().log()
                .build();
    }

    //
    @Test
    public void testGetStudentGrades() {
        restService.loginManager();
        String cId = courseRestService.getCourseByNameAndYear(CourseName.OCTAVO_C, "2018").getId();
        String sId = subjectRestService.getSubjectByNameAndCourse(SubjectName.MATEMATICAS, cId).getId();

        restService.loginUser("u032", "p032@ESE1");
        List<GradeDto> rawList = gradeRestService.getStudentGradesBySubject(sId, "u032");

        ObjectMapper mapper = new ObjectMapper();
        List<GradeDto> gList = mapper.convertValue(rawList, new TypeReference<List<GradeDto>>() {
        });

        Assert.assertTrue(gList.size() > 0);
        Assert.assertEquals("u032", gList.get(0).getStudent().getUsername());
    }

    @Test
    public void testGetStudentGradesPreAuthorize() {
        restService.loginManager();
        String cId = courseRestService.getCourseByNameAndYear(CourseName.OCTAVO_C, "2018").getId();
        String sId = subjectRestService.getSubjectByNameAndCourse(SubjectName.MATEMATICAS, cId).getId();

        restService.loginAdmin();// PreAuthorize("hasRole STUDENT")
        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        gradeRestService.getStudentGradesBySubject(sId, "u010");
    }

    @Test
    public void testGetStudentGradesNoBearerAuth() {
        restService.loginManager();
        String cId = courseRestService.getCourseByNameAndYear(CourseName.OCTAVO_C, "2018").getId();
        String sId = subjectRestService.getSubjectByNameAndCourse(SubjectName.MATEMATICAS, cId).getId();

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(GradeController.GRADE)
                .path(GradeController.SUBJECT)
                .path(GradeController.PATH_ID).expand(sId)
                .path(GradeController.STUDENT)
                .path(GradeController.PATH_USERNAME).expand("u032")
                //.bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }
}
