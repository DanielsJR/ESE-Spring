package nx.ESE.restControllers;

import static org.junit.Assert.assertEquals;

import org.junit.After;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import nx.ESE.dataServices.DatabaseSeederService;
import nx.ESE.dtos.UserDto;
import nx.ESE.restControllers.UserResource;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class UserStudentResourceFuntionalTesting {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Autowired
	private RestService restService;

	private UserDto userDto;
	private UserDto userDto2;
	
	@Value("${nx.test.userDto.username}")
	private String userDtoUsername;

	@Value("${nx.test.userDto.password}")
	private String userDtoPassword;
	
	@Value("${nx.test.userDto2.username}")
	private String userDto2Username;

	@Value("${nx.test.userDto2.password}")
	private String userDto2Password;

	private static final Logger logger = LoggerFactory.getLogger(DatabaseSeederService.class);

	@Before
	public void before() {
		this.userDto = new UserDto(userDtoUsername);
		this.userDto2 = new UserDto(userDto2Username);
	}

	@After
	public void delete() {
		logger.warn("*********************************DELETING_DB**************************************************");
		this.restService.loginManager();

		try {
			this.restService.restBuilder().path(UserResource.USERS).path(UserResource.STUDENTS)
					.path(UserResource.PATH_USERNAME).expand(userDto.getUsername())
					.bearerAuth(restService.getAuthToken().getToken()).delete().build();
		} catch (Exception e) {
			logger.warn("error: " + e.getMessage() + "DTO1 nothing to delete");
		}

		try {
			this.restService.restBuilder().path(UserResource.USERS).path(UserResource.STUDENTS)
					.path(UserResource.PATH_USERNAME).expand(userDto2.getUsername())
					.bearerAuth(restService.getAuthToken().getToken()).delete().build();
		} catch (Exception e) {
			logger.warn("error: " + e.getMessage() + "DTO2 nothing to delete");
		}
		logger.warn("***********************************************************************************************");
	}

	private UserDto getStudentByID(String id) {
		return userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserResource.USERS).path(UserResource.STUDENTS).path(UserResource.ID).path(UserResource.PATH_ID)
				.expand(id).get().bearerAuth(restService.getAuthToken().getToken()).build();
	}

	private UserDto getStudentByUsername(String username) {
		return userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserResource.USERS).path(UserResource.STUDENTS).path(UserResource.USER_NAME)
				.path(UserResource.PATH_USERNAME).expand(username).bearerAuth(restService.getAuthToken().getToken())
				.get().build();
	}

	private void postStudent() {
		userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserResource.USERS)
				.path(UserResource.STUDENTS).bearerAuth(restService.getAuthToken().getToken()).body(userDto).post()
				.build();
	}

	private void postStudent2() {
		userDto2 = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserResource.USERS)
				.path(UserResource.STUDENTS).bearerAuth(restService.getAuthToken().getToken()).body(userDto2).post()
				.build();
	}

	private void putStudent(UserDto dto) {
		userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserResource.USERS)
				.path(UserResource.STUDENTS).path(UserResource.PATH_USERNAME).expand(dto.getUsername())
				.bearerAuth(restService.getAuthToken().getToken()).body(dto).put().build();
	}

	private void patchStudentResetPass(String username, String resetedPass) {
		restService.restBuilder(new RestBuilder<>()).path(UserResource.USERS).path(UserResource.STUDENTS)
				.path(UserResource.PASS).path(UserResource.PATH_USERNAME).expand(username)
				.bearerAuth(restService.getAuthToken().getToken()).body(resetedPass).patch().build();

	}

	// POST--------------------------------------------
	public void testPostStudent() {
		restService.loginManager();
		this.postStudent();
	}

	@Test
	public void testPostStudentUsernameFieldAlreadyExistException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto2.setUsername(userDtoUsername);
		this.postStudent();
		this.postStudent2();
	}

	@Test
	public void testPostStudentDniFieldAlreadyExistException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setDni("14130268-k");
		userDto2.setDni("14130268-k");
		this.postStudent();
		this.postStudent2();
	}

	@Test
	public void testPostStudentEmailFieldAlreadyExistException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setEmail(userDtoUsername + "@email.com");
		userDto2.setEmail(userDtoUsername + "@email.com");
		this.postStudent();
		this.postStudent2();
	}

	@Test
	public void testPostMangerMobiledFieldAlreadyExistException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setMobile("123456789");
		userDto2.setMobile("123456789");
		this.postStudent();
		this.postStudent2();
	}

	@Test
	public void testPostStudentNoBearerAuth() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(UserResource.USERS).path(UserResource.STUDENTS).body(userDto).post().build();
	}

	@Test
	public void testPostStudentPreAuthorize() {
		restService.loginStudent();// PreAuthorize("hasRole('Manager')")
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		restService.restBuilder().path(UserResource.USERS).path(UserResource.STUDENTS)
				.bearerAuth(restService.getAuthToken().getToken()).body(userDto).post().build();
	}

	@Test
	public void testPostStudentWithoutUser() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setUsername(null);
		restService.restBuilder().path(UserResource.USERS).path(UserResource.STUDENTS)
				.bearerAuth(restService.getAuthToken().getToken()).post().build();
	}

	@Test
	public void testPostStudentUsernameNull() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setUsername(null);
		this.postStudent();
	}

	@Test
	public void testPostStudentPassNull() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setPassword(null);
		this.postStudent();
	}

	@Test
	public void testPostStudentBadUsername() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setUsername("lu");
		this.postStudent();
	}

	@Test
	public void testPostStudentUnsafePassword() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setPassword("password");
		this.postStudent();
	}

	@Test
	public void testPostStudentBadDni() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setDni("14130268-1");
		this.postStudent();
	}

	@Test
	public void testPostStudentBadMobile() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setMobile("12t678a");
		this.postStudent();
	}

	@Test
	public void testPostStudentBadEmail() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setMobile(userDtoUsername + "SinArroba.com");
		this.postStudent();
	}

	// PUT--------------------------------------------

	@Test
	public void testPutStudent() {
		restService.loginManager();
		this.postStudent();
		userDto.setEmail(userDtoUsername + "@email.com");
		this.putStudent(userDto);
		assertEquals(userDtoUsername + "@email.com", userDto.getEmail());
	}

	@Test
	public void testPutStudentUsernameNotFoundException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		userDto.setUsername("fdfdgd");
		this.putStudent(userDto);
	}

	@Test
	public void testPutStudentUsernameFieldAlreadyExistException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setUsername("u006");
		this.postStudent();
		this.postStudent2();
		userDto2.setUsername("u006");
		this.putStudent(userDto2);
	}

	@Test
	public void testPutStudentDniFieldAlreadyExistException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setDni("14130268-k");
		this.postStudent();
		this.postStudent2();
		userDto2.setDni("14130268-k");
		this.putStudent(userDto2);
	}

	@Test
	public void testPutStudentMobileFieldAlreadyExistException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setMobile("123456789");
		this.postStudent();
		this.postStudent2();
		userDto2.setMobile("123456789");
		this.putStudent(userDto2);
	}

	@Test
	public void testPutStudentEmailFieldAlreadyExistException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setEmail(userDtoUsername + "@email.com");
		this.postStudent();
		this.postStudent2();
		userDto2.setEmail(userDtoUsername + "@email.com");
		this.putStudent(userDto2);
	}

	@Test
	public void testPutStudentNoBearerAuth() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(UserResource.USERS).path(UserResource.STUDENTS).body(userDto).put().build();
	}

	@Test
	public void testPutStudentPreAuthorize() {
		restService.loginStudent();// PreAuthorize("hasRole('MANAGER')")
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		this.putStudent(userDto);
	}

	// GET---------------------------

	@Test
	public void testGetStudentById() {
		restService.loginManager();
		this.postStudent();
		assertEquals(userDtoUsername, this.getStudentByID(userDto.getId()).getUsername());
	}

	@Test
	public void testGetStudentByUsername() {
		restService.loginManager();
		this.postStudent();
		assertEquals(userDtoUsername, this.getStudentByUsername(userDtoUsername).getUsername());
	}

	@Test
	public void testGetStudentNoBearerAuth() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		this.postStudent();
		restService.restBuilder().path(UserResource.USERS).path(UserResource.STUDENTS).path(UserResource.PATH_ID)
				.expand(userDto.getId()).get().build();
	}

	@Test
	public void testGetStudentPreAuthorize() {
		restService.loginStudent();// PreAuthorize("hasRole('MANAGER')")
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		this.postStudent();
		this.getStudentByID(userDto.getId());
	}

	@Test
	public void testGetStudentIdNotFoundException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		this.getStudentByID("u64563456");
	}

	@Test
	public void testGetStudentUsernameNotFoundException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		this.getStudentByUsername("rupertina");
	}

	// PATCH-----------------------------
	@Test
	public void testPatchStudentResetPass() {
		restService.loginManager();
		this.postStudent();
		this.patchStudentResetPass(userDto.getUsername(), userDto.getUsername() + "@ESE2");
		restService.loginUser(userDto.getUsername(), userDto.getUsername() + "@ESE2");
	}

	@Test
	public void testPatchStudentResetPassUsernameNotFoundException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		this.patchStudentResetPass("rupertina", userDto.getUsername() + "@ESE2");
	}



}