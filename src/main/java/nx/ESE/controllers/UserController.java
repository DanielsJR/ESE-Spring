package nx.ESE.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import nx.ESE.documents.Role;
import nx.ESE.documents.User;
import nx.ESE.dtos.UserDto;
import nx.ESE.dtos.UserMinDto;
import nx.ESE.repositories.UserRepository;


@Controller
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	private void setUserFromDto(User user, UserDto userDto){
		user.setUsername(userDto.getUsername());
		user.setPassword(userDto.getPassword());
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setDni(userDto.getDni());
		user.setAge(userDto.getAge());
		user.setGender(userDto.getGender());
		user.setEmail(userDto.getEmail());
		user.setMobile(userDto.getMobile());
		user.setAddress(userDto.getAddress());
		user.setCommune(userDto.getCommune());
	}
	
	public boolean usernameRepeated(String oldUsername, UserDto userDto) {
		User user = this.userRepository.findByUsername(userDto.getUsername());
		return user != null && !user.getUsername().equals(oldUsername);
	}

	public boolean existsUsername(String username) {
		return this.userRepository.findByUsername(username) != null;
	}

	public boolean emailRepeated(String username, UserDto userDto) {
		if(userDto.getEmail() == null){
			return false;
		}
		User user = this.userRepository.findByEmail(userDto.getEmail());
		return user != null && !user.getUsername().equals(username);
	}

	public boolean emailRepeated(UserDto userDto) {
		return this.emailRepeated(userDto.getUsername(), userDto);
	}

	public boolean dniRepeated(String username, UserDto userDto) {
		if(userDto.getDni() == null){
			return false;
		}
		User user = this.userRepository.findByDni(userDto.getDni());
		return user != null && !user.getUsername().equals(username);
	}

	public boolean dniRepeated(UserDto userDto) {
		return this.dniRepeated(userDto.getUsername(), userDto);
	}
	
	public boolean mobileRepeated(String username, UserDto userDto) {
		if(userDto.getMobile() == null){
			return false;
		}
		User user = this.userRepository.findByMobile(userDto.getMobile());
		return user != null && !user.getUsername().equals(username);
	}
	
	public boolean mobileRepeated(UserDto userDto) {
		return this.mobileRepeated(userDto.getUsername(), userDto);
	}


	
	public Optional<UserDto> getUser(String username, Role[] roles) {
        User user = this.userRepository.findByUsername(username);
        if (user == null) {
            return Optional.empty();
        } else if (Arrays.asList(roles).containsAll(Arrays.asList(user.getRoles()))) {
            return Optional.of(new UserDto(user));
        } else {
            return Optional.empty();
        }
	}
	
	public List<UserDto> getUsers() {
		return this.userRepository.findStudentFullAll();

	}
	
	public Optional<UserDto> createUser(UserDto userDto, Role[] roles) {
		User user = new User(userDto.getUsername(), userDto.getPassword());
		this.setUserFromDto(user, userDto);
		user.setRoles(roles);
		this.userRepository.save(user);
		return Optional.of(new UserDto(user));
	}


	public Optional<UserDto> modifyUser(String username, UserDto userDto, Role[] roles){
		User user = this.userRepository.findByUsername(username);
		assert user != null;
		if (Arrays.asList(roles).containsAll(Arrays.asList(user.getRoles()))) {
			this.setUserFromDto(user, userDto);
			this.userRepository.save(user);
			
			return Optional.of(new UserDto(user));
		}else {
			return Optional.empty();
		}
	
	}
	
    public boolean deleteUser(String username, Role[] roles) {
        User user = this.userRepository.findByUsername(username);
        if (user == null) {
            return true;
        } else if (Arrays.asList(roles).containsAll(Arrays.asList(user.getRoles()))) {
            this.userRepository.delete(user);
            return true;
        } else {
            return false;
        }
    }





}
