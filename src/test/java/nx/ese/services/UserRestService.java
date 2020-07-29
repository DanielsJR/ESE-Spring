package nx.ese.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import nx.ese.services.data.DatabaseSeederService;
import nx.ese.utils.UserTestDto;
import org.apache.commons.lang.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;
import nx.ese.controllers.UserController;
import nx.ese.dtos.UserDto;

@Service
public class UserRestService {

    @Autowired
    private RestService restService;

    @Autowired
    private DatabaseSeederService dbSeederService;

    // managerDTOS
    @Getter
    @Setter
    private UserDto managerDto;

    @Getter
    @Setter
    @Value("${nx.test.managerDto.username}")
    private String managerDtoUsername;

    @Getter
    @Setter
    private UserDto managerDto2;

    @Getter
    @Setter
    @Value("${nx.test.managerDto2.username}")
    private String managerDto2Username;

    // teacherDTOs
    @Getter
    @Setter
    private UserDto teacherDto;

    @Getter
    @Setter
    @Value("${nx.test.teacherDto.username}")
    private String teacherDtoUsername;

    @Getter
    @Setter
    private UserDto teacherDto2;

    @Getter
    @Setter
    @Value("${nx.test.teacherDto2.username}")
    private String teacherDto2Username;

    // studentDTOs
    @Getter
    @Setter
    private UserDto studentDto;

    @Getter
    @Setter
    @Value("${nx.test.studentDto.username}")
    private String studentDtoUsername;

    @Getter
    @Setter
    private UserDto studentDto2;

    @Getter
    @Setter
    @Value("${nx.test.studentDto2.username}")
    private String studentDto2Username;

    private static final Logger logger = LoggerFactory.getLogger(UserRestService.class);

    public void createUsersDto() {
        logger.info("*****************************CREATING_USERS********************************************");
        this.managerDto = new UserDto(managerDtoUsername);
        this.managerDto2 = new UserDto(managerDto2Username);

        this.teacherDto = new UserDto(teacherDtoUsername);
        this.teacherDto2 = new UserDto(teacherDto2Username);

        this.studentDto = new UserDto(studentDtoUsername);
        this.studentDto2 = new UserDto(studentDto2Username);
        logger.info("********************************************************************************************");

    }

