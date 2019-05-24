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
		restService.loginManager();
		userRestService.createUsersDto();
	}

	@After
	public void delete() {
		userRestService.deleteTeachers();
	}

	// POST--------------------------------------------
	@Test
	public void testPostTeacher() {
		userRestService.postTeacher();

		UserDto uDto = userRestService.getTeacherByUsername(userRestService.getTeacherDtoUsername());
		assertEquals(uDto, userRestService.getTeacherDto());
	}

	@Test
	public void testPostTeacherPreAuthorize() {
		restService.loginTeacher();// PreAuthorize("hasRole('MANAGER')")
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.postTeacher();
	}

	@Test
	public void testPostTeacherNoBearerAuth() {
		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(UserController.USERS).path(UserController.TEACHERS)
				.body(userRestService.getTeacherDto()).post().build();
	}

	@Test
	public void testPostTeacherFieldInvalidExceptionId() {
		userRestService.postTeacher();

		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.postTeacher(); // it goes with Id
	}

	@Test
	public void testPostTeacherUsernameUnique() {
		userRestService.getTeacherDto().setUsername("u006");
		userRestService.postTeacher();

		userRestService.getTeacherDto2().setUsername("u006");
		userRestService.postTeacher2();

		Assert.assertNotEquals(userRestService.getTeacherDto2().getUsername(),
				userRestService.getTeacherDto().getUsername());
	}

	@Test
	public void testPostTeacherDniFieldAlreadyExistException() {
		userRestService.getTeacherDto().setDni("14130268-k");
		userRestService.getTeacherDto2().setDni("14130268-k");

		userRestService.postTeacher();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.postTeacher2();
	}

	@Test
	public void testPostTeacherEmailFieldAlreadyExistException() {
		userRestService.getTeacherDto().setEmail(userRestService.getTeacherDtoUsername() + "@email.com");
		userRestService.getTeacherDto2().setEmail(userRestService.getTeacherDtoUsername() + "@email.com");

		userRestService.postTeacher();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.postTeacher2();
	}

	@Test
	public void testPostMangerMobiledFieldAlreadyExistException() {
		userRestService.getTeacherDto().setMobile("123456789");
		userRestService.getTeacherDto2().setMobile("123456789");

		userRestService.postTeacher();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.postTeacher2();
	}

	@Test
	public void testPostTeacherWithoutUser() {
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.getTeacherDto().setUsername(null);
		restService.restBuilder().path(UserController.USERS).path(UserController.TEACHERS)
				.bearerAuth(restService.getAuthToken().getToken()).post().build();
	}

	@Test
	public void testPostTeacherUsernameNull() {
		userRestService.getTeacherDto().setUsername(null);

		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.postTeacher();
	}

	@Test
	public void testPostTeacherPassNull() {
		userRestService.getTeacherDto().setPassword(null);

		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.postTeacher();
	}

	@Test
	public void testPostTeacherBadUsername() {
		userRestService.getTeacherDto().setUsername("lu");

		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.postTeacher();
	}

	@Test
	public void testPostTeacherUnsafePassword() {
		userRestService.getTeacherDto().setPassword("password");

		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.postTeacher();
	}

	@Test
	public void testPostTeacherBadDni() {
		userRestService.getTeacherDto().setDni("14130268-1");

		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.postTeacher();
	}

	@Test
	public void testPostTeacherBadMobile() {
		userRestService.getTeacherDto().setMobile("12t678a");

		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.postTeacher();
	}

	@Test
	public void testPostTeacherBadEmail() {
		userRestService.getTeacherDto().setMobile(userRestService.getTeacherDtoUsername() + "SinArroba.com");

		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.postTeacher();
	}

	// PUT--------------------------------------------
	@Test
	public void testPutTeacher() {
		userRestService.postTeacher();

		String newEmail = userRestService.getTeacherDtoUsername() + "@email.com";
		userRestService.getTeacherDto().setEmail(newEmail);
		userRestService.putTeacher();

		UserDto uDto = userRestService.getTeacherByUsername(userRestService.getTeacherDtoUsername());
		assertEquals(uDto.getEmail(), userRestService.getTeacherDto().getEmail());
	}

	@Test
	public void testPutTeacherPreAuthorize() {
		restService.loginTeacher();// PreAuthorize("hasRole('MANAGER')")
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.putTeacher();
	}

	@Test
	public void testPutTeacherNoBearerAuth() {
		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(UserController.USERS).path(UserController.TEACHERS)
				.body(userRestService.getTeacherDto()).put().build();
	}

	@Test
	public void testPutTeacherUsernameNotFoundException() {
		userRestService.getTeacherDto().setUsername("fdfdgd");

		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		userRestService.putTeacher();
	}

	@Test
	public void testPutTeacherUsernameFieldAlreadyExistException() {
		userRestService.postTeacher();

		userRestService.getTeacherDto2().setUsername("u006");
		userRestService.postTeacher2();

		userRestService.getTeacherDto().setUsername(userRestService.getTeacherDto2().getUsername());

		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.putTeacher();
	}

	@Test
	public void testPutTeacherDniFieldAlreadyExistException() {
		userRestService.postTeacher();

		userRestService.getTeacherDto2().setDni("14130268-k");
		userRestService.postTeacher2();

		userRestService.getTeacherDto().setDni(userRestService.getTeacherDto2().getDni());

		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.putTeacher();
	}

	@Test
	public void testPutTeacherMobileFieldAlreadyExistException() {
		userRestService.postTeacher();

		userRestService.getTeacherDto2().setMobile("123456789");
		userRestService.postTeacher2();

		userRestService.getTeacherDto().setMobile(userRestService.getTeacherDto2().getMobile());

		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.putTeacher();
	}

	@Test
	public void testPutTeacherEmailFieldAlreadyExistException() {
		userRestService.postTeacher();

		userRestService.getTeacherDto2().setEmail(userRestService.getTeacherDto2Username() + "@email.com");
		userRestService.postTeacher2();

		userRestService.getTeacherDto().setEmail(userRestService.getTeacherDto2().getEmail());

		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userRestService.putTeacher();
	}

	@Test
	public void testPutTeacherHasUserGreaterPrivileges() {
		restService.loginAdmin();
		UserDto uDto = userRestService.getManagerByUsername("u010");

		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		restService.restBuilder().path(UserController.USERS).path(UserController.TEACHERS)
				.path(UserController.PATH_USERNAME).expand(uDto.getUsername())
				.bearerAuth(restService.getAuthToken().getToken()).body(uDto).put().build();
	}

	// PATCH-----------------------------
	@Test
	public void testPatchTeacherResetPass() {
		userRestService.postTeacher();

		String newPass = "newPass@ESE1";
		userRestService.patchTeacherResetPass(userRestService.getTeacherDto().getUsername(), newPass);

		restService.loginUser(userRestService.getTeacherDto().getUsername(), newPass);
	}

	@Test
	public void testPatchTeacherResetPassPreAuthorize() {
		userRestService.postTeacher();

		String newPass = userRestService.getTeacherDto().getUsername() + "@ESE2";
		restService.loginTeacher();// PreAuthorize("hasRole('MANAGER')")
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.patchTeacherResetPass(userRestService.getTeacherDto().getUsername(), newPass);

	}

	@Test
	public void testPatchTeacherResetPassNoBearerAuth() {
		userRestService.postTeacher();

		String newPass = "newPass@ESE1";

		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(UserController.USERS).path(UserController.TEACHERS).path(UserController.PASS)
				.path(UserController.PATH_USERNAME).expand(userRestService.getTeacherDto().getUsername()).body(newPass)
				.patch().build();
	}

	@Test
	public void testPatchTeacherResetPassUsernameNotFoundException() {
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		userRestService.patchTeacherResetPass("rupertina", userRestService.getTeacherDto().getUsername() + "@ESE2");
	}

	@Test
	public void testPatchTeacherResetPassHasUserGreaterPrivileges() {
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.patchTeacherResetPass("u010", "newPass@ESE1");
	}

	//
	@Test
	public void testPatchTeacherSetRole() {
		userRestService.postTeacher();

		restService.loginAdmin();
		Role[] newRoles = new Role[] { Role.MANAGER, Role.TEACHER };
		userRestService.getTeacherDto().setRoles(newRoles);
		userRestService.patchTeacherSetRole(userRestService.getTeacherDto());

		UserDto uDto = userRestService.getManagerByUsername(userRestService.getTeacherDto().getUsername());
		Assert.assertArrayEquals(uDto.getRoles(), newRoles);

		Role[] restartRoles = new Role[] { Role.TEACHER };
		userRestService.getTeacherDto().setRoles(restartRoles);
		userRestService.patchTeacherSetRole(userRestService.getTeacherDto());

		restService.loginManager();
		uDto = userRestService.getTeacherByUsername(userRestService.getTeacherDto().getUsername());
		Assert.assertArrayEquals(uDto.getRoles(), restartRoles);

	}

	@Test
	public void testPatchTeacherSetRolePreAuthorize() {
		userRestService.postTeacher();

		restService.loginAdmin();
		Role[] newRoles = new Role[] { Role.MANAGER, Role.TEACHER };
		userRestService.getTeacherDto().setRoles(newRoles);

		restService.loginManager();// PreAuthorize("hasRole('ADMIN')")
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.patchTeacherSetRole(userRestService.getTeacherDto());

	}

	@Test
	public void testPatchTeacherSetRoleNoBearerAuth() {
		userRestService.postTeacher();

		Role[] newRoles = new Role[] { Role.MANAGER, Role.TEACHER };
		userRestService.getTeacherDto().setRoles(newRoles);

		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(UserController.USERS).path(UserController.TEACHERS).path(UserController.ROLE)
				.path(UserController.PATH_USERNAME).expand(userRestService.getTeacherDto().getUsername())
				.body(userRestService.getTeacherDto()).patch().build();
	}

	@Test
	public void testPatchTeacherSetRoleUsernameNotFoundException() {
		restService.loginAdmin();

		Role[] newRoles = new Role[] { Role.MANAGER, Role.TEACHER };
		userRestService.getTeacherDto().setRoles(newRoles);

		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		userRestService.patchTeacherSetRole(userRestService.getTeacherDto2());
	}

	@Test
	public void testPatchTeacherSetRoleForbiddenChangeRoleFoundExceptionChiefTeacher() {
		restService.loginAdmin();
		UserDto TeacherDto = userRestService.getManagerByUsername("u020");

		Role[] newRoles = new Role[] { Role.MANAGER };
		TeacherDto.setRoles(newRoles);
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.patchTeacherSetRole(TeacherDto);

	}

	@Test
	public void testPatchTeacherSetRoleForbiddenChangeRoleFoundExceptionSubject() {
		restService.loginAdmin();
		UserDto TeacherDto = userRestService.getManagerByUsername("u023");

		restService.loginAdmin();
		Role[] newRoles = new Role[] { Role.MANAGER };
		TeacherDto.setRoles(newRoles);

		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.patchTeacherSetRole(TeacherDto);

	}

	@Test
	public void testPatchTeacherSetRoleHasUserGreaterPrivileges() {
		restService.loginMegaUser();
		UserDto uDto = userRestService.getUserByUsernameSecure("megauser");

		Role[] newRoles = new Role[] { Role.MANAGER, Role.TEACHER };
		uDto.setRoles(newRoles);

		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.patchTeacherSetRole(uDto);
	}

	// DELETE----------------------------------
	@Test
	public void testDeleteTeacher() {
		userRestService.postTeacher();

		userRestService.deleteTeacher(userRestService.getTeacherDto().getUsername());
	}

	@Test
	public void testDeleteTeacherPreAuthorize() {
		userRestService.postTeacher();

		restService.loginTeacher(); // PreAuthorize("hasRole('MANAGER')")
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.deleteTeacher(userRestService.getTeacherDto().getUsername());
	}

	@Test
	public void testDeleteTeacherNoBearerAuth() {
		userRestService.postTeacher();

		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(UserController.USERS).path(UserController.TEACHERS)
				.path(UserController.PATH_USERNAME).expand(userRestService.getTeacherDto().getUsername()).delete()
				.build();
	}

	@Test
	public void testDeleteTeacherForbiddenDeleteExceptionChiefTeacher() {
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.deleteTeacher("u020");
	}

	@Test
	public void testDeleteTeacherForbiddenDeleteExceptionSubject() {
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.deleteTeacher("u023");
	}

	@Test
	public void testDeleteTeacherHasUserGreaterPrivileges() {
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.deleteTeacher("megauser");
	}

	// GET---------------------------
	@Test
	public void testGetTeacherById() {
		userRestService.postTeacher();

		UserDto uDto = userRestService.getTeacherByID(userRestService.getTeacherDto().getId());
		assertEquals(userRestService.getTeacherDto(), uDto);
	}

	@Test
	public void testGetTeacherByIdPreAuthorize() {
		userRestService.postTeacher();

		restService.loginTeacher();// PreAuthorize("hasRole('MANAGER')")
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.getTeacherByID(userRestService.getTeacherDto().getId());
	}

	@Test
	public void testGetTeacherByIdNoBearerAuth() {
		userRestService.postTeacher();

		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(UserController.USERS).path(UserController.TEACHERS).path(UserController.PATH_ID)
				.expand(userRestService.getTeacherDto().getId()).get().build();
	}

	@Test
	public void testGetTeacherIdNotFoundException() {
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		userRestService.getTeacherByID("u64563456");
	}

	@Test
	public void testGetTeacherByIdHasUserGreaterPrivileges() {
		restService.loginAdmin();
		UserDto uDto = userRestService.getManagerByUsername("u010");

		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.getTeacherByID(uDto.getId());
	}

	//
	@Test
	public void testGetTeacherByUsername() {
		userRestService.postTeacher();

		UserDto uDto = userRestService.getTeacherByUsername(userRestService.getTeacherDto().getUsername());
		assertEquals(userRestService.getTeacherDto(), uDto);
	}

	@Test
	public void testGetTeacherByUsernamePreAuthorize() {
		userRestService.postTeacher();

		restService.loginTeacher();// PreAuthorize("hasRole('MANAGER')")
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.getTeacherByUsername(userRestService.getTeacherDto().getUsername());
	}

	@Test
	public void testGetTeacherByUsernameNoBearerAuth() {
		userRestService.postTeacher();

		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(UserController.USERS).path(UserController.TEACHERS)
				.path(UserController.PATH_USERNAME).expand(userRestService.getTeacherDto().getUsername()).get().build();
	}

	@Test
	public void testGetTeacherUsernameNotFoundException() {
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		userRestService.getTeacherByUsername("rupertina");
	}

	@Test
	public void testGetTeacherByUsernameHasUserGreaterPrivileges() {
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.getTeacherByUsername("u010");
	}

	//
	@Test
	public void testGetFullTeachers() {
		List<UserDto> uDtos = userRestService.getFullTeachers();
		assertEquals(uDtos.size() > 0, true);
	}

	@Test
	public void testGetFullTeachersPreAuthorize() {
		restService.loginTeacher();// PreAuthorize("hasRole('ADMIN')") or
									// 'MANAGER'

		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.getFullTeachers();
	}

	@Test
	public void testGetFullTeachersNoBearerAuth() {
		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(UserController.USERS).path(UserController.TEACHERS).get().build();
	}

	//
	@Test
	public void testGetMinTeachers() {
		List<UserDto> uDtos = userRestService.getMinTeachers();
		assertEquals(uDtos.size() > 0, true);
	}

	@Test
	public void testGetMinTeachersPreAuthorize() {
		restService.loginTeacher();// PreAuthorize("hasRole('ADMIN')") or
									// 'MANAGER'

		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		userRestService.getMinTeachers();
	}

	@Test
	public void testGetMinManagersNoBearerAuth() {
		restService.loginAdmin();

		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(UserController.USERS).path(UserController.MANAGERS).path(UserController.USER_MIN)
				.get().build();
	}

}
