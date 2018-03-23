package nx.ESE.resources;

import static org.junit.Assert.assertEquals;

import org.junit.After;
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

import nx.ESE.dtos.UserDto;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class UserResourceFuntionalTesting {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Autowired
	private RestService restService;

	private UserDto userDto;
	

	@Before
	public void before() {
		this.userDto = new UserDto("Lukas");
	}
	

	private UserDto getUser() {
		return userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
		.path(UserResource.USERS).path(UserResource.STUDENT).path(UserResource.USER_NAME).expand(userDto.getUsername())
		.get().build();
	}
	
	private UserDto getUser(String username) {
		return userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
		.path(UserResource.USERS).path(UserResource.STUDENT).path(UserResource.USER_NAME).expand(username)
		.get().build();
	}
	
	private void createUser() {
		restService.loginAdmin()
		.restBuilder().path(UserResource.USERS).path(UserResource.STUDENT)
		.body(userDto).post().build();
	}
	
	private void modifyUser(String path, UserDto dto ) {
		 restService.loginAdmin().restBuilder()
	     .path(UserResource.USERS).path(UserResource.STUDENT).path(UserResource.USER_NAME).expand(path)
	     .body(dto).put().build();
	}
	
	
	//POST--------------------------------------------
	@Test
    public void testPostStudent() {
        this.createUser();
    }
	
	@Test
    public void testPostStudentPassNull() {
        userDto.setPassword(null);
        this.createUser();
    }

    @Test
    public void testPostStudentWithoutUserException() {
        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        userDto.setUsername(null);
        restService.loginAdmin().restBuilder().path(UserResource.USERS).path(UserResource.STUDENT)
        .post().build();
    }

    @Test
    public void testPostStudentUsernameNullException() {
        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        userDto.setUsername(null);
        this.createUser();
    }
    
    @Test
    public void testPostStudentUsernameFieldInvalidException() {
        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        userDto.setUsername("");
        this.createUser();
    }
    
    @Test
    public void testPostStudentUsernameRepeatedUserFieldAlreadyExistException() {
        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        this.createUser();
        this.createUser();
    }
    
    @Test
    public void testPostStudentDniRepeatedUserFieldAlreadyExistException() {
        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        userDto.setDni("d030");
        this.createUser();
    }

    @Test
    public void testPostStudentEmailRepeatedUserFieldAlreadyExistException() {
        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        userDto.setEmail("e030");
        this.createUser();
    }
    
    @Test
    public void testPostStudentMobiledRepeatedUserFieldAlreadyExistException() {
        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        userDto.setMobile("m030");
        this.createUser();
    }
    
    @Test
    public void testPostUnauthorized() {
        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        restService.logout().restBuilder().path(UserResource.USERS).path(UserResource.STUDENT)
        .body(userDto).post().build();
    }
    
    @Test
    public void testReadUser() {
        this.createUser();
        this.getUser();
    }
    
    @Test
    public void testReadUserUnauthorized() {
        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        restService.logout().restBuilder().path(UserResource.USERS).path(UserResource.STUDENT).path(UserResource.USER_NAME)
        .expand("u030").get().build();
    }
    
    @Test
    public void testReadUserNotRol() {
        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        restService.loginAdmin();
        this.getUser("u000");
    }
    
    @Test
    public void testReadUserNotFound() {
        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        restService.loginAdmin();
        this.getUser("u64563456");
    }

    //PUT--------------------------------------------   
	@Test
	public void testPutStudent() {
		this.createUser();
		userDto.setUsername("Lucho");
		userDto.setEmail("lucho@lucho.com");
		this.modifyUser("Lukas", userDto);
		this.getUser();
        assertEquals("lucho@lucho.com", userDto.getEmail());
	}

    @Test
    public void testPutStudentUsernameNotExistsUserIdNotFoundException() {
        thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
        this.modifyUser(userDto.getUsername(), userDto);
    }
    
    @Test
    public void testPutStudentUsernameRepeatedUserFieldAlreadyExistException() {
        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        this.createUser();
        this.modifyUser("u031", userDto);
    }
    
    @Test
    public void testPutStudentDniRepeatedUserFieldAlreadyExistException() {
        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        userDto.setDni("d030");
        this.modifyUser("u031", userDto);
    }
    
    @Test
    public void testPutStudentMobileRepeatedUserFieldAlreadyExistException() {
        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        userDto.setMobile("m030");
        this.modifyUser("u031", userDto);
    }
    
    @Test
    public void testPutStudentEmailRepeatedUserFieldAlreadyExistException() {
        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        userDto.setEmail("e030");
        this.modifyUser("u031", userDto);
    }
    
    
    
	
    @After
    public void delete() {
    	try{
        this.restService.loginAdmin().restBuilder()
        .path(UserResource.USERS).path(UserResource.STUDENT).path(UserResource.USER_NAME).expand(userDto.getUsername())
        .delete().build();
    	}catch (Exception e) {
			
		}
    }

}
