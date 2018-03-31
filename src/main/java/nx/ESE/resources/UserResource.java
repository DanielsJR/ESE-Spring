package nx.ESE.resources;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jayway.jsonpath.internal.path.PathToken;

import nx.ESE.controllers.UserController;
import nx.ESE.documents.Role;
import nx.ESE.dtos.UserDto;
import nx.ESE.dtos.UserMinDto;
import nx.ESE.resources.exceptions.FieldInvalidException;
import nx.ESE.resources.exceptions.ForbiddenException;
import nx.ESE.resources.exceptions.UserFieldAlreadyExistException;
import nx.ESE.resources.exceptions.UserIdNotFoundException;

@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('TEACHER') or hasRole('STUDENT')")
@RestController
@RequestMapping(UserResource.USERS)
public class UserResource {

	public static final String USERS = "/users";
	public static final String ADMINS = "/admins";
	public static final String MANAGERS = "/managers" ;
	public static final String TEACHERS = "/teachers";
	public static final String STUDENTS = "/students";
	
	public static final String USER_MIN = "/user-min";
	
	public static final String ID = "/id";
	public static final String USER_NAME = "/username";
	public static final String TOKEN = "/token";
	
	public static final String PATH_ID = "/{id}";
	public static final String PATH_USERNAME = "/{username}";
	public static final String PATH_TOKEN = "/{token}";
	
	
	@Autowired
	private UserController userController;
	
	//ADMINS************************************
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(ADMINS + PATH_ID)
	public UserDto getAdminById(@PathVariable String id) throws UserIdNotFoundException {
		return this.userController.getUserByUsername(id, new Role[] { Role.ADMIN })
				.orElseThrow(() -> new UserIdNotFoundException(id));
	}
	