    // MANGER********************************************
    public void postManager() {
        managerDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserController.USERS)
                .path(UserController.MANAGERS).bearerAuth(restService.getAuthToken().getToken()).body(new UserTestDto(managerDto)).post()
                .build();
    }

    public void postManager2() {
        managerDto2 = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
                .path(UserController.USERS).path(UserController.MANAGERS)
                .bearerAuth(restService.getAuthToken().getToken()).body(new UserTestDto(managerDto2)).post().build();
    }

    public void putManager() {
        managerDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserController.USERS)
                .path(UserController.MANAGERS).path(UserController.PATH_USERNAME).expand(managerDto.getUsername())
                .bearerAuth(restService.getAuthToken().getToken()).body(managerDto).put().build();
    }

    public void patchManagerResetPass(String username, String resetedPass) {
        restService.restBuilder(new RestBuilder<>()).path(UserController.USERS).path(UserController.MANAGERS)
                .path(UserController.PASS).path(UserController.PATH_USERNAME).expand(username)
                .bearerAuth(restService.getAuthToken().getToken()).body(resetedPass).patch().build();

    }

    public void patchManagerSetRole(UserDto userDto) {
        managerDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserController.USERS)
                .path(UserController.MANAGERS).path(UserController.ROLE).path(UserController.PATH_USERNAME)
                .expand(userDto.getUsername()).bearerAuth(restService.getAuthToken().getToken()).body(userDto).patch()
                .build();
    }

    public void deleteManager(String username) {
        restService.restBuilder(new RestBuilder<>()).path(UserController.USERS).path(UserController.MANAGERS)
                .path(UserController.PATH_USERNAME).expand(username).bearerAuth(restService.getAuthToken().getToken())
                .delete().build();

    }

    public UserDto getManagerByID(String id) {
        return restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserController.USERS)
                .path(UserController.MANAGERS).path(UserController.ID).path(UserController.PATH_ID).expand(id)
                .bearerAuth(restService.getAuthToken().getToken()).get().build();
    }

    public UserDto getManagerByUsername(String username) {
        return restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserController.USERS)
                .path(UserController.MANAGERS).path(UserController.USER_NAME).path(UserController.PATH_USERNAME)
                .expand(username).bearerAuth(restService.getAuthToken().getToken()).get().build();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<UserDto> getFullManagers() {
        return restService.restBuilder(new RestBuilder<List>()).clazz(List.class).path(UserController.USERS)
                .path(UserController.MANAGERS).get().bearerAuth(restService.getAuthToken().getToken()).build();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<UserDto> getMinManagers() {
        return restService.restBuilder(new RestBuilder<List>()).clazz(List.class).path(UserController.USERS)
                .path(UserController.MANAGERS).path(UserController.USER_MIN).get()
                .bearerAuth(restService.getAuthToken().getToken()).build();

    }

    public void deleteManagers() {
        logger.info("*********************************DELETING_MANAGERS*************************************");
        this.restService.loginAdmin();

        try {
            this.deleteManager(managerDto.getUsername());
        } catch (Exception e) {
            logger.info("error: " + e.getMessage() + "DTO1 nothing to delete");
        }

        try {
            this.deleteManager(managerDto2.getUsername());
        } catch (Exception e) {
            logger.info("error: " + e.getMessage() + "DTO2 nothing to delete");
        }
        logger.info("**************************************************************************************");

    }

    // TEACHER*******************************************
    public void postTeacher() {
        teacherDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserController.USERS)
                .path(UserController.TEACHERS).bearerAuth(restService.getAuthToken().getToken()).body(new UserTestDto(teacherDto)).post()
                .build();
    }

    public void postTeacher2() {
        teacherDto2 = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
                .path(UserController.USERS).path(UserController.TEACHERS)
                .bearerAuth(restService.getAuthToken().getToken()).body(new UserTestDto(teacherDto2)).post().build();
    }

    public void putTeacher() {
        teacherDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserController.USERS)
                .path(UserController.TEACHERS).path(UserController.PATH_USERNAME).expand(teacherDto.getUsername())
                .bearerAuth(restService.getAuthToken().getToken()).body(teacherDto).put().build();
    }

    public void patchTeacherResetPass(String username, String resetedPass) {
        restService.restBuilder(new RestBuilder<>()).path(UserController.USERS).path(UserController.TEACHERS)
                .path(UserController.PASS).path(UserController.PATH_USERNAME).expand(username)
                .bearerAuth(restService.getAuthToken().getToken()).body(resetedPass).patch().build();

    }

    public void patchTeacherSetRole(UserDto userDto) {
        teacherDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserController.USERS)
                .path(UserController.TEACHERS).path(UserController.ROLE).path(UserController.PATH_USERNAME)
                .expand(userDto.getUsername()).bearerAuth(restService.getAuthToken().getToken()).body(userDto).patch()
                .build();
    }

    public void deleteTeacher(String username) {
        restService.restBuilder(new RestBuilder<>()).path(UserController.USERS).path(UserController.TEACHERS)
                .path(UserController.PATH_USERNAME).expand(username).bearerAuth(restService.getAuthToken().getToken())
                .delete().build();

    }

    public UserDto getTeacherByID(String id) {
        return restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserController.USERS)
                .path(UserController.TEACHERS).path(UserController.ID).path(UserController.PATH_ID).expand(id).get()
                .bearerAuth(restService.getAuthToken().getToken()).build();
    }

    public UserDto getTeacherByUsername(String username) {
        return restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserController.USERS)
                .path(UserController.TEACHERS).path(UserController.USER_NAME).path(UserController.PATH_USERNAME)
                .expand(username).bearerAuth(restService.getAuthToken().getToken()).get().build();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<UserDto> getFullTeachers() {
        return restService.restBuilder(new RestBuilder<List>()).clazz(List.class).path(UserController.USERS)
                .path(UserController.TEACHERS).get().bearerAuth(restService.getAuthToken().getToken()).build();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<UserDto> getMinTeachers() {
        return restService.restBuilder(new RestBuilder<List>()).clazz(List.class).path(UserController.USERS)
                .path(UserController.TEACHERS).path(UserController.USER_MIN).get()
                .bearerAuth(restService.getAuthToken().getToken()).build();

    }

    public void deleteTeachers() {
        logger.info("************************DELETING_TEACHERS**************************************************");
        this.restService.loginManager();

        try {
            this.deleteTeacher(teacherDto.getUsername());
        } catch (Exception e) {
            logger.info("error: " + e.getMessage() + "DTO1 nothing to delete");
        }

        try {
            this.deleteTeacher(teacherDto2.getUsername());
        } catch (Exception e) {
            logger.info("error: " + e.getMessage() + "DTO2 nothing to delete");
        }
        logger.info("*******************************************************************************************");
    }

    // STUDENT*******************************************
    public void postStudent() {
        studentDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserController.USERS)
                .path(UserController.STUDENTS).bearerAuth(restService.getAuthToken().getToken()).body(new UserTestDto(studentDto)).post()
                .log()
                .build();
    }

    public void postStudent2() {
        studentDto2 = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
                .path(UserController.USERS).path(UserController.STUDENTS)
                .bearerAuth(restService.getAuthToken().getToken()).body(new UserTestDto(studentDto2)).post().build();
    }

    public void putStudent() {
        studentDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserController.USERS)
                .path(UserController.STUDENTS).path(UserController.PATH_USERNAME).expand(studentDto.getUsername())
                .bearerAuth(restService.getAuthToken().getToken()).body(studentDto).put().build();
    }

    public void patchStudentResetPass(String username, String resetedPass) {
        restService.restBuilder(new RestBuilder<>()).path(UserController.USERS).path(UserController.STUDENTS)
                .path(UserController.PASS).path(UserController.PATH_USERNAME).expand(username)
                .bearerAuth(restService.getAuthToken().getToken()).body(resetedPass).patch().build();

    }

    public void deleteStudent(String username) {
        restService.restBuilder(new RestBuilder<>()).path(UserController.USERS).path(UserController.STUDENTS)
                .path(UserController.PATH_USERNAME).expand(username).bearerAuth(restService.getAuthToken().getToken())
                .delete().build();

    }

    public UserDto getStudentByID(String id) {
        return restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserController.USERS)
                .path(UserController.STUDENTS).path(UserController.ID).path(UserController.PATH_ID).expand(id).get()
                .bearerAuth(restService.getAuthToken().getToken()).build();
    }

    public UserDto getStudentByUsername(String username) {
        return restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserController.USERS)
                .path(UserController.STUDENTS).path(UserController.USER_NAME).path(UserController.PATH_USERNAME)
                .expand(username).bearerAuth(restService.getAuthToken().getToken()).get().build();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<UserDto> getFullStudents() {
        return restService.restBuilder(new RestBuilder<List>()).clazz(List.class).path(UserController.USERS)
                .path(UserController.STUDENTS).get().bearerAuth(restService.getAuthToken().getToken()).build();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<UserDto> getMinStudents() {
        return restService.restBuilder(new RestBuilder<List>()).clazz(List.class).path(UserController.USERS)
                .path(UserController.STUDENTS).path(UserController.USER_MIN).get()
                .bearerAuth(restService.getAuthToken().getToken()).build();

    }

    public void deleteStudents() {
        logger.info("*********************************DELETING_STUDENTS******************************************");
        this.restService.loginManager();

        try {
            this.deleteStudent(studentDto.getUsername());
        } catch (Exception e) {
            logger.info("error: " + e.getMessage() + "DTO1 nothing to delete");
        }

        try {
            this.deleteStudent(studentDto2.getUsername());
        } catch (Exception e) {
            logger.info("error: " + e.getMessage() + "DTO2 nothing to delete");
        }

        logger.info("******************************************************************************************");
    }

    // USER****************************************
    public UserDto getUserByUsernameSecure(String username) {
        return restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserController.USERS)
                .path(UserController.PATH_USERNAME).expand(username).get()
                .bearerAuth(restService.getAuthToken().getToken()).build();
    }

}
