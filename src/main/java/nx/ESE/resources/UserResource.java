package nx.ESE.resources;

import java.util.List;
import java.util.Optional;
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

import nx.ESE.controllers.UserController;
import nx.ESE.documents.Role;
import nx.ESE.dtos.UserDto;
import nx.ESE.dtos.UserMinDto;
import nx.ESE.resources.exceptions.FieldInvalidException;
import nx.ESE.resources.exceptions.ForbiddenException;
import nx.ESE.resources.exceptions.UserFieldAlreadyExistException;
import nx.ESE.resources.exceptions.UserIdNotFoundException;

// @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('TEACHER')")
@RestController
@RequestMapping(UserResource.USERS)
public class UserResource {

	public static final String USERS = "/users";

	public static final String STUDENT = "/student";

	public static final String USER_NAME = "/{username}";

	@Autowired
	private UserController userController;

	@GetMapping(STUDENT + USER_NAME)
	public UserDto getStudent(@PathVariable String username) throws UserIdNotFoundException {
		return this.userController.getUser(username, new Role[] { Role.STUDENT })
				.orElseThrow(() -> new UserIdNotFoundException(username));
	}

	@GetMapping(STUDENT)
	public List<UserDto> getStudents() {
		return this.userController.getUsers();
	}

	@PostMapping(STUDENT)
	public UserDto createStudent(@Valid @RequestBody UserDto userDto)
			throws ForbiddenException,FieldInvalidException, UserFieldAlreadyExistException {
		if (userDto.getUsername().trim().equals("")) {
			throw new FieldInvalidException("username not valid");
		}
		if (userDto.getPassword() == null) {
			userDto.setPassword(UUID.randomUUID().toString());
		}
		if (this.userController.existsUsername(userDto.getUsername())) {
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

	@PutMapping(STUDENT + USER_NAME)
	public UserDto modifyStudent(@PathVariable String username, @Valid @RequestBody UserDto userDto)
			throws ForbiddenException, UserIdNotFoundException, FieldInvalidException, UserFieldAlreadyExistException {
		if (userDto.getUsername().trim().equals("")) {
			throw new FieldInvalidException("username not valid");
		}
		if (!this.userController.existsUsername(username)) {
			throw new UserIdNotFoundException("Not found username");
		}
		if (this.userController.usernameRepeated(username, userDto)) {
			throw new UserFieldAlreadyExistException("Existing username");
		}
		if (this.userController.dniRepeated(username, userDto)) {
			throw new UserFieldAlreadyExistException("Existing dni");
		}
		if (this.userController.mobileRepeated(username, userDto)) {
	            throw new UserFieldAlreadyExistException("Existing mobile");
	    }
		if (this.userController.emailRepeated(username, userDto)) {
			throw new UserFieldAlreadyExistException("Existing email");
		}

        return this.userController.modifyUser(username, userDto, new Role[] { Role.STUDENT })
				.orElseThrow(() -> new ForbiddenException());
	}

	@DeleteMapping(STUDENT + USER_NAME)
	public void deleteStudent(@PathVariable String username) throws ForbiddenException, FieldInvalidException, UserIdNotFoundException {
		if (username.trim().equals("")) {
			throw new FieldInvalidException("username not valid");
		}
		if (!this.userController.existsUsername(username)) {
			throw new UserIdNotFoundException("Not found username");
		}
		if (!this.userController.deleteUser(username, new Role[] { Role.STUDENT })) {
			throw new ForbiddenException();
		}
	}
}
