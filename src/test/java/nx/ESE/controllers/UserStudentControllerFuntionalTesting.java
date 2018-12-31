package nx.ESE.controllers;

import static org.junit.Assert.assertEquals;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import nx.ESE.controllers.UserController;
import nx.ESE.dtos.UserDto;
import nx.ESE.services.HttpMatcher;
import nx.ESE.services.RestService;
import nx.ESE.services.UserRestService;
import nx.ESE.services.data.DatabaseSeederService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class UserStudentControllerFuntionalTesting {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Autowired
	private RestService restService;

	@Autowired
	private UserRestService userRestService;

	@Before
	public void before() {
		userRestService.createUserDtos();
	}

	@After
	public void delete() {
		this.userRestService.deleteStudents();
	}

	// POST--------------------------------------------
	@Test
	public void testPostStudent() {
		restService.loginManager();
		userRestService.postStudent();

	}

	@Test
	public void testPostStudentUsernameUnique() {
		restService.loginManager();
		userRestService.getStudentDto().setUsername("u006");
		userRestService.postStudent();
		userRestService.getStudentDto2().setUsername("u006");
		userRestService.postStudent2();
		Assert.assertNotEquals(userRestService.getStudentDto2().getUsername(),
				userRestService.getStudentDto().getUsername());
	}

	@Test
	public void testPostStudentDniFieldAlreadyExistException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getStudentDto().setDni("14130268-k");
		userRestService.getStudentDto2().setDni("14130268-k");
		userRestService.postStudent();
		userRestService.postStudent2();
	}

	@Test
	public void testPostStudentEmailFieldAlreadyExistException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getStudentDto().setEmail(userRestService.getStudentDtoUsername() + "@email.com");
		userRestService.getStudentDto2().setEmail(userRestService.getStudentDtoUsername() + "@email.com");
		userRestService.postStudent();
		userRestService.postStudent2();
	}

	@Test
	public void testPostMangerMobiledFieldAlreadyExistException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getStudentDto().setMobile("123456789");
		userRestService.getStudentDto2().setMobile("123456789");
		userRestService.postStudent();
		userRestService.postStudent2();
	}

	@Test
	public void testPostStudentNoBearerAuth() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(UserController.USERS).path(UserController.STUDENTS)
				.body(userRestService.getStudentDto()).post().build();
	}

	@Test
	public void testPostStudentPreAuthorize() {
		restService.loginStudent();// PreAuthorize("hasRole('Manager')")
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		restService.restBuilder().path(UserController.USERS).path(UserController.STUDENTS)
				.bearerAuth(restService.getAuthToken().getToken()).body(userRestService.getStudentDto()).post().build();
	}

	@Test
	public void testPostStudentWithoutUser() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getStudentDto().setUsername(null);
		restService.restBuilder().path(UserController.USERS).path(UserController.STUDENTS)
				.bearerAuth(restService.getAuthToken().getToken()).post().build();
	}

	@Test
	public void testPostStudentUsernameNull() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getStudentDto().setUsername(null);
		userRestService.postStudent();
	}

	@Test
	public void testPostStudentPassNull() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getStudentDto().setPassword(null);
		userRestService.postStudent();
	}

	@Test
	public void testPostStudentBadUsername() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getStudentDto().setUsername("lu");
		userRestService.postStudent();
	}

	@Test
	public void testPostStudentUnsafePassword() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getStudentDto().setPassword("password");
		userRestService.postStudent();
	}

	@Test
	public void testPostStudentBadDni() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getStudentDto().setDni("14130268-1");
		userRestService.postStudent();
	}

	@Test
	public void testPostStudentBadMobile() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getStudentDto().setMobile("12t678a");
		userRestService.postStudent();
	}

	@Test
	public void testPostStudentBadEmail() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getStudentDto().setMobile(userRestService.getStudentDtoUsername() + "SinArroba.com");
		userRestService.postStudent();
	}

	// PUT--------------------------------------------

	@Test
	public void testPutStudent() {
		restService.loginManager();
		userRestService.postStudent();
		userRestService.getStudentDto().setEmail(userRestService.getStudentDtoUsername() + "@email.com");
		userRestService.putStudent(userRestService.getStudentDto());
		assertEquals(userRestService.getStudentDtoUsername() + "@email.com",
				userRestService.getStudentDto().getEmail());
	}

	@Test
	public void testPutStudentUsernameNotFoundException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		userRestService.getStudentDto().setUsername("fdfdgd");
		userRestService.putStudent(userRestService.getStudentDto());
	}

	@Test
	public void testPutStudentUsernameFieldAlreadyExistException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getStudentDto().setUsername("u006");
		userRestService.postStudent();
		userRestService.postStudent2();
		userRestService.getStudentDto2().setUsername("u006");
		userRestService.putStudent(userRestService.getStudentDto2());
	}

	@Test
	public void testPutStudentDniFieldAlreadyExistException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getStudentDto().setDni("14130268-k");
		userRestService.postStudent();
		userRestService.postStudent2();
		userRestService.getStudentDto2().setDni("14130268-k");
		userRestService.putStudent(userRestService.getStudentDto2());
	}

	@Test
	public void testPutStudentMobileFieldAlreadyExistException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getStudentDto().setMobile("123456789");
		userRestService.postStudent();
		userRestService.postStudent2();
		userRestService.getStudentDto2().setMobile("123456789");
		userRestService.putStudent(userRestService.getStudentDto2());
	}

	@Test
	public void testPutStudentEmailFieldAlreadyExistException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getStudentDto().setEmail(userRestService.getStudentDtoUsername() + "@email.com");
		userRestService.postStudent();
		userRestService.postStudent2();
		userRestService.getStudentDto2().setEmail(userRestService.getStudentDtoUsername() + "@email.com");
		userRestService.putStudent(userRestService.getStudentDto2());
	}

	@Test
	public void testPutStudentNoBearerAuth() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(UserController.USERS).path(UserController.STUDENTS)
				.body(userRestService.getStudentDto()).put().build();
	}

	@Test
	public void testPutStudentPreAuthorize() {
		restService.loginStudent();// PreAuthorize("hasRole('MANAGER')")
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.putStudent(userRestService.getStudentDto());
	}

	// GET---------------------------

	@Test
	public void testGetStudentById() {
		restService.loginManager();
		userRestService.postStudent();
		assertEquals(userRestService.getStudentDtoUsername(),
				userRestService.getStudentByID(userRestService.getStudentDto().getId()).getUsername());
	}

	@Test
	public void testGetStudentByUsername() {
		restService.loginManager();
		userRestService.postStudent();
		assertEquals(userRestService.getStudentDtoUsername(),
				userRestService.getStudentByUsername(userRestService.getStudentDtoUsername()).getUsername());
	}

	@Test
	public void testGetStudentNoBearerAuth() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		userRestService.postStudent();
		restService.restBuilder().path(UserController.USERS).path(UserController.STUDENTS).path(UserController.PATH_ID)
				.expand(userRestService.getStudentDto().getId()).get().build();
	}

	@Test
	public void testGetStudentPreAuthorize() {
		restService.loginStudent();// PreAuthorize("hasRole('MANAGER')")
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.postStudent();
		userRestService.getStudentByID(userRestService.getStudentDto().getId());
	}

	@Test
	public void testGetStudentIdNotFoundException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		userRestService.getStudentByID("u64563456");
	}

	@Test
	public void testGetStudentUsernameNotFoundException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		userRestService.getStudentByUsername("rupertina");
	}

	// PATCH-----------------------------
	@Test
	public void testPatchStudentResetPass() {
		restService.loginManager();
		userRestService.postStudent();
		userRestService.patchStudentResetPass(userRestService.getStudentDto().getUsername(),
				userRestService.getStudentDto().getUsername() + "@ESE2");
		restService.loginUser(userRestService.getStudentDto().getUsername(),
				userRestService.getStudentDto().getUsername() + "@ESE2");
	}

	@Test
	public void testPatchStudentResetPassUsernameNotFoundException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		userRestService.patchStudentResetPass("rupertina", userRestService.getStudentDto().getUsername() + "@ESE2");
	}

}