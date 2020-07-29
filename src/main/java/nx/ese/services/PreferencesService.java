package nx.ese.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nx.ese.documents.Gender;
import nx.ese.documents.Preferences;
import nx.ese.documents.Theme;
import nx.ese.documents.User;
import nx.ese.dtos.ThemeDto;
import nx.ese.repositories.PreferencesRepository;
import nx.ese.repositories.UserRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PreferencesService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PreferencesRepository preferencesRepository;

    public ThemeDto getUserTheme(String id) {
        ThemeDto themeDto = new ThemeDto();
        User user = userRepository.findById(id).orElseThrow(NoSuchElementException::new);
        if (preferencesRepository.findByUserId(id) != null
                && preferencesRepository.findByUserId(id).getTheme() != null) {
            Theme themeDb = preferencesRepository.findByUserId(id).getTheme();
            themeDto.setName(themeDb.getName());
            themeDto.setIsDark(themeDb.getIsDark());
            themeDto.setColor(themeDb.getColor());
            return themeDto;
        } else {
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
        Theme nTheme = new Theme(theme.getName(), theme.getIsDark(), theme.getColor());

        Optional<Preferences> preferences = preferencesRepository.findById(id);
        if (preferences.isPresent()) {
            preferences.ifPresent(p -> {
                p.setTheme(nTheme);
                preferencesRepository.save(p);
            });
            return true;

        } else {
            Optional<User> user = this.userRepository.findById(id);
            if (user.isPresent()) {
                Preferences prefUser = new Preferences(user.get(), nTheme);
                preferencesRepository.save(prefUser);
                return true;
            }
            return false;
        }
    }

}
