package nx.ESE.resources;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import nx.ESE.controllers.UserController;
import nx.ESE.documents.Role;
import nx.ESE.dtos.UserDto;
import nx.ESE.resources.exceptions.UserFieldAlreadyExistException;


@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(UserResource.USERS)
public class UserResource {
	
    public static final String USERS = "/users";

   
    @Autowired
    private UserController userController;

    @PostMapping
    public void createStudent(@Valid @RequestBody UserDto userDto) throws UserFieldAlreadyExistException {
        if (userDto.getPassword() == null) {
            userDto.setPassword(UUID.randomUUID().toString());
        }
        if (this.userController.existsUsername(userDto.getUsername())) {
            throw new UserFieldAlreadyExistException("Existing username");
        }
        if (this.userController.emailRepeated(userDto)) {
            throw new UserFieldAlreadyExistException("Existing email");
        }
        if (this.userController.dniRepeated(userDto)) {
            throw new UserFieldAlreadyExistException("Existing dni");
        }
        this.userController.createUser(userDto, new Role[] {Role.STUDENT});
    }

}
