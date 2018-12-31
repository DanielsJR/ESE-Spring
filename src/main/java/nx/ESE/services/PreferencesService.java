package nx.ESE.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import nx.ESE.documents.Gender;
import nx.ESE.documents.Preferences;
import nx.ESE.documents.Theme;
import nx.ESE.documents.User;
import nx.ESE.dtos.ThemeDto;
import nx.ESE.repositories.PreferencesRepository;
import nx.ESE.repositories.UserRepository;

@Controller
public class PreferencesService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PreferencesRepository preferencesRepository;

	public ThemeDto getUserTheme(String id) {
		ThemeDto themeDto = new ThemeDto();
		User user = userRepository.findById(id).get();
		if (preferencesRepository.findByUserId(id) != null && preferencesRepository.findByUserId(id).getTheme() != null) {
			Theme themeDb = preferencesRepository.findByUserId(id).getTheme();
			themeDto.setName(themeDb.getName());
			themeDto.setIsDark(themeDb.getIsDark());
			themeDto.setColor(themeDb.getColor());
			return themeDto;
		} else {
			// System.out.println("setting default theme!:::::: ");
			themeDto.setIsDark(false);
			if (user.getGender().equals(Gender.MUJER)) {
				themeDto.setName("pink-purple");
				themeDto.setColor("#E91E63");
			} else {
				themeDto.setName("indigo-pink");
				themeDto.setColor("#3F51B5");
			}
			return themeDto;
		}
	}

	public boolean saveUserTheme(String id, ThemeDto theme) {
		Theme nTheme = new Theme();
		nTheme.setName(theme.getName());
		nTheme.setIsDark(theme.getIsDark());
		nTheme.setColor(theme.getColor());

		Preferences preferences = preferencesRepository.findByUserId(id);
		if (preferences != null) {
			preferences.setTheme(nTheme);
			preferencesRepository.save(preferences);
			return true;
		}

		User user = this.userRepository.findById(id).get();
		if (user != null) {
			preferences = new Preferences(user, nTheme);
			preferencesRepository.save(preferences);
			return true;
		}
		return false;

	}

}
