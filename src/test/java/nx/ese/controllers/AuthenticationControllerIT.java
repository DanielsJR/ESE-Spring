package nx.ese.controllers;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import nx.ese.documents.Role;
import nx.ese.dtos.UserDto;
import nx.ese.services.HttpMatcher;
import nx.ese.services.RestBuilder;
import nx.ese.services.RestService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class AuthenticationControllerIT {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private RestService restService;

    private UserDto getUserByUsername(String username) {
        UserDto userDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
                .path(UserController.USER)
                .path(UserController.PATH_USERNAME).expand(username)
                .bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
        return userDto;
    }

    @Test
    public void testLoginAdmin() {
        restService.loginAdmin();
        assertEquals(Role.ADMIN, this.getUserByUsername(restService.getAdminUsername()).getRoles()[0]);
    }

    @Test
    public void testLoginAdminUnauthorized() {
        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.loginUser(restService.getAdminUsername(), "incorrectPass");
    }

    @Test
    public void testLoginUnauthorized() {
        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.loginUser("incorrectUser", "incorrectPass");
    }

    @Test
    public void testLoginNoUserAndPass() {
        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(AuthenticationController.TOKEN)
                .path(AuthenticationController.GENERATE_TOKEN)
                //.basicAuth("null","null")
                .post()
                .build();
    }


}
