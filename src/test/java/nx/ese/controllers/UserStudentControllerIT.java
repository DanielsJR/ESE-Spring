package nx.ese.controllers;

import static org.junit.Assert.assertEquals;

import java.util.List;

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

import nx.ese.dtos.UserDto;
import nx.ese.services.HttpMatcher;
import nx.ese.services.RestService;
import nx.ese.services.UserRestService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class UserStudentControllerIT {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private RestService restService;

    @Autowired
    private UserRestService userRestService;

    @Before
    public void before() {
        restService.loginManager();
        userRestService.createUsersDto();
    }

    @After
    public void delete() {
        this.userRestService.deleteStudents();
    }

    private static final Logger logger = LoggerFactory.getLogger(UserStudentControllerIT.class);

    // POST--------------------------------------------
    @Test
    public void testPostStudent() {
        userRestService.postStudent();

        UserDto uDto = userRestService.getStudentByUsername(userRestService.getStudentDtoUsername());
        assertEquals(userRestService.getStudentDto(),uDto);
    }

    @Test
    public void testPostStudentPreAuthorize() {
        restService.loginTeacher();// PreAuthorize("hasRole('MANAGER')")

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        userRestService.postStudent();
    }

    @Test
    public void testPostStudentNoBearerAuth() {
        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(UserController.USER)
                .path(UserController.STUDENT)
                .body(userRestService.getStudentDto())
                .post()
                .build();
    }

    @Test
    public void testPostStudentFieldInvalidExceptionId() {
        userRestService.postStudent();

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        userRestService.postStudent();// it goes with Id
    }

    @Test
    public void testPostStudentUsernameUnique() {
        userRestService.getStudentDto().setUsername("u006");
        userRestService.postStudent();

        userRestService.getStudentDto2().setUsername("u006");
        userRestService.postStudent2();

        Assert.assertNotEquals(userRestService.getStudentDto2().getUsername(),
                userRestService.getStudentDto().getUsername());
    }

    @Test
    public void testPostStudentDniFieldAlreadyExistException() {
        userRestService.getStudentDto().setDni("14130268-k");
        userRestService.postStudent();

        userRestService.getStudentDto2().setDni("14130268-k");
        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        userRestService.postStudent2();
    }

    @Test
    public void testPostStudentEmailFieldAlreadyExistException() {
        userRestService.getStudentDto().setEmail(userRestService.getStudentDtoUsername() + "@email.com");
        userRestService.postStudent();

        userRestService.getStudentDto2().setEmail(userRestService.getStudentDtoUsername() + "@email.com");
        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        userRestService.postStudent2();
    }

    @Test
    public void testPostMangerMobiledFieldAlreadyExistException() {
        userRestService.getStudentDto().setMobile("123456789");
        userRestService.postStudent();

        userRestService.getStudentDto2().setMobile("123456789");
        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        userRestService.postStudent2();
    }

    @Test
    public void testPostStudentWithoutBody() {
        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        restService.restBuilder()
                .path(UserController.USER)
                .path(UserController.STUDENT)
                .bearerAuth(restService.getAuthToken().getToken())
                .post()
                .build();
    }


    @Test
    public void testPostStudentPassNull() {
        userRestService.getStudentDto().setPassword(null);

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        userRestService.postStudent();
    }

    @Test
    public void testPostStudentBadUsername() {
        userRestService.getStudentDto().setUsername("lu");

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        userRestService.postStudent();
    }

    @Test
    public void testPostStudentUnsafePassword() {
        userRestService.getStudentDto().setPassword("password");

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        userRestService.postStudent();
    }

    @Test
    public void testPostStudentBadDni() {
        userRestService.getStudentDto().setDni("14130268-1");

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        userRestService.postStudent();
    }

    @Test
    public void testPostStudentBadMobile() {
        userRestService.getStudentDto().setMobile("12t678a");

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        userRestService.postStudent();
    }

    @Test
    public void testPostStudentBadEmail() {
        userRestService.getStudentDto().setMobile(userRestService.getStudentDtoUsername() + "SinArroba.com");

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        userRestService.postStudent();
    }

    // PUT--------------------------------------------
    @Test
    public void testPutStudent() {
        userRestService.postStudent();

        String newEmail = userRestService.getStudentDtoUsername() + "@email.com";
        userRestService.getStudentDto().setEmail(newEmail);
        userRestService.putStudent();

        UserDto uDto = userRestService.getStudentByUsername(userRestService.getStudentDtoUsername());
        assertEquals(uDto.getEmail(), userRestService.getStudentDto().getEmail());
    }

    @Test
    public void testPutStudentPreAuthorize() {
        userRestService.postStudent();

        restService.loginStudent();// PreAuthorize("hasRole('MANAGER')")

        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        userRestService.putStudent();
    }

    @Test
    public void testPutStudentNoBearerAuth() {
        userRestService.postStudent();

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(UserController.USER)
                .path(UserController.STUDENT)
                .body(userRestService.getStudentDto())
                .put()
                .build();
    }

    @Test
    public void testPutStudentUsernameNotFoundException() {
        userRestService.getStudentDto().setUsername("fdfdgd");

        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        userRestService.putStudent();
    }

    @Test
    public void testPutStudentUsernameFieldAlreadyExistException() {
        userRestService.postStudent();
        userRestService.postStudent2();

        userRestService.getStudentDto().setUsername(userRestService.getStudentDto2().getUsername());

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        userRestService.putStudent();
    }

    @Test
    public void testPutStudentDniFieldAlreadyExistException() {
        userRestService.postStudent();

        userRestService.getStudentDto2().setDni("14130268-k");
        userRestService.postStudent2();

        userRestService.getStudentDto().setDni(userRestService.getStudentDto2().getDni());

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        userRestService.putStudent();
    }

    @Test
    public void testPutStudentMobileFieldAlreadyExistException() {
        userRestService.postStudent();

        userRestService.getStudentDto2().setMobile("123456789");
        userRestService.postStudent2();

        userRestService.getStudentDto().setMobile(userRestService.getStudentDto2().getMobile());

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        userRestService.putStudent();
    }

    @Test
    public void testPutStudentEmailFieldAlreadyExistException() {
        userRestService.postStudent();

        userRestService.getStudentDto2().setEmail(userRestService.getStudentDtoUsername() + "@email.com");
        userRestService.postStudent2();

        userRestService.getStudentDto().setEmail(userRestService.getStudentDto2().getEmail());

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        userRestService.putStudent();
    }

    @Test
    public void testPutStudentHasUserGreaterPrivileges() {
        restService.loginMegaUser();

        UserDto uDto = userRestService.getUserByUsernameSecure("megauser");
        uDto.setEmail("admin@email.com");

        restService.loginManager();
        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        restService.restBuilder()
                .path(UserController.USER)
                .path(UserController.STUDENT)
                .path(UserController.PATH_USERNAME).expand(uDto.getUsername())
                .bearerAuth(restService.getAuthToken().getToken()).body(uDto)
                .put()
                .build();
    }

    @Test
    public void testPutStudentBadDni() {
        userRestService.postStudent();

        userRestService.getStudentDto().setDni("14130268-1");

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        userRestService.putStudent();
    }

    @Test
    public void testPutStudentBadMobile() {
        userRestService.postStudent();

        userRestService.getStudentDto().setMobile("12t678a");

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        userRestService.putStudent();
    }

    @Test
    public void testPutStudentBadEmail() {
        userRestService.postStudent();

        userRestService.getStudentDto().setMobile(userRestService.getStudentDtoUsername() + "SinArroba.com");

        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        userRestService.putStudent();
    }

    // PATCH-----------------------------
    @Test
    public void testPatchStudentResetPass() {
        userRestService.postStudent();

        String newPass = "newPass@ESE1";
        userRestService.patchStudentResetPass(userRestService.getStudentDto().getUsername(), newPass);

        restService.loginUser(userRestService.getStudentDto().getUsername(), newPass);

        UserDto uDto = userRestService.getUserByUsernameSecure(userRestService.getStudentDtoUsername());
        assertEquals(uDto, userRestService.getStudentDto());
    }

    @Test
    public void testPatchStudentResetPassPreAuthorize() {
        userRestService.postStudent();

        String newPass = "newPass@ESE1";

        restService.loginStudent(); // PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        userRestService.patchStudentResetPass(userRestService.getStudentDto().getUsername(), newPass);
    }

    @Test
    public void testPatchStudentResetPassNoBearerAuth() {
        userRestService.postStudent();

        String newPass = "newPass@ESE1";

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(UserController.USER)
                .path(UserController.STUDENT)
                .path(UserController.PASS)
                .path(UserController.PATH_USERNAME).expand(userRestService.getStudentDto().getUsername())
                .body(newPass)
                .patch()
                .build();
    }

    @Test
    public void testPatchStudentResetPassUsernameNotFoundException() {
        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        userRestService.patchStudentResetPass("rupertina", userRestService.getStudentDto().getUsername() + "@ESE2");
    }

    @Test
    public void testPatchResetPassHasUserGreaterPrivileges() {
        String newPass = "newPass@ESE1";
        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        userRestService.patchStudentResetPass("megauser", newPass);

    }

    // DELETE----------------------------------
    @Test
    public void testDeleteStudent() {
        userRestService.postStudent();

        userRestService.deleteStudent(userRestService.getStudentDto().getUsername());

        //thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        //userRestService.getStudentByUsername(userRestService.getStudentDto().getUsername());
    }

    @Test
    public void testDeleteStudentPreAuthorize() {
        userRestService.postStudent();

        restService.loginTeacher();
        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        userRestService.deleteStudent(userRestService.getStudentDto().getUsername());
    }

    @Test
    public void testDeleteStudentNoBearerAuth() {
        userRestService.postStudent();

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(UserController.USER)
                .path(UserController.STUDENT)
                .path(UserController.PATH_USERNAME).expand(userRestService.getStudentDto().getUsername())
                .delete()
                .build();
    }

    @Test
    public void testDeleteStudentForbiddenDeleteException() {
        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        userRestService.deleteStudent("u036");// u036 is in a course
    }

    @Test
    public void testDeleteStudentHasUserGreaterPrivileges() {
        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        userRestService.deleteStudent("megauser");
    }

    // GET---------------------------
    @Test
    public void testGetStudentById() {
        userRestService.postStudent();

        UserDto uDto = userRestService.getStudentById(userRestService.getStudentDto().getId());

        assertEquals(userRestService.getStudentDto(), uDto);
    }

    @Test
    public void testGetStudentByIdPreAuthorize() {
        userRestService.postStudent();

        restService.loginStudent();// PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        userRestService.getStudentById(userRestService.getStudentDto().getId());
    }

    @Test
    public void testGetStudentByIdNoBearerAuth() {
        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        userRestService.postStudent();
        restService.restBuilder()
                .path(UserController.USER)
                .path(UserController.STUDENT)
                .path(UserController.PATH_ID)
                .expand(userRestService.getStudentDto().getId())
                .get()
                .build();
    }

    @Test
    public void testGetStudentByIdGreaterPrivileges() {
        restService.loginMegaUser();

        UserDto uDto = userRestService.getUserByUsernameSecure("megauser");

        restService.loginManager();
        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        restService.restBuilder()
                .path(UserController.USER)
                .path(UserController.STUDENT)
                .path(UserController.ID)
                .path(UserController.PATH_ID).expand(uDto.getId())
                .bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }

    @Test
    public void testGetStudentByIdIdNotFoundException() {
        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        userRestService.getStudentById("u64563456");
    }

    //
    @Test
    public void testGetStudentByUsername() {
        userRestService.postStudent();

        UserDto uDto = userRestService.getStudentByUsername(userRestService.getStudentDto().getUsername());

        assertEquals(userRestService.getStudentDto(), uDto);
    }

    @Test
    public void testGetStudentByUsernamePreAuthorize() {
        userRestService.postStudent();

        restService.loginStudent();// PreAuthorize("hasRole('MANAGER') or
        // hasRole('TEACHER')")
        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        userRestService.getStudentByUsername(userRestService.getStudentDto().getUsername());
    }

    @Test
    public void testGetStudentByUsernameNoBearerAuth() {
        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        userRestService.postStudent();
        restService.restBuilder()
                .path(UserController.USER)
                .path(UserController.STUDENT)
                .path(UserController.PATH_USERNAME).expand(userRestService.getStudentDto().getUsername())
                .get()
                .build();
    }

    @Test
    public void testGetStudentByUsernameGreaterPrivileges() {
        restService.loginMegaUser();

        UserDto uDto = userRestService.getUserByUsernameSecure("megauser");

        restService.loginManager();
        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        restService.restBuilder()
                .path(UserController.USER)
                .path(UserController.STUDENT)
                .path(UserController.USERNAME)
                .path(UserController.PATH_USERNAME).expand(uDto.getUsername())
                .bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }

    @Test
    public void testGetStudentByUsernameUsernameNotFoundException() {
        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        userRestService.getStudentByUsername("rupertina");
    }

    //
    @Test
    public void testGetFullStudents() {
        List<UserDto> uDtos = userRestService.getFullStudents();
        assertEquals(uDtos.size() > 0, true);
    }

    @Test
    public void testGetFullStudentsPreAuthorize() {
        restService.loginStudent();// PreAuthorize("hasRole('MANAGER') or
        // 'TEACHER'
        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        userRestService.getFullStudents();
    }

    @Test
    public void testGetFullStudentsNoBearerAuth() {
        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(UserController.USER)
                .path(UserController.STUDENT)
                .get()
                .build();
    }

    //
    @Test
    public void testGetMinStudents() {
        List<UserDto> uDtos = userRestService.getMinStudents();
        assertEquals(uDtos.size() > 0, true);
    }

    @Test
    public void testGetMinStudentsPreAuthorize() {
        restService.loginStudent();// PreAuthorize("hasRole('MANAGER') or
        // 'TEACHER'
        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        userRestService.getMinStudents();
    }

    @Test
    public void testGetMinStudentsNoBearerAuth() {
        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(UserController.USER)
                .path(UserController.STUDENT)
                .path(UserController.USER_MIN)
                .get()
                .build();
    }

}