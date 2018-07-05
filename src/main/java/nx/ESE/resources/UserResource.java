package nx.ESE.resources;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nx.ESE.controllers.UserController;
import nx.ESE.documents.Role;
import nx.ESE.dtos.UserDto;
import nx.ESE.dtos.UserMinDto;
import nx.ESE.resources.exceptions.ForbiddenException;
import nx.ESE.resources.exceptions.PasswordNotMatchException;
import nx.ESE.resources.exceptions.UserFieldAlreadyExistException;
import nx.ESE.resources.exceptions.UserIdNotFoundException;
import nx.ESE.resources.exceptions.UserUsernameNotFoundException;

@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('TEACHER') or hasRole('STUDENT')")
@RestController
@RequestMapping(UserResource.USERS)
public class UserResource {

	public static final String USERS = "/users";
	public static final String ADMINS = "/admins";
	public static final String MANAGERS = "/managers";
	public static final String TEACHERS = "/teachers";
	public static final String STUDENTS = "/students";

	public static final String USER_MIN = "/user-min";

	public static final String ID = "/id";
	public static final String USER_NAME = "/username";
	public static final String ROLE = "/role";
	public static final String PASS = "/pass";

	public static final String PATH_ID = "/{id}";
	public static final String PATH_USERNAME = "/{username}";

	@Autowired
	private UserController userController;


