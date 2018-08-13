package nx.ESE.resources;

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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import nx.ESE.dtos.UserDto;
import nx.ESE.services.DatabaseSeederService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class UserResourceFuntionalTesting {

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
	
	
	private static final Logger logger = LoggerFactory.getLogger(DatabaseSeederService.class);

	@Before
	public void before() {
		this.userDto = new UserDto(userDtoUsername);
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
		
		logger.warn("***********************************************************************************************");

	}

	private void postUser() {
		userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserResource.USERS)
				.path(UserResource.MANAGERS).bearerAuth(restService.getAuthToken().getToken()).body(userDto).post()
				.build();
	}

	private UserDto getUserByUsername(String username) {
		return userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserResource.USERS).path(UserResource.PATH_USERNAME).expand(username)
				.bearerAuth(restService.getAuthToken().getToken()).get().log().build();

	}

	private void putUser(UserDto dto) {
		userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserResource.USERS)
				.path(UserResource.PATH_USERNAME).expand(dto.getUsername())
				.bearerAuth(restService.getAuthToken().getToken()).body(dto).put().build();
	}

	private void patchSetUserPass(UserDto userDto, String[] passwords) {
		restService.restBuilder(new RestBuilder<>()).path(UserResource.USERS).path(UserResource.PATH_USERNAME)
				.expand(userDto.getUsername()).bearerAuth(restService.getAuthToken().getToken()).body(passwords).patch()
				.build();

	}

	// ******************TESTS******************************************************************************

	// GET-------------------------------------------
	@Test
	public void testGetUserByUsername() {
		restService.loginAdmin();
		this.postUser();
		restService.loginUser(userDtoUsername, userDtoPassword);
		assertEquals(userDtoUsername, this.getUserByUsername(userDtoUsername).getUsername());
	}

	@Test
	public void testGetUserByUsernamePreAuthorize() {
		restService.loginAdmin();
		this.postUser();
		restService.loginUser(userDtoUsername, userDtoPassword);
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		this.getUserByUsername("u010");// PreAuthorize(authentication.name)
	}

	@Test
	public void testGetUserByUsernameNoBearerAuth() {
		restService.loginAdmin();
		this.postUser();
		restService.loginUser(userDtoUsername, userDtoPassword);
		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(UserResource.USER_NAME).path(UserResource.PATH_USERNAME).expand(userDtoUsername).get()
				.build();
	}

	// PUT------------------------------------------
	@Test
	public void testPutUser() {
		restService.loginAdmin();
		this.postUser();
		restService.loginUser(userDtoUsername, userDtoPassword);
		userDto.setEmail("new@email.com");
		this.putUser(userDto);
		assertEquals("new@email.com", userDto.getEmail());
	}

	@Test
	public void testPutUserDniFieldAlreadyExistException() {
		restService.loginAdmin();
		this.postUser();
		restService.loginUser(userDtoUsername, userDtoPassword);
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setDni("17667557-8");
		this.putUser(userDto);
	}

	@Test
	public void testPutUserMobileFieldAlreadyExistException() {
		restService.loginAdmin();
		this.postUser();
		restService.loginUser(userDtoUsername, userDtoPassword);
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setMobile("011111111");
		this.putUser(userDto);
	}

	@Test
	public void testPutUserEmailFieldAlreadyExistException() {
		restService.loginAdmin();
		this.postUser();
		restService.loginUser(userDtoUsername, userDtoPassword);
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setEmail("e011@email.com");
		this.putUser(userDto);
	}

	@Test
	public void testPutUserNoBearerAuth() {
		restService.loginAdmin();
		this.postUser();
		restService.loginUser(userDtoUsername, userDtoPassword);
		userDto.setEmail("new@email.com");
		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(UserResource.USERS).path(UserResource.PATH_USERNAME)
				.expand(userDto.getUsername()).body(userDto).put().build();
	}

	@Test
	public void testPutUserPreAuthorize() {
		restService.loginAdmin();
		this.postUser();
		restService.loginUser(userDtoUsername, userDtoPassword);
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserResource.USERS)
				.path(UserResource.PATH_USERNAME).expand("other" + userDtoUsername).bearerAuth(restService.getAuthToken().getToken())
				.body(userDto).put().build();// PreAuthorize(authentication.name)
	}

	// PATCH----------------------------------------
	@Test
	public void testPatchSetUserPass() {
		restService.loginAdmin();
		this.postUser();
		restService.loginUser(userDtoUsername, userDtoPassword);
		String[] passwords = { userDtoPassword, "new"+ userDtoPassword };
		this.patchSetUserPass(userDto, passwords);
	}

	@Test
	public void testPatchSetUserPassPasswordNotMatchException() {
		restService.loginAdmin();
		this.postUser();
		restService.loginUser(userDtoUsername, userDtoPassword);
		String[] passwords = { "wrong"+ userDtoPassword, "new"+ userDtoPassword };
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		this.patchSetUserPass(userDto, passwords);
	}

	@Test
	public void testPatchSetUserPassNoBearerAuth() {
		restService.loginAdmin();
		this.postUser();
		restService.loginUser(userDtoUsername, userDtoPassword);
		String[] passwords = { userDtoPassword, "new"+ userDtoPassword };
		thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
		restService.restBuilder().path(UserResource.USERS).body(passwords).put().build();
	}

	@Test
	public void testPatchSetUserPassPreAuthorize() {
		restService.loginAdmin();
		this.postUser();
		restService.loginUser(userDtoUsername, userDtoPassword);
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		String[] passwords = { userDtoPassword, "new"+ userDtoPassword };
		restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserResource.USERS)
		.path(UserResource.PATH_USERNAME).expand("other" + userDtoUsername).bearerAuth(restService.getAuthToken().getToken())
		.body(passwords).patch().build();// PreAuthorize(authentication.name)
	}

}
