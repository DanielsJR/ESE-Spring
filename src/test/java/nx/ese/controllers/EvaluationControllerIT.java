package nx.ese.controllers;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nx.ese.documents.core.Evaluation;
import nx.ese.documents.core.EvaluationType;
import nx.ese.dtos.validators.NxPattern;
import nx.ese.repositories.EvaluationRepository;

import nx.ese.services.UserRestService;
import nx.ese.utils.NxDateFormatter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import nx.ese.dtos.EvaluationDto;

import nx.ese.services.EvaluationRestService;

import nx.ese.services.HttpMatcher;
import nx.ese.services.RestService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class EvaluationControllerIT {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private EvaluationRestService evaluationRestService;

    @Autowired
    private UserRestService userRestService;

    @Autowired
    private RestService restService;

    private static final Logger logger = LoggerFactory.getLogger(EvaluationControllerIT.class);

    @Before
    public void before() {
        evaluationRestService.createEvaluationsDto();
        restService.loginTeacher();
    }

    @After
    public void delete() {
        evaluationRestService.deleteEvaluationsDto();
    }

    // POST********************************
    @Test
    public void testPostEvaluation() {
        evaluationRestService.postEvaluation();

        restService.loginManager();
        EvaluationDto eDto = evaluationRestService.getEvaluationById(evaluationRestService.getEvaluationDto().getId());
        assertEquals(eDto, evaluationRestService.getEvaluationDto());
    }

    @Test
    public void testPostEvaluationPreAuthorize() {
        restService.loginAdmin();// PreAuthorize("hasRole('TEACHER')")

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        evaluationRestService.postEvaluation();
    }

    @Test
    public void testPostEvaluationNoBearerAuth() {
        evaluationRestService.postEvaluation();

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(EvaluationController.EVALUATION)
                .body(evaluationRestService.getEvaluationDto())
                .post()
                .build();
    }

    @Test
    public void testPostEvaluationFieldInvalidExceptionId() {
        evaluationRestService.postEvaluation();

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        evaluationRestService.postEvaluation();// it goes with Id
    }

    @Test
    public void testPostEvaluationDocumentAlreadyExistException() {
        evaluationRestService.postEvaluation();

        evaluationRestService.getEvaluationDto2().setType(evaluationRestService.getEvaluationDto().getType());
        evaluationRestService.getEvaluationDto2().setTitle(evaluationRestService.getEvaluationDto().getTitle());
        evaluationRestService.getEvaluationDto2().setSubject(evaluationRestService.getEvaluationDto().getSubject());
        evaluationRestService.getEvaluationDto2().setDate(evaluationRestService.getEvaluationDto().getDate());

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        evaluationRestService.postEvaluation2();

    }

    @Test
    public void testPostEvaluationBodyNull() {
        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        restService.restBuilder()
                .path(EvaluationController.EVALUATION)
                .bearerAuth(restService.getAuthToken().getToken())
                .body(null)
                .post()
                .build();
    }

    @Test
    public void testPostEvaluationTypeNull() {
        evaluationRestService.getEvaluationDto().setType(null);

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        evaluationRestService.postEvaluation();
    }

    @Test
    public void testPostEvaluationTitleNull() {
        evaluationRestService.getEvaluationDto().setTitle(null);

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        evaluationRestService.postEvaluation();
    }

    @Test
    public void testPostEvaluationSubjectNull() {
        evaluationRestService.getEvaluationDto().setSubject(null);

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        evaluationRestService.postEvaluation();
    }

    @Test
    public void testPostEvaluationDateNull() {
        evaluationRestService.getEvaluationDto().setDate(null);

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        evaluationRestService.postEvaluation();
    }

    // PUT********************************
    @Test
    public void testPutEvaluation() {
        evaluationRestService.postEvaluation();

        evaluationRestService.getEvaluationDto().setTitle("new Title");
        evaluationRestService.putEvaluation();

        restService.loginManager();
        EvaluationDto eDto = evaluationRestService.getEvaluationById(evaluationRestService.getEvaluationDto().getId());
        Assert.assertEquals("new Title", eDto.getTitle());
    }

    @Test
    public void testPutEvaluationPreAuthorize() {
        evaluationRestService.postEvaluation();

        evaluationRestService.getEvaluationDto().setTitle("new Title");

        restService.loginManager();// PreAuthorize("hasRole('TEACHER')")

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        evaluationRestService.putEvaluation();
    }

    @Test
    public void testPutEvaluationNoBearerAuth() {
        evaluationRestService.postEvaluation();

        evaluationRestService.getEvaluationDto().setTitle("new Title");

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(EvaluationController.EVALUATION)
                .path(EvaluationController.PATH_ID).expand(evaluationRestService.getEvaluationDto().getId())
                .body(evaluationRestService.getEvaluationDto())
                .put()
                .build();
    }

    @Test
    public void testPutEvaluationDocumentAlreadyExistException() {
        evaluationRestService.postEvaluation();
        evaluationRestService.postEvaluation2();

        evaluationRestService.getEvaluationDto().setType(evaluationRestService.getEvaluationDto2().getType());
        evaluationRestService.getEvaluationDto().setTitle(evaluationRestService.getEvaluationDto2().getTitle());
        evaluationRestService.getEvaluationDto().setSubject(evaluationRestService.getEvaluationDto2().getSubject());
        evaluationRestService.getEvaluationDto().setDate(evaluationRestService.getEvaluationDto2().getDate());

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        evaluationRestService.putEvaluation();

    }

    @Test
    public void testPutEvaluationFieldNotFoundExceptionId() {
        evaluationRestService.getEvaluationDto().setTitle("new Title");

        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        restService.restBuilder()
                .path(EvaluationController.EVALUATION)
                .path(EvaluationController.PATH_ID).expand("xxx")
                .bearerAuth(restService.getAuthToken().getToken())
                .body(evaluationRestService.getEvaluationDto())
                .put()
                .build();
    }

    @Test
    public void testPutEvaluationBodyNull() {
        evaluationRestService.postEvaluation();

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        restService.restBuilder()
                .path(EvaluationController.EVALUATION)
                .path(EvaluationController.PATH_ID).expand(evaluationRestService.getEvaluationDto().getId())
                .bearerAuth(restService.getAuthToken().getToken())
                .body(null)
                .put()
                .build();
    }

    @Test
    public void testPutEvaluationTitleNull() {
        evaluationRestService.postEvaluation();

        evaluationRestService.getEvaluationDto().setTitle(null);
        ;

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        evaluationRestService.putEvaluation();
    }

    @Test
    public void testPutEvaluationTypeNull() {
        evaluationRestService.postEvaluation();

        evaluationRestService.getEvaluationDto().setType(null);

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        evaluationRestService.putEvaluation();
    }

    @Test
    public void testPutEvaluationSubjectNull() {
        evaluationRestService.postEvaluation();

        evaluationRestService.getEvaluationDto().setSubject(null);

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        evaluationRestService.putEvaluation();
    }

    @Test
    public void testPutEvaluationDateNull() {
        evaluationRestService.postEvaluation();

        evaluationRestService.getEvaluationDto().setDate(null);

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        evaluationRestService.putEvaluation();
    }

    // DELETE
    @Test
    public void testDeleteEvaluation() {
        evaluationRestService.postEvaluation();

        evaluationRestService.deleteEvaluation(evaluationRestService.getEvaluationDto().getId());

        restService.loginManager();
        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        evaluationRestService.getEvaluationById(evaluationRestService.getEvaluationDto().getId());
    }

    @Test
    public void testDeleteEvaluationPreAuthorize() {
        evaluationRestService.postEvaluation();

        restService.loginManager();// PreAuthorize("hasRole('TEACHER')")

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        evaluationRestService.deleteEvaluation(evaluationRestService.getEvaluationDto().getId());
    }

    @Test
    public void testDeleteEvaluationNoBearerAuth() {
        evaluationRestService.postEvaluation();

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(EvaluationController.EVALUATION)
                .path(EvaluationController.PATH_ID).expand(evaluationRestService.getEvaluationDto()
                .getId())
                .delete()
                .build();
    }

    @Test
    public void testDeleteEvaluationFieldNotFoundExceptionId() {
        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        evaluationRestService.deleteEvaluation("xxx");
    }

    // GET********************************
    @Test
    public void testGetEvaluationById() {
        evaluationRestService.postEvaluation();

        restService.loginManager();
        EvaluationDto eDto = evaluationRestService.getEvaluationById(evaluationRestService.getEvaluationDto().getId());
        Assert.assertEquals(eDto, evaluationRestService.getEvaluationDto());
    }

    @Test
    public void testGetEvaluationByIdPreAuthorize() {
        evaluationRestService.postEvaluation();

        restService.loginAdmin();// PreAuthorize("hasRole MANAGER")

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        evaluationRestService.getEvaluationById(evaluationRestService.getEvaluationDto().getId());

    }

    @Test
    public void testGetEvaluationByIdNoBearerAuth() {
        evaluationRestService.postEvaluation();

        restService.loginManager();
        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(EvaluationController.EVALUATION)
                .path(EvaluationController.PATH_ID).expand(evaluationRestService.getEvaluationDto().getId())
                .body(evaluationRestService.getEvaluationDto().getId())
                .get()
                .build();
    }

    @Test
    public void testGetEvaluationByIdFieldNotFoundExceptionId() {
        restService.loginManager();
        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        evaluationRestService.getEvaluationById("xxx");
    }

    //
    @Test
    public void testGetTeacherEvaluationsById() {
        evaluationRestService.postEvaluation();

        String eId = evaluationRestService.getEvaluationDto().getId();

        restService.loginUser(userRestService.getTeacherDto2Username(), userRestService.getTeacherDto2Username() + "@ESE1");
        EvaluationDto eDto = evaluationRestService.getTeacherEvaluationById(eId, userRestService.getTeacherDto2Username());

        Assert.assertEquals(EvaluationType.DISERTACION, eDto.getType());
        Assert.assertEquals(EvaluationRestService.EVALUATION_TITLE_01, eDto.getTitle());

/*        restService.loginUser(userRestService.getTeacherDtoUsername(), userRestService.getTeacherDtoUsername() + "@ESE1");
        EvaluationDto eDto2 = evaluationRestService.getTeacherEvaluationById(eId, userRestService.getTeacherDtoUsername());

        Assert.assertNull(eDto2);*/
    }

    @Test
    public void testGetTeacherEvaluationsByIdPreAuthorize() {
        evaluationRestService.postEvaluation();

        String eId = evaluationRestService.getEvaluationDto().getId();

        restService.loginAdmin();//hasRole('TEACHER')

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        evaluationRestService.getTeacherEvaluationById(eId, userRestService.getTeacherDto2Username());

    }

    @Test
    public void testGetTeacherEvaluationsByIdAuthorizeUsername() {
        evaluationRestService.postEvaluation();

        String eId = evaluationRestService.getEvaluationDto().getId();

        //#username == authentication.principal.username
        restService.loginUser(userRestService.getTeacherDto2Username(), userRestService.getTeacherDto2Username() + "@ESE1");

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        evaluationRestService.getTeacherEvaluationById(eId, userRestService.getTeacherDtoUsername());

    }

    @Test
    public void testGetTeacherEvaluationsByIdNoBearerAuth() {
        evaluationRestService.postEvaluation();

        String eId = evaluationRestService.getEvaluationDto().getId();

        restService.loginUser(userRestService.getTeacherDto2Username(), userRestService.getTeacherDto2Username() + "@ESE1");

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(EvaluationController.EVALUATION)
                .path(EvaluationController.PATH_ID).expand(eId)
                .path(EvaluationController.TEACHER)
                .path(EvaluationController.PATH_USERNAME).expand(userRestService.getTeacherDto2Username())
                //.bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }

    @Test
    public void testGetTeacherEvaluationsByIdDocumentNotFoundException() {
        evaluationRestService.postEvaluation();

        String eId = evaluationRestService.getEvaluationDto().getId();
        restService.loginUser(userRestService.getTeacherDtoUsername(), userRestService.getTeacherDtoUsername() + "@ESE1");
        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        evaluationRestService.getTeacherEvaluationById(eId, userRestService.getTeacherDtoUsername());

    }

    //
    @Test
    public void testGetEvaluationsBySubject() {
        evaluationRestService.postEvaluation();

        restService.loginManager();
        List rawList = evaluationRestService.getEvaluationsBySubject(evaluationRestService.getEvaluationDto().getSubject().getId());

        ObjectMapper mapper = new ObjectMapper();
        List<EvaluationDto> evsDto = mapper.convertValue(rawList, new TypeReference<List<EvaluationDto>>() {
        });

        Assert.assertTrue(evsDto.size() > 0);
        Assert.assertEquals(EvaluationType.DISERTACION, evsDto.get(0).getType());
        Assert.assertEquals(EvaluationRestService.EVALUATION_TITLE_01, evsDto.get(0).getTitle());
    }

    @Test
    public void testGetEvaluationsBySubjectPreAuthorize() {
        evaluationRestService.postEvaluation();

        restService.loginAdmin();// PreAuthorize("hasRole MANAGER")

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        evaluationRestService.getEvaluationsBySubject(evaluationRestService.getEvaluationDto().getSubject().getId());
    }

    @Test
    public void testGetEvaluationsBySubjectNoBearerAuth() {
        evaluationRestService.postEvaluation();

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(EvaluationController.EVALUATION)
                .path(EvaluationController.SUBJECT)
                .path(EvaluationController.PATH_ID).expand(evaluationRestService.getEvaluationDto().getSubject().getId())
                .get()
                .build();
    }

    //
    @Test
    public void testGetTeacherEvaluationsBySubject() {
        evaluationRestService.postEvaluation();

        String sId = evaluationRestService.getEvaluationDto().getSubject().getId();

        restService.loginUser(userRestService.getTeacherDto2Username(), userRestService.getTeacherDto2Username() + "@ESE1");
        List rawList = evaluationRestService.getTeacherEvaluationsBySubject(sId, userRestService.getTeacherDto2Username());

        ObjectMapper mapper = new ObjectMapper();
        List<EvaluationDto> evsDto = mapper.convertValue(rawList, new TypeReference<List<EvaluationDto>>() {
        });

        Assert.assertTrue(evsDto.size() > 0);
        Assert.assertEquals(EvaluationType.DISERTACION, evsDto.get(0).getType());
        Assert.assertEquals(EvaluationRestService.EVALUATION_TITLE_01, evsDto.get(0).getTitle());
    }

    @Test
    public void testGetTeacherEvaluationsBySubjectPreAuthorize() {
        evaluationRestService.postEvaluation();

        String sId = evaluationRestService.getEvaluationDto().getSubject().getId();

        restService.loginManager();//hasRole('TEACHER')

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        evaluationRestService.getTeacherEvaluationsBySubject(sId, userRestService.getTeacherDto2Username());

    }

    @Test
    public void testGetTeacherEvaluationsBySubjectPreAuthorizeUsername() {
        evaluationRestService.postEvaluation();

        String sId = evaluationRestService.getEvaluationDto().getSubject().getId();

        //#username == authentication.principal.username
        restService.loginUser(userRestService.getTeacherDtoUsername(), userRestService.getTeacherDtoUsername() + "@ESE1");

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        evaluationRestService.getTeacherEvaluationsBySubject(sId, userRestService.getTeacherDto2Username());

    }

    @Test
    public void testGetTeacherEvaluationsBySubjectNoBearerAuth() {
        evaluationRestService.postEvaluation();

        String sId = evaluationRestService.getEvaluationDto().getSubject().getId();

        restService.loginUser(userRestService.getTeacherDto2Username(), userRestService.getTeacherDto2Username() + "@ESE1");

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(EvaluationController.EVALUATION)
                .path(EvaluationController.SUBJECT)
                .path(EvaluationController.PATH_ID).expand(sId)
                .path(EvaluationController.TEACHER)
                .path(EvaluationController.PATH_USERNAME).expand(userRestService.getTeacherDto2Username())
                //.bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }

    //
    @Test
    public void testGetEvaluationBySubjectAndDate() {
        evaluationRestService.postEvaluation();
        String sId = evaluationRestService.getEvaluationDto().getSubject().getId();
        LocalDate date = evaluationRestService.getEvaluationDto().getDate();

        logger.warn("date Raw = {}", date);

        restService.loginManager();
        Assert.assertNotNull(evaluationRestService.getEvaluationBySubjectAndDate(sId, date));

    }

    @Test
    public void testGetEvaluationBySubjectAndDatePreAuthorize() {
        evaluationRestService.postEvaluation();
        String sId = evaluationRestService.getEvaluationDto().getSubject().getId();
        LocalDate date = evaluationRestService.getEvaluationDto().getDate();

        restService.loginAdmin();// PreAuthorize("hasRole('MANAGER')

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        evaluationRestService.getEvaluationBySubjectAndDate(sId, date);
    }

    @Test
    public void testGetEvaluationBySubjectAndDateNoBearerAuth() {
        evaluationRestService.postEvaluation();
        String sId = evaluationRestService.getEvaluationDto().getSubject().getId();
        LocalDate date = evaluationRestService.getEvaluationDto().getDate();

        restService.loginManager();
        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(EvaluationController.EVALUATION)
                .path(EvaluationController.SUBJECT)
                .path(EvaluationController.PATH_ID).expand(sId)
                .path(EvaluationController.DATE)
                .path(EvaluationController.PATH_DATE).expand(date)
                .get()
                .build();
    }

    @Test
    public void testGetEvaluationBySubjectAndDateDocumentNotFoundException() {
        evaluationRestService.postEvaluation();

        restService.loginManager();
        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        restService.restBuilder()
                .path(EvaluationController.EVALUATION)
                .path(EvaluationController.SUBJECT)
                .path(EvaluationController.PATH_ID).expand("767644")
                .path(EvaluationController.DATE)
                .path(EvaluationController.PATH_DATE).expand("2018-08-11")
                .bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }

    @Test
    public void testGetEvaluationBySubjectAndDateFieldInvalidException() {
        evaluationRestService.postEvaluation();
        String sId = evaluationRestService.getEvaluationDto().getSubject().getId();

        restService.loginManager();
        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        restService.restBuilder()
                .path(EvaluationController.EVALUATION)
                .path(EvaluationController.SUBJECT)
                .path(EvaluationController.PATH_ID).expand(sId)
                .path(EvaluationController.DATE)
                .path(EvaluationController.PATH_DATE).expand("11-02-2018")
                .bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }

    //
    @Test
    public void testGetTeacherEvaluationBySubjectAndDate() {
        evaluationRestService.postEvaluation();
        String sId = evaluationRestService.getEvaluationDto().getSubject().getId();
        LocalDate date = evaluationRestService.getEvaluationDto().getDate();

        restService.loginUser(userRestService.getTeacherDto2Username(), userRestService.getTeacherDto2Username() + "@ESE1");
        EvaluationDto eDto = evaluationRestService.getTeacherEvaluationBySubjectAndDate(sId, date, userRestService.getTeacherDto2Username());

        Assert.assertNotNull(eDto);
        Assert.assertEquals(EvaluationType.DISERTACION, eDto.getType());
        Assert.assertEquals(EvaluationRestService.EVALUATION_TITLE_01, eDto.getTitle());

    }

    @Test
    public void testGetTeacherEvaluationBySubjectAndDatePreAuthorize() {
        evaluationRestService.postEvaluation();
        String sId = evaluationRestService.getEvaluationDto().getSubject().getId();
        LocalDate date = evaluationRestService.getEvaluationDto().getDate();

        restService.loginManager();//PreAuthorize("hasRole('TEACHER')

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        evaluationRestService.getTeacherEvaluationBySubjectAndDate(sId, date, userRestService.getTeacherDto2Username());

    }

    @Test
    public void testGetTeacherEvaluationBySubjectAndDatePreAuthorizeUsername() {
        evaluationRestService.postEvaluation();
        String sId = evaluationRestService.getEvaluationDto().getSubject().getId();
        LocalDate date = evaluationRestService.getEvaluationDto().getDate();

        //#username == authentication.principal.username
        restService.loginUser(userRestService.getTeacherDtoUsername(), userRestService.getTeacherDtoUsername() + "@ESE1");

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        evaluationRestService.getTeacherEvaluationBySubjectAndDate(sId, date, userRestService.getTeacherDto2Username());
    }

    @Test
    public void testGetTeacherEvaluationBySubjectAndDateNoBearerAuth() {
        evaluationRestService.postEvaluation();
        String sId = evaluationRestService.getEvaluationDto().getSubject().getId();
        LocalDate date = evaluationRestService.getEvaluationDto().getDate();

        restService.loginUser(userRestService.getTeacherDto2Username(), userRestService.getTeacherDto2Username() + "@ESE1");
        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(EvaluationController.EVALUATION)
                .path(EvaluationController.SUBJECT)
                .path(EvaluationController.PATH_ID).expand(sId)
                .path(EvaluationController.DATE)
                .path(EvaluationController.PATH_DATE).expand(date)
                .path(EvaluationController.TEACHER)
                .path(EvaluationController.PATH_USERNAME).expand(userRestService.getTeacherDto2Username())
                //.bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }

    @Test
    public void testGetTeacherEvaluationBySubjectAndDateDocumentNotFoundException() {
        evaluationRestService.postEvaluation();
        String sId = evaluationRestService.getEvaluationDto().getSubject().getId();
        LocalDate date = evaluationRestService.getEvaluationDto().getDate();

        restService.loginUser(userRestService.getTeacherDtoUsername(), userRestService.getTeacherDtoUsername() + "@ESE1");
        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        evaluationRestService.getTeacherEvaluationBySubjectAndDate(sId, date, userRestService.getTeacherDtoUsername());
    }


}
