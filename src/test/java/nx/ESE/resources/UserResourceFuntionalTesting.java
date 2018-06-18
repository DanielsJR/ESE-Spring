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
		this.userDto = new UserDto("lukas");
		this.userDto2 = new UserDto("lucho");
	}

	private UserDto getAdmin(String id) {
		return userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserResource.USERS).path(UserResource.ADMINS).path(UserResource.PATH_ID).expand(id).get()
				.build();
	}
	
	private UserDto getManager(String id) {
		return userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserResource.USERS).path(UserResource.MANAGERS).path(UserResource.PATH_ID).expand(id).get()
				.build();
	}
	
	private UserDto getTeacher(String id) {
		return userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserResource.USERS).path(UserResource.TEACHERS).path(UserResource.PATH_ID).expand(id).get()
				.build();
	}
	
	private UserDto getStudent(String id) {
		return userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserResource.USERS).path(UserResource.STUDENTS).path(UserResource.PATH_ID).expand(id).get()
				.build();
	}
	
	private UserDto getAdminByUsername(String username) {
		return userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserResource.USERS).path(UserResource.ADMINS).path(UserResource.USER_NAME).path(UserResource.PATH_USERNAME).expand(username).get()
				.build();
	}
	
	private UserDto getManagerByUsername(String username) {
		return userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserResource.USERS).path(UserResource.MANAGERS).path(UserResource.USER_NAME).path(UserResource.PATH_USERNAME).expand(username).get()
				.build();
	}
	
	private UserDto getTeacherByUsername(String username) {
		return userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserResource.USERS).path(UserResource.TEACHERS).path(UserResource.USER_NAME).path(UserResource.PATH_USERNAME).expand(username).get()
				.build();
	}
	
	private UserDto getStudentByUsername(String username) {
		return userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserResource.USERS).path(UserResource.STUDENTS).path(UserResource.USER_NAME).path(UserResource.PATH_USERNAME).expand(username).get()
				.build();
	}
	
	private UserDto getAdminByToken(String token) {
		return userDto = restService.loginAdmin().restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserResource.USERS).path(UserResource.ADMINS).path(UserResource.TOKEN).path(UserResource.PATH_TOKEN).expand(token).get()
				.build();
	}
	
	private UserDto getManagerByToken(String token) {
		return userDto = restService.loginAdmin().restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserResource.USERS).path(UserResource.MANAGERS).path(UserResource.TOKEN).path(UserResource.PATH_TOKEN).expand(token).get()
				.build();
	}
	
	private UserDto getTeacherByToken(String token) {
		return userDto = restService.loginAdmin().restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserResource.USERS).path(UserResource.TEACHERS).path(UserResource.TOKEN).path(UserResource.PATH_TOKEN).expand(token).get()
				.build();
	}
	
	private UserDto getStudentByToken(String token) {
		return userDto = restService.loginAdmin().restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserResource.USERS).path(UserResource.STUDENTS).path(UserResource.TOKEN).path(UserResource.PATH_TOKEN).expand(token).get()
				.build();
	}
	
	
	
	private void postAdmin() {
		 userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserResource.USERS).path(UserResource.ADMINS).body(userDto).post().build();
	}
	
	private void postManager() {
		 userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserResource.USERS).path(UserResource.MANAGERS).body(userDto).post().build();
	}
	
	private void postTeacher() {
		 userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserResource.USERS).path(UserResource.TEACHERS).body(userDto).post().build();
	}
	
	private void postStudent() {
		 userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserResource.USERS).path(UserResource.STUDENTS).body(userDto).post().build();
	}

	private void postAdmin2() {
		 userDto2 = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserResource.USERS).path(UserResource.ADMINS).body(userDto2).post().build();
	}
	
	private void postManager2() {
		 userDto2 = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserResource.USERS).path(UserResource.MANAGERS).body(userDto2).post().build();
	}
	
	private void postTeacher2() {
		 userDto2 = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserResource.USERS).path(UserResource.TEACHERS).body(userDto2).post().build();
	}
	
	private void postStudent2() {
		 userDto2 = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserResource.USERS).path(UserResource.STUDENTS).body(userDto2).post().build();
	}

	private void putAdmin(UserDto dto) {
		 userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserResource.USERS).path(UserResource.ADMINS).path(UserResource.PATH_ID).expand(dto.getId())
				.body(dto).put().build();
	}
	
	private void putManager(UserDto dto) {
		 userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserResource.USERS).path(UserResource.MANAGERS).path(UserResource.PATH_ID).expand(dto.getId())
				.body(dto).put().build();
	}
	
	private void putTeacher(UserDto dto) {
		 userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserResource.USERS).path(UserResource.TEACHERS).path(UserResource.PATH_ID).expand(dto.getId())
				.body(dto).put().build();
	}
	
	private void putStudent(UserDto dto) {
		 userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserResource.USERS).path(UserResource.STUDENTS).path(UserResource.PATH_ID).expand(dto.getId())
				.body(dto).put().build();
	}

	// POST--------------------------------------------
	@Test
	public void testPostAdmin() {
		restService.loginAdmin();
		this.postAdmin();
	}
	
	@Test
	public void testPostManager() {
		restService.loginAdmin();
		this.postManager();
	}
	
	@Test
	public void testPostTeacher() {
		restService.loginManager();
		this.postTeacher();
	}
	
	@Test
	public void testPostStudent() {
		restService.loginManager();
		this.postStudent();
	}

	/*
	 * @Test public void testPostStudentPassNull() { userDto.setPassword(null);
	 * this.postUser(); }
	 */

	@Test
	public void testPostAdminWithoutUserException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setUsername(null);
		restService.restBuilder().path(UserResource.USERS).path(UserResource.ADMINS).post().build();
	}
	
	@Test
	public void testPostManagerWithoutUserException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setUsername(null);
		restService.restBuilder().path(UserResource.USERS).path(UserResource.MANAGERS).post().build();
	}
	
	@Test
	public void testPostTeacherWithoutUserException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setUsername(null);
		restService.restBuilder().path(UserResource.USERS).path(UserResource.TEACHERS).post().build();
	}
	
	@Test
	public void testPostStudentWithoutUserException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setUsername(null);
		restService.restBuilder().path(UserResource.USERS).path(UserResource.STUDENTS).post().build();
	}

	@Test
	public void testPostAdminUsernameNullException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setUsername(null);
		this.postAdmin();
	}
	
	@Test
	public void testPostManagerUsernameNullException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setUsername(null);
		this.postManager();
	}
	@Test
	public void testPostTeacherUsernameNullException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setUsername(null);
		this.postTeacher();
	}
	
	@Test
	public void testPostStudentUsernameNullException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setUsername(null);
		this.postStudent();
	}
	
	@Test
	public void testPostAdminUsernameLengthAtLeast3CharException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setUsername("lu");
		this.postAdmin();
	}
	
	@Test
	public void testPostManagerUsernameLengthAtLeast3CharException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setUsername("lu");
		this.postManager();
	}
	
	@Test
	public void testPostTeacherUsernameLengthAtLeast3CharException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setUsername("lu");
		this.postTeacher();
	}
	
	@Test
	public void testPostStudentUsernameLengthAtLeast3CharException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setUsername("lu");
		this.postStudent();
	}


	@Test
	public void testPostAdminUsernameRepeatedUserFieldAlreadyExistException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto2.setUsername("lukas");
		this.postAdmin();
		this.postAdmin2();
	}
	
	@Test
	public void testPostManagerUsernameRepeatedUserFieldAlreadyExistException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto2.setUsername("lukas");
		this.postManager();
		this.postManager2();
	}
	
	@Test
	public void testPostTeacherUsernameRepeatedUserFieldAlreadyExistException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto2.setUsername("lukas");
		this.postTeacher();
		this.postTeacher2();
	}
		
	@Test
	public void testPostStudentUsernameRepeatedUserFieldAlreadyExistException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto2.setUsername("lukas");
		this.postStudent();
		this.postStudent2();
	}
	
	@Test
	public void testPostAdminDniRepeatedUserFieldAlreadyExistException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setDni("14130268-k");
		userDto2.setDni("14130268-k");
		this.postAdmin();
		this.postAdmin2();
	}

	@Test
	public void testPostManagerDniRepeatedUserFieldAlreadyExistException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setDni("14130268-k");
		userDto2.setDni("14130268-k");
		this.postManager();
		this.postManager2();
	}
	
	
	@Test
	public void testPostTeacherDniRepeatedUserFieldAlreadyExistException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setDni("14130268-k");
		userDto2.setDni("14130268-k");
		this.postTeacher();
		this.postTeacher2();
	}
	
	@Test
	public void testPostStudentDniRepeatedUserFieldAlreadyExistException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setDni("14130268-k");
		userDto2.setDni("14130268-k");
		this.postStudent();
		this.postStudent2();
	}

	@Test
	public void testPostAdminEmailRepeatedUserFieldAlreadyExistException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setEmail("lukas@pollo.com");
		userDto2.setEmail("lukas@pollo.com");
		this.postAdmin();
		this.postAdmin2();
	}
	
	@Test
	public void testPostManagerEmailRepeatedUserFieldAlreadyExistException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setEmail("lukas@pollo.com");
		userDto2.setEmail("lukas@pollo.com");
		this.postManager();
		this.postManager2();
	}
	
	@Test
	public void testPostTeacherEmailRepeatedUserFieldAlreadyExistException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setEmail("lukas@pollo.com");
		userDto2.setEmail("lukas@pollo.com");
		this.postTeacher();
		this.postTeacher2();
	}
	
	@Test
	public void testPostStudentEmailRepeatedUserFieldAlreadyExistException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setEmail("lukas@pollo.com");
		userDto2.setEmail("lukas@pollo.com");
		this.postStudent();
		this.postStudent2();
	}

	@Test
	public void testPostAdminMobiledRepeatedUserFieldAlreadyExistException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setMobile("123456789");
		userDto2.setMobile("123456789");
		this.postAdmin();
		this.postAdmin2();
	}
	
	@Test
	public void testPostMangerMobiledRepeatedUserFieldAlreadyExistException() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setMobile("123456789");
		userDto2.setMobile("123456789");
		this.postManager();
		this.postManager2();
	}
	
	@Test
	public void testPostTeacherMobiledRepeatedUserFieldAlreadyExistException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setMobile("123456789");
		userDto2.setMobile("123456789");
		this.postTeacher();
		this.postTeacher2();
	}
	
	@Test
	public void testPostStudentMobiledRepeatedUserFieldAlreadyExistException() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		userDto.setMobile("123456789");
		userDto2.setMobile("123456789");
		this.postStudent();
		this.postStudent2();
	}

	@Test
	public void testPostAdminUnauthorized() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		restService.logout().restBuilder().path(UserResource.USERS).path(UserResource.ADMINS).body(userDto).post()
				.build();
	}
	
	@Test
	public void testPostManagerUnauthorized() {
		restService.loginAdmin();
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		restService.logout().restBuilder().path(UserResource.USERS).path(UserResource.MANAGERS).body(userDto).post()
				.build();
	}
	
	@Test
	public void testPostTeacherUnauthorized() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		restService.logout().restBuilder().path(UserResource.USERS).path(UserResource.TEACHERS).body(userDto).post()
				.build();
	}
	
	@Test
	public void testPostStudentUnauthorized() {
		restService.loginManager();
		thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
		restService.logout().restBuilder().path(UserResource.USERS).path(UserResource.STUDENTS).body(userDto).post()
				.build();
	}

	// PUT--------------------------------------------
		@Test
		public void testPutAdmin() {
			restService.loginAdmin();
			this.postAdmin();
			userDto.setEmail("lucho@mario.com");
			this.putAdmin(userDto);
			assertEquals("lucho@mario.com", userDto.getEmail());
		}
		
		@Test
		public void testPutManager() {
			restService.loginAdmin();
			this.postManager();
			userDto.setEmail("lucho@mario.com");
			this.putManager(userDto);
			assertEquals("lucho@mario.com", userDto.getEmail());
		}
		
		@Test
		public void testPutTeacher() {
			restService.loginManager();
			this.postTeacher();
			userDto.setEmail("lucho@mario.com");
			this.putTeacher(userDto);
			assertEquals("lucho@mario.com", userDto.getEmail());
		}
		
		@Test
		public void testPutStudent() {
			restService.loginManager();
			this.postStudent();
			userDto.setEmail("lucho@mario.com");
			this.putStudent(userDto);
			assertEquals("lucho@mario.com", userDto.getEmail());
		}

		@Test
		public void testPutAdmintIdNotExistsUserIdNotFoundException() {
			restService.loginAdmin();
			thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
			userDto.setId("fdfdgd");
			this.putAdmin(userDto);
		}
		
		@Test
		public void testPutManagerIdNotExistsUserIdNotFoundException() {
			restService.loginAdmin();
			thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
			userDto.setId("fdfdgd");
			this.putManager(userDto);
		}
		
		@Test
		public void testPutTeacherIdNotExistsUserIdNotFoundException() {
			restService.loginManager();
			thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
			userDto.setId("fdfdgd");
			this.putTeacher(userDto);
		}
		
		@Test
		public void testPutStudentIdNotExistsUserIdNotFoundException() {
			restService.loginManager();
			thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
			userDto.setId("fdfdgd");
			this.putStudent(userDto);
		}

		@Test
		public void testPutAdminUsernameRepeatedUserFieldAlreadyExistException() {
			restService.loginAdmin();
			thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
			userDto.setUsername("u006");
			this.postAdmin();
			this.postAdmin2();
			userDto2.setUsername("u006");
			this.putAdmin(userDto2);
		}
		
		@Test
		public void testPutManagerUsernameRepeatedUserFieldAlreadyExistException() {
			restService.loginAdmin();
			thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
			userDto.setUsername("u006");
			this.postManager();
			this.postManager2();
			userDto2.setUsername("u006");
			this.putManager(userDto2);
		}
		
		@Test
		public void testPutTeacherUsernameRepeatedUserFieldAlreadyExistException() {
			restService.loginManager();
			thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
			userDto.setUsername("u006");
			this.postTeacher();
			this.postTeacher2();
			userDto2.setUsername("u006");
			this.putTeacher(userDto2);
		}
		
		@Test
		public void testPutStudentUsernameRepeatedUserFieldAlreadyExistException() {
			restService.loginManager();
			thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
			userDto.setUsername("u006");
			this.postStudent();
			this.postStudent2();
			userDto2.setUsername("u006");
			this.putStudent(userDto2);
		}

		@Test
		public void testPutAdminDniRepeatedUserFieldAlreadyExistException() {
			restService.loginAdmin();
			thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
			userDto.setDni("14130268-k");
			this.postAdmin();
			this.postAdmin2();
			userDto2.setDni("14130268-k");
			this.putAdmin(userDto2);
		}
		
		@Test
		public void testPutManagerDniRepeatedUserFieldAlreadyExistException() {
			restService.loginAdmin();
			thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
			userDto.setDni("14130268-k");
			this.postManager();
			this.postManager2();
			userDto2.setDni("14130268-k");
			this.putManager(userDto2);
		}
		
		@Test
		public void testPutTeacherDniRepeatedUserFieldAlreadyExistException() {
			restService.loginManager();
			thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
			userDto.setDni("14130268-k");
			this.postTeacher();
			this.postTeacher2();
			userDto2.setDni("14130268-k");
			this.putTeacher(userDto2);
		}
		
		@Test
		public void testPutStudentDniRepeatedUserFieldAlreadyExistException() {
			restService.loginManager();
			thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
			userDto.setDni("14130268-k");
			this.postStudent();
			this.postStudent2();
			userDto2.setDni("14130268-k");
			this.putStudent(userDto2);
		}

		@Test
		public void testPutAdminMobileRepeatedUserFieldAlreadyExistException() {
			restService.loginAdmin();
			thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
			userDto.setMobile("123456789");
			this.postAdmin();
			this.postAdmin2();
			userDto2.setMobile("123456789");
			this.putAdmin(userDto2);
		}
		
		@Test
		public void testPutManagerMobileRepeatedUserFieldAlreadyExistException() {
			restService.loginAdmin();
			thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
			userDto.setMobile("123456789");
			this.postManager();
			this.postManager2();
			userDto2.setMobile("123456789");
			this.putManager(userDto2);
		}
		
		@Test
		public void testPutTeacherMobileRepeatedUserFieldAlreadyExistException() {
			restService.loginManager();
			thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
			userDto.setMobile("123456789");
			this.postTeacher();
			this.postTeacher2();
			userDto2.setMobile("123456789");
			this.putTeacher(userDto2);
		}
		
		@Test
		public void testPutStudentMobileRepeatedUserFieldAlreadyExistException() {
			restService.loginManager();
			thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
			userDto.setMobile("123456789");
			this.postStudent();
			this.postStudent2();
			userDto2.setMobile("123456789");
			this.putStudent(userDto2);
		}

		@Test
		public void testPutAdminEmailRepeatedUserFieldAlreadyExistException() {
			restService.loginAdmin();
			thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
			userDto.setEmail("lukas@pollo.com");
			this.postAdmin();
			this.postAdmin2();
			userDto2.setEmail("lukas@pollo.com");
			this.putAdmin(userDto2);
		}
		
		@Test
		public void testPutManagerEmailRepeatedUserFieldAlreadyExistException() {
			restService.loginAdmin();
			thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
			userDto.setEmail("lukas@pollo.com");
			this.postManager();
			this.postManager2();
			userDto2.setEmail("lukas@pollo.com");
			this.putManager(userDto2);
		}
		
		@Test
		public void testPutTeacherEmailRepeatedUserFieldAlreadyExistException() {
			restService.loginManager();
			thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
			userDto.setEmail("lukas@pollo.com");
			this.postTeacher();
			this.postTeacher2();
			userDto2.setEmail("lukas@pollo.com");
			this.putTeacher(userDto2);
		}
		
		@Test
		public void testPutStudentEmailRepeatedUserFieldAlreadyExistException() {
			restService.loginManager();
			thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
			userDto.setEmail("lukas@pollo.com");
			this.postStudent();
			this.postStudent2();
			userDto2.setEmail("lukas@pollo.com");
			this.putStudent(userDto2);
		}
		
		
		//GET---------------------------
		@Test
		public void testReadAdmin() {
			restService.loginAdmin();
			this.postAdmin();
			assertEquals("lukas", this.getAdmin(userDto.getId()).getUsername());
		}
		
		@Test
		public void testReadManager() {
			restService.loginAdmin();
			this.postManager();
			assertEquals("lukas", this.getManager(userDto.getId()).getUsername());
		}
		
		@Test
		public void testReadTeacher() {
			restService.loginManager();
			this.postTeacher();
			assertEquals("lukas", this.getTeacher(userDto.getId()).getUsername());
		}
		
		@Test
		public void testReadStudent() {
			restService.loginManager();
			this.postStudent();
			assertEquals("lukas", this.getStudent(userDto.getId()).getUsername());
		}

		@Test
		public void testGetAdminUnauthorized() {
			restService.loginAdmin();
			thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
			this.postAdmin();
			restService.logout().restBuilder().path(UserResource.USERS).path(UserResource.ADMINS)
					.path(UserResource.PATH_ID).expand(userDto.getId()).get().build();
		}
		
		@Test
		public void testGetManagerUnauthorized() {
			restService.loginAdmin();
			thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
			this.postManager();
			restService.logout().restBuilder().path(UserResource.USERS).path(UserResource.MANAGERS)
					.path(UserResource.PATH_ID).expand(userDto.getId()).get().build();
		}
		
		@Test
		public void testGetTeacherUnauthorized() {
			restService.loginManager();
			thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
			this.postTeacher();
			restService.logout().restBuilder().path(UserResource.USERS).path(UserResource.TEACHERS)
					.path(UserResource.PATH_ID).expand(userDto.getId()).get().build();
		}
		
		@Test
		public void testGetStudentUnauthorized() {
			restService.loginManager();
			thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
			this.postStudent();
			restService.logout().restBuilder().path(UserResource.USERS).path(UserResource.STUDENTS)
					.path(UserResource.PATH_ID).expand(userDto.getId()).get().build();
		}

		@Test
		public void testGetAdminNotRol() {
			thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
			restService.loginAdmin();
			this.getAdmin("u030");
		}
		
		@Test
		public void testGetManagerNotRol() {
			thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
			restService.loginAdmin();
			this.getManager("u020");
		}
		
		@Test
		public void testGetTeacherNotRol() {
			thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
			restService.loginManager();
			this.getTeacher("u010");
		}
		
		@Test
		public void testGetStudentNotRol() {
			thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
			restService.loginManager();
			this.getStudent("u000");
		}

		@Test
		public void testGetAdminNotFound() {
			restService.loginAdmin();
			thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		    this.getAdmin("u64563456");
		}
		
		@Test
		public void testGetManagerNotFound() {
			restService.loginAdmin();
			thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
			this.getManager("u64563456");
		}
		
		@Test
		public void testGetTeacherNotFound() {
			restService.loginManager();
			thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
			this.getTeacher("u64563456");
		}
		
		@Test
		public void testGetStudentNotFound() {
			restService.loginManager();
			thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
			this.getStudent("u64563456");
		}
		
		@Test
		public void testGetAdminByUsername() {
			restService.loginAdmin();
			userDto.setUsername("juana");
			this.postAdmin();
			assertEquals("juana", this.getAdminByUsername("juana").getUsername());
		}
		
		@Test
		public void testGetManagerByUsername() {
			restService.loginAdmin();
			userDto.setUsername("juana");
			this.postManager();
			assertEquals("juana", this.getManagerByUsername("juana").getUsername());
		}
		
		@Test
		public void testGetTeacherByUsername() {
			restService.loginAdmin();
			userDto.setUsername("juana");
			this.postTeacher();
			assertEquals("juana", this.getTeacherByUsername("juana").getUsername());
		}
		
		@Test
		public void testGetStudentByUsername() {
			restService.loginAdmin();
			userDto.setUsername("juana");
			this.postStudent();
			assertEquals("juana", this.getStudentByUsername("juana").getUsername());
		}

		
		@Test
		public void testGetAdminByToken() {
			restService.loginAdmin();
			Token t = new Token();
			userDto.setToken(t);
			this.postAdmin();
			String token = userDto.getToken().getValue();
		 	assertEquals(token, this.getAdminByToken(token).getToken().getValue());
		}
		
		@Test
		public void testGetManagerByToken() {
			restService.loginAdmin();
			Token t = new Token();
			userDto.setToken(t);
			this.postManager();
			String token = userDto.getToken().getValue();
		 	assertEquals(token, this.getManagerByToken(token).getToken().getValue());
		}
		
		@Test
		public void testGetTeacherByToken() {
			restService.loginManager();
			Token t = new Token();
			userDto.setToken(t);
			this.postTeacher();
			String token = userDto.getToken().getValue();
		 	assertEquals(token, this.getTeacherByToken(token).getToken().getValue());
		}
		
		@Test
		public void testGetStudentByToken() {
			restService.loginManager();
			Token t = new Token();
			userDto.setToken(t);
			this.postStudent();
			String token = userDto.getToken().getValue();
		 	assertEquals(token, this.getStudentByToken(token).getToken().getValue());
		}

	


	@After
	public void delete() {
		this.restService.loginAdmin();
		try {
			this.restService.restBuilder().path(UserResource.USERS).path(UserResource.ADMINS)
					.path(UserResource.PATH_ID).expand(userDto.getId()).delete().build();
		} catch (Exception e) {
			logger.warn("error: " + e.getMessage() + "DTO1 nothing to delete");
		}

		try {
			this.restService.restBuilder().path(UserResource.USERS).path(UserResource.ADMINS)
					.path(UserResource.PATH_ID).expand(userDto2.getId()).delete().build();
		} catch (Exception e) {
			logger.warn("error: " + e.getMessage() + "DTO2 nothing to delete");
		}
		
		try {
			this.restService.restBuilder().path(UserResource.USERS).path(UserResource.MANAGERS)
					.path(UserResource.PATH_ID).expand(userDto.getId()).delete().build();
		} catch (Exception e) {
			logger.warn("error: " + e.getMessage() + "DTO1 nothing to delete");
		}

		try {
			this.restService.restBuilder().path(UserResource.USERS).path(UserResource.MANAGERS)
					.path(UserResource.PATH_ID).expand(userDto2.getId()).delete().build();
		} catch (Exception e) {
			logger.warn("error: " + e.getMessage() + "DTO2 nothing to delete");
		}
		
		try {
			this.restService.restBuilder().path(UserResource.USERS).path(UserResource.TEACHERS)
					.path(UserResource.PATH_ID).expand(userDto.getId()).delete().build();
		} catch (Exception e) {
			logger.warn("error: " + e.getMessage() + "DTO1 nothing to delete");
		}

		try {
			this.restService.restBuilder().path(UserResource.USERS).path(UserResource.TEACHERS)
					.path(UserResource.PATH_ID).expand(userDto2.getId()).delete().build();
		} catch (Exception e) {
			logger.warn("error: " + e.getMessage() + "DTO2 nothing to delete");
		}
		
		try {
			this.restService.restBuilder().path(UserResource.USERS).path(UserResource.STUDENTS)
					.path(UserResource.PATH_ID).expand(userDto.getId()).delete().build();
		} catch (Exception e) {
			logger.warn("error: " + e.getMessage() + "DTO1 nothing to delete");
		}

		try {
			this.restService.restBuilder().path(UserResource.USERS).path(UserResource.STUDENTS)
					.path(UserResource.PATH_ID).expand(userDto2.getId()).delete().build();
		} catch (Exception e) {
			logger.warn("error: " + e.getMessage() + "DTO2 nothing to delete");
		}

	}

}
