package nx.ESE.controllers;


import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
public class UserController {

	@Autowired
	private UserRepository userRepository;

	// input
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
		//user.setRoles(userDto.getRoles());
		//user.setToken(userDto.getToken());
		// System.out.println("userToString:::::: " + user.toString());
	}

	private void setOutPutUserAvatar(User user) {
		if (user.getAvatar() != null) {
			String path = Avatar.SERVER_AVATAR_PATH + user.getAvatar().getName();
			String avatarBase64 = UtilBase64Image.encoder(path);
			user.getAvatar().setData(avatarBase64);
		} else if (user.getGender() != null && user.getGender().equals(Gender.MUJER)) {
			String femaleAvatarName = Gender.MUJER + "-" + user.getRoles()[0].toString() + ".svg";
			String femaleAvatarType = "image/svg+xml";
			String pathDefaultFemale = Avatar.SERVER_AVATAR_PATH + femaleAvatarName;
			String avatarBase64DefaultFemale = UtilBase64Image.encoder(pathDefaultFemale);
			user.setAvatar(new Avatar(femaleAvatarName, femaleAvatarType, avatarBase64DefaultFemale));
		} else {
			String maleAvatarName = Gender.HOMBRE + "-" + user.getRoles()[0].toString() + ".svg";
			String pathDefaultMale = Avatar.SERVER_AVATAR_PATH + maleAvatarName;
			String maleAvatarType = "image/svg+xml";
			String avatarBase64DefaultMale = UtilBase64Image.encoder(pathDefaultMale);
			user.setAvatar(new Avatar(maleAvatarName, maleAvatarType, avatarBase64DefaultMale));
		}
	}

	private List<UserDto> setOutPutUsersAvatars(List<UserDto> usersDto) {
		List<UserDto> users = new CopyOnWriteArrayList<UserDto>(usersDto);
		Iterator<UserDto> it = users.iterator();
		while (it.hasNext()) {
			UserDto user = it.next();
			if (user.getAvatar() != null) {
				String path = Avatar.SERVER_AVATAR_PATH + user.getAvatar().getName();
				String avatarBase64 = UtilBase64Image.encoder(path);
				user.getAvatar().setData(avatarBase64);
			} else if (user.getGender() != null && user.getGender().equals(Gender.MUJER)) {
				String femaleAvatarName = Gender.MUJER + "-" + user.getRoles()[0].toString() + ".svg";
				String femaleAvatarType = "image/svg+xml";
				String pathDefaultFemale = Avatar.SERVER_AVATAR_PATH + femaleAvatarName;
				String avatarBase64DefaultFemale = UtilBase64Image.encoder(pathDefaultFemale);
				user.setAvatar(new Avatar(femaleAvatarName, femaleAvatarType, avatarBase64DefaultFemale));
			} else {
				String maleAvatarName = Gender.HOMBRE + "-" + user.getRoles()[0].toString() + ".svg";
				String pathDefaultMale = Avatar.SERVER_AVATAR_PATH + maleAvatarName;
				String maleAvatarType = "image/svg+xml";
				String avatarBase64DefaultMale = UtilBase64Image.encoder(pathDefaultMale);
				user.setAvatar(new Avatar(maleAvatarName, maleAvatarType, avatarBase64DefaultMale));
			}
		}
		return users;
	}

	public boolean existsUserId(String id) {
		return this.userRepository.findByIdQuery(id) != null;
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

	private void saveUserAvatarInServer(Avatar avatar) {
		String path = Avatar.SERVER_AVATAR_PATH + avatar.getName();
		UtilBase64Image.decoder(avatar.getData(), path);
		avatar.setData(null);// not save in BD
	}

	public Optional<UserDto> createUser(UserDto userDto, Role[] roles) {
		User user = new User(userDto.getUsername(), userDto.getPassword());
		this.setUserFromDto(user, userDto);
		user.setRoles(roles);
		if (user.getAvatar() != null) { this.saveUserAvatarInServer(user.getAvatar()); }
		this.userRepository.save(user);
		
		 this.setOutPutUserAvatar(user);
		return Optional.of(new UserDto(user));
	}

	public Optional<UserDto> modifyUser(String id, UserDto userDto, Role[] roles) {
		User user = this.userRepository.findByIdQuery(id);
		assert user != null;
		if (Arrays.asList(user.getRoles()).containsAll(Arrays.asList(roles))) {
			this.setUserFromDto(user, userDto);
			if (user.getAvatar() != null) { this.saveUserAvatarInServer(user.getAvatar()); }
			this.userRepository.save(user);
			
			this.setOutPutUserAvatar(user);
			return Optional.of(new UserDto(user));
		} else {
			return Optional.empty();
		}

	}

	public boolean deleteUser(String id, Role[] roles) {
		User user = this.userRepository.findByIdQuery(id);
		if (user == null) {
			return true;
		} else if (Arrays.asList(user.getRoles()).containsAll(Arrays.asList(roles))) {
			this.userRepository.delete(user);
			return true;
		} else {
			return false;
		}
	}

	public List<UserMinDto> getMinUsers(Role role) {
		return this.userRepository.findUsersAll(role);

	}

	public List<UserDto> getFullUsers(Role role) {
		return this.setOutPutUsersAvatars(this.userRepository.findUsersFullAll(role));
		//return this.userRepository.findUsersFullAll(role);
	}

	public Optional<UserDto> getUserById(String id, Role[] roles) {
		User user = this.userRepository.findByIdQuery(id);
		if (user == null) {
			return Optional.empty();
		} else if (Arrays.asList(user.getRoles()).containsAll(Arrays.asList(roles))) {
			this.setOutPutUserAvatar(user);
			return Optional.of(new UserDto(user));
		} else {
			return Optional.empty();
		}
	}

	public Optional<UserDto> getUserByUsername(String username, Role[] roles) {
		User user = this.userRepository.findByUsername(username);
		if (user == null) {
			return Optional.empty();
		} else if (Arrays.asList(user.getRoles()).containsAll(Arrays.asList(roles))) {
			this.setOutPutUserAvatar(user);
			return Optional.of(new UserDto(user));
		} else {
			return Optional.empty();
		}
	}

	public Optional<UserDto> getUserByToken(String token, Role[] roles) {
		User user = this.userRepository.findByTokenValue(token);
		if (user == null) {
			return Optional.empty();
		} else if (Arrays.asList(user.getRoles()).containsAll(Arrays.asList(roles))) {
			this.setOutPutUserAvatar(user);
			return Optional.of(new UserDto(user));
		} else {
			return Optional.empty();
		}
	}

	public Optional<UserDto> setRoleUser(String id,Role[] roles, Role[] roles2) {
		User user = this.userRepository.findByIdQuery(id);
		 if (Arrays.asList(user.getRoles()).containsAll(Arrays.asList(roles2))) {
			user.setRoles(roles);
			this.userRepository.save(user);
			this.setOutPutUserAvatar(user);
			return Optional.of(new UserDto(user));
		} else {
			return Optional.empty();
		}
	}
	
	public boolean resetPassUser(String id, String resetedPass, Role[] roles) {
		User user = this.userRepository.findByIdQuery(id);
		 if (Arrays.asList(user.getRoles()).containsAll(Arrays.asList(roles))) {
			user.setPassword(resetedPass);
			this.userRepository.save(user);
			return true;
		} else {
			return false;
		}
	}




}