	@PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
	@GetMapping(ADMINS + USER_NAME + PATH_USERNAME)
	public UserDto getAdminByUsername(@PathVariable String username) throws UserIdNotFoundException {
		return this.userController.getUserByUsername(username, new Role[] { Role.ADMIN })
				.orElseThrow(() -> new UserIdNotFoundException(username));
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(ADMINS + TOKEN + PATH_TOKEN)
	public UserDto getAdminByToken(@PathVariable String token) throws UserIdNotFoundException {
		return this.userController.getUserByToken(token, new Role[] { Role.ADMIN })
				.orElseThrow(() -> new UserIdNotFoundException(token));
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(ADMINS)
	public List<UserDto> getFullAdmins() {
		return this.userController.getFullUsers(Role.ADMIN);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(ADMINS + USER_MIN)
	public List<UserMinDto> getMinAdmins() {
		return this.userController.getMinUsers(Role.ADMIN );
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(ADMINS)
	public UserDto createAdmin(@Valid @RequestBody UserDto userDto)
			throws ForbiddenException, UserFieldAlreadyExistException {
		if (userDto.getPassword() == null) {
			userDto.setPassword(UUID.randomUUID().toString());
		}
		if (this.userController.usernameRepeated(userDto)) {
			throw new UserFieldAlreadyExistException("Existing username");
		}
		if (this.userController.dniRepeated(userDto)) {
			throw new UserFieldAlreadyExistException("Existing dni");
		}
		if (this.userController.mobileRepeated(userDto)) {
	            throw new UserFieldAlreadyExistException("Existing mobile");
	    }
		if (this.userController.emailRepeated(userDto)) {
			throw new UserFieldAlreadyExistException("Existing email");
		}
	     
		return this.userController.createUser(userDto, new Role[] { Role.ADMIN })
				.orElseThrow(() -> new ForbiddenException());
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(ADMINS + PATH_ID)
	public UserDto modifyAdmin(@PathVariable String id, @Valid @RequestBody UserDto userDto)
			throws ForbiddenException, UserIdNotFoundException, UserFieldAlreadyExistException {
		if (!this.userController.existsUserId(id)) {
			throw new UserIdNotFoundException("Not found username");
		}
		if (this.userController.usernameRepeated(userDto)) {
			throw new UserFieldAlreadyExistException("Existing username");
		}
		if (this.userController.dniRepeated(userDto)) {
			throw new UserFieldAlreadyExistException("Existing dni");
		}
		if (this.userController.mobileRepeated(userDto)) {
	            throw new UserFieldAlreadyExistException("Existing mobile");
	    }
		if (this.userController.emailRepeated(userDto)) {
			throw new UserFieldAlreadyExistException("Existing email");
		}

        return this.userController.modifyUser(id, userDto, new Role[] { Role.ADMIN })
				.orElseThrow(() -> new ForbiddenException());
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(ADMINS + PATH_ID)
	public void deleteAdmin(@PathVariable String id) throws ForbiddenException, FieldInvalidException, UserIdNotFoundException {
		if (!this.userController.existsUserId(id)) {
			throw new UserIdNotFoundException("Not found id");
		}
		if (!this.userController.deleteUser(id, new Role[] { Role.ADMIN })) {
			throw new ForbiddenException();
		}
	}
	
	//MANAGERS************************************
	@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
	@GetMapping(MANAGERS + PATH_ID)
	public UserDto getManagerById(@PathVariable String id) throws UserIdNotFoundException {
		return this.userController.getUserByUsername(id, new Role[] { Role.MANAGER })
				.orElseThrow(() -> new UserIdNotFoundException(id));
	}
	
	@PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
	@GetMapping(MANAGERS + USER_NAME + PATH_USERNAME)
	public UserDto getManagerByUsername(@PathVariable String username) throws UserIdNotFoundException {
		return this.userController.getUserByUsername(username, new Role[] { Role.MANAGER })
				.orElseThrow(() -> new UserIdNotFoundException(username));
	}
	
	@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
	@GetMapping(MANAGERS + TOKEN + PATH_TOKEN)
	public UserDto getManagerByToken(@PathVariable String token) throws UserIdNotFoundException {
		return this.userController.getUserByToken(token, new Role[] { Role.MANAGER })
				.orElseThrow(() -> new UserIdNotFoundException(token));
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
	@GetMapping(MANAGERS)
	public List<UserDto> getFullManagers() {
		return this.userController.getFullUsers(Role.MANAGER);
	}
	
	@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
	@GetMapping(MANAGERS + USER_MIN)
	public List<UserMinDto> getMinManagers() {
		return this.userController.getMinUsers(Role.MANAGER );
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
	@PostMapping(MANAGERS)
	public UserDto createManager(@Valid @RequestBody UserDto userDto)
			throws ForbiddenException, UserFieldAlreadyExistException {
		if (userDto.getPassword() == null) {
			userDto.setPassword(UUID.randomUUID().toString());
		}
		if (this.userController.usernameRepeated(userDto)) {
			throw new UserFieldAlreadyExistException("Existing username");
		}
		if (this.userController.dniRepeated(userDto)) {
			throw new UserFieldAlreadyExistException("Existing dni");
		}
		if (this.userController.mobileRepeated(userDto)) {
	            throw new UserFieldAlreadyExistException("Existing mobile");
	    }
		if (this.userController.emailRepeated(userDto)) {
			throw new UserFieldAlreadyExistException("Existing email");
		}
	     
		return this.userController.createUser(userDto, new Role[] { Role.MANAGER })
				.orElseThrow(() -> new ForbiddenException());
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
	@PutMapping(MANAGERS + PATH_ID)
	public UserDto modifyManager(@PathVariable String id, @Valid @RequestBody UserDto userDto)
			throws ForbiddenException, UserIdNotFoundException, UserFieldAlreadyExistException {
		if (!this.userController.existsUserId(id)) {
			throw new UserIdNotFoundException("Not found id");
		}
		if (this.userController.usernameRepeated(userDto)) {
			throw new UserFieldAlreadyExistException("Existing username");
		}
		if (this.userController.dniRepeated(userDto)) {
			throw new UserFieldAlreadyExistException("Existing dni");
		}
		if (this.userController.mobileRepeated(userDto)) {
	            throw new UserFieldAlreadyExistException("Existing mobile");
	    }
		if (this.userController.emailRepeated(userDto)) {
			throw new UserFieldAlreadyExistException("Existing email");
		}

        return this.userController.modifyUser(id, userDto, new Role[] { Role.MANAGER })
				.orElseThrow(() -> new ForbiddenException());
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
	@DeleteMapping(MANAGERS + PATH_ID)
	public void deleteManager(@PathVariable String id) throws ForbiddenException, FieldInvalidException, UserIdNotFoundException {
		if (!this.userController.existsUserId(id)) {
			throw new UserIdNotFoundException("Not found id");
		}
		if (!this.userController.deleteUser(id, new Role[] { Role.MANAGER })) {
			throw new ForbiddenException();
		}
	}
	
	//TEACHERS************************************
	@PreAuthorize("hasRole('MANAGER')")
	@GetMapping(TEACHERS + PATH_ID)
	public UserDto getTeacherById(@PathVariable String id) throws UserIdNotFoundException {
		return this.userController.getUserByUsername(id, new Role[] { Role.TEACHER })
				.orElseThrow(() -> new UserIdNotFoundException(id));
	}
	
	@PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
	@GetMapping(TEACHERS + USER_NAME + PATH_USERNAME)
	public UserDto getTeacherByUsername(@PathVariable String username) throws UserIdNotFoundException {
		return this.userController.getUserByUsername(username, new Role[] { Role.TEACHER })
				.orElseThrow(() -> new UserIdNotFoundException(username));
	}
	
	@PreAuthorize("hasRole('MANAGER')")
	@GetMapping(TEACHERS + TOKEN + PATH_TOKEN)
	public UserDto getTeacherByToken(@PathVariable String token) throws UserIdNotFoundException {
		return this.userController.getUserByToken(token, new Role[] { Role.TEACHER })
				.orElseThrow(() -> new UserIdNotFoundException(token));
	}
	
	@PreAuthorize("hasRole('MANAGER')")
	@GetMapping(TEACHERS)
	public List<UserDto> getFullTeachers() {
		return this.userController.getFullUsers(Role.TEACHER);
	}
	
	@PreAuthorize("hasRole('MANAGER')")
	@GetMapping(TEACHERS + USER_MIN)
	public List<UserMinDto> getMinTeachers() {
		return this.userController.getMinUsers(Role.TEACHER );
	}

	@PreAuthorize("hasRole('MANAGER')")
	@PostMapping(TEACHERS)
	public UserDto createTeacher(@Valid @RequestBody UserDto userDto)
			throws ForbiddenException, UserFieldAlreadyExistException {
		if (userDto.getPassword() == null) {
			userDto.setPassword(UUID.randomUUID().toString());
		}
		if (this.userController.usernameRepeated(userDto)) {
			throw new UserFieldAlreadyExistException("Existing username");
		}
		if (this.userController.dniRepeated(userDto)) {
			throw new UserFieldAlreadyExistException("Existing dni");
		}
		if (this.userController.mobileRepeated(userDto)) {
	            throw new UserFieldAlreadyExistException("Existing mobile");
	    }
		if (this.userController.emailRepeated(userDto)) {
			throw new UserFieldAlreadyExistException("Existing email");
		}
	     
		return this.userController.createUser(userDto, new Role[] { Role.TEACHER })
				.orElseThrow(() -> new ForbiddenException());
	}

	@PreAuthorize("hasRole('MANAGER')")
	@PutMapping(TEACHERS + PATH_ID)
	public UserDto modifyTeacher(@PathVariable String id, @Valid @RequestBody UserDto userDto)
			throws ForbiddenException, UserIdNotFoundException, UserFieldAlreadyExistException {
		if (!this.userController.existsUserId(id)) {
			throw new UserIdNotFoundException("Not found id");
		}
		if (this.userController.usernameRepeated(userDto)) {
			throw new UserFieldAlreadyExistException("Existing username");
		}
		if (this.userController.dniRepeated(userDto)) {
			throw new UserFieldAlreadyExistException("Existing dni");
		}
		if (this.userController.mobileRepeated(userDto)) {
	            throw new UserFieldAlreadyExistException("Existing mobile");
	    }
		if (this.userController.emailRepeated(userDto)) {
			throw new UserFieldAlreadyExistException("Existing email");
		}

        return this.userController.modifyUser(id, userDto, new Role[] { Role.TEACHER })
				.orElseThrow(() -> new ForbiddenException());
	}

	@PreAuthorize("hasRole('MANAGER')")
	@DeleteMapping(TEACHERS + PATH_ID)
	public void deleteTeacher(@PathVariable String id) throws ForbiddenException, FieldInvalidException, UserIdNotFoundException {
		if (!this.userController.existsUserId(id)) {
			throw new UserIdNotFoundException("Not found id");
		}
		if (!this.userController.deleteUser(id, new Role[] { Role.TEACHER })) {
			throw new ForbiddenException();
		}
	}
	
	//STUDENTS************************************
	@PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
	@GetMapping(STUDENTS + PATH_ID)
	public UserDto getStudentById(@PathVariable String id) throws UserIdNotFoundException {
		return this.userController.getUserById(id, new Role[] { Role.STUDENT })
				.orElseThrow(() -> new UserIdNotFoundException(id));
	}
	
	@PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
	@GetMapping(STUDENTS + USER_NAME + PATH_USERNAME)
	public UserDto getStudentByUsername(@PathVariable String username) throws UserIdNotFoundException {
		return this.userController.getUserByUsername(username, new Role[] { Role.STUDENT })
				.orElseThrow(() -> new UserIdNotFoundException(username));
	}
	
	@PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
	@GetMapping(STUDENTS + TOKEN + PATH_TOKEN)
	public UserDto getStudentByToken(@PathVariable String token) throws UserIdNotFoundException {
		return this.userController.getUserByToken(token, new Role[] { Role.STUDENT })
				.orElseThrow(() -> new UserIdNotFoundException(token));
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
		if (userDto.getPassword() == null) {
			userDto.setPassword(UUID.randomUUID().toString());
		}
		if (this.userController.usernameRepeated(userDto)) {
			throw new UserFieldAlreadyExistException("Existing username");
		}
		if (this.userController.dniRepeated(userDto)) {
			throw new UserFieldAlreadyExistException("Existing dni");
		}
		if (this.userController.mobileRepeated(userDto)) {
	            throw new UserFieldAlreadyExistException("Existing mobile");
	    }
		if (this.userController.emailRepeated(userDto)) {
			throw new UserFieldAlreadyExistException("Existing email");
		}
	     
		return this.userController.createUser(userDto, new Role[] { Role.STUDENT })
				.orElseThrow(() -> new ForbiddenException());
	}

	@PreAuthorize("hasRole('MANAGER')")
	@PutMapping(STUDENTS + PATH_ID)
	public UserDto modifyStudent(@PathVariable String id, @Valid @RequestBody UserDto userDto)
			throws ForbiddenException, UserIdNotFoundException, UserFieldAlreadyExistException {
		if (!this.userController.existsUserId(id)) {
			throw new UserIdNotFoundException("Not found id");
		}
		if (this.userController.usernameRepeated(userDto)) {
			throw new UserFieldAlreadyExistException("Existing username");
		}
		if (this.userController.dniRepeated(userDto)) {
			throw new UserFieldAlreadyExistException("Existing dni");
		}
		if (this.userController.mobileRepeated(userDto)) {
	            throw new UserFieldAlreadyExistException("Existing mobile");
	    }
		if (this.userController.emailRepeated(userDto)) {
			throw new UserFieldAlreadyExistException("Existing email");
		}

        return this.userController.modifyUser(id, userDto, new Role[] { Role.STUDENT })
				.orElseThrow(() -> new ForbiddenException());
	}

	@PreAuthorize("hasRole('MANAGER')")
	@DeleteMapping(STUDENTS + PATH_ID)
	public void deleteStudent(@PathVariable String id) throws ForbiddenException, UserIdNotFoundException, FieldInvalidException {
		if (id == null || id.trim().equals("")) {
			throw new FieldInvalidException("id not valid");
		}
		if (!this.userController.existsUserId(id)) {
			throw new UserIdNotFoundException("Not found id");
		}
		if (!this.userController.deleteUser(id, new Role[] { Role.STUDENT })) {
			throw new ForbiddenException();
		}
	}
}
