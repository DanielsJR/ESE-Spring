package nx.ese.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nx.ese.dtos.ThemeDto;
import nx.ese.exceptions.FieldNotFoundException;
import nx.ese.exceptions.ForbiddenException;
import nx.ese.services.PreferencesService;
import nx.ese.services.UserService;


@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('TEACHER') or hasRole('STUDENT')")
@RestController
@RequestMapping(PreferencesController.PREFERENCES)
public class PreferencesController {

	@Autowired
	private PreferencesService preferencesController;

	@Autowired
	private UserService userService;

	public static final String PREFERENCES = "/preferences";
	public static final String THEME = "/theme";

	public static final String PATH_ID = "/{id}";

	@GetMapping(THEME + PATH_ID)
	public ThemeDto getThemeByUsername(@PathVariable String id) throws ForbiddenException, FieldNotFoundException {

		if (!this.userService.existsUserId(id))
			throw new FieldNotFoundException("Id.");

		return this.preferencesController.getUserTheme(id);

	}

	@PutMapping(THEME + PATH_ID)
	public boolean saveUserTheme(@PathVariable String id, @RequestBody ThemeDto theme)
			throws ForbiddenException, FieldNotFoundException {

		if (!this.userService.existsUserId(id))
			throw new FieldNotFoundException("Id.");

		return this.preferencesController.saveUserTheme(id, theme);

	}

}
