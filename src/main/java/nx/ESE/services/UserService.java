package nx.ESE.services;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.validation.Valid;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;

import nx.ESE.documents.Avatar;
import nx.ESE.documents.Gender;
import nx.ESE.documents.Role;
import nx.ESE.documents.User;
import nx.ESE.dtos.UserDto;
import nx.ESE.dtos.UserMinDto;
import nx.ESE.repositories.UserRepository;
import nx.ESE.utils.UtilBase64Image;

@Controller
public class UserService {

	@Autowired
	private UserRepository userRepository;

	// input
	private void saveUserAvatarInServer(Avatar avatar) {
		String path = Avatar.SERVER_AVATAR_PATH + avatar.getName();
		UtilBase64Image.decoder(avatar.getData(), path);
		avatar.setData(null);// not save in BD
	}

	private String uniqueUsername(String username) {
		String newUsername = username;

		while (this.existsUserUsername(newUsername)) {
			int length = 3;
			boolean useLetters = false;
			boolean useNumbers = true;
			String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
			System.out.println(generatedString);
			newUsername += generatedString;
		}
		System.out.println("NEW_USERNAME::::::::::::::::::::::::::::::::::::" + newUsername);
		return newUsername;

	}

	private void setUserFromDto(User user, UserDto userDto) {
		user.setUsername(userDto.getUsername());
		// user.setPassword(userDto.getPassword());
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
		// user.setRoles(userDto.getRoles());
		// user.setToken(userDto.getToken());
		// System.out.println("userToString:::::: " + user.toString());
	}

	// output
	private void setOutPutUserAvatar(User user) {
		String imgPrefix = "default-";
		String imgType = "image/png";
		if (user.getAvatar() != null) {
			String path = Avatar.SERVER_AVATAR_PATH + user.getAvatar().getName();
			String avatarBase64 = UtilBase64Image.encoder(path);
			user.getAvatar().setData(avatarBase64);
		} else if (user.getGender() != null && user.getGender().equals(Gender.MUJER)) {
			String femaleAvatarName = imgPrefix + Gender.MUJER + "-" + user.getRoles()[0].toString() + ".png";
			String pathDefaultFemale = Avatar.SERVER_AVATAR_PATH + femaleAvatarName.toLowerCase();
			String avatarBase64DefaultFemale = UtilBase64Image.encoder(pathDefaultFemale);
			user.setAvatar(new Avatar(femaleAvatarName.toLowerCase(), imgType, avatarBase64DefaultFemale));
		} else {
			String maleAvatarName = imgPrefix + Gender.HOMBRE + "-" + user.getRoles()[0].toString() + ".png";
			String pathDefaultMale = Avatar.SERVER_AVATAR_PATH + maleAvatarName.toLowerCase();
			String avatarBase64DefaultMale = UtilBase64Image.encoder(pathDefaultMale);
			user.setAvatar(new Avatar(maleAvatarName.toLowerCase(), imgType, avatarBase64DefaultMale));
		}
	}

	private List<UserDto> setOutPutUsersAvatars(List<UserDto> usersDto) {
		String imgPrefix = "default-";
		String imgType = "image/png";
		List<UserDto> users = new CopyOnWriteArrayList<UserDto>(usersDto);
		Iterator<UserDto> it = users.iterator();
		while (it.hasNext()) {
			UserDto user = it.next();
			if (user.getAvatar() != null) {
				String path = Avatar.SERVER_AVATAR_PATH + user.getAvatar().getName();
				String avatarBase64 = UtilBase64Image.encoder(path);
				user.getAvatar().setData(avatarBase64);
			} else if (user.getGender() != null && user.getGender().equals(Gender.MUJER)) {
				String femaleAvatarName = imgPrefix + Gender.MUJER + "-" + user.getRoles()[0].toString() + ".png";
				String pathDefaultFemale = Avatar.SERVER_AVATAR_PATH + femaleAvatarName.toLowerCase();
				String avatarBase64DefaultFemale = UtilBase64Image.encoder(pathDefaultFemale);
				user.setAvatar(new Avatar(femaleAvatarName.toLowerCase(), imgType, avatarBase64DefaultFemale));
			} else {
				String maleAvatarName = imgPrefix + Gender.HOMBRE + "-" + user.getRoles()[0].toString() + ".png";
				String pathDefaultMale = Avatar.SERVER_AVATAR_PATH + maleAvatarName.toLowerCase();
				String avatarBase64DefaultMale = UtilBase64Image.encoder(pathDefaultMale);
				user.setAvatar(new Avatar(maleAvatarName.toLowerCase(), imgType, avatarBase64DefaultMale));
			}
		}
		return users;
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
		return this.setOutPutUsersAvatars(this.userRepository.findUsersFullAll(role));
		// return this.userRepository.findUsersFullAll(role);
	}

	public UserDto getUserById(String id) {
		User user = this.userRepository.findById(id).get();
		this.setOutPutUserAvatar(user);
		return new UserDto(user);

	}

	public UserDto getUserByUsername(String username) {
		User user = this.userRepository.findByUsername(username);
		this.setOutPutUserAvatar(user);
		return new UserDto(user);

	}

	public UserDto createUser(UserDto userDto, Role[] roles) {
		User user = new User(userDto.getUsername(), userDto.getPassword());
		this.setUserFromDto(user, userDto);
		user.setUsername(uniqueUsername(userDto.getUsername()));
		user.setRoles(roles);
		if (user.getAvatar() != null)
			this.saveUserAvatarInServer(user.getAvatar());
		this.userRepository.save(user);

		this.setOutPutUserAvatar(user);
		return new UserDto(user);
	}

	public UserDto modifyUser(String username, UserDto userDto) {
		User user = this.userRepository.findByUsername(username);
		assert user != null;
		this.setUserFromDto(user, userDto);
		if (user.getAvatar() != null)
			this.saveUserAvatarInServer(user.getAvatar());
		this.userRepository.save(user);

		this.setOutPutUserAvatar(user);
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
		if (user.getAvatar() != null && user.getAvatar().getName().startsWith("default-"))
			user.setAvatar(null);
		this.setOutPutUserAvatar(user);
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
