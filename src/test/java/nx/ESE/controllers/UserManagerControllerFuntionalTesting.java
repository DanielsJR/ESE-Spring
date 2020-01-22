package nx.ESE.controllers;

import static org.junit.Assert.assertEquals;

import java.util.List;

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
public class UserManagerControllerFuntionalTesting {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Autowired
	private RestService restService;

	@Autowired
	private UserRestService userRestService;

	@Before
	public void before() {
		restService.loginAdmin();
		userRestService.createUsersDto();
	}

	@After
	public void delete() {
		userRestService.deleteManagers();
	}

	// POST--------------------------------------------

	@Test
	public void testPostManager() {
		userRestService.postManager();

		UserDto uDto = userRestService.getManagerByUsername(userRestService.getManagerDtoUsername());
		assertEquals(uDto, userRestService.getManagerDto());
	}

	@Test
	public void testPostManagerPreAuthorize() {
		restService.loginManager();// PreAuthorize("hasRole('ADMIN')")

		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.postManager();
	}

	@Test
	public void testPostManagerNoBearerAuth() {
		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(UserController.USERS).path(UserController.MANAGERS)
				.body(userRestService.getManagerDto()).post().build();
	}

	@Test
	public void testPostManagerFieldInvalidExceptionId() {
		userRestService.postManager();

		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.postManager();// it goes with Id
	}

	@Test
	public void testPostManagerUsernameUnique() {
		userRestService.getManagerDto().setUsername("u006");
		userRestService.postManager();

		userRestService.getManagerDto2().setUsername("u006");
		userRestService.postManager2();
		Assert.assertNotEquals(userRestService.getManagerDto2().getUsername(),
				userRestService.getManagerDto().getUsername());
	}

	@Test
	public void testPostManagerDniFieldAlreadyExistException() {
		userRestService.getManagerDto().setDni("14130268-k");
		userRestService.postManager();

		userRestService.getManagerDto2().setDni("14130268-k");

		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.postManager2();
	}

	@Test
	public void testPostManagerEmailFieldAlreadyExistException() {
		userRestService.getManagerDto().setEmail(userRestService.getManagerDtoUsername() + "@email.com");
		userRestService.postManager();

		userRestService.getManagerDto2().setEmail(userRestService.getManagerDtoUsername() + "@email.com");
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.postManager2();
	}

	@Test
	public void testPostMangerMobiledFieldAlreadyExistException() {
		userRestService.getManagerDto().setMobile("123456789");
		userRestService.postManager();

		userRestService.getManagerDto2().setMobile("123456789");
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.postManager2();
	}

	@Test
	public void testPostManagerWithoutBody() {
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		restService.restBuilder().path(UserController.USERS).path(UserController.MANAGERS)
				.bearerAuth(restService.getAuthToken().getToken()).post().build();
	}

	@Test
	public void testPostManagerUsernameNull() {
		userRestService.getManagerDto().setUsername(null);

		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.postManager();
	}

	@Test
	public void testPostManagerPassNull() {
		userRestService.getManagerDto().setPassword(null);

		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.postManager();
	}

	@Test
	public void testPostManagerBadUsername() {
		userRestService.getManagerDto().setUsername("lu");		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.postManager();
	}

	@Test
	public void testPostManagerUnsafePassword() {
		userRestService.getManagerDto().setPassword("password");

		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.postManager(); 
		
	}

	@Test
	public void testPostManagerBadDni() {
		userRestService.getManagerDto().setDni("14130268-1");

		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.postManager();
	}

	@Test
	public void testPostManagerBadMobile() {
		userRestService.getManagerDto().setMobile("12t678a");

		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.postManager();
	}

	@Test
	public void testPostManagerBadEmail() {
		userRestService.getManagerDto().setMobile(userRestService.getManagerDtoUsername() + "SinArroba.com");

		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.postManager();
	}

