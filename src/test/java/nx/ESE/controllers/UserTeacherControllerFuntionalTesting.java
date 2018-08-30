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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import nx.ESE.controllers.UserController;
import nx.ESE.documents.Role;
import nx.ESE.dtos.UserDto;
import nx.ESE.services.data.DatabaseSeederService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class UserTeacherControllerFuntionalTesting {

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
			this.restService.restBuilder().path(UserController.USERS).path(UserController.TEACHERS)
					.path(UserController.PATH_USERNAME).expand(userDto.getUsername())
					.bearerAuth(restService.getAuthToken().getToken()).delete().build();
		} catch (Exception e) {
			logger.warn("error: " + e.getMessage() + "DTO1 nothing to delete");
		}

		try {
			this.restService.restBuilder().path(UserController.USERS).path(UserController.TEACHERS)
					.path(UserController.PATH_USERNAME).expand(userDto2.getUsername())
					.bearerAuth(restService.getAuthToken().getToken()).delete().build();
		} catch (Exception e) {
			logger.warn("error: " + e.getMessage() + "DTO2 nothing to delete");
		}
		logger.warn("***********************************************************************************************");
	}

	private UserDto getTeacherByID(String id) {
		return userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserController.USERS).path(UserController.TEACHERS).path(UserController.ID).path(UserController.PATH_ID)
				.expand(id).get().bearerAuth(restService.getAuthToken().getToken()).build();
	}

	private UserDto getTeacherByUsername(String username) {
		return userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserController.USERS).path(UserController.TEACHERS).path(UserController.USER_NAME)
				.path(UserController.PATH_USERNAME).expand(username).bearerAuth(restService.getAuthToken().getToken())
				.get().build();
	}

	private void postTeacher() {
		userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserController.USERS)
				.path(UserController.TEACHERS).bearerAuth(restService.getAuthToken().getToken()).body(userDto).post()
				.build();
	}

	private void postTeacher2() {
		userDto2 = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserController.USERS)
				.path(UserController.TEACHERS).bearerAuth(restService.getAuthToken().getToken()).body(userDto2).post()
				.build();
	}

	private void putTeacher(UserDto dto) {
		userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserController.USERS)
				.path(UserController.TEACHERS).path(UserController.PATH_USERNAME).expand(dto.getUsername())
				.bearerAuth(restService.getAuthToken().getToken()).body(dto).put().build();
	}

	private void patchTeacherResetPass(String username, String resetedPass) {
		restService.restBuilder(new RestBuilder<>()).path(UserController.USERS).path(UserController.TEACHERS)
				.path(UserController.PASS).path(UserController.PATH_USERNAME).expand(username)
				.bearerAuth(restService.getAuthToken().getToken()).body(resetedPass).patch().build();

	}

	private void patchTeacherSetRole(String username, Role[] roles) {
		userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserController.USERS)
				.path(UserController.TEACHERS).path(UserController.ROLE).path(UserController.PATH_USERNAME).expand(username)
				.bearerAuth(restService.getAuthToken().getToken()).body(roles).patch().build();
	}

	// POST--------------------------------------------
	public void testPostTeacher() {
		restService.loginManager();
		this.postTeacher();
	}
	
	@Test
	public void testPostTeacherUsernameUnique() {
		restService.loginAdmin();
		userDto.setUsername("u006");
		this.postTeacher();
		userDto2.setUsername("u006");
		this.postTeacher2();
		Assert.assertNotEquals(userDto2.getUsername(), userDto.getUsername());
	}

	@Test
	public void testPostTeacherDniFieldAlreadyExistException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setDni("14130268-k");
		userDto2.setDni("14130268-k");
		this.postTeacher();
		this.postTeacher2();
	}

	@Test
	public void testPostTeacherEmailFieldAlreadyExistException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setEmail(userDtoUsername + "@email.com");
		userDto2.setEmail(userDtoUsername + "@email.com");
		this.postTeacher();
		this.postTeacher2();
	}

	@Test
	public void testPostMangerMobiledFieldAlreadyExistException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setMobile("123456789");
		userDto2.setMobile("123456789");
		this.postTeacher();
		this.postTeacher2();
	}

	@Test
	public void testPostTeacherNoBearerAuth() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(UserController.USERS).path(UserController.TEACHERS).body(userDto).post().build();
	}

	@Test
	public void testPostTeacherPreAuthorize() {
		restService.loginTeacher();// PreAuthorize("hasRole('MANAGER')")
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		restService.restBuilder().path(UserController.USERS).path(UserController.TEACHERS)
				.bearerAuth(restService.getAuthToken().getToken()).body(userDto).post().build();
	}

	@Test
	public void testPostTeacherWithoutUser() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setUsername(null);
		restService.restBuilder().path(UserController.USERS).path(UserController.TEACHERS)
				.bearerAuth(restService.getAuthToken().getToken()).post().build();
	}

	@Test
	public void testPostTeacherUsernameNull() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setUsername(null);
		this.postTeacher();
	}

	@Test
	public void testPostTeacherPassNull() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setPassword(null);
		this.postTeacher();
	}

	@Test
	public void testPostTeacherBadUsername() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setUsername("lu");
		this.postTeacher();
	}

	@Test
	public void testPostTeacherUnsafePassword() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setPassword("password");
		this.postTeacher();
	}

	@Test
	public void testPostTeacherBadDni() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setDni("14130268-1");
		this.postTeacher();
	}

	@Test
	public void testPostTeacherBadMobile() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setMobile("12t678a");
		this.postTeacher();
	}

	@Test
	public void testPostTeacherBadEmail() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setMobile(userDtoUsername + "SinArroba.com");
		this.postTeacher();
	}

	// PUT--------------------------------------------

	@Test
	public void testPutTeacher() {
		restService.loginManager();
		this.postTeacher();
		userDto.setEmail(userDtoUsername + "@email.com");
		this.putTeacher(userDto);
		assertEquals(userDtoUsername + "@email.com", userDto.getEmail());
	}

	@Test
	public void testPutTeacherUsernameNotFoundException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		userDto.setUsername("fdfdgd");
		this.putTeacher(userDto);
	}

	@Test
	public void testPutTeacherUsernameFieldAlreadyExistException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setUsername("u006");
		this.postTeacher();
		this.postTeacher2();
		userDto2.setUsername("u006");
		this.putTeacher(userDto2);
	}

	@Test
	public void testPutTeacherDniFieldAlreadyExistException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setDni("14130268-k");
		this.postTeacher();
		this.postTeacher2();
		userDto2.setDni("14130268-k");
		this.putTeacher(userDto2);
	}

	@Test
	public void testPutTeacherMobileFieldAlreadyExistException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setMobile("123456789");
		this.postTeacher();
		this.postTeacher2();
		userDto2.setMobile("123456789");
		this.putTeacher(userDto2);
	}

	@Test
	public void testPutTeacherEmailFieldAlreadyExistException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setEmail(userDtoUsername + "@email.com");
		this.postTeacher();
		this.postTeacher2();
		userDto2.setEmail(userDtoUsername + "@email.com");
		this.putTeacher(userDto2);
	}

	@Test
	public void testPutTeacherNoBearerAuth() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(UserController.USERS).path(UserController.TEACHERS).body(userDto).put().build();
	}

	@Test
	public void testPutTeacherPreAuthorize() {
		restService.loginTeacher();// PreAuthorize("hasRole('MANAGER')")
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		this.putTeacher(userDto);
	}

	@Test
	public void testPutTeacherHasUserGreaterPrivileges() {
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		restService.loginManager();
		userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserController.USERS)
				.path(UserController.PATH_USERNAME).expand("010").bearerAuth(restService.getAuthToken().getToken()).get()
				.build();
		userDto.setEmail("teacher@email.com");
		this.putTeacher(userDto);
	}

	// GET---------------------------

	@Test
	public void testGetTeacherById() {
		restService.loginManager();
		this.postTeacher();
		assertEquals(userDtoUsername, this.getTeacherByID(userDto.getId()).getUsername());
	}

	@Test
	public void testGetTeacherByUsername() {
		restService.loginManager();
		this.postTeacher();
		assertEquals(userDtoUsername, this.getTeacherByUsername(userDtoUsername).getUsername());
	}

	@Test
	public void testGetTeacherNoBearerAuth() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		this.postTeacher();
		restService.restBuilder().path(UserController.USERS).path(UserController.TEACHERS).path(UserController.PATH_ID)
				.expand(userDto.getId()).get().build();
	}

	@Test
	public void testGetTeacherPreAuthorize() {
		restService.loginTeacher();// PreAuthorize("hasRole('MANAGER')")
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		this.postTeacher();
		this.getTeacherByID(userDto.getId());
	}

	@Test
	public void testGetTeacherIdNotFoundException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		this.getTeacherByID("u64563456");
	}

	@Test
	public void testGetTeacherUsernameNotFoundException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		this.getTeacherByUsername("rupertina");
	}

	@Test
	public void testGetTeacherHasUserGreaterPrivilegesByUsername() {
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		restService.loginManager();
		this.getTeacherByUsername("u010");
	}

	@Test
	public void testGetTeacherHasUserGreaterPrivilegesById() {
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		restService.loginManager();
		userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserController.USERS)
				.path(UserController.TEACHERS).path(UserController.USER_NAME).path(UserController.PATH_USERNAME)
				.expand("u010").bearerAuth(restService.getAuthToken().getToken()).get().build();
		this.getTeacherByID(userDto.getId());
	}

	// PATCH-----------------------------
	@Test
	public void testPatchTeacherResetPass() {
		restService.loginManager();
		this.postTeacher();
		this.patchTeacherResetPass(userDto.getUsername(), userDto.getUsername() + "@ESE2");
		restService.loginUser(userDto.getUsername(), userDto.getUsername() + "@ESE2");
	}

	@Test
	public void testPatchTeacherResetPassUsernameNotFoundException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		this.patchTeacherResetPass("rupertina", userDto.getUsername() + "@ESE2");
	}

	@Test
	public void testPatchTeacherResetPassHasUserGreaterPrivileges() {
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		restService.loginManager();
		this.patchTeacherResetPass("u010", userDto.getUsername() + "@ESE2");
	}

	@Test
	public void testPatchTeacherSetRole() {
		restService.loginAdmin();
		this.postTeacher();
		Role[] newRoles = new Role[] { Role.MANAGER, Role.TEACHER };
		this.patchTeacherSetRole(userDto.getUsername(), newRoles);
		Role[] restartRoles = new Role[] { Role.TEACHER };
		this.patchTeacherSetRole(userDto.getUsername(), restartRoles);
		Assert.assertArrayEquals(this.getTeacherByUsername(userDto.getUsername()).getRoles(), restartRoles);

	}

	@Test
	public void testPatchTeacherSetRoleUsernameNotFoundException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		Role[] newRoles = new Role[] { Role.MANAGER, Role.TEACHER };
		this.patchTeacherSetRole("rupertina", newRoles);
	}

	@Test
	public void testPatchTeacherSetRoleHasUserGreaterPrivileges() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		Role[] newRoles = new Role[] { Role.MANAGER, Role.TEACHER };
		this.patchTeacherSetRole("111", newRoles);
	}

}
