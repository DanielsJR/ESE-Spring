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

import java.util.Optional;

@Service
public class PreferencesService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PreferencesRepository preferencesRepository;

    public ThemeDto getUserTheme(String username) {
        User user = userRepository.findByUsernameOptional(username).orElseThrow();

        Optional<Preferences> preferences = preferencesRepository.findByUser(user.getId());

        if (preferences.isPresent() && preferences.get().getTheme() != null)
            return new ThemeDto(preferences.get().getTheme());

        return this.defaultTheme(user.getId());
    }

    private ThemeDto defaultTheme(String userId) {
        ThemeDto themeDto = new ThemeDto();
        Optional<User> user = userRepository.findById(userId);
        themeDto.setDark(false);
        if (user.isPresent() && (user.get().getGender() != (null) && user.get().getGender().equals(Gender.MUJER))) {
            themeDto.setName("purple-pink");
            themeDto.setPrimaryColor("#9c27b0");
            themeDto.setAccentColor("#ff4081");
        } else {
            themeDto.setName("indigo-pink");
            themeDto.setPrimaryColor("#3F51B5");
            themeDto.setAccentColor("#ff4081");
        }

        return themeDto;
    }

    public ThemeDto saveUserTheme(String username, ThemeDto theme) {
        Theme nTheme = new Theme(theme.getName(), theme.getDark(), theme.getPrimaryColor(), theme.getAccentColor());
        User user = this.userRepository.findByUsernameOptional(username).orElseThrow();

        Optional<Preferences> preferences = preferencesRepository.findByUser(user.getId());
        if (preferences.isPresent()) {
            preferences.ifPresent(p -> {
                p.setTheme(nTheme);
                preferencesRepository.save(p);
            });
            return preferences
                    .map(Preferences::getTheme)
                    .map(ThemeDto::new).orElseThrow();

        } else {
            Preferences prefUser = new Preferences(user, nTheme);
            preferencesRepository.insert(prefUser);
            return new ThemeDto(prefUser.getTheme());
        }

    }

    public boolean deleteUserTheme(String username) {
        User user = this.userRepository.findByUsernameOptional(username).orElseThrow();
        preferencesRepository.findByUser(user.getId())
                .ifPresent(p -> {
                    p.setTheme(null);
                    preferencesRepository.save(p);
                });
        return true;
    }


}
