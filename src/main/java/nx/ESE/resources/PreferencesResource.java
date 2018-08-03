package nx.ESE.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nx.ESE.controllers.PreferencesController;
import nx.ESE.controllers.UserController;
import nx.ESE.dtos.ThemeDto;
import nx.ESE.resources.exceptions.ForbiddenException;
import nx.ESE.resources.exceptions.UserIdNotFoundException;

@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('TEACHER') or hasRole('STUDENT')")
@RestController
@RequestMapping(PreferencesResource.PREFERENCES)
public class PreferencesResource {

	@Autowired
	private PreferencesController preferencesController;

	@Autowired
	private UserController userController;

	public static final String PREFERENCES = "/preferences";
	public static final String THEME = "/theme";

	public static final String PATH_ID = "/{id}";

	@GetMapping(THEME + PATH_ID)
	public ThemeDto getThemeByUsername(@PathVariable String id) throws ForbiddenException, UserIdNotFoundException {

		if (!this.userController.existsUserId(id))
			throw new UserIdNotFoundException();

		return this.preferencesController.getUserTheme(id);

	}

	@PutMapping(THEME + PATH_ID)
	public boolean saveUserTheme(@PathVariable String id, @RequestBody ThemeDto theme)
			throws ForbiddenException, UserIdNotFoundException {

		if (!this.userController.existsUserId(id))
			throw new UserIdNotFoundException();

		return this.preferencesController.saveUserTheme(id, theme);

	}

}
