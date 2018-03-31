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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import nx.ESE.documents.Token;
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

	private static final Logger logger = LoggerFactory.getLogger(DatabaseSeederService.class);

	@Before
	public void before() {
		restService.loginAdmin();
		this.userDto = new UserDto("lukas");
		this.userDto2 = new UserDto("lucho");
	}

	private UserDto getStudent(String id) {
		return userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserResource.USERS).path(UserResource.STUDENTS).path(UserResource.PATH_ID).expand(id).get()
				.build();
	}
	
	private UserDto getStudentByUsername(String username) {
		return userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserResource.USERS).path(UserResource.STUDENTS).path(UserResource.USER_NAME).path(UserResource.PATH_USERNAME).expand(username).get()
				.build();
	}
	
	private UserDto getStudentByToken(String token) {
		return userDto = restService.loginAdmin().restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserResource.USERS).path(UserResource.STUDENTS).path(UserResource.TOKEN).path(UserResource.PATH_TOKEN).expand(token).get()
				.build();
	}
	
	
	
	private void postStudent() {
		 userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserResource.USERS).path(UserResource.STUDENTS).body(userDto).post().build();
	}

	private void postStudent2() {
		 userDto2 = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserResource.USERS).path(UserResource.STUDENTS).body(userDto2).post().build();
	}

	private void putStudent(UserDto dto) {
		 userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserResource.USERS).path(UserResource.STUDENTS).path(UserResource.PATH_ID).expand(dto.getId())
				.body(dto).put().build();
	}

	// POST--------------------------------------------
	@Test
	public void testPostStudent() {
		this.postStudent();
	}

	/*
	 * @Test public void testPostStudentPassNull() { userDto.setPassword(null);
	 * this.postUser(); }
	 */

	@Test
	public void testPostStudentWithoutUserException() {
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setUsername(null);
		restService.loginAdmin().restBuilder().path(UserResource.USERS).path(UserResource.STUDENTS).post().build();
	}

	@Test
	public void testPostStudentUsernameNullException() {
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setUsername(null);
		this.postStudent();
	}
	
	@Test
	public void testPostStudentUsernameLengthAtLeast3CharException() {
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setUsername("lu");
		this.postStudent();
	}


	@Test
	public void testPostStudentUsernameRepeatedUserFieldAlreadyExistException() {
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto2.setUsername("lukas");
		this.postStudent();
		this.postStudent2();
	}

	@Test
	public void testPostStudentDniRepeatedUserFieldAlreadyExistException() {
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setDni("14130268-k");
		userDto2.setDni("14130268-k");
		this.postStudent();
		this.postStudent2();
	}

	@Test
	public void testPostStudentEmailRepeatedUserFieldAlreadyExistException() {
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setEmail("lukas@pollo.com");
		userDto2.setEmail("lukas@pollo.com");
		this.postStudent();
		this.postStudent2();
	}

	@Test
	public void testPostStudentMobiledRepeatedUserFieldAlreadyExistException() {
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setMobile("123456789");
		userDto2.setMobile("123456789");
		this.postStudent();
		this.postStudent2();
	}

	@Test
	public void testPostUnauthorized() {
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		restService.logout().restBuilder().path(UserResource.USERS).path(UserResource.STUDENTS).body(userDto).post()
				.build();
	}


	// PUT--------------------------------------------
	@Test
	public void testPutStudent() {
		this.postStudent();
		userDto.setEmail("lucho@mario.com");
		this.putStudent(userDto);
		assertEquals("lucho@mario.com", userDto.getEmail());
	}

	@Test
	public void testPutStudentIdNotExistsUserIdNotFoundException() {
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		userDto.setId("fdfdgd");
		this.putStudent(userDto);
	}

	@Test
	public void testPutStudentUsernameRepeatedUserFieldAlreadyExistException() {
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setUsername("d030");
		this.postStudent();
		this.postStudent2();
		userDto2.setUsername("d030");
		this.putStudent(userDto2);

	}

	@Test
	public void testPutStudentDniRepeatedUserFieldAlreadyExistException() {
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setDni("14130268-k");
		this.postStudent();
		this.postStudent2();
		userDto2.setDni("14130268-k");
		this.putStudent(userDto2);
	}

	@Test
	public void testPutStudentMobileRepeatedUserFieldAlreadyExistException() {
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setMobile("123456789");
		this.postStudent();
		this.postStudent2();
		userDto2.setMobile("123456789");
		this.putStudent(userDto2);
	}

	@Test
	public void testPutStudentEmailRepeatedUserFieldAlreadyExistException() {
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setEmail("lukas@pollo.com");
		this.postStudent();
		this.postStudent2();
		userDto2.setEmail("lukas@pollo.com");
		this.putStudent(userDto2);
	}
	
	
	
	//GET---------------------------
	@Test
	public void testReadUser() {
		this.postStudent();
		assertEquals("lukas", this.getStudent(userDto.getId()).getUsername());
	}

	@Test
	public void testGetUserUnauthorized() {
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		this.postStudent();
		restService.logout().restBuilder().path(UserResource.USERS).path(UserResource.STUDENTS)
				.path(UserResource.PATH_ID).expand(userDto.getId()).get().build();
	}

	/*@Test
	public void testGetUserNotRol() {
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		restService.loginAdmin();
		this.getStudent("u000");
	}*/

	@Test
	public void testGetUserNotFound() {
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		restService.loginAdmin();
		this.getStudent("u64563456");
	}
	
	@Test
	public void testGetUserByUsername() {
		userDto.setUsername("juana");
		this.postStudent();
		assertEquals("juana", this.getStudentByUsername("juana").getUsername());
	}

	
	@Test
	public void testGetByToken() {
		Token t = new Token();
		userDto.setToken(t);
		this.postStudent();
		String token = userDto.getToken().getValue();
	 	assertEquals(token, this.getStudentByToken(token).getToken().getValue());
	}




	@After
	public void delete() {
		try {
			this.restService.loginAdmin().restBuilder().path(UserResource.USERS).path(UserResource.STUDENTS)
					.path(UserResource.PATH_ID).expand(userDto.getId()).delete().build();
		} catch (Exception e) {
			logger.warn("error: " + e.getMessage() + "DTO1 nothing to delete");
		}

		try {
			this.restService.loginAdmin().restBuilder().path(UserResource.USERS).path(UserResource.STUDENTS)
					.path(UserResource.PATH_ID).expand(userDto2.getId()).delete().build();
		} catch (Exception e) {
			logger.warn("error: " + e.getMessage() + "DTO2 nothing to delete");
		}

	}

}
