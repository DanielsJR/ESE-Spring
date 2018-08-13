package nx.ESE.resources;

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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import nx.ESE.documents.Commune;
import nx.ESE.documents.Gender;
import nx.ESE.documents.Role;
import nx.ESE.dtos.UserDto;
import nx.ESE.services.DatabaseSeederService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class UserManagerResourceFuntionalTesting {

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
		this.restService.loginAdmin();

		try {
			this.restService.restBuilder().path(UserResource.USERS).path(UserResource.MANAGERS)
					.path(UserResource.PATH_USERNAME).expand(userDto.getUsername())
					.bearerAuth(restService.getAuthToken().getToken()).delete().build();
		} catch (Exception e) {
			logger.warn("error: " + e.getMessage() + "DTO1 nothing to delete");
		}

		try {
			this.restService.restBuilder().path(UserResource.USERS).path(UserResource.MANAGERS)
					.path(UserResource.PATH_USERNAME).expand(userDto2.getUsername())
					.bearerAuth(restService.getAuthToken().getToken()).delete().build();
		} catch (Exception e) {
			logger.warn("error: " + e.getMessage() + "DTO2 nothing to delete");
		}
		logger.warn("***********************************************************************************************");

	}

	private void postManager() {
		userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserResource.USERS)
				.path(UserResource.MANAGERS).bearerAuth(restService.getAuthToken().getToken()).body(userDto).post()
				.build();
	}

	private void postManager2() {
		userDto2 = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserResource.USERS)
				.path(UserResource.MANAGERS).bearerAuth(restService.getAuthToken().getToken()).body(userDto2).post()
				.build();
	}

	private void putManager(UserDto dto) {
		userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserResource.USERS)
				.path(UserResource.MANAGERS).path(UserResource.PATH_USERNAME).expand(dto.getUsername())
				.bearerAuth(restService.getAuthToken().getToken()).body(dto).put().build();
	}

	private UserDto getManagerByID(String id) {
		return userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserResource.USERS).path(UserResource.MANAGERS).path(UserResource.ID).path(UserResource.PATH_ID)
				.expand(id).bearerAuth(restService.getAuthToken().getToken()).get().build();
	}

	private UserDto getManagerByUsername(String username) {
		return userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserResource.USERS).path(UserResource.MANAGERS).path(UserResource.USER_NAME)
				.path(UserResource.PATH_USERNAME).expand(username).bearerAuth(restService.getAuthToken().getToken())
				.get().build();
	}

	private void patchManagerResetPass(String username, String resetedPass) {
		restService.restBuilder(new RestBuilder<>()).path(UserResource.USERS).path(UserResource.MANAGERS)
				.path(UserResource.PASS).path(UserResource.PATH_USERNAME).expand(username)
				.bearerAuth(restService.getAuthToken().getToken()).body(resetedPass).patch().build();

	}

	private void patchManagerSetRole(String username, Role[] roles) {
		userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserResource.USERS)
				.path(UserResource.MANAGERS).path(UserResource.ROLE).path(UserResource.PATH_USERNAME).expand(username)
				.bearerAuth(restService.getAuthToken().getToken()).body(roles).patch().build();
	}

	// ******************TESTS******************************************************************************

	// POST--------------------------------------------

	@Test
	public void testPostManager() {
		restService.loginAdmin();
		this.postManager();
	}

	@Test
	public void testPostManagerUsernameFieldAlreadyExistException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto2.setUsername(userDtoUsername);
		this.postManager();
		this.postManager2();
	}

	@Test
	public void testPostManagerDniFieldAlreadyExistException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setDni("14130268-k");
		userDto2.setDni("14130268-k");
		this.postManager();
		this.postManager2();
	}

	@Test
	public void testPostManagerEmailFieldAlreadyExistException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setEmail(userDtoUsername + "@email.com");
		userDto2.setEmail(userDtoUsername + "@email.com");
		this.postManager();
		this.postManager2();
	}

	@Test
	public void testPostMangerMobiledFieldAlreadyExistException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setMobile("123456789");
		userDto2.setMobile("123456789");
		this.postManager();
		this.postManager2();
	}

	@Test
	public void testPostManagerNoBearerAuth() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(UserResource.USERS).path(UserResource.MANAGERS).body(userDto).post().build();
	}

	@Test
	public void testPostManagerPreAuthorize() {
		restService.loginManager();// PreAuthorize("hasRole('ADMIN')")
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		restService.restBuilder().path(UserResource.USERS).path(UserResource.MANAGERS)
				.bearerAuth(restService.getAuthToken().getToken()).body(userDto).post().build();
	}

	@Test
	public void testPostManagerWithoutUser() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setUsername(null);
		restService.restBuilder().path(UserResource.USERS).path(UserResource.MANAGERS)
				.bearerAuth(restService.getAuthToken().getToken()).post().build();
	}

	@Test
	public void testPostManagerUsernameNull() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setUsername(null);
		this.postManager();
	}

	@Test
	public void testPostManagerPassNull() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setPassword(null);
		this.postManager();
	}

	@Test
	public void testPostManagerBadUsername() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setUsername("lu");
		this.postManager();
	}

	@Test
	public void testPostManagerUnsafePassword() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setPassword("password");
		this.postManager();
	}

	@Test
	public void testPostManagerBadDni() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setDni("14130268-1");
		this.postManager();
	}

	@Test
	public void testPostManagerBadMobile() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setMobile("12t678a");
		this.postManager();
	}

	@Test
	public void testPostManagerBadEmail() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setMobile(userDtoUsername + "SinArroba.com");
		this.postManager();
	}

	// PUT--------------------------------------------

	@Test
	public void testPutManager() {
		restService.loginAdmin();
		this.postManager();
		userDto.setEmail(userDtoUsername + "@email.com");
		this.putManager(userDto);
		assertEquals(userDtoUsername + "@email.com", userDto.getEmail());
	}

	@Test
	public void testPutManagerUsernameNotFoundException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		userDto.setUsername("fdfdgd");
		this.putManager(userDto);
	}

	@Test
	public void testPutManagerUsernameFieldAlreadyExistException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setUsername("u006");
		this.postManager();
		this.postManager2();
		userDto2.setUsername("u006");
		this.putManager(userDto2);
	}

	@Test
	public void testPutManagerDniFieldAlreadyExistException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setDni("14130268-k");
		this.postManager();
		this.postManager2();
		userDto2.setDni("14130268-k");
		this.putManager(userDto2);
	}

	@Test
	public void testPutManagerMobileFieldAlreadyExistException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setMobile("123456789");
		this.postManager();
		this.postManager2();
		userDto2.setMobile("123456789");
		this.putManager(userDto2);
	}

	@Test
	public void testPutManagerEmailFieldAlreadyExistException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setEmail(userDtoUsername + "@email.com");
		this.postManager();
		this.postManager2();
		userDto2.setEmail(userDtoUsername + "@email.com");
		this.putManager(userDto2);
	}

	@Test
	public void testPutManagerNoBearerAuth() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(UserResource.USERS).path(UserResource.MANAGERS).body(userDto).put().build();
	}

	@Test
	public void testPutManagerPreAuthorize() {
		restService.loginManager();// PreAuthorize("hasRole('ADMIN')")
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		this.putManager(userDto);
	}

	@Test
	public void testPutManagerHasUserGreaterPrivileges() {
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		restService.loginAdmin();
		userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserResource.USERS)
				.path(UserResource.PATH_USERNAME).expand("111").bearerAuth(restService.getAuthToken().getToken()).get()
				.build();
		userDto.setEmail("admin@email.com");
		this.putManager(userDto);
	}

	// GET---------------------------

	@Test
	public void testGetManagerById() {
		restService.loginAdmin();
		this.postManager();
		assertEquals(userDtoUsername, this.getManagerByID(userDto.getId()).getUsername());
	}

	@Test
	public void testGetManagerByUsername() {
		restService.loginAdmin();
		this.postManager();
		assertEquals(userDtoUsername, this.getManagerByUsername(userDtoUsername).getUsername());
	}

	@Test
	public void testGetManagerNoBearerAuth() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		this.postManager();
		restService.restBuilder().path(UserResource.USERS).path(UserResource.MANAGERS).path(UserResource.PATH_ID)
				.expand(userDto.getId()).get().build();
	}

	@Test
	public void testGetManagerPreAuthorize() {
		restService.loginManager();// PreAuthorize("hasRole('ADMIN')")
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		this.postManager();
		this.getManagerByID(userDto.getId());
	}

	@Test
	public void testGetManagerIdNotFoundException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		this.getManagerByID("u64563456");
	}

	@Test
	public void testGetManagerUsernameNotFoundException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		this.getManagerByUsername("rupertina");
	}

	@Test
	public void testGetManagerHasUserGreaterPrivilegesByUsername() {
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		restService.loginAdmin();
		this.getManagerByUsername("111");
	}

	@Test
	public void testGetManagerHasUserGreaterPrivilegesById() {
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		restService.loginAdmin();
		userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserResource.USERS)
				.path(UserResource.MANAGERS).path(UserResource.USER_NAME).path(UserResource.PATH_USERNAME).expand("111")
				.bearerAuth(restService.getAuthToken().getToken()).get().build();
		this.getManagerByID(userDto.getId());
	}

	// PATCH-----------------------------

	@Test
	public void testPatchManagerResetPass() {
		restService.loginAdmin();
		this.postManager();
		this.patchManagerResetPass(userDto.getUsername(), userDto.getUsername() + "@ESE2");
		restService.loginUser(userDto.getUsername(), userDto.getUsername() + "@ESE2");
	}

	@Test
	public void testPatchManagerResetPassUsernameNotFoundException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		this.patchManagerResetPass("rupertina", userDto.getUsername() + "@ESE2");
	}

	@Test
	public void testPatchManagerResetPassHasUserGreaterPrivileges() {
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		restService.loginAdmin();
		this.patchManagerResetPass("111", userDto.getUsername() + "@ESE2");
	}

	@Test
	public void testPatchManagerSetRole() {
		restService.loginAdmin();
		this.postManager();
		Role[] newRoles = new Role[] { Role.MANAGER, Role.TEACHER };
		this.patchManagerSetRole(userDto.getUsername(), newRoles);
		Assert.assertArrayEquals(this.getManagerByUsername(userDto.getUsername()).getRoles(), newRoles);
	}

	@Test
	public void testPatchManagerSetRoleUsernameNotFoundException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		Role[] newRoles = new Role[] { Role.MANAGER, Role.TEACHER };
		this.patchManagerSetRole("rupertina", newRoles);
	}

	@Test
	public void testPatchManagerSetRoleHasUserGreaterPrivileges() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		Role[] newRoles = new Role[] { Role.MANAGER, Role.TEACHER };
		this.patchManagerSetRole("111", newRoles);
	}

}