	// PUT--------------------------------------------
	@Test
	public void testPutManager() {
		userRestService.postManager();

		String newEmail = userRestService.getManagerDtoUsername() + "@email.com";
		userRestService.getManagerDto().setEmail(newEmail);
		userRestService.putManager();

		UserDto uDto = userRestService.getManagerByUsername(userRestService.getManagerDtoUsername());
		assertEquals(uDto.getEmail(), userRestService.getManagerDto().getEmail());
	}

	@Test
	public void testPutManagerPreAuthorize() {
		userRestService.postManager();

		restService.loginManager();// PreAuthorize("hasRole('ADMIN')")
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.putManager();
	}

	@Test
	public void testPutManagerNoBearerAuth() {
		userRestService.postManager();

		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(UserController.USERS).path(UserController.MANAGERS)
				.body(userRestService.getManagerDto()).put().build();
	}

	@Test
	public void testPutManagerUsernameNotFoundException() {
		userRestService.getManagerDto().setUsername("fdfdgd");

		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		userRestService.putManager();
	}

	@Test
	public void testPutManagerUsernameFieldAlreadyExistException() {
		userRestService.postManager();

		userRestService.getManagerDto2().setUsername("u006");
		userRestService.postManager2();

		userRestService.getManagerDto().setUsername(userRestService.getManagerDto2().getUsername());

		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.putManager();
	}

	@Test
	public void testPutManagerDniFieldAlreadyExistException() {
		userRestService.postManager();

		userRestService.getManagerDto2().setDni("14130268-k");
		userRestService.postManager2();

		userRestService.getManagerDto().setDni(userRestService.getManagerDto2().getDni());

		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.putManager();
	}

	@Test
	public void testPutManagerMobileFieldAlreadyExistException() {
		userRestService.postManager();

		userRestService.getManagerDto2().setMobile("123456789");
		userRestService.postManager2();

		userRestService.getManagerDto().setMobile(userRestService.getManagerDto2().getMobile());

		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.putManager();
	}

	@Test
	public void testPutManagerEmailFieldAlreadyExistException() {
		userRestService.postManager();

		userRestService.getManagerDto2().setEmail(userRestService.getManagerDto2Username() + "@email.com");
		userRestService.postManager2();

		userRestService.getManagerDto().setEmail(userRestService.getManagerDto2().getEmail());

		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.putManager();
	}

	@Test
	public void testPutManagerHasUserGreaterPrivileges() {
		restService.loginMegaUser();

		UserDto uDto = userRestService.getUserByUsernameSecure("megauser");
		uDto.setEmail("admin@email.com");

		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		restService.restBuilder().path(UserController.USERS).path(UserController.MANAGERS)
				.path(UserController.PATH_USERNAME).expand(uDto.getUsername())
				.bearerAuth(restService.getAuthToken().getToken()).body(uDto).put().build();
	}

	// PATCH-----------------------------
	@Test
	public void testPatchManagerResetPass() {
		userRestService.postManager();

		String newPass = "newPass@ESE1";
		userRestService.patchManagerResetPass(userRestService.getManagerDto().getUsername(), newPass);

		restService.loginUser(userRestService.getManagerDto().getUsername(), newPass);
	}

	@Test
	public void testPatchManagerResetPassPreAuthorize() {
		userRestService.postManager();

		String newPass = userRestService.getManagerDto().getUsername() + "@ESE2";
		restService.loginManager();// PreAuthorize("hasRole('ADMIN')")
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.patchManagerResetPass(userRestService.getManagerDto().getUsername(), newPass);

	}

	@Test
	public void testPatchManagerResetPassNoBearerAuth() {
		userRestService.postManager();

		String newPass = "newPass@ESE1";

		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(UserController.USERS).path(UserController.MANAGERS).path(UserController.PASS)
				.path(UserController.PATH_USERNAME).expand(userRestService.getManagerDto().getUsername()).body(newPass)
				.patch().build();
	}

	@Test
	public void testPatchManagerResetPassUsernameNotFoundException() {
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		userRestService.patchManagerResetPass("rupertina", userRestService.getManagerDto().getUsername() + "@ESE2");
	}

