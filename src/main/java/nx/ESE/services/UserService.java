package nx.ESE.services;

import java.util.Arrays;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import nx.ESE.documents.Role;
import nx.ESE.documents.User;
import nx.ESE.documents.core.Course;
import nx.ESE.dtos.UserDto;
import nx.ESE.dtos.UserMinDto;
import nx.ESE.repositories.CourseRepository;
import nx.ESE.repositories.SubjectRepository;
import nx.ESE.repositories.UserRepository;

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
			// System.out.println("generatedString::::::::::::::::::::::::::::::::::::"
			// + generatedString);
			newUsername += generatedString;
		}
		// System.out.println("newUsername::::::::::::::::::::::::::::::::::::"
		// + newUsername);
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
		// System.out.println("userToString:::::: " + user.toString());
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
		User user = this.userRepository.findById(id).get();
		return user != null && !Arrays.asList(roles).containsAll(Arrays.asList(user.getRoles()));
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
    	boolean roleTeacher = Stream.of(userDto.getRoles()).filter(r -> r.toString() == "TEACHER").findFirst()
				.isPresent();

		return (!roleTeacher) && (this.courseRepository.findFirstByChiefTeacher(userDto.getId()) != null);

	}

	public boolean isTeacherInSubject(String username) {
		User user = this.userRepository.findByUsername(username);
		return user != null && this.subjectRepository.findFirstByTeacher(user.getId()) != null;
	}

	public boolean isTeacherInSubjectSetRoles(UserDto userDto) {
		boolean roleTeacher = Stream.of(userDto.getRoles()).filter(r -> r.toString() == "TEACHER").findFirst()
				.isPresent();

		return (!roleTeacher) && (this.subjectRepository.findFirstByTeacher(userDto.getId()) != null);

	}

	public boolean isStudentInACourse(String username) {
		Boolean[] found = { false };

		List<Course> courses;
		if (!courseRepository.findAll().isEmpty()) {
			courses = courseRepository.findAll();

			courses.stream().map(c -> c.getStudents()).forEach(sts -> sts.forEach(s -> {
				if (s.getUsername().equals(username)) {
					found[0] = true;
				}
			}));
		}

		System.out.println("----------------------------------------isStudentInACourse " + found[0]);
		return found[0];
	}

	// CRUD***********************************
	public List<UserMinDto> getMinUsers(Role role) {
		return this.userRepository.findUsersAll(role);

	}

	public List<UserDto> getFullUsers(Role role) {
		return this.userRepository.findUsersFullAll(role)
				.stream()
				.parallel()
				.sorted((u1, u2) -> u1.getFirstName().toString().compareTo(u2.getFirstName().toString()))
				.collect(Collectors.toList());
	}

	public UserDto getUserById(String id) {
		User user = this.userRepository.findById(id).get();
		return new UserDto(user);

	}

	public UserDto getUserByUsername(String username) {
		User user = this.userRepository.findByUsername(username);
		return new UserDto(user);

	}

	public UserDto createUser(UserDto userDto, Role[] roles) {
		User user = new User(userDto.getUsername(), userDto.getPassword());
		this.setUserFromDto(user, userDto);
		user.setUsername(uniqueUsername(userDto.getUsername()));
		user.setRoles(roles);
		this.userRepository.save(user);

		return new UserDto(user);
	}

	public UserDto modifyUser(String username, UserDto userDto) {
		User user = this.userRepository.findByUsername(username);
		assert user != null;
		this.userRepository.save(this.setUserFromDto(user, userDto));

		return new UserDto(user);
	}

	public boolean deleteUser(String username) {
		User user = this.userRepository.findByUsername(username);
		this.userRepository.delete(user);
		return true;
	}

	public UserDto setRoleUser(UserDto userDto) {

		User user = this.userRepository.findByUsername(userDto.getUsername());
		user.setRoles(userDto.getRoles());
		user.setAvatar(userDto.getAvatar());
		this.userRepository.save(user);

		return new UserDto(user);
	}

	public boolean resetPassUser(String username, String resetedPass) {
		User user = this.userRepository.findByUsername(username);
		user.setPassword(resetedPass);
		this.userRepository.save(user);
		return true;
	}

	public boolean setPassUser(String username, String[] setPass) {
		User user = this.userRepository.findByUsername(username);
		user.setPassword(setPass[1]);
		this.userRepository.save(user);
		return true;
	}

}
