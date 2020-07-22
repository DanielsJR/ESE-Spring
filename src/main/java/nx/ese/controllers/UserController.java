package nx.ese.controllers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import nx.ese.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import nx.ese.documents.Role;
import nx.ese.dtos.UserDto;
import nx.ese.dtos.UserMinDto;
import nx.ese.services.UserService;

@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('TEACHER') or hasRole('STUDENT')")
@RestController
@RequestMapping(UserController.USERS)
public class UserController {

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
    private UserService userService;


    // ***************MANAGERS*********************
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(MANAGERS)
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createManager(@Valid @RequestBody UserDto userDto)
            throws FieldAlreadyExistException, FieldNullException, FieldInvalidException {

        if (!this.userService.isIdNull(userDto))
            throw new FieldInvalidException("Id");

        if (this.userService.isPassNull(userDto))
            throw new FieldNullException("Password");

        if (this.userService.dniRepeated(userDto))
            throw new FieldAlreadyExistException("RUT");

        if (this.userService.mobileRepeated(userDto))
            throw new FieldAlreadyExistException("Telefono");

        if (this.userService.emailRepeated(userDto))
            throw new FieldAlreadyExistException("Email");

        return this.userService.createUser(userDto, new Role[]{Role.MANAGER});
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(MANAGERS + PATH_USERNAME)
    public UserDto modifyManager(@PathVariable String username, @Valid @RequestBody UserDto userDto)
            throws ForbiddenException, FieldAlreadyExistException, FieldNotFoundException, DocumentNotFoundException {

        if (!this.userService.existsUserUsername(username))
            throw new FieldNotFoundException("Username");

        if (this.userService.usernameRepeated(userDto))
            throw new FieldAlreadyExistException("Nombre de usuario");

        if (this.userService.dniRepeated(userDto))
            throw new FieldAlreadyExistException("RUT");

        if (this.userService.mobileRepeated(userDto))
            throw new FieldAlreadyExistException("Telefono");

        if (this.userService.emailRepeated(userDto))
            throw new FieldAlreadyExistException("Email");

        if (this.userService.hasUserGreaterPrivilegesByUsername(userDto.getUsername(),
                new Role[]{Role.MANAGER, Role.TEACHER}))
            throw new ForbiddenException();

        return this.userService.modifyUser(username, userDto).orElseThrow(() -> new DocumentNotFoundException("Usuario"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(MANAGERS + PASS + PATH_USERNAME)
    public boolean resetPassManager(@PathVariable String username, @RequestBody String resetedPass)
            throws ForbiddenException, FieldNotFoundException, DocumentNotFoundException {

        if (!this.userService.existsUserUsername(username))
            throw new FieldNotFoundException("Username");

        if (this.userService.hasUserGreaterPrivilegesByUsername(username, new Role[]{Role.MANAGER, Role.TEACHER}))
            throw new ForbiddenException();

        return this.userService.resetPassUser(username, resetedPass).orElseThrow(() -> new DocumentNotFoundException("Usuario"));

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(MANAGERS + ROLE + PATH_USERNAME)
    public UserDto setRoleManager(@PathVariable String username, @RequestBody UserDto userDto)
            throws ForbiddenException, FieldNotFoundException, ForbiddenChangeRoleException, DocumentNotFoundException {

        if (!this.userService.existsUserUsername(username))
            throw new FieldNotFoundException("Username");

        if (this.userService.hasUserGreaterPrivilegesByUsername(username, new Role[]{Role.MANAGER, Role.TEACHER}))
            throw new ForbiddenException();

        if (this.userService.isChiefTeacherSetRoles(userDto))
            throw new ForbiddenChangeRoleException("Docente es Profesor Jefe");

        if (this.userService.isTeacherInSubjectSetRoles(userDto))
            throw new ForbiddenChangeRoleException("Docente imparte asignatura(s)");

        return this.userService.setRoleUser(userDto).orElseThrow(() -> new DocumentNotFoundException("Usuario"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(MANAGERS + PATH_USERNAME)
    public UserDto deleteManager(@PathVariable String username)
            throws ForbiddenException, FieldNotFoundException, ForbiddenDeleteException, DocumentNotFoundException {

        if (!this.userService.existsUserUsername(username))
            throw new FieldNotFoundException("Username");

        if (this.userService.hasUserGreaterPrivilegesByUsername(username, new Role[]{Role.MANAGER, Role.TEACHER}))
            throw new ForbiddenException();

        if (this.userService.isChiefTeacher(username))
            throw new ForbiddenDeleteException("Docente es Profesor Jefe");

        if (this.userService.isTeacherInSubject(username))
            throw new ForbiddenDeleteException("Docente imparte asignatura(s)");

        return this.userService.deleteUser(username).orElseThrow(() -> new DocumentNotFoundException("Usuario"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(MANAGERS + ID + PATH_ID)
    public UserDto getManagerById(@PathVariable String id) throws ForbiddenException, FieldNotFoundException, DocumentNotFoundException {

        if (!this.userService.existsUserId(id))
            throw new FieldNotFoundException("Id");

        if (this.userService.hasUserGreaterPrivilegesById(id, new Role[]{Role.MANAGER, Role.TEACHER}))
            throw new ForbiddenException();

        return this.userService.getUserById(id).orElseThrow(() -> new DocumentNotFoundException("Administrador"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(MANAGERS + USER_NAME + PATH_USERNAME)
    public UserDto getManagerByUsername(@PathVariable String username)
            throws ForbiddenException, FieldNotFoundException, DocumentNotFoundException {

        if (!this.userService.existsUserUsername(username))
            throw new FieldNotFoundException("Username");

        if (this.userService.hasUserGreaterPrivilegesByUsername(username, new Role[]{Role.MANAGER, Role.TEACHER}))
            throw new ForbiddenException();

        return this.userService.getUserByUsername(username).orElseThrow(() -> new DocumentNotFoundException("Administrador"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(MANAGERS)
    public List<UserDto> getFullManagers() {
        return this.userService.getFullUsers(Role.MANAGER).orElse(Collections.emptyList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(MANAGERS + USER_MIN)
    public List<UserMinDto> getMinManagers() {
        return this.userService.getMinUsers(Role.MANAGER).orElse(Collections.emptyList());
    }


    // **************TEACHERS****************
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping(TEACHERS)
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createTeacher(@Valid @RequestBody UserDto userDto)
            throws ForbiddenException, FieldAlreadyExistException, FieldNullException, FieldInvalidException {

        if (!this.userService.isIdNull(userDto))
            throw new FieldInvalidException("Id");

        if (this.userService.isPassNull(userDto))
            throw new FieldNullException("Password");

        if (this.userService.dniRepeated(userDto))
            throw new FieldAlreadyExistException("RUT");

        if (this.userService.mobileRepeated(userDto))
            throw new FieldAlreadyExistException("Telefono");

        if (this.userService.emailRepeated(userDto))
            throw new FieldAlreadyExistException("Email");

        return this.userService.createUser(userDto, new Role[]{Role.TEACHER});
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping(TEACHERS + PATH_USERNAME)
    public UserDto modifyTeacher(@PathVariable String username, @Valid @RequestBody UserDto userDto)
            throws ForbiddenException, FieldAlreadyExistException, FieldNotFoundException, DocumentNotFoundException {

        if (!this.userService.existsUserUsername(username))
            throw new FieldNotFoundException("Username");

        if (this.userService.usernameRepeated(userDto))
            throw new FieldAlreadyExistException("Nombre de usuario");

        if (this.userService.dniRepeated(userDto))
            throw new FieldAlreadyExistException("RUT");

        if (this.userService.mobileRepeated(userDto))
            throw new FieldAlreadyExistException("Telefono");

        if (this.userService.emailRepeated(userDto))
            throw new FieldAlreadyExistException("Email");

        if (this.userService.hasUserGreaterPrivilegesByUsername(userDto.getUsername(), new Role[]{Role.TEACHER}))
            throw new ForbiddenException();

        return this.userService.modifyUser(username, userDto).orElseThrow(() -> new DocumentNotFoundException("Usuario"));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PatchMapping(TEACHERS + PASS + PATH_USERNAME)
    public boolean resetPassTeacher(@PathVariable String username, @RequestBody String resetedPass)
            throws ForbiddenException, FieldNotFoundException, DocumentNotFoundException {

        if (!this.userService.existsUserUsername(username))
            throw new FieldNotFoundException("Username");

        if (this.userService.hasUserGreaterPrivilegesByUsername(username, new Role[]{Role.TEACHER}))
            throw new ForbiddenException();

        return this.userService.resetPassUser(username, resetedPass).orElseThrow(() -> new DocumentNotFoundException("Usuario"));

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(TEACHERS + ROLE + PATH_USERNAME)
    public UserDto setRoleTeacher(@PathVariable String username, @RequestBody UserDto userDto)
            throws ForbiddenException, FieldNotFoundException, ForbiddenChangeRoleException, DocumentNotFoundException {

        if (!this.userService.existsUserUsername(username))
            throw new FieldNotFoundException("Username");

        if (this.userService.hasUserGreaterPrivilegesByUsername(username, new Role[]{Role.MANAGER, Role.TEACHER}))
            throw new ForbiddenException();

        if (this.userService.isChiefTeacherSetRoles(userDto))
            throw new ForbiddenChangeRoleException("Docente es Profesor Jefe");

        if (this.userService.isTeacherInSubjectSetRoles(userDto))
            throw new ForbiddenChangeRoleException("Docente imparte asignatura(s)");

        return this.userService.setRoleUser(userDto).orElseThrow(() -> new DocumentNotFoundException("Usuario"));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping(TEACHERS + PATH_USERNAME)
    public UserDto deleteTeacher(@PathVariable String username)
            throws ForbiddenException, FieldNotFoundException, ForbiddenDeleteException, DocumentNotFoundException {

        if (!this.userService.existsUserUsername(username))
            throw new FieldNotFoundException("Username");

        if (this.userService.hasUserGreaterPrivilegesByUsername(username, new Role[]{Role.TEACHER}))
            throw new ForbiddenException();

        if (this.userService.isChiefTeacher(username))
            throw new ForbiddenDeleteException("Docente es Profesor Jefe");

        if (this.userService.isTeacherInSubject(username))
            throw new ForbiddenDeleteException("Docente imparte asignatura(s)");

        return this.userService.deleteUser(username).orElseThrow(() -> new DocumentNotFoundException("Usuario"));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping(TEACHERS + ID + PATH_ID)
    public UserDto getTeacherById(@PathVariable String id) throws ForbiddenException, FieldNotFoundException, DocumentNotFoundException {

        if (!this.userService.existsUserId(id))
            throw new FieldNotFoundException("Id");

        if (this.userService.hasUserGreaterPrivilegesById(id, new Role[]{Role.TEACHER}))
            throw new ForbiddenException();

        return this.userService.getUserById(id).orElseThrow(() -> new DocumentNotFoundException("Docente"));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping(TEACHERS + USER_NAME + PATH_USERNAME)
    public UserDto getTeacherByUsername(@PathVariable String username)
            throws ForbiddenException, FieldNotFoundException, DocumentNotFoundException {

        if (!this.userService.existsUserUsername(username))
            throw new FieldNotFoundException("Username");

        if (this.userService.hasUserGreaterPrivilegesByUsername(username, new Role[]{Role.TEACHER}))
            throw new ForbiddenException();

        return this.userService.getUserByUsername(username).orElseThrow(() -> new DocumentNotFoundException("Docente"));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @GetMapping(TEACHERS)
    public List<UserDto> getFullTeachers() {

        return this.userService.getFullUsers(Role.TEACHER).orElse(Collections.emptyList());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @GetMapping(TEACHERS + USER_MIN)
    public List<UserMinDto> getMinTeachers() {

        return this.userService.getMinUsers(Role.TEACHER).orElse(Collections.emptyList());
    }


    // ****************STUDENTS********************
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping(STUDENTS)
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createStudent(@Valid @RequestBody UserDto userDto)
            throws ForbiddenException, FieldAlreadyExistException, FieldNullException, FieldInvalidException {

        if (!this.userService.isIdNull(userDto))
            throw new FieldInvalidException("Id");

        if (this.userService.isPassNull(userDto))
            throw new FieldNullException("Password");

        if (this.userService.dniRepeated(userDto))
            throw new FieldAlreadyExistException("RUT");

        if (this.userService.mobileRepeated(userDto))
            throw new FieldAlreadyExistException("Telefono");

        if (this.userService.emailRepeated(userDto))
            throw new FieldAlreadyExistException("Email");

        return this.userService.createUser(userDto, new Role[]{Role.STUDENT});
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping(STUDENTS + PATH_USERNAME)
    public UserDto modifyStudent(@PathVariable String username, @Valid @RequestBody UserDto userDto)
            throws ForbiddenException, FieldAlreadyExistException, FieldNotFoundException, DocumentNotFoundException {

        if (!this.userService.existsUserUsername(username))
            throw new FieldNotFoundException("Username");

        if (this.userService.usernameRepeated(userDto))
            throw new FieldAlreadyExistException("Nombre de usuario");

        if (this.userService.dniRepeated(userDto))
            throw new FieldAlreadyExistException("RUT");

        if (this.userService.mobileRepeated(userDto))
            throw new FieldAlreadyExistException("Telefono");

        if (this.userService.emailRepeated(userDto))
            throw new FieldAlreadyExistException("Email");

        if (this.userService.hasUserGreaterPrivilegesByUsername(username, new Role[]{Role.STUDENT}))
            throw new ForbiddenException();

        return this.userService.modifyUser(username, userDto).orElseThrow(() -> new DocumentNotFoundException("Usuario"));
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
    @PatchMapping(STUDENTS + PASS + PATH_USERNAME)
    public boolean resetPassStudent(@PathVariable String username, @RequestBody String resetedPass)
            throws ForbiddenException, FieldNotFoundException, DocumentNotFoundException {

        if (!this.userService.existsUserUsername(username))
            throw new FieldNotFoundException("Username");

        if (this.userService.hasUserGreaterPrivilegesByUsername(username, new Role[]{Role.STUDENT}))
            throw new ForbiddenException();

        return this.userService.resetPassUser(username, resetedPass).orElseThrow(() -> new DocumentNotFoundException("Usuario"));

    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping(STUDENTS + PATH_USERNAME)
    public UserDto deleteStudent(@PathVariable String username)
            throws ForbiddenException, FieldNotFoundException, ForbiddenDeleteException, DocumentNotFoundException {

        if (!this.userService.existsUserUsername(username))
            throw new FieldNotFoundException("Username");

        if (this.userService.isStudentInACourse(username))
            throw new ForbiddenDeleteException("Estudiante pertence a un curso.");

        if (this.userService.hasUserGreaterPrivilegesByUsername(username, new Role[]{Role.STUDENT}))
            throw new ForbiddenException();

        return this.userService.deleteUser(username).orElseThrow(() -> new DocumentNotFoundException("Usuario"));
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
    @GetMapping(STUDENTS + ID + PATH_ID)
    public UserDto getStudentById(@PathVariable String id) throws ForbiddenException, FieldNotFoundException, DocumentNotFoundException {

        if (!this.userService.existsUserId(id))
            throw new FieldNotFoundException("Id");

        return this.userService.getUserById(id).orElseThrow(() -> new DocumentNotFoundException("Estudiante"));
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
    @GetMapping(STUDENTS + USER_NAME + PATH_USERNAME)
    public UserDto getStudentByUsername(@PathVariable String username)
            throws ForbiddenException, FieldNotFoundException, DocumentNotFoundException {

        if (!this.userService.existsUserUsername(username))
            throw new FieldNotFoundException("Username");

        return this.userService.getUserByUsername(username).orElseThrow(() -> new DocumentNotFoundException("Estudiante"));
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
    @GetMapping(STUDENTS)
    public List<UserDto> getFullStudents() {

        return this.userService.getFullUsers(Role.STUDENT).orElse(Collections.emptyList());
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
    @GetMapping(STUDENTS + USER_MIN)
    public List<UserMinDto> getMinStudents() {

        return this.userService.getMinUsers(Role.STUDENT).orElse(Collections.emptyList());
    }


    // ***************ALL-USERS*******************
    @PreAuthorize("authentication.name == #username")
    @GetMapping(PATH_USERNAME)
    public UserDto getUserByUsername(@PathVariable String username) throws FieldNotFoundException, DocumentNotFoundException {

        if (!this.userService.existsUserUsername(username))
            throw new FieldNotFoundException("Username");

        return this.userService.getUserByUsername(username).orElseThrow(() -> new DocumentNotFoundException("Usuario"));
    }

    @PreAuthorize("authentication.name == #username")
    @PutMapping(PATH_USERNAME)
    public UserDto modifyUser(@PathVariable String username, @Valid @RequestBody UserDto userDto)
            throws ForbiddenException, FieldAlreadyExistException, FieldNotFoundException, DocumentNotFoundException {

        if (!this.userService.existsUserUsername(username))
            throw new FieldNotFoundException("Username");

        if (this.userService.usernameRepeated(userDto))
            throw new FieldAlreadyExistException("Nombre de usuario");

        if (this.userService.dniRepeated(userDto))
            throw new FieldAlreadyExistException("RUT");

        if (this.userService.mobileRepeated(userDto))
            throw new FieldAlreadyExistException("Telefono");

        if (this.userService.emailRepeated(userDto))
            throw new FieldAlreadyExistException("Email");

        return this.userService.modifyUser(username, userDto).orElseThrow(() -> new DocumentNotFoundException("Usuario"));
    }

    @PreAuthorize("authentication.name == #username")
    @PatchMapping(PATH_USERNAME)
    public boolean setUserPass(@PathVariable String username, @RequestBody String[] setPass)
            throws PasswordNotMatchException, FieldNotFoundException, DocumentNotFoundException {

        if (!this.userService.existsUserUsername(username))
            throw new FieldNotFoundException("Username");

        if (!this.userService.passMatch(username, setPass[0]))
            throw new PasswordNotMatchException();

        return this.userService.setPassUser(username, setPass).orElseThrow(() -> new DocumentNotFoundException("Usuario"));
    }

}