	@Test
	public void testPatchManagerResetPassHasUserGreaterPrivileges() {
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.patchManagerResetPass("megauser", "newPass@ESE1");
	}

	//
	@Test
	public void testPatchManagerSetRole() {
		userRestService.postManager();

		Role[] newRoles = new Role[] { Role.MANAGER, Role.TEACHER };
		userRestService.getManagerDto().setRoles(newRoles);
		userRestService.patchManagerSetRole(userRestService.getManagerDto());

		UserDto uDto = userRestService.getManagerByUsername(userRestService.getManagerDto().getUsername());
		Assert.assertArrayEquals(uDto.getRoles(), newRoles);

		Role[] restartRoles = new Role[] { Role.TEACHER };
		userRestService.getManagerDto().setRoles(restartRoles);
		userRestService.patchManagerSetRole(userRestService.getManagerDto());

		uDto = userRestService.getManagerByUsername(userRestService.getManagerDto().getUsername());
		Assert.assertArrayEquals(uDto.getRoles(), restartRoles);

	}

	@Test
	public void testPatchManagerSetRolePreAuthorize() {
		userRestService.postManager();

		Role[] newRoles = new Role[] { Role.MANAGER, Role.TEACHER };
		userRestService.getManagerDto().setRoles(newRoles);

		restService.loginManager();// PreAuthorize("hasRole('ADMIN')")
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.patchManagerSetRole(userRestService.getManagerDto());

	}

	@Test
	public void testPatchManagerSetRoleNoBearerAuth() {
		userRestService.postManager();

		Role[] newRoles = new Role[] { Role.MANAGER, Role.TEACHER };
		userRestService.getManagerDto().setRoles(newRoles);

		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(UserController.USERS).path(UserController.MANAGERS).path(UserController.ROLE)
				.path(UserController.PATH_USERNAME).expand(userRestService.getManagerDto().getUsername())
				.body(userRestService.getManagerDto()).patch().build();
	}

	@Test
	public void testPatchManagerSetRoleForbiddenChangeRoleFoundExceptionChiefTeacher() {
		UserDto managerTeacherDto = userRestService.getManagerByUsername("u012");// ChiefTeacher

		Role[] newRoles = new Role[] { Role.MANAGER };
		managerTeacherDto.setRoles(newRoles);

		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.patchManagerSetRole(managerTeacherDto);
	}

	@Test
	public void testPatchManagerSetRoleForbiddenChangeRoleFoundExceptionSubject() {
		UserDto managerTeacherDto = userRestService.getManagerByUsername("u010");// Subject

		Role[] newRoles = new Role[] { Role.MANAGER };
		managerTeacherDto.setRoles(newRoles);

		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.patchManagerSetRole(managerTeacherDto);
	}

	@Test
	public void testPatchManagerSetRoleUsernameNotFoundException() {
		Role[] newRoles = new Role[] { Role.MANAGER, Role.TEACHER };
		userRestService.getManagerDto().setRoles(newRoles);

		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		userRestService.patchManagerSetRole(userRestService.getManagerDto());
	}

	@Test
	public void testPatchManagerSetRoleHasUserGreaterPrivileges() {
		restService.loginMegaUser();
		UserDto uDto = userRestService.getUserByUsernameSecure("megauser");

		Role[] newRoles = new Role[] { Role.MANAGER, Role.TEACHER };
		uDto.setRoles(newRoles);

		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.patchManagerSetRole(uDto);
	}

	// DELETE----------------------------------
	@Test
	public void testDeleteManager() {
		userRestService.postManager();

		userRestService.deleteManager(userRestService.getManagerDto().getUsername());
	}

	@Test
	public void testDeleteManagerPreAuthorize() {
		userRestService.postManager();

		restService.loginManager();// PreAuthorize("hasRole('ADMIN')")
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.deleteManager(userRestService.getManagerDto().getUsername());
	}

