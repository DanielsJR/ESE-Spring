package nx.ese.controllers;

import nx.ese.dtos.ThemeDto;
import nx.ese.services.*;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class PreferencesControllerIT {

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Autowired
    private RestService restService;

    @Autowired
    private UserRestService userRestService;

    @Autowired
    private PreferencesRestService preferencesRestService;


    @Before
    public void before() {
        preferencesRestService.createThemesDto();
        restService.loginUser(userRestService.getTeacherDtoUsername(), userRestService.getTeacherDtoUsername() + "@ESE1");
    }

    @After
    public void delete() {
        preferencesRestService.deleteThemesDto();
    }

    // PUT********************************
    @Test
    public void testPutTheme() {
        preferencesRestService.putTheme();
        ThemeDto tDto = preferencesRestService.getThemeByUsername(userRestService.getTeacherDto().getUsername());
        Assert.assertEquals(tDto, preferencesRestService.getThemeDto());
    }

    @Test
    public void testPutThemeNoBearerAuth() {
        preferencesRestService.putTheme();

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(PreferencesController.PREFERENCES)
                .path(PreferencesController.THEME)
                .path(PreferencesController.PATH_USERNAME).expand(userRestService.getTeacherDto().getUsername())
                //.bearerAuth(restService.getAuthToken().getToken())
                .body(preferencesRestService.getThemeDto())
                .put()
                .build();
    }

    @Test
    public void testPutThemePreAuthorizeUsername() {
        restService.loginManager();
        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        preferencesRestService.putTheme();
    }

    @Test
    public void testPutThemeBodyNull() {
        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        preferencesRestService.putTheme(userRestService.getTeacherDto().getUsername(), null);
    }

    @Test
    public void testPutThemeNameNull() {
        preferencesRestService.getThemeDto().setName(null);//@NotNull
        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        preferencesRestService.putTheme();
    }

    @Test
    public void testPutThemePrimaryColorNull() {
        preferencesRestService.getThemeDto().setPrimaryColor(null);//@NotNull
        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        preferencesRestService.putTheme();
    }

    @Test
    public void testPutThemeAccentColorNull() {
        preferencesRestService.getThemeDto().setAccentColor(null);//@NotNull
        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        preferencesRestService.putTheme();
    }

    // DELETE********************************
    @Test
    public void testDeleteTheme() {
        preferencesRestService.putTheme();
        Assert.assertTrue(preferencesRestService.deleteTheme(userRestService.getTeacherDto().getUsername()));
    }

    @Test
    public void testDeleteThemeNoBearerAuth() {
        preferencesRestService.putTheme();

        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(PreferencesController.PREFERENCES)
                .path(PreferencesController.THEME)
                .path(PreferencesController.PATH_USERNAME).expand(userRestService.getTeacherDto().getUsername())
                //.bearerAuth(restService.getAuthToken().getToken())
                .delete()
                .build();
    }

    @Test
    public void testDeleteThemePreAuthorizeUsername() {
        preferencesRestService.putTheme();

        restService.loginManager();
        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        preferencesRestService.deleteTheme(userRestService.getTeacherDto().getUsername());
    }


    // GET********************************
    @Test
    public void testGetThemeByUsername() {
        preferencesRestService.putTheme();
        ThemeDto tDto = preferencesRestService.getThemeByUsername(userRestService.getTeacherDto().getUsername());
        Assert.assertEquals(tDto, preferencesRestService.getThemeDto());
    }

    @Test
    public void testGetThemeByUsernameDefault() {
        ThemeDto tDto = preferencesRestService.getThemeByUsername(userRestService.getTeacherDto().getUsername());
        Assert.assertEquals("indigo-pink", tDto.getName());
    }

    @Test
    public void testGetThemeByUsernameNoBearerAuth() {
        thrown.expect(new HttpMatcher(HttpStatus.UNAUTHORIZED));
        restService.restBuilder()
                .path(PreferencesController.PREFERENCES)
                .path(PreferencesController.THEME)
                .path(PreferencesController.PATH_USERNAME).expand(userRestService.getTeacherDto().getUsername())
                //.bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }

    @Test
    public void testGetThemeByUsernamePreAuthorizeUsername() {
        restService.loginManager();
        thrown.expect(new HttpMatcher(HttpStatus.FORBIDDEN));
        preferencesRestService.getThemeByUsername(userRestService.getTeacherDto().getUsername());
    }


}
