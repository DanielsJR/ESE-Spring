package nx.ese.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import nx.ese.dtos.ThemeDto;
import nx.ese.services.PreferencesService;
import nx.ese.services.UserService;

import javax.validation.Valid;


@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('TEACHER') or hasRole('STUDENT')")
@RestController
@RequestMapping(PreferencesController.PREFERENCES)
public class PreferencesController {

    @Autowired
    private PreferencesService preferencesService;

    @Autowired
    private UserService userService;

    public static final String PREFERENCES = "/preferences";
    public static final String THEME = "/theme";
    public static final String USER = "/user";

    public static final String PATH_ID = "/{id}";
    public static final String PATH_USERNAME = "/{username}";


    @PreAuthorize("#username == authentication.principal.username")
    @PutMapping(THEME + PATH_USERNAME)
    public ThemeDto saveUserTheme(@PathVariable String username, @Valid @RequestBody ThemeDto theme) {
        return this.preferencesService.saveUserTheme(username, theme);
    }

    @PreAuthorize("#username == authentication.principal.username")
    @DeleteMapping(THEME + PATH_USERNAME)
    public boolean deleteUserTheme(@PathVariable String username) {
        return this.preferencesService.deleteUserTheme(username);
    }

    @PreAuthorize("#username == authentication.principal.username")
    @GetMapping(THEME + PATH_USERNAME)
    public ThemeDto getThemeByUsername(@PathVariable String username) {
        return this.preferencesService.getUserTheme(username);
    }

}