	@Test
	public void testDeleteManagerNoBearerAuth() {
		userRestService.postManager();

		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(UserController.USERS).path(UserController.MANAGERS)
				.path(UserController.PATH_USERNAME).expand(userRestService.getManagerDto().getUsername()).delete()
				.build();
	}

	@Test
	public void testDeleteManagerForbiddenDeleteExceptionChiefTeacher() {
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.deleteManager("u012");
	}

	@Test
	public void testDeleteManagerForbiddenDeleteExceptionSubject() {
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.deleteManager("u010");
	}

	@Test
	public void testDeleteManagerHasUserGreaterPrivileges() {
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.deleteManager("megauser");
	}

	// GET---------------------------
	@Test
	public void testGetManagerById() {
		userRestService.postManager();

		UserDto uDto = userRestService.getManagerByID(userRestService.getManagerDto().getId());
		assertEquals(userRestService.getManagerDto(), uDto);
	}

	@Test
	public void testGetManagerByIdPreAuthorize() {
		userRestService.postManager();

		restService.loginManager();// PreAuthorize("hasRole('ADMIN')")
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.getManagerByID(userRestService.getManagerDto().getId());
	}

	@Test
	public void testGetManagerByIdNoBearerAuth() {
		userRestService.postManager();

		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(UserController.USERS).path(UserController.MANAGERS).path(UserController.PATH_ID)
				.expand(userRestService.getManagerDto().getId()).get().build();
	}

	@Test
	public void testGetManagerByIdNotFoundException() {
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		userRestService.getManagerByID("u64563456");
	}

	@Test
	public void testGetManagerByIdHasUserGreaterPrivileges() {
		restService.loginMegaUser();
		UserDto uDto = userRestService.getUserByUsernameSecure("megauser");

		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.getManagerByID(uDto.getId());
	}

	//
	@Test
	public void testGetManagerByUsername() {
		userRestService.postManager();

		UserDto uDto = userRestService.getManagerByUsername(userRestService.getManagerDtoUsername());
		assertEquals(userRestService.getManagerDto(), uDto);

	}

	@Test
	public void testGetManagerByUsernamePreAuthorize() {
		userRestService.postManager();

		restService.loginManager();// PreAuthorize("hasRole('ADMIN')")
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.getManagerByUsername(userRestService.getManagerDto().getUsername());
	}

	@Test
	public void testGetManagerByusernameNoBearerAuth() {
		userRestService.postManager();

		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(UserController.USERS).path(UserController.MANAGERS)
				.path(UserController.PATH_USERNAME).expand(userRestService.getManagerDto().getUsername()).get().build();
	}

	@Test
	public void testGetManagerUsernameNotFoundException() {
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		userRestService.getManagerByUsername("rupertina");
	}

	@Test
	public void testGetManagerByUsernameHasUserGreaterPrivilegesBy() {
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.getManagerByUsername("megauser");
	}

	//
	@Test
	public void testGetFullManagers() {
		List<UserDto> uDtos = userRestService.getFullManagers();
		assertEquals(uDtos.size() > 0, true);
	}

	@Test
	public void testGetFullManagersPreAuthorize() {
		restService.loginManager();// PreAuthorize("hasRole('ADMIN')")

		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.getFullManagers();
	}

	@Test
	public void testGetFullManagersNoBearerAuth() {
		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(UserController.USERS).path(UserController.MANAGERS).get().build();
	}

	//
	@Test
	public void testGetMinManagers() {
		List<UserDto> uDtos = userRestService.getMinManagers();
		assertEquals(uDtos.size() > 0, true);
	}

	@Test
	public void testGetMinManagersPreAuthorize() {
		restService.loginManager();// PreAuthorize("hasRole('ADMIN')")

		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.getMinManagers();
	}

	@Test
	public void testGetMinManagersNoBearerAuth() {
		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(UserController.USERS).path(UserController.MANAGERS).path(UserController.USER_MIN)
				.get().build();
	}

}