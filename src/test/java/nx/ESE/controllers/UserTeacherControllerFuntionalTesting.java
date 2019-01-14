package nx.ESE.controllers;

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

import nx.ESE.controllers.UserController;
import nx.ESE.documents.Role;
import nx.ESE.dtos.UserDto;
import nx.ESE.services.HttpMatcher;
import nx.ESE.services.RestBuilder;
import nx.ESE.services.RestService;
import nx.ESE.services.UserRestService;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class UserTeacherControllerFuntionalTesting {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Autowired
	private RestService restService;

	@Autowired
	private UserRestService userRestService;

	@Before
	public void before() {
		userRestService.createUsersDto();
	}

	@After
	public void delete() {
		userRestService.deleteTeachers();
	}

	// POST--------------------------------------------
	public void testPostTeacher() {
		restService.loginManager();
		userRestService.postTeacher();
	}

	@Test
	public void testPostTeacherUsernameUnique() {
		restService.loginManager();
		userRestService.getTeacherDto().setUsername("u006");
		userRestService.postTeacher();
		userRestService.getTeacherDto2().setUsername("u006");
		userRestService.postTeacher2();
		Assert.assertNotEquals(userRestService.getTeacherDto2().getUsername(),
				userRestService.getTeacherDto().getUsername());
	}

	@Test
	public void testPostTeacherDniFieldAlreadyExistException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getTeacherDto().setDni("14130268-k");
		userRestService.getTeacherDto2().setDni("14130268-k");
		userRestService.postTeacher();
		userRestService.postTeacher2();
	}

	@Test
	public void testPostTeacherEmailFieldAlreadyExistException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getTeacherDto().setEmail(userRestService.getTeacherDtoUsername() + "@email.com");
		userRestService.getTeacherDto2().setEmail(userRestService.getTeacherDtoUsername() + "@email.com");
		userRestService.postTeacher();
		userRestService.postTeacher2();
	}

	@Test
	public void testPostMangerMobiledFieldAlreadyExistException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getTeacherDto().setMobile("123456789");
		userRestService.getTeacherDto2().setMobile("123456789");
		userRestService.postTeacher();
		userRestService.postTeacher2();
	}

	@Test
	public void testPostTeacherNoBearerAuth() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(UserController.USERS).path(UserController.TEACHERS)
				.body(userRestService.getTeacherDto()).post().build();
	}

	@Test
	public void testPostTeacherPreAuthorize() {
		restService.loginTeacher();// PreAuthorize("hasRole('MANAGER')")
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		restService.restBuilder().path(UserController.USERS).path(UserController.TEACHERS)
				.bearerAuth(restService.getAuthToken().getToken()).body(userRestService.getTeacherDto()).post().build();
	}

	@Test
	public void testPostTeacherWithoutUser() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getTeacherDto().setUsername(null);
		restService.restBuilder().path(UserController.USERS).path(UserController.TEACHERS)
				.bearerAuth(restService.getAuthToken().getToken()).post().build();
	}

	@Test
	public void testPostTeacherUsernameNull() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getTeacherDto().setUsername(null);
		userRestService.postTeacher();
	}

	@Test
	public void testPostTeacherPassNull() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getTeacherDto().setPassword(null);
		userRestService.postTeacher();
	}

	@Test
	public void testPostTeacherBadUsername() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getTeacherDto().setUsername("lu");
		userRestService.postTeacher();
	}

	@Test
	public void testPostTeacherUnsafePassword() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getTeacherDto().setPassword("password");
		userRestService.postTeacher();
	}

	@Test
	public void testPostTeacherBadDni() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getTeacherDto().setDni("14130268-1");
		userRestService.postTeacher();
	}

	@Test
	public void testPostTeacherBadMobile() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getTeacherDto().setMobile("12t678a");
		userRestService.postTeacher();
	}

	@Test
	public void testPostTeacherBadEmail() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getTeacherDto().setMobile(userRestService.getTeacherDtoUsername() + "SinArroba.com");
		userRestService.postTeacher();
	}

	// PUT--------------------------------------------

	@Test
	public void testPutTeacher() {
		restService.loginManager();
		userRestService.postTeacher();
		userRestService.getTeacherDto().setEmail(userRestService.getTeacherDtoUsername() + "@email.com");
		userRestService.putTeacher(userRestService.getTeacherDto());
		assertEquals(userRestService.getTeacherDtoUsername() + "@email.com",
				userRestService.getTeacherDto().getEmail());
	}

	@Test
	public void testPutTeacherUsernameNotFoundException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		userRestService.getTeacherDto().setUsername("fdfdgd");
		userRestService.putTeacher(userRestService.getTeacherDto());
	}

	@Test
	public void testPutTeacherUsernameFieldAlreadyExistException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getTeacherDto().setUsername("u006");
		userRestService.postTeacher();
		userRestService.postTeacher2();
		userRestService.getTeacherDto2().setUsername("u006");
		userRestService.putTeacher(userRestService.getTeacherDto2());
	}

	@Test
	public void testPutTeacherDniFieldAlreadyExistException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getTeacherDto().setDni("14130268-k");
		userRestService.postTeacher();
		userRestService.postTeacher2();
		userRestService.getTeacherDto2().setDni("14130268-k");
		userRestService.putTeacher(userRestService.getTeacherDto2());
	}

	@Test
	public void testPutTeacherMobileFieldAlreadyExistException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getTeacherDto().setMobile("123456789");
		userRestService.postTeacher();
		userRestService.postTeacher2();
		userRestService.getTeacherDto2().setMobile("123456789");
		userRestService.putTeacher(userRestService.getTeacherDto2());
	}

	@Test
	public void testPutTeacherEmailFieldAlreadyExistException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getTeacherDto().setEmail(userRestService.getTeacherDtoUsername() + "@email.com");
		userRestService.postTeacher();
		userRestService.postTeacher2();
		userRestService.getTeacherDto2().setEmail(userRestService.getTeacherDtoUsername() + "@email.com");
		userRestService.putTeacher(userRestService.getTeacherDto2());
	}

	@Test
	public void testPutTeacherNoBearerAuth() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(UserController.USERS).path(UserController.TEACHERS)
				.body(userRestService.getTeacherDto()).put().build();
	}

	@Test
	public void testPutTeacherPreAuthorize() {
		restService.loginTeacher();// PreAuthorize("hasRole('MANAGER')")
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.putTeacher(userRestService.getTeacherDto());
	}

	@Test
	public void testPutTeacherHasUserGreaterPrivileges() {
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		restService.loginManager();
		userRestService.setTeacherDto(restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserController.USERS).path(UserController.PATH_USERNAME).expand("010")
				.bearerAuth(restService.getAuthToken().getToken()).get().build());
		userRestService.getTeacherDto().setEmail("teacher@email.com");
		userRestService.putTeacher(userRestService.getTeacherDto());
	}

	// GET---------------------------

	@Test
	public void testGetTeacherById() {
		restService.loginManager();
		userRestService.postTeacher();
		assertEquals(userRestService.getTeacherDtoUsername(),
				userRestService.getTeacherByID(userRestService.getTeacherDto().getId()).getUsername());
	}

	@Test
	public void testGetTeacherByUsername() {
		restService.loginManager();
		userRestService.postTeacher();
		assertEquals(userRestService.getTeacherDtoUsername(),
				userRestService.getTeacherByUsername(userRestService.getTeacherDtoUsername()).getUsername());
	}

	@Test
	public void testGetTeacherNoBearerAuth() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		userRestService.postTeacher();
		restService.restBuilder().path(UserController.USERS).path(UserController.TEACHERS).path(UserController.PATH_ID)
				.expand(userRestService.getTeacherDto().getId()).get().build();
	}

	@Test
	public void testGetTeacherPreAuthorize() {
		restService.loginTeacher();// PreAuthorize("hasRole('MANAGER')")
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.postTeacher();
		userRestService.getTeacherByID(userRestService.getTeacherDto().getId());
	}

	@Test
	public void testGetTeacherIdNotFoundException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		userRestService.getTeacherByID("u64563456");
	}

	@Test
	public void testGetTeacherUsernameNotFoundException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		userRestService.getTeacherByUsername("rupertina");
	}

	@Test
	public void testGetTeacherHasUserGreaterPrivilegesByUsername() {
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		restService.loginManager();
		userRestService.getTeacherByUsername("u010");
	}

	@Test
	public void testGetTeacherHasUserGreaterPrivilegesById() {
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		restService.loginManager();
		userRestService.setTeacherDto(
				restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserController.USERS)
						.path(UserController.TEACHERS).path(UserController.USER_NAME).path(UserController.PATH_USERNAME)
						.expand("u010").bearerAuth(restService.getAuthToken().getToken()).get().build());
		userRestService.getTeacherByID(userRestService.getTeacherDto().getId());
	}

	// PATCH-----------------------------
	@Test
	public void testPatchTeacherResetPass() {
		restService.loginManager();
		userRestService.postTeacher();
		userRestService.patchTeacherResetPass(userRestService.getTeacherDto().getUsername(),
				userRestService.getTeacherDto().getUsername() + "@ESE2");
		restService.loginUser(userRestService.getTeacherDto().getUsername(),
				userRestService.getTeacherDto().getUsername() + "@ESE2");
	}

	@Test
	public void testPatchTeacherResetPassUsernameNotFoundException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		userRestService.patchTeacherResetPass("rupertina", userRestService.getTeacherDto().getUsername() + "@ESE2");
	}

	@Test
	public void testPatchTeacherResetPassHasUserGreaterPrivileges() {
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		restService.loginManager();
		userRestService.patchTeacherResetPass("u010", userRestService.getTeacherDto().getUsername() + "@ESE2");
	}

	@Test
	public void testPatchTeacherSetRole() {
		restService.loginManager();
		userRestService.postTeacher();
		Role[] newRoles = new Role[] { Role.MANAGER, Role.TEACHER };
		restService.loginAdmin();
		userRestService.patchTeacherSetRole(userRestService.getTeacherDto().getUsername(), newRoles);
		Role[] restartRoles = new Role[] { Role.TEACHER };
		userRestService.patchTeacherSetRole(userRestService.getTeacherDto().getUsername(), restartRoles);
		restService.loginManager();
		Assert.assertArrayEquals(
				userRestService.getTeacherByUsername(userRestService.getTeacherDto().getUsername()).getRoles(),
				restartRoles);

	}

	@Test
	public void testPatchTeacherSetRoleUsernameNotFoundException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		Role[] newRoles = new Role[] { Role.MANAGER, Role.TEACHER };
		userRestService.patchTeacherSetRole("rupertina", newRoles);
	}

	@Test
	public void testPatchTeacherSetRoleHasUserGreaterPrivileges() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		Role[] newRoles = new Role[] { Role.MANAGER, Role.TEACHER };
		userRestService.patchTeacherSetRole("111", newRoles);
	}

}
