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
import nx.ESE.services.HttpMatcher;
import nx.ESE.services.RestBuilder;
import nx.ESE.services.RestService;
import nx.ESE.services.UserRestService;
import nx.ESE.services.data.DatabaseSeederService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class UserManagerControllerFuntionalTesting {

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
		userRestService.deleteManagers();
	}

	// POST--------------------------------------------

	@Test
	public void testPostManager() {
		restService.loginAdmin();
		userRestService.postManager();
	}

	@Test
	public void testPostManagerUsernameUnique() {
		restService.loginAdmin();
		userRestService.getManagerDto().setUsername("u006");
		userRestService.postManager();
		userRestService.getManagerDto2().setUsername("u006");
		userRestService.postManager2();
		Assert.assertNotEquals(userRestService.getManagerDto2().getUsername(),
				userRestService.getManagerDto().getUsername());
	}

	@Test
	public void testPostManagerDniFieldAlreadyExistException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getManagerDto().setDni("14130268-k");
		userRestService.getManagerDto2().setDni("14130268-k");
		userRestService.postManager();
		userRestService.postManager2();
	}

	@Test
	public void testPostManagerEmailFieldAlreadyExistException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getManagerDto().setEmail(userRestService.getManagerDtoUsername() + "@email.com");
		userRestService.getManagerDto2().setEmail(userRestService.getManagerDtoUsername() + "@email.com");
		userRestService.postManager();
		userRestService.postManager2();
	}

	@Test
	public void testPostMangerMobiledFieldAlreadyExistException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getManagerDto().setMobile("123456789");
		userRestService.getManagerDto2().setMobile("123456789");
		userRestService.postManager();
		userRestService.postManager2();
	}

	@Test
	public void testPostManagerNoBearerAuth() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(UserController.USERS).path(UserController.MANAGERS)
				.body(userRestService.getManagerDto()).post().build();
	}

	@Test
	public void testPostManagerPreAuthorize() {
		restService.loginManager();// PreAuthorize("hasRole('ADMIN')")
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		restService.restBuilder().path(UserController.USERS).path(UserController.MANAGERS)
				.bearerAuth(restService.getAuthToken().getToken()).body(userRestService.getManagerDto()).post().build();
	}

	@Test
	public void testPostManagerWithoutUser() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getManagerDto().setUsername(null);
		restService.restBuilder().path(UserController.USERS).path(UserController.MANAGERS)
				.bearerAuth(restService.getAuthToken().getToken()).post().build();
	}

	@Test
	public void testPostManagerUsernameNull() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getManagerDto().setUsername(null);
		userRestService.postManager();
	}

	@Test
	public void testPostManagerPassNull() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getManagerDto().setPassword(null);
		userRestService.postManager();
	}

	@Test
	public void testPostManagerBadUsername() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getManagerDto().setUsername("lu");
		userRestService.postManager();
	}

	@Test
	public void testPostManagerUnsafePassword() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getManagerDto().setPassword("password");
		userRestService.postManager();
	}

	@Test
	public void testPostManagerBadDni() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getManagerDto().setDni("14130268-1");
		userRestService.postManager();
	}

	@Test
	public void testPostManagerBadMobile() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getManagerDto().setMobile("12t678a");
		userRestService.postManager();
	}

	@Test
	public void testPostManagerBadEmail() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getManagerDto().setMobile(userRestService.getManagerDtoUsername() + "SinArroba.com");
		userRestService.postManager();
	}

	// PUT--------------------------------------------

	@Test
	public void testPutManager() {
		restService.loginAdmin();
		userRestService.postManager();
		userRestService.getManagerDto().setEmail(userRestService.getManagerDtoUsername() + "@email.com");
		userRestService.putManager(userRestService.getManagerDto());
		assertEquals(userRestService.getManagerDtoUsername() + "@email.com",
				userRestService.getManagerDto().getEmail());
	}

	@Test
	public void testPutManagerUsernameNotFoundException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		userRestService.getManagerDto().setUsername("fdfdgd");
		userRestService.putManager(userRestService.getManagerDto());
	}

	@Test
	public void testPutManagerUsernameFieldAlreadyExistException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getManagerDto().setUsername("u006");
		userRestService.postManager();
		userRestService.postManager2();
		userRestService.getManagerDto2().setUsername("u006");
		userRestService.putManager(userRestService.getManagerDto2());
	}

	@Test
	public void testPutManagerDniFieldAlreadyExistException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getManagerDto().setDni("14130268-k");
		userRestService.postManager();
		userRestService.postManager2();
		userRestService.getManagerDto2().setDni("14130268-k");
		userRestService.putManager(userRestService.getManagerDto2());
	}

	@Test
	public void testPutManagerMobileFieldAlreadyExistException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getManagerDto().setMobile("123456789");
		userRestService.postManager();
		userRestService.postManager2();
		userRestService.getManagerDto2().setMobile("123456789");
		userRestService.putManager(userRestService.getManagerDto2());
	}

	@Test
	public void testPutManagerEmailFieldAlreadyExistException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getManagerDto().setEmail(userRestService.getManagerDtoUsername() + "@email.com");
		userRestService.postManager();
		userRestService.postManager2();
		userRestService.getManagerDto2().setEmail(userRestService.getManagerDtoUsername() + "@email.com");
		userRestService.putManager(userRestService.getManagerDto2());
	}

	@Test
	public void testPutManagerNoBearerAuth() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(UserController.USERS).path(UserController.MANAGERS)
				.body(userRestService.getManagerDto()).put().build();
	}

	@Test
	public void testPutManagerPreAuthorize() {
		restService.loginManager();// PreAuthorize("hasRole('ADMIN')")
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.putManager(userRestService.getManagerDto());
	}

	@Test
	public void testPutManagerHasUserGreaterPrivileges() {
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		restService.loginAdmin();
		userRestService.setManagerDto(restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserController.USERS).path(UserController.PATH_USERNAME).expand("111")
				.bearerAuth(restService.getAuthToken().getToken()).get().build());
		userRestService.getManagerDto().setEmail("admin@email.com");
		userRestService.putManager(userRestService.getManagerDto());
	}

	// GET---------------------------

	@Test
	public void testGetManagerById() {
		restService.loginAdmin();
		userRestService.postManager();
		assertEquals(userRestService.getManagerDtoUsername(),
				userRestService.getManagerByID(userRestService.getManagerDto().getId()).getUsername());
	}

	@Test
	public void testGetManagerByUsername() {
		restService.loginAdmin();
		userRestService.postManager();
		assertEquals(userRestService.getManagerDtoUsername(),
				userRestService.getManagerByUsername(userRestService.getManagerDtoUsername()).getUsername());
	}

	@Test
	public void testGetManagerNoBearerAuth() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		userRestService.postManager();
		restService.restBuilder().path(UserController.USERS).path(UserController.MANAGERS).path(UserController.PATH_ID)
				.expand(userRestService.getManagerDto().getId()).get().build();
	}

	@Test
	public void testGetManagerPreAuthorize() {
		restService.loginManager();// PreAuthorize("hasRole('ADMIN')")
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.postManager();
		userRestService.getManagerByID(userRestService.getManagerDto().getId());
	}

	@Test
	public void testGetManagerIdNotFoundException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		userRestService.getManagerByID("u64563456");
	}

	@Test
	public void testGetManagerUsernameNotFoundException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		userRestService.getManagerByUsername("rupertina");
	}

	@Test
	public void testGetManagerHasUserGreaterPrivilegesByUsername() {
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		restService.loginAdmin();
		userRestService.getManagerByUsername("111");
	}

	@Test
	public void testGetManagerHasUserGreaterPrivilegesById() {
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		restService.loginAdmin();
		userRestService.setManagerDto(
				restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserController.USERS)
						.path(UserController.MANAGERS).path(UserController.USER_NAME).path(UserController.PATH_USERNAME)
						.expand("111").bearerAuth(restService.getAuthToken().getToken()).get().build());
		userRestService.getManagerByID(userRestService.getManagerDto().getId());
	}

	// PATCH-----------------------------

	@Test
	public void testPatchManagerResetPass() {
		restService.loginAdmin();
		userRestService.postManager();
		userRestService.patchManagerResetPass(userRestService.getManagerDto().getUsername(),
				userRestService.getManagerDto().getUsername() + "@ESE2");
		restService.loginUser(userRestService.getManagerDto().getUsername(),
				userRestService.getManagerDto().getUsername() + "@ESE2");
	}

	@Test
	public void testPatchManagerResetPassUsernameNotFoundException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		userRestService.patchManagerResetPass("rupertina", userRestService.getManagerDto().getUsername() + "@ESE2");
	}

	@Test
	public void testPatchManagerResetPassHasUserGreaterPrivileges() {
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		restService.loginAdmin();
		userRestService.patchManagerResetPass("111", userRestService.getManagerDto().getUsername() + "@ESE2");
	}

	@Test
	public void testPatchManagerSetRole() {
		restService.loginAdmin();
		userRestService.postManager();
		Role[] newRoles = new Role[] { Role.MANAGER, Role.TEACHER };
		userRestService.patchManagerSetRole(userRestService.getManagerDto().getUsername(), newRoles);
		Assert.assertArrayEquals(
				userRestService.getManagerByUsername(userRestService.getManagerDto().getUsername()).getRoles(),
				newRoles);
	}

	@Test
	public void testPatchManagerSetRoleUsernameNotFoundException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		Role[] newRoles = new Role[] { Role.MANAGER, Role.TEACHER };
		userRestService.patchManagerSetRole("rupertina", newRoles);
	}

	@Test
	public void testPatchManagerSetRoleHasUserGreaterPrivileges() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		Role[] newRoles = new Role[] { Role.MANAGER, Role.TEACHER };
		userRestService.patchManagerSetRole("111", newRoles);
	}

}