	// MANAGERS************************************
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(MANAGERS + PATH_ID)
	public UserDto getManagerById(@PathVariable String id) throws ForbiddenException, UserIdNotFoundException {

		if (!this.userController.existsUserId(id))
			throw new UserIdNotFoundException();

		if (!this.userController.checkEqualOrGreaterPrivileges_Id(id, new Role[] { Role.MANAGER, Role.TEACHER }))
			throw new ForbiddenException();
		
		return this.userController.getUserById(id);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(MANAGERS + USER_NAME + PATH_USERNAME)
	public UserDto getManagerByUsername(@PathVariable String username)
			throws ForbiddenException, UserUsernameNotFoundException {

		if (!this.userController.existsUserUsername(username))
			throw new UserUsernameNotFoundException();

		if (!this.userController.checkEqualOrGreaterPrivileges(username, new Role[] { Role.MANAGER, Role.TEACHER }))
			throw new ForbiddenException();

		return this.userController.getUserByUsername(username);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(MANAGERS)
	public List<UserDto> getFullManagers() {
		return this.userController.getFullUsers(Role.MANAGER);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(MANAGERS + USER_MIN)
	public List<UserMinDto> getMinManagers() {
		return this.userController.getMinUsers(Role.MANAGER);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(MANAGERS)
	public UserDto createManager(@Valid @RequestBody UserDto userDto) throws UserFieldAlreadyExistException {

		if (this.userController.usernameRepeated(userDto))
			throw new UserFieldAlreadyExistException("Nombre de usuario existente.");

		if (this.userController.dniRepeated(userDto))
			throw new UserFieldAlreadyExistException("RUT existente.");

		if (this.userController.mobileRepeated(userDto))
			throw new UserFieldAlreadyExistException("Telefono existente.");

		if (this.userController.emailRepeated(userDto))
			throw new UserFieldAlreadyExistException("Email existente.");

		return this.userController.createUser(userDto, new Role[] { Role.MANAGER });
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(MANAGERS + PATH_USERNAME)
	public UserDto modifyManager(@PathVariable String username, @Valid @RequestBody UserDto userDto)
			throws ForbiddenException, UserFieldAlreadyExistException, UserUsernameNotFoundException {

		if (!this.userController.existsUserUsername(username))
			throw new UserUsernameNotFoundException();

		if (this.userController.usernameRepeated(userDto))
			throw new UserFieldAlreadyExistException("Nombre de usuario existente.");

		if (this.userController.dniRepeated(userDto))
			throw new UserFieldAlreadyExistException("RUT existente.");

		if (this.userController.mobileRepeated(userDto))
			throw new UserFieldAlreadyExistException("Telefono existente.");

		if (this.userController.emailRepeated(userDto))
			throw new UserFieldAlreadyExistException("Email existente.");

		if (!this.userController.checkEqualOrGreaterPrivileges(userDto.getUsername(), new Role[] { Role.MANAGER, Role.TEACHER }))
			throw new ForbiddenException();

		return this.userController.modifyUser(username, userDto);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(MANAGERS + PATH_USERNAME)
	public boolean deleteManager(@PathVariable String username) throws ForbiddenException, UserUsernameNotFoundException {

		if (!this.userController.existsUserUsername(username))
			throw new UserUsernameNotFoundException();

		if (!this.userController.checkEqualOrGreaterPrivileges(username, new Role[] { Role.MANAGER, Role.TEACHER }))
			throw new ForbiddenException();

		return this.userController.deleteUser(username);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping(MANAGERS + PATH_USERNAME)
	public boolean resetPassManager(@PathVariable String username, @RequestBody String resetedPass)
			throws ForbiddenException, UserUsernameNotFoundException {

		if (!this.userController.existsUserUsername(username))
			throw new UserUsernameNotFoundException();

		if (!this.userController.checkEqualOrGreaterPrivileges(username, new Role[] { Role.MANAGER, Role.TEACHER }))
			throw new ForbiddenException();

		return this.userController.resetPassUser(username, resetedPass);

	}

	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping(MANAGERS + ROLE + PATH_USERNAME)
	public UserDto setRoleManager(@PathVariable String username, @RequestBody Role[] roles)
			throws ForbiddenException, UserUsernameNotFoundException {

		if (!this.userController.existsUserUsername(username))
			throw new UserUsernameNotFoundException();

		if (!this.userController.checkEqualOrGreaterPrivileges(username, new Role[] { Role.MANAGER, Role.TEACHER }))
			throw new ForbiddenException();

		return this.userController.setRoleUser(username, roles);
	}


	// TEACHERS************************************
	@PreAuthorize("hasRole('MANAGER')")
	@GetMapping(TEACHERS + ID + PATH_ID)
	public UserDto getTeacherById(@PathVariable String id) throws ForbiddenException, UserIdNotFoundException {

		if (!this.userController.existsUserId(id))
			throw new UserIdNotFoundException();

		if (!this.userController.checkEqualOrGreaterPrivileges_Id(id, new Role[] { Role.TEACHER }))
			throw new ForbiddenException();
		
		return this.userController.getUserById(id);
	}

	@PreAuthorize("hasRole('MANAGER')")
	@GetMapping(TEACHERS + USER_NAME + PATH_USERNAME)
	public UserDto getTeacherByUsername(@PathVariable String username)
			throws ForbiddenException, UserUsernameNotFoundException {

		if (!this.userController.existsUserUsername(username))
			throw new UserUsernameNotFoundException();

		if (!this.userController.checkEqualOrGreaterPrivileges(username, new Role[] { Role.TEACHER }))
			throw new ForbiddenException();

		return this.userController.getUserByUsername(username);
	}

	@PreAuthorize("hasRole('MANAGER')")
	@GetMapping(TEACHERS)
	public List<UserDto> getFullTeachers() {
		return this.userController.getFullUsers(Role.TEACHER);
	}

	@PreAuthorize("hasRole('MANAGER')")
	@GetMapping(TEACHERS + USER_MIN)
	public List<UserMinDto> getMinTeachers() {
		return this.userController.getMinUsers(Role.TEACHER);
	}

	@PreAuthorize("hasRole('MANAGER')")
	@PostMapping(TEACHERS)
	public UserDto createTeacher(@Valid @RequestBody UserDto userDto)
			throws ForbiddenException, UserFieldAlreadyExistException {

		if (this.userController.usernameRepeated(userDto))
			throw new UserFieldAlreadyExistException("Nombre de usuario existente.");

		if (this.userController.dniRepeated(userDto))
			throw new UserFieldAlreadyExistException("RUT existente.");

		if (this.userController.mobileRepeated(userDto))
			throw new UserFieldAlreadyExistException("Telefono existente.");

		if (this.userController.emailRepeated(userDto))
			throw new UserFieldAlreadyExistException("Email existente.");

		return this.userController.createUser(userDto, new Role[] { Role.TEACHER });
	}

	@PreAuthorize("hasRole('MANAGER')")
	@PutMapping(TEACHERS + PATH_USERNAME)
	public UserDto modifyTeacher(@PathVariable String username, @Valid @RequestBody UserDto userDto)
			throws ForbiddenException, UserFieldAlreadyExistException, UserUsernameNotFoundException {

		if (!this.userController.existsUserUsername(username))
			throw new UserUsernameNotFoundException();

		if (this.userController.usernameRepeated(userDto))
			throw new UserFieldAlreadyExistException("Nombre de usuario existente.");

		if (this.userController.dniRepeated(userDto))
			throw new UserFieldAlreadyExistException("RUT existente.");

		if (this.userController.mobileRepeated(userDto))
			throw new UserFieldAlreadyExistException("Telefono existente.");

		if (this.userController.emailRepeated(userDto))
			throw new UserFieldAlreadyExistException("Email existente.");

		if (!this.userController.checkEqualOrGreaterPrivileges(userDto.getUsername(), new Role[] { Role.TEACHER }))
			throw new ForbiddenException();

		return this.userController.modifyUser(username, userDto);
	}

	@PreAuthorize("hasRole('MANAGER')")
	@DeleteMapping(TEACHERS + PATH_USERNAME)
	public boolean deleteTeacher(@PathVariable String username) throws ForbiddenException, UserUsernameNotFoundException {

		if (!this.userController.existsUserUsername(username))
			throw new UserUsernameNotFoundException();

		if (!this.userController.checkEqualOrGreaterPrivileges(username, new Role[] { Role.TEACHER }))
			throw new ForbiddenException();

		return this.userController.deleteUser(username);
	}

	@PreAuthorize("hasRole('MANAGER')")
	@PatchMapping(TEACHERS + PATH_USERNAME)
	public boolean resetPassTeacher(@PathVariable String username, @RequestBody String resetedPass)
			throws ForbiddenException, UserUsernameNotFoundException {

		if (!this.userController.existsUserUsername(username))
			throw new UserUsernameNotFoundException();

		if (!this.userController.checkEqualOrGreaterPrivileges(username, new Role[] { Role.TEACHER }))
			throw new ForbiddenException();

		return this.userController.resetPassUser(username, resetedPass);

	}

	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping(TEACHERS + ROLE + PATH_USERNAME)
	public UserDto setRoleTeacher(@PathVariable String username, @RequestBody Role[] roles)
			throws ForbiddenException, UserUsernameNotFoundException {

		if (!this.userController.existsUserUsername(username))
			throw new UserUsernameNotFoundException();

		if (!this.userController.checkEqualOrGreaterPrivileges(username, new Role[] { Role.MANAGER, Role.TEACHER }))
			throw new ForbiddenException();

		return this.userController.setRoleUser(username, roles);
	}

	// STUDENTS************************************
	@PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
	@GetMapping(STUDENTS + ID + PATH_ID)
	public UserDto getStudentById(@PathVariable String id) throws ForbiddenException, UserIdNotFoundException {

		if (!this.userController.existsUserId(id))
			throw new UserIdNotFoundException();

		if (!this.userController.checkEqualOrGreaterPrivileges_Id(id, new Role[] { Role.STUDENT }))
			throw new ForbiddenException();
		
		return this.userController.getUserById(id);
	}

	@PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
	@GetMapping(STUDENTS + USER_NAME + PATH_USERNAME)
	public UserDto getStudentByUsername(@PathVariable String username)
			throws ForbiddenException, UserUsernameNotFoundException {

		if (!this.userController.existsUserUsername(username))
			throw new UserUsernameNotFoundException();

		if (!this.userController.checkEqualOrGreaterPrivileges(username, new Role[] { Role.STUDENT }))
			throw new ForbiddenException();

		return this.userController.getUserByUsername(username);
	}

	@PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
	@GetMapping(STUDENTS)
	public List<UserDto> getFullStudents() {
		return this.userController.getFullUsers(Role.STUDENT);
	}

	@PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
	@GetMapping(STUDENTS + USER_MIN)
	public List<UserMinDto> getMinStudents() {
		return this.userController.getMinUsers(Role.STUDENT);
	}

	@PreAuthorize("hasRole('MANAGER')")
	@PostMapping(STUDENTS)
	public UserDto createStudent(@Valid @RequestBody UserDto userDto)
			throws ForbiddenException, UserFieldAlreadyExistException {

		if (this.userController.usernameRepeated(userDto))
			throw new UserFieldAlreadyExistException("Nombre de usuario existente.");

		if (this.userController.dniRepeated(userDto))
			throw new UserFieldAlreadyExistException("RUT existente.");

		if (this.userController.mobileRepeated(userDto))
			throw new UserFieldAlreadyExistException("Telefono existente.");

		if (this.userController.emailRepeated(userDto))
			throw new UserFieldAlreadyExistException("Email existente.");

		return this.userController.createUser(userDto, new Role[] { Role.STUDENT });
	}

	@PreAuthorize("hasRole('MANAGER')")
	@PutMapping(STUDENTS + PATH_USERNAME)
	public UserDto modifyStudent(@PathVariable String username, @Valid @RequestBody UserDto userDto)
			throws ForbiddenException, UserFieldAlreadyExistException, UserUsernameNotFoundException {

		if (!this.userController.existsUserUsername(username))
			throw new UserUsernameNotFoundException();

		if (this.userController.usernameRepeated(userDto))
			throw new UserFieldAlreadyExistException("Nombre de usuario existente.");

		if (this.userController.dniRepeated(userDto))
			throw new UserFieldAlreadyExistException("RUT existente.");

		if (this.userController.mobileRepeated(userDto))
			throw new UserFieldAlreadyExistException("Telefono existente.");

		if (this.userController.emailRepeated(userDto))
			throw new UserFieldAlreadyExistException("Email existente.");

		if (!this.userController.checkEqualOrGreaterPrivileges(userDto.getUsername(), new Role[] { Role.STUDENT }))
			throw new ForbiddenException();

		return this.userController.modifyUser(username, userDto);
	}

	@PreAuthorize("hasRole('MANAGER')")
	@DeleteMapping(STUDENTS + PATH_USERNAME)
	public boolean deleteStudent(@PathVariable String username) throws ForbiddenException, UserUsernameNotFoundException {

		if (!this.userController.existsUserUsername(username))
			throw new UserUsernameNotFoundException();

		if (!this.userController.checkEqualOrGreaterPrivileges(username, new Role[] { Role.STUDENT }))
			throw new ForbiddenException();

		return this.userController.deleteUser(username);
	}

	@PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
	@PatchMapping(STUDENTS + PATH_USERNAME)
	public boolean resetPassStudent(@PathVariable String username, @RequestBody String resetedPass)
			throws ForbiddenException, UserUsernameNotFoundException {

		if (!this.userController.existsUserUsername(username))
			throw new UserUsernameNotFoundException();

		if (!this.userController.checkEqualOrGreaterPrivileges(username, new Role[] { Role.STUDENT }))
			throw new ForbiddenException();

		return this.userController.resetPassUser(username, resetedPass);

	}


    // ALL-USERS**********************************
	@PreAuthorize("authentication.name == #id")
	@GetMapping(ID + PATH_ID)
	public UserDto getUserById(@PathVariable String id) throws ForbiddenException, UserIdNotFoundException {

		if (!this.userController.existsUserId(id))
			throw new UserIdNotFoundException();

		return this.userController.getUserById(id);
	}
	
	@PreAuthorize("authentication.name == #username")
	@GetMapping(USER_NAME + PATH_USERNAME)
	public UserDto getUserByUsername(@PathVariable String username) throws ForbiddenException, UserUsernameNotFoundException {

		if (!this.userController.existsUserUsername(username))
			throw new UserUsernameNotFoundException();

		return this.userController.getUserByUsername(username);
	}
	
	@PreAuthorize("authentication.name == #username")
	@PutMapping(PATH_USERNAME)
	public UserDto modifyUser(@PathVariable String username, @Valid @RequestBody UserDto userDto)
			throws ForbiddenException, UserFieldAlreadyExistException, UserUsernameNotFoundException {

		if (!this.userController.existsUserUsername(username))
			throw new UserUsernameNotFoundException();

		if (this.userController.usernameRepeated(userDto))
			throw new UserFieldAlreadyExistException("Existing username");

		if (this.userController.dniRepeated(userDto))
			throw new UserFieldAlreadyExistException("Existing dni");

		if (this.userController.mobileRepeated(userDto))
			throw new UserFieldAlreadyExistException("Existing mobile");

		if (this.userController.emailRepeated(userDto))
			throw new UserFieldAlreadyExistException("Existing email");


		return this.userController.modifyUser(username, userDto);
	}
	
	@PreAuthorize("authentication.name == #username")
	@PatchMapping(PASS + PATH_USERNAME)
	public boolean setUserPass(@PathVariable String username, @RequestBody String[] setPass)
			throws PasswordNotMatchException, UserUsernameNotFoundException {

		if (!this.userController.existsUserUsername(username))
			throw new UserUsernameNotFoundException();

		if (!this.userController.passMatch(username, setPass[0]))
			throw new PasswordNotMatchException();

		return this.userController.setPassUser(username, setPass);
	}

	

}
