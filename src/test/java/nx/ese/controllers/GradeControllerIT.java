package nx.ese.controllers;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private RestService restService;

    private String teacherUsername;
    private String teacherUsername2;


    @Before
    public void before() {
        gradeRestService.createGradesDto();

        teacherUsername = gradeRestService.getGradeDto().getEvaluation().getSubject().getTeacher().getUsername();
        teacherUsername2 = gradeRestService.getGradeDto2().getEvaluation().getSubject().getTeacher().getUsername();
        restService.loginUser(teacherUsername, teacherUsername + "@ESE1");
    }

    @After
    public void delete() {
        gradeRestService.deleteGrades();
    }

    // POST********************************
    @Test
    public void testPostGrade() {
        gradeRestService.postGrade(teacherUsername);

        GradeDto gDto = gradeRestService.getTeacherGradeById(gradeRestService.getGradeDto().getId(), teacherUsername);
        assertEquals(gDto, gradeRestService.getGradeDto());
    }

    @Test
    public void testPostGradePreAuthorizeRole() {
        restService.loginManager();// PreAuthorize("hasRole('TEACHER')")

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        gradeRestService.postGrade(teacherUsername);
    }

    @Test
    public void testPostGradePreAuthorizeUsername() {
        restService.loginTeacher();//#username == authentication.principal.username

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        gradeRestService.postGrade(teacherUsername);
    }

    @Test
    public void testPostGradeNoBearerAuth() {
        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(GradeController.GRADE)
                .path(GradeController.TEACHER)
                .path(GradeController.PATH_USERNAME).expand(teacherUsername)
                //.bearerAuth(restService.getAuthToken().getToken())
                .body(gradeRestService.getGradeDto())
                .post()
                .build();
    }

    @Test
    public void testPostGradeFieldInvalidExceptionId() {
        gradeRestService.postGrade(teacherUsername);

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        gradeRestService.postGrade(teacherUsername);// it goes with Id
    }

    @Test
    public void testPostGradeDocumentAlreadyExistException() {
        gradeRestService.postGrade(teacherUsername);
        gradeRestService.getGradeDto2().setStudent(gradeRestService.getGradeDto().getStudent());
        gradeRestService.getGradeDto2().setEvaluation(gradeRestService.getGradeDto().getEvaluation());

        restService.loginUser(teacherUsername2, teacherUsername2 + "@ESE1");
        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        gradeRestService.postGrade2(teacherUsername2);

    }

    @Test
    public void testPostGradeIsTeacherInGrade() {
        restService.loginUser(teacherUsername2, teacherUsername2 + "@ESE1");

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        gradeRestService.postGrade(teacherUsername2);
    }

    @Test
    public void testPostGradeBodyNull() {
        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        restService.restBuilder()
                .path(GradeController.GRADE)
                .path(GradeController.TEACHER)
                .path(GradeController.PATH_USERNAME).expand(teacherUsername)
                .bearerAuth(restService.getAuthToken().getToken())
                .body(null)//@NotNull
                .post()
                .build();
    }

    @Test
    public void testPostGradeStudentNull() {
        gradeRestService.getGradeDto().setStudent(null);//@NotNull

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        gradeRestService.postGrade(teacherUsername);
    }

    @Test
    public void testPostGradeEvaluationNull() {
        gradeRestService.getGradeDto().setEvaluation(null);//@NotNull

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        gradeRestService.postGrade(teacherUsername);
    }

    @Test
    public void testPostGradeGradeNull() {
        gradeRestService.getGradeDto().setGrade(null);//@NotNull

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        gradeRestService.postGrade(teacherUsername);
    }


    // PUT********************************
    @Test
    public void testPutGrade() {
        gradeRestService.postGrade(teacherUsername);

        gradeRestService.getGradeDto().setGrade(1.0);
        gradeRestService.putGrade(teacherUsername);

        GradeDto gDto = gradeRestService.getTeacherGradeById(gradeRestService.getGradeDto().getId(), teacherUsername);
        Assert.assertEquals(0, Double.compare(gDto.getGrade(), 1.0));
    }

    @Test
    public void testPutGradePreAuthorizeRole() {
        gradeRestService.postGrade(teacherUsername);

        gradeRestService.getGradeDto().setGrade(1.0);

        restService.loginManager();// PreAuthorize("hasRole('TEACHER')")

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        gradeRestService.putGrade(teacherUsername);
    }

    @Test
    public void testPutGradePreAuthorizeUsername() {
        gradeRestService.postGrade(teacherUsername);

        restService.loginTeacher();//#username == authentication.principal.username

        gradeRestService.getGradeDto().setGrade(1.0);

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        gradeRestService.putGrade(teacherUsername);
    }

    @Test
    public void testPutGradeNoBearerAuth() {
        gradeRestService.postGrade(teacherUsername);

        gradeRestService.getGradeDto().setGrade(1.0);

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(GradeController.GRADE)
                .path(GradeController.PATH_ID).expand(gradeRestService.getGradeDto().getId())
                .path(GradeController.TEACHER)
                .path(GradeController.PATH_USERNAME).expand(teacherUsername)
                .body(gradeRestService.getGradeDto())
                //.bearerAuth(restService.getAuthToken().getToken())
                .put()
                .build();

    }

    @Test
    public void testPutGradeBodyNull() {
        gradeRestService.postGrade(teacherUsername);

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        restService.restBuilder()
                .path(GradeController.GRADE)
                .path(GradeController.PATH_ID).expand(gradeRestService.getGradeDto().getId())
                .path(GradeController.TEACHER)
                .path(GradeController.PATH_USERNAME).expand(teacherUsername)
                .bearerAuth(restService.getAuthToken().getToken())
                .body(null)
                .put()
                .build();
    }

    @Test
    public void testPutGradeStudentNull() {
        gradeRestService.postGrade(teacherUsername);

        gradeRestService.getGradeDto().setStudent(null);

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));//@NotNull
        gradeRestService.putGrade(teacherUsername);
    }

    @Test
    public void testPutGradeEvaluationNull() {
        gradeRestService.postGrade(teacherUsername);

        gradeRestService.getGradeDto().setEvaluation(null);//@NotNull

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        gradeRestService.putGrade(teacherUsername);
    }

    @Test
    public void testPutGradeGradeNull() {
        gradeRestService.postGrade(teacherUsername);

        gradeRestService.getGradeDto().setGrade(null);//@NotNull

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        gradeRestService.putGrade(teacherUsername);
    }

    @Test
    public void testPutGradeDocumentAlreadyExistException() {
        restService.loginUser(teacherUsername2, teacherUsername2 + "@ESE1");
        gradeRestService.postGrade2(teacherUsername2);

        restService.loginUser(teacherUsername, teacherUsername + "@ESE1");

        gradeRestService.postGrade(teacherUsername);

        gradeRestService.getGradeDto().setStudent(gradeRestService.getGradeDto2().getStudent());
        gradeRestService.getGradeDto().setEvaluation(gradeRestService.getGradeDto2().getEvaluation());

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        gradeRestService.putGrade(teacherUsername);

    }

    @Test
    public void testPutGradeIsTeacherInGrade() {
        gradeRestService.postGrade(teacherUsername);
        gradeRestService.getGradeDto().setGrade(1.0);

        restService.loginUser(teacherUsername2, teacherUsername2 + "@ESE1");

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        gradeRestService.putGrade(teacherUsername2);
    }

    @Test
    public void testPutGradeFieldNotFoundExceptionId() {
        gradeRestService.getGradeDto().setGrade(1.0);

        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        restService.restBuilder()
                .path(GradeController.GRADE)
                .path(GradeController.PATH_ID).expand("xxx")
                .path(GradeController.TEACHER)
                .path(GradeController.PATH_USERNAME).expand(teacherUsername)
                .bearerAuth(restService.getAuthToken().getToken())
                .body(gradeRestService.getGradeDto())
                .put()
                .build();
    }

    // DELETE********************************
    @Test
    public void testDeleteGrade() {
        gradeRestService.postGrade(teacherUsername);

        gradeRestService.deleteGrade(gradeRestService.getGradeDto().getId(), teacherUsername);

        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        gradeRestService.getTeacherGradeById(gradeRestService.getGradeDto().getId(), teacherUsername);
    }

    @Test
    public void testDeleteGradePreAuthorize() {
        gradeRestService.postGrade(teacherUsername);

        restService.loginManager();// PreAuthorize("hasRole('TEACHER')")

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        gradeRestService.deleteGrade(gradeRestService.getGradeDto().getId(), teacherUsername);
    }

    @Test
    public void testDeleteGradePreAuthorizeUsername() {
        gradeRestService.postGrade(teacherUsername);

        restService.loginTeacher();//#username == authentication.principal.username

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        gradeRestService.deleteGrade(gradeRestService.getGradeDto().getId(), teacherUsername);
    }

    @Test
    public void testDeleteGradeNoBearerAuth() {
        gradeRestService.postGrade(teacherUsername);

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(GradeController.GRADE)
                .path(GradeController.PATH_ID)
                .expand(gradeRestService.getGradeDto().getId())
                .path(GradeController.TEACHER)
                .path(GradeController.PATH_USERNAME).expand(teacherUsername)
                //.bearerAuth(restService.getAuthToken().getToken())
                .delete()
                .build();
    }

    @Test
    public void testDeleteGradeIsTeacherInGrade() {
        gradeRestService.postGrade(teacherUsername);

        restService.loginUser(teacherUsername2, teacherUsername2 + "@ESE1");

        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        gradeRestService.deleteGrade(gradeRestService.getGradeDto().getId(), teacherUsername2);
    }

    @Test
    public void testDeleteGradeFieldNotFoundExceptionId() {
        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        gradeRestService.deleteGrade("xxx", teacherUsername);
    }


    // GET********************************
    @Test
    public void testGetGradeById() {
        gradeRestService.postGrade(teacherUsername);

        restService.loginManager();
        GradeDto gDto = gradeRestService.getGradeById(gradeRestService.getGradeDto().getId());
        Assert.assertEquals(gDto, gradeRestService.getGradeDto());
    }

    @Test
    public void testGetGradeByIdPreAuthorizeRole() {
        gradeRestService.postGrade(teacherUsername);

        restService.loginAdmin();// PreAuthorize("hasRole MANAGER")

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        gradeRestService.getGradeById(gradeRestService.getGradeDto().getId());
    }

    @Test
    public void testGetGradeByIdNoBearerAuth() {
        gradeRestService.postGrade(teacherUsername);

        restService.loginManager();
        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(GradeController.GRADE)
                .path(GradeController.PATH_ID).expand(gradeRestService.getGradeDto().getId())
                .body(gradeRestService.getGradeDto().getId())
                //.bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }

    @Test
    public void testGetGradeByIdFieldNotFoundExceptionId() {
        restService.loginManager();
        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        gradeRestService.getGradeById("xxx");
    }

    //
    @Test
    public void testGetTeacherGradeById() {
        gradeRestService.postGrade(teacherUsername);

        GradeDto gDto = gradeRestService.getTeacherGradeById(gradeRestService.getGradeDto().getId(), teacherUsername);
        Assert.assertEquals(gDto, gradeRestService.getGradeDto());
    }

    @Test
    public void testGetTeacherGradeByIdPreAuthorizeRole() {
        gradeRestService.postGrade(teacherUsername);

        restService.loginManager();// PreAuthorize("hasRole TEACHER")

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        gradeRestService.getTeacherGradeById(gradeRestService.getGradeDto().getId(), teacherUsername);
    }

    @Test
    public void testGetTeacherGradeByIdPreAuthorizeUsername() {
        gradeRestService.postGrade(teacherUsername);

        restService.loginTeacher();//#username == authentication.principal.username

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        gradeRestService.getTeacherGradeById(gradeRestService.getGradeDto().getId(), teacherUsername);
    }

    @Test
    public void testGetTeacherGradeByIdNoBearerAuth() {
        gradeRestService.postGrade(teacherUsername);

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(GradeController.GRADE)
                .path(GradeController.PATH_ID).expand(gradeRestService.getGradeDto().getId())
                .body(gradeRestService.getGradeDto().getId())
                .path(GradeController.TEACHER)
                .path(GradeController.PATH_USERNAME).expand(teacherUsername)
                //.bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }

    @Test
    public void testGetTeacherGradeByIdIsTeacherInGrade() {
        gradeRestService.postGrade(teacherUsername);

        restService.loginUser(teacherUsername2, teacherUsername2 + "@ESE1");
        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        gradeRestService.getTeacherGradeById(gradeRestService.getGradeDto().getId(), teacherUsername2);
    }

    @Test
    public void testGetTeacherGradeByIdFieldNotFoundExceptionId() {
        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        gradeRestService.getTeacherGradeById("xxx", teacherUsername);
    }

    //
    @Test
    public void testGetGradesBySubject() {
        GradeDto gDto = gradeRestService.postGrade(teacherUsername);
        String sId = gDto.getEvaluation().getSubject().getId();

        restService.loginManager();
        List<GradeDto> rawList = gradeRestService.getGradesBySubject(sId);

        ObjectMapper mapper = new ObjectMapper();
        List<GradeDto> gList = mapper.convertValue(rawList, new TypeReference<List<GradeDto>>() {
        });

        Assert.assertTrue(gList.size() > 0);
    }

    @Test
    public void testGetGradesBySubjectPreAuthorizeRole() {
        GradeDto gDto = gradeRestService.postGrade(teacherUsername);
        String sId = gDto.getEvaluation().getSubject().getId();

        restService.loginAdmin();// PreAuthorize("hasRole MANAGER")

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        gradeRestService.getGradesBySubject(sId);
    }

    @Test
    public void testGetGradesBySubjectNoBearerAuth() {
        GradeDto gDto = gradeRestService.postGrade(teacherUsername);
        String sId = gDto.getEvaluation().getSubject().getId();

        restService.loginManager();
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
    public void testGetTeacherGradesBySubject() {
        GradeDto gDto = gradeRestService.postGrade(teacherUsername);
        String sId = gDto.getEvaluation().getSubject().getId();

        List<GradeDto> rawList = gradeRestService.getTeacherGradesBySubject(sId, teacherUsername);

        ObjectMapper mapper = new ObjectMapper();
        List<GradeDto> gList = mapper.convertValue(rawList, new TypeReference<List<GradeDto>>() {
        });

        Assert.assertTrue(gList.size() > 0);
        Assert.assertEquals(teacherUsername, gList.get(0).getEvaluation().getSubject().getTeacher().getUsername());
    }

    @Test
    public void testGetTeacherGradesBySubjectPreAuthorizeRole() {
        GradeDto gDto = gradeRestService.postGrade(teacherUsername);
        String sId = gDto.getEvaluation().getSubject().getId();

        restService.loginAdmin();// PreAuthorize("hasRole TEACHER")
        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        gradeRestService.getTeacherGradesBySubject(sId, teacherUsername);
    }

    @Test
    public void testGetTeacherGradesBySubjectPreAuthorizeUsername() {
        GradeDto gDto = gradeRestService.postGrade(teacherUsername);
        String sId = gDto.getEvaluation().getSubject().getId();

        restService.loginTeacher();//#username == authentication.principal.username
        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        gradeRestService.getTeacherGradesBySubject(sId, teacherUsername);
    }

    @Test
    public void testGetTeacherGradesBySubjectIsTeacherInGrade() {
        GradeDto gDto = gradeRestService.postGrade(teacherUsername);
        String sId = gDto.getEvaluation().getSubject().getId();

        restService.loginUser(teacherUsername2, teacherUsername2 + "@ESE1");
        List<GradeDto> rawList = gradeRestService.getTeacherGradesBySubject(sId, teacherUsername2);

        ObjectMapper mapper = new ObjectMapper();
        List<GradeDto> gList = mapper.convertValue(rawList, new TypeReference<List<GradeDto>>() {
        });

        assertEquals(0, gList.size());
    }

    @Test
    public void testGetTeacherGradesBySubjectNoBearerAuth() {
        GradeDto gDto = gradeRestService.postGrade(teacherUsername);
        String sId = gDto.getEvaluation().getSubject().getId();

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(GradeController.GRADE)
                .path(GradeController.SUBJECT)
                .path(GradeController.PATH_ID).expand(sId)
                .path(GradeController.TEACHER)
                .path(GradeController.PATH_USERNAME).expand(teacherUsername)
                //.bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }

    //
    @Test
    public void testGetStudentGradesBySubject() {
        GradeDto gDto = gradeRestService.postGrade(teacherUsername);
        String sId = gDto.getEvaluation().getSubject().getId();
        String studentUsername = gDto.getStudent().getUsername();

        restService.loginUser(studentUsername, studentUsername + "@ESE1");
        List<GradeDto> rawList = gradeRestService.getStudentGradesBySubject(sId, studentUsername);

        ObjectMapper mapper = new ObjectMapper();
        List<GradeDto> gList = mapper.convertValue(rawList, new TypeReference<List<GradeDto>>() {
        });

        Assert.assertTrue(gList.size() > 0);
        Assert.assertEquals(studentUsername, gList.get(0).getStudent().getUsername());
    }

    @Test
    public void testGetStudentGradesBySubjectPreAuthorizeRole() {
        GradeDto gDto = gradeRestService.postGrade(teacherUsername);
        String sId = gDto.getEvaluation().getSubject().getId();
        String studentUsername = gDto.getStudent().getUsername();

        restService.loginAdmin();// PreAuthorize("hasRole STUDENT")
        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        gradeRestService.getStudentGradesBySubject(sId, studentUsername);
    }

    @Test
    public void testGetStudentGradesBySubjectPreAuthorizeUsername() {
        GradeDto gDto = gradeRestService.postGrade(teacherUsername);
        String sId = gDto.getEvaluation().getSubject().getId();
        String studentUsername = gDto.getStudent().getUsername();

        restService.loginStudent();
        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        gradeRestService.getStudentGradesBySubject(sId, studentUsername);
    }

    @Test
    public void testGetStudentGradesBySubjectIsStudentInGrade() {
        GradeDto gDto = gradeRestService.postGrade(teacherUsername);
        String sId = gDto.getEvaluation().getSubject().getId();
        String studentUsername = gDto.getStudent().getUsername();

        restService.loginUser("u031", "p031@ESE1");
        List<GradeDto> rawList = gradeRestService.getStudentGradesBySubject(sId, "u031");

        ObjectMapper mapper = new ObjectMapper();
        List<GradeDto> gList = mapper.convertValue(rawList, new TypeReference<List<GradeDto>>() {
        });

        assertEquals(0, gList.size());

    }

    @Test
    public void testGetStudentGradesBySubjectNoBearerAuth() {
        GradeDto gDto = gradeRestService.postGrade(teacherUsername);
        String sId = gDto.getEvaluation().getSubject().getId();
        String studentUsername = gDto.getStudent().getUsername();

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(GradeController.GRADE)
                .path(GradeController.SUBJECT)
                .path(GradeController.PATH_ID).expand(sId)
                .path(GradeController.STUDENT)
                .path(GradeController.PATH_USERNAME).expand(studentUsername)
                //.bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }

    //
    @Test
    public void testGetGradesByEvaluation() {
        GradeDto gDto = gradeRestService.postGrade(teacherUsername);
        String eId = gDto.getEvaluation().getId();

        restService.loginManager();
        List<GradeDto> rawList = gradeRestService.getGradesByEvaluation(eId);

        ObjectMapper mapper = new ObjectMapper();
        List<GradeDto> gList = mapper.convertValue(rawList, new TypeReference<List<GradeDto>>() {
        });

        Assert.assertTrue(gList.size() > 0);

    }

    @Test
    public void testGetGradesByEvaluationPreAuthorizeRole() {
        GradeDto gDto = gradeRestService.postGrade(teacherUsername);
        String eId = gDto.getEvaluation().getId();

        restService.loginTeacher();// PreAuthorize("hasRole MANAGER")
        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        gradeRestService.getGradesByEvaluation(eId);

    }

    @Test
    public void testGetGradesByEvaluationNoBearerAuth() {
        GradeDto gDto = gradeRestService.postGrade(teacherUsername);
        String eId = gDto.getEvaluation().getId();

        restService.loginManager();
        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(GradeController.GRADE)
                .path(GradeController.EVALUATION)
                .path(GradeController.PATH_ID).expand(eId)
                //.bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }


    //
    @Test
    public void testGetTeacherGradesByEvaluation() {
        GradeDto gDto = gradeRestService.postGrade(teacherUsername);
        String eId = gDto.getEvaluation().getId();

        List<GradeDto> rawList = gradeRestService.getTeacherGradesByEvaluation(eId, teacherUsername);

        ObjectMapper mapper = new ObjectMapper();
        List<GradeDto> gList = mapper.convertValue(rawList, new TypeReference<List<GradeDto>>() {
        });

        Assert.assertTrue(gList.size() > 0);
    }

    @Test
    public void testGetTeacherGradesByEvaluationPreAuthorizeRole() {
        GradeDto gDto = gradeRestService.postGrade(teacherUsername);
        String eId = gDto.getEvaluation().getId();

        restService.loginManager();// PreAuthorize("hasRole TEACHER")
        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        gradeRestService.getTeacherGradesByEvaluation(eId, teacherUsername);

    }

    @Test
    public void testGetTeacherGradesByEvaluationPreAuthorizeUsername() {
        GradeDto gDto = gradeRestService.postGrade(teacherUsername);
        String eId = gDto.getEvaluation().getId();

        restService.loginTeacher();//#username == authentication.principal.username
        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        gradeRestService.getTeacherGradesByEvaluation(eId, teacherUsername);

    }

    @Test
    public void testGetTeacherGradesByEvaluationIsTeacherInGrade() {
        GradeDto gDto = gradeRestService.postGrade(teacherUsername);
        String eId = gDto.getEvaluation().getId();

        restService.loginUser(teacherUsername2, teacherUsername2 + "@ESE1");
        List<GradeDto> rawList = gradeRestService.getTeacherGradesByEvaluation(eId, teacherUsername2);

        ObjectMapper mapper = new ObjectMapper();
        List<GradeDto> gList = mapper.convertValue(rawList, new TypeReference<List<GradeDto>>() {
        });

        assertEquals(0, gList.size());
    }

    @Test
    public void testGetTeacherGradesByEvaluationNoBearerAuth() {
        GradeDto gDto = gradeRestService.postGrade(teacherUsername);
        String eId = gDto.getEvaluation().getId();

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(GradeController.GRADE)
                .path(GradeController.EVALUATION)
                .path(GradeController.PATH_ID).expand(eId)
                .path(GradeController.TEACHER)
                .path(GradeController.PATH_USERNAME).expand(teacherUsername)
                //.bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }
}
