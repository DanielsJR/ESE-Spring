package nx.ESE.services;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;

import nx.ESE.documents.Role;
import nx.ESE.documents.User;
import nx.ESE.dtos.UserDto;
import nx.ESE.dtos.UserMinDto;
import nx.ESE.repositories.UserRepository;

@Controller
public class UserService {

	@Autowired
	private UserRepository userRepository;

	private String uniqueUsername(String username) {
		String newUsername = username;

		while (this.existsUserUsername(newUsername)) {
			int length = 3;
			boolean useLetters = false;
			boolean useNumbers = true;
			String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
			//System.out.println("generatedString::::::::::::::::::::::::::::::::::::" + generatedString);
			newUsername += generatedString;
		}
		//System.out.println("newUsername::::::::::::::::::::::::::::::::::::" + newUsername);
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
		if (user != null && Arrays.asList(roles).containsAll(Arrays.asList(user.getRoles())))
			return false;
		return true;
	}

	public boolean hasUserGreaterPrivilegesById(String id, Role[] roles) {
		User user = this.userRepository.findById(id).get();
		if (user != null && Arrays.asList(roles).containsAll(Arrays.asList(user.getRoles())))
			return false;
		return true;
	}

	public boolean passMatch(String username, String pass) {
		if (pass == null) {
			return false;
		}

		User user = this.userRepository.findByUsername(username);
		return user != null && new BCryptPasswordEncoder().matches(pass, user.getPassword());
	}

	// CRUD***********************************
	public List<UserMinDto> getMinUsers(Role role) {
		return this.userRepository.findUsersAll(role);

	}

	public List<UserDto> getFullUsers(Role role) {
		return this.userRepository.findUsersFullAll(role)
				.stream()
				.parallel()
				.sorted((u1,u2) -> u1.getFirstName().toString().compareTo(u2.getFirstName().toString()))
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
		User user = new User();
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

	public UserDto setRoleUser(String username, Role[] roles) {
		User user = this.userRepository.findByUsername(username);
		user.setRoles(roles);
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
