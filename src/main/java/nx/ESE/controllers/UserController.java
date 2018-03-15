package nx.ESE.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import nx.ESE.documents.ContactInformation;
import nx.ESE.documents.PersonalInformation;
import nx.ESE.documents.Role;
import nx.ESE.documents.User;
import nx.ESE.dtos.UserDto;
import nx.ESE.repositories.ContactInformationRepository;
import nx.ESE.repositories.PersonalInformationRepository;
import nx.ESE.repositories.UserRepository;

@Controller
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PersonalInformationRepository piRepository;

	@Autowired
	private ContactInformationRepository ciRepository;

	public void createUser(UserDto userDto, Role[] roles) {
		User user = new User(userDto.getUsername(), userDto.getPassword(), userDto.getGender());
		user.setRoles(roles);
		this.userRepository.save(user);
	}

	public boolean mobileRepeated(String oldMobile, UserDto userDto) {
		User user = this.userRepository.findByUsername(userDto.getUsername());
		ContactInformation ci = this.ciRepository.findByUser(user.getId());
		return (user != null) && (ci != null) && !ci.getMobile().equals(oldMobile);
	}

	public boolean existsUsername(String username) {
		return this.userRepository.findByUsername(username) != null;
	}

	public boolean emailRepeated(String username, UserDto userDto) {
		User user = this.userRepository.findByUsername(username);
		ContactInformation ci = this.ciRepository.findByUser(user.getId());
		return (userDto.getEmail() != null) && (user != null) && (ci != null) && (!user.getUsername().equals(username));
	}

	public boolean emailRepeated(UserDto userDto) {
		return this.emailRepeated(userDto.getUsername(), userDto);
	}

	public boolean dniRepeated(String username, UserDto userDto) {
		User user = this.userRepository.findByUsername(username);
		PersonalInformation pi = this.piRepository.findByUser(user.getId());
		return (userDto.getDni() != null) && (user != null) && (pi != null) && (!user.getUsername().equals(username));
	}

	public boolean dniRepeated(UserDto userDto) {
		return this.dniRepeated(userDto.getUsername(), userDto);
	}


}
