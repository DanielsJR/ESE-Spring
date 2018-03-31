package nx.ESE.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
		//user.setPassword(userDto.getPassword());
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setDni(userDto.getDni());
		user.setBirthday(userDto.getBirthday());
		user.setGender(userDto.getGender());
		user.setEmail(userDto.getEmail());
		user.setMobile(userDto.getMobile());
		user.setAddress(userDto.getAddress());
		user.setCommune(userDto.getCommune());
		user.setToken(userDto.getToken());
		//System.out.println("userToString:::::: " + user.toString());
	}
	

	public boolean existsUserId(String id) {
		return this.userRepository.findByIdQuery(id) != null;
	}
	
	public boolean usernameRepeated(UserDto userDto) {
		User user = this.userRepository.findByUsername(userDto.getUsername());
		return user != null && !user.getId().equals(userDto.getId());
	}


	public boolean emailRepeated(UserDto userDto) {
		if(userDto.getEmail() == null){
			return false;
		}
		User user = this.userRepository.findByEmail(userDto.getEmail());
		return user != null && !user.getId().equals(userDto.getId());
	}


	public boolean dniRepeated(UserDto userDto) {
		if(userDto.getDni() == null){
			return false;
		}
		User user = this.userRepository.findByDni(userDto.getDni());
		return user != null && !user.getId().equals(userDto.getId());
	}

	
	public boolean mobileRepeated(UserDto userDto) {
		if(userDto.getMobile() == null){
			return false;
		}
		User user = this.userRepository.findByMobile(userDto.getMobile());
		return user != null && !user.getId().equals(userDto.getId());
	}
	
	
	
	public Optional<UserDto> createUser(UserDto userDto, Role[] roles) {
		User user = new User(userDto.getUsername(), userDto.getPassword());
		this.setUserFromDto(user, userDto);
		user.setRoles(roles);
		this.userRepository.save(user);
		return Optional.of(new UserDto(user));
	}


	public Optional<UserDto> modifyUser(String id, UserDto userDto, Role[] roles){
		User user = this.userRepository.findByIdQuery(id);
		assert user != null;
		if (Arrays.asList(roles).containsAll(Arrays.asList(user.getRoles()))) {
			this.setUserFromDto(user, userDto);
			this.userRepository.save(user);
			
			return Optional.of(new UserDto(user));
		}else {
			return Optional.empty();
		}
	
	}
	
    public boolean deleteUser(String id, Role[] roles) {
        User user = this.userRepository.findByIdQuery(id);
        if (user == null) {
            return true;
        } else if (Arrays.asList(roles).containsAll(Arrays.asList(user.getRoles()))) {
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
		return this.userRepository.findUsersFullAll(role);

	}
	
	public Optional<UserDto> getUserById(String id, Role[] roles) {
        User user = this.userRepository.findByIdQuery(id);
        if (user == null) {
            return Optional.empty();
        } else if (Arrays.asList(roles).containsAll(Arrays.asList(user.getRoles()))) {
            return Optional.of(new UserDto(user));
        } else {
            return Optional.empty();
        }
	}
	

	public Optional<UserDto> getUserByUsername(String username, Role[] roles) {
        User user = this.userRepository.findByUsername(username);
        if (user == null) {
            return Optional.empty();
        } else if (Arrays.asList(roles).containsAll(Arrays.asList(user.getRoles()))) {
            return Optional.of(new UserDto(user));
        } else {
            return Optional.empty();
        }
	}
	
	public Optional<UserDto> getUserByToken(String token, Role[] roles) {
        User user = this.userRepository.findByTokenValue(token);
        if (user == null) {
            return Optional.empty();
        } else if (Arrays.asList(roles).containsAll(Arrays.asList(user.getRoles()))) {
            return Optional.of(new UserDto(user));
        } else {
            return Optional.empty();
        }
	}
	




}
