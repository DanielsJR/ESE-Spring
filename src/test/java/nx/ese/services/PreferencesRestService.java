package nx.ese.services;

import lombok.Getter;
import lombok.Setter;
import nx.ese.controllers.PreferencesController;
import nx.ese.dtos.ThemeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PreferencesRestService {

    @Autowired
    private RestService restService;

    @Autowired
    private UserRestService userRestService;

    @Getter
    @Setter
    private ThemeDto themeDto;

    @Getter
    @Setter
    private ThemeDto themeDto2;

    private static final Logger logger = LoggerFactory.getLogger(PreferencesRestService.class);

    public void createThemesDto() {
        userRestService.createUsersDto();

        restService.loginManager();
        userRestService.postTeacher();
        userRestService.postTeacher2();

        logger.info("*********************************CREATING_THEMES*****************************************");

        this.themeDto = new ThemeDto("indigo-lightblue", false, "#3f51b5", "#03a9f4");
        this.themeDto2 = new ThemeDto("deeppurple-amber", false, "#673ab7", "#ffab00");

        logger.info("**************************************************************************************");

    }

    public void deleteThemesDto() {
        logger.info("*********************************DELETING_THEMES**************************************");
        this.restService.loginUser(userRestService.getTeacherDto().getUsername(), userRestService.getTeacherDto().getUsername() + "@ESE1");
        try {
            this.deleteTheme(userRestService.getTeacherDto().getId());
        } catch (Exception e) {
            logger.info("ThemeDto: nothing to delete");
        }
        this.restService.loginUser(userRestService.getTeacherDto2().getUsername(), userRestService.getTeacherDto2().getUsername() + "@ESE1");
        try {
            this.deleteTheme(userRestService.getTeacherDto2().getId());
        } catch (Exception e) {
            logger.info("ThemeDto2: nothing to delete");
        }

        this.userRestService.deleteTeachers();

        logger.info("********************************************************************************");

    }


    public ThemeDto putTheme() {
        return themeDto = restService.restBuilder(new RestBuilder<ThemeDto>()).clazz(ThemeDto.class)
                .path(PreferencesController.PREFERENCES)
                .path(PreferencesController.THEME)
                .path(PreferencesController.PATH_USERNAME).expand(userRestService.getTeacherDto().getUsername())
                .bearerAuth(restService.getAuthToken().getToken())
                .body(themeDto)
                .put()
                .build();
    }

    public ThemeDto putTheme(String username, ThemeDto themeDto) {
        return restService.restBuilder(new RestBuilder<ThemeDto>()).clazz(ThemeDto.class)
                .path(PreferencesController.PREFERENCES)
                .path(PreferencesController.THEME)
                .path(PreferencesController.PATH_USERNAME).expand(username)
                .bearerAuth(restService.getAuthToken().getToken())
                .body(themeDto)
                .put()
                .build();
    }

    public boolean deleteTheme(String username) {
        return restService.restBuilder(new RestBuilder<Boolean>()).clazz(Boolean.class)
                .path(PreferencesController.PREFERENCES)
                .path(PreferencesController.THEME)
                .path(PreferencesController.PATH_USERNAME).expand(username)
                .bearerAuth(restService.getAuthToken().getToken())
                .delete()
                .build();
    }

    public ThemeDto getThemeByUsername(String username) {
        return restService.restBuilder(new RestBuilder<ThemeDto>()).clazz(ThemeDto.class)
                .path(PreferencesController.PREFERENCES)
                .path(PreferencesController.THEME)
                .path(PreferencesController.PATH_USERNAME).expand(username)
                .bearerAuth(restService.getAuthToken().getToken())
                .get()
                .build();
    }


}
