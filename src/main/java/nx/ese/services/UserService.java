package nx.ese.services;

import java.util.Arrays;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import nx.ese.documents.Role;
import nx.ese.documents.User;
import nx.ese.documents.core.Course;
import nx.ese.dtos.UserDto;
import nx.ese.dtos.UserMinDto;
import nx.ese.repositories.CourseRepository;
import nx.ese.repositories.SubjectRepository;
import nx.ese.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    private String uniqueUsername(String username) {
        String newUsername = username;

        while (this.existsUserUsername(newUsername)) {
            int length = 3;
            boolean useLetters = false;
            boolean useNumbers = true;
            String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
            newUsername += generatedString;
        }
        return newUsername;

    }

    public User setUserFromDto(User user, UserDto userDto) {
        user.setUsername(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setDni(userDto.getDni());
        user.setBirthday(userDto.getBirthday());
        user.setGender(userDto.getGender());
        user.setAvatar(userDto.getAvatar());
        user.setEmail(userDto.getEmail());
        user.setMobile(userDto.getMobile());
        user.setAddress(userDto.getAddress());
        user.setCommune(userDto.getCommune());
        return user;
    }

    // Exceptions*********************

    public boolean isIdNull(UserDto userDto) {
        return userDto.getId() == null;
    }

    public boolean isPassNull(UserDto userDto) {
        return userDto.getPassword() == null;
    }

    public boolean existsUserId(String id) {
        return this.userRepository.existsById(id);
    }

    public boolean existsUserUsername(String username) {
        return this.userRepository.findByUsername(username) != null;
    }

    public boolean usernameRepeated(UserDto userDto) {
        User user = this.userRepository.findByUsername(userDto.getUsername());
        return user != null && !user.getId().equals(userDto.getId());
    }

    public boolean emailRepeated(UserDto userDto) {
        if (userDto.getEmail() == null) {
            return false;
        }
        User user = this.userRepository.findByEmail(userDto.getEmail());
        return user != null && !user.getId().equals(userDto.getId());
    }

    public boolean dniRepeated(UserDto userDto) {
        if (userDto.getDni() == null) {
            return false;
        }
        User user = this.userRepository.findByDni(userDto.getDni());
        return user != null && !user.getId().equals(userDto.getId());
    }

    public boolean mobileRepeated(UserDto userDto) {
        if (userDto.getMobile() == null) {
            return false;
        }
        User user = this.userRepository.findByMobile(userDto.getMobile());
        return user != null && !user.getId().equals(userDto.getId());
    }

    public boolean hasUserGreaterPrivilegesByUsername(String username, Role[] roles) {
        User user = this.userRepository.findByUsername(username);
        return user != null && !Arrays.asList(roles).containsAll(Arrays.asList(user.getRoles()));
    }

    public boolean hasUserGreaterPrivilegesById(String id, Role[] roles) {
        Optional<User> user = this.userRepository.findById(id);
        return user.isPresent() && !Arrays.asList(roles).containsAll(Arrays.asList(user.get().getRoles()));
    }

    public boolean passMatch(String username, String pass) {
        if (pass == null) {
            return false;
        }

        User user = this.userRepository.findByUsername(username);
        return user != null && new BCryptPasswordEncoder().matches(pass, user.getPassword());
    }

    public boolean isChiefTeacher(String username) {
        User user = this.userRepository.findByUsername(username);
        return user != null && courseRepository.findFirstByChiefTeacher(user.getId()) != null;
    }

    public boolean isChiefTeacherSetRoles(UserDto userDto) {
        boolean roleTeacher = Stream.of(userDto.getRoles())
                .anyMatch(r -> r.toString().equals(Role.TEACHER.toString()));

        return (!roleTeacher) && (this.courseRepository.findFirstByChiefTeacher(userDto.getId()) != null);
    }

    public boolean isTeacherInSubject(String username) {
        User user = this.userRepository.findByUsername(username);
        return user != null && this.subjectRepository.findFirstByTeacher(user.getId()) != null;
    }

    public boolean isTeacherInSubjectSetRoles(UserDto userDto) {
        boolean roleTeacher = Stream.of(userDto.getRoles())
                .anyMatch(r -> r.toString().equals(Role.TEACHER.toString()));

        return (!roleTeacher) && (this.subjectRepository.findFirstByTeacher(userDto.getId()) != null);
    }

    public boolean isStudentInACourse(String username, Course course) {
        return course.getStudents()
                .stream()
                .anyMatch(s -> s.getUsername().equals(username));
    }

    public boolean isStudentInCourses(String username) {
        if (!courseRepository.findAll().isEmpty()) {
            return courseRepository.findAll()
                    .stream()
                    .anyMatch(c -> this.isStudentInACourse(username, c));
        } else {
            return false;
        }
    }

    // CRUD***********************************
    public Optional<List<UserMinDto>> getMinUsers(Role role) {
        List<UserMinDto> list = this.userRepository.findUsersAll(role);
        if (list.isEmpty())
            return Optional.empty();

        return Optional.of(list);
    }

    public Optional<List<UserDto>> getFullUsers(Role role) {
        List<UserDto> list = this.userRepository.findUsersFullAll(role)
                .stream()
                .parallel()
                .sorted(Comparator.comparing(UserDto::getFirstName))
                .collect(Collectors.toList());
        if (list.isEmpty())
            return Optional.empty();

        return Optional.of(list);
    }

    public Optional<UserDto> getUserById(String id) {
        Optional<User> user = this.userRepository.findById(id);
        return user.map(UserDto::new);
    }

    public Optional<UserDto> getUserByUsername(String username) {
        User user = this.userRepository.findByUsername(username);
        if (user != null)
            return Optional.of(new UserDto(user));

        return Optional.empty();
    }

    public UserDto createUser(UserDto userDto, Role[] roles) {
        User user = new User(userDto.getUsername(), userDto.getPassword());
        this.setUserFromDto(user, userDto);
        user.setUsername(uniqueUsername(userDto.getUsername()));
        user.setRoles(roles);
        return new UserDto(this.userRepository.insert(user));
    }

    public Optional<UserDto> modifyUser(String username, UserDto userDto) {
        User user = this.userRepository.findByUsername(username);
        if (user != null) {
            User su = this.userRepository.save(this.setUserFromDto(user, userDto));
            return Optional.of(new UserDto(su));
        }
        return Optional.empty();
    }

    public Optional<UserDto> deleteUser(String username) {
        User user = this.userRepository.findByUsername(username);
        if (user != null) {
            this.userRepository.delete(user);
            return Optional.of(new UserDto(user));
        }
        return Optional.empty();
    }

    public Optional<UserDto> setRoleUser(UserDto userDto) {
        User user = this.userRepository.findByUsername(userDto.getUsername());
        if (user != null) {
            user.setRoles(userDto.getRoles());
            user.setAvatar(userDto.getAvatar());
            User su = this.userRepository.save(user);
            return Optional.of(new UserDto(su));
        }
        return Optional.empty();
    }

    public Optional<Boolean> resetPassUser(String username, String resetedPass) {
        User user = this.userRepository.findByUsername(username);
        if (user != null) {
            user.setPassword(resetedPass);
            this.userRepository.save(user);
            return Optional.of(true);
        }
        return Optional.empty();
    }

    public Optional<Boolean> setPassUser(String username, String[] setPass) {
        User user = this.userRepository.findByUsername(username);
        if (user != null) {
            user.setPassword(setPass[1]);
            this.userRepository.save(user);
            return Optional.of(true);
        }
        return Optional.empty();
    }

}
