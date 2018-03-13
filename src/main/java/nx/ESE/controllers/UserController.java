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

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public boolean putUser(String mobile, UserDto userDto, Role[] roles) {
	 * User user = this.userRepository.findByMobile(mobile); assert user !=
	 * null; if
	 * (Arrays.asList(roles).containsAll(Arrays.asList(user.getRoles()))) {
	 * user.setMobile(userDto.getMobile());
	 * user.setUsername(userDto.getUsername());
	 * user.setEmail(userDto.getEmail()); user.setDni(userDto.getDni());
	 * user.setAddress(userDto.getAddress());
	 * user.setActive(userDto.isActive()); this.userRepository.save(user); }
	 * else { return false; } return true; }
	 * 
	 * public boolean deleteUser(String mobile, Role[] roles) { User userBd =
	 * this.userRepository.findByMobile(mobile); if (userBd == null) { return
	 * true; } else if
	 * (Arrays.asList(roles).containsAll(Arrays.asList(userBd.getRoles()))) {
	 * this.userRepository.delete(userBd); return true; } else { return false; }
	 * }
	 * 
	 * public Optional<UserDto> readUser(String mobile, Role[] roles) { User
	 * userBd = this.userRepository.findByMobile(mobile); if (userBd == null) {
	 * return Optional.empty(); } else if
	 * (Arrays.asList(roles).containsAll(Arrays.asList(userBd.getRoles()))) {
	 * return Optional.of(new UserDto(userBd)); } else { return
	 * Optional.empty(); } }
	 * 
	 * public List<UserDto> readCustomerAll() { return
	 * this.userRepository.findStudentAll(); }
	 */
}
