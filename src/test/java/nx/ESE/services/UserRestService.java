package nx.ESE.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import nx.ESE.controllers.UserController;
import nx.ESE.documents.Role;
import nx.ESE.dtos.UserDto;

@Service
public class UserRestService {

	@Autowired
	private RestService restService;

	// managerDTOS
	private UserDto managerDto;

	@Value("${nx.test.managerDto.username}")
	private String managerDtoUsername;

	@Value("${nx.test.managerDto.password}")
	private String managerDtoPassword;

	private UserDto managerDto2;

	@Value("${nx.test.managerDto2.username}")
	private String managerDto2Username;

	@Value("${nx.test.managerDto.password}")
	private String managerDto2Password;

	// teacherDTOs
	private UserDto teacherDto;

	@Value("${nx.test.teacherDto.username}")
	private String teacherDtoUsername;

	@Value("${nx.test.teacherDto.password}")
	private String teacherDtoPassword;

	private UserDto teacherDto2;

	@Value("${nx.test.teacherDto2.username}")
	private String teacherDto2Username;

	@Value("${nx.test.teacherDto.password}")
	private String teacherDto2Password;

	// studentDTOs
	private UserDto studentDto;

	@Value("${nx.test.studentDto.username}")
	private String studentDtoUsername;

	@Value("${nx.test.studentDto.password}")
	private String studentDtoPassword;

	private UserDto studentDto2;

	@Value("${nx.test.studentDto2.username}")
	private String studentDto2Username;

	@Value("${nx.test.studentDto.password}")
	private String studentDto2Password;

	private static final Logger logger = LoggerFactory.getLogger(UserRestService.class);

	public UserDto getManagerDto() {
		return managerDto;
	}

	public void setManagerDto(UserDto managerDto) {
		this.managerDto = managerDto;
	}

	public String getManagerDtoUsername() {
		return managerDtoUsername;
	}

	public void setManagerDtoUsername(String managerDtoUsername) {
		this.managerDtoUsername = managerDtoUsername;
	}

	public String getManagerDtoPassword() {
		return managerDtoPassword;
	}

	public void setManagerDtoPassword(String managerDtoPassword) {
		this.managerDtoPassword = managerDtoPassword;
	}

	public UserDto getManagerDto2() {
		return managerDto2;
	}

	public void setManagerDto2(UserDto managerDto2) {
		this.managerDto2 = managerDto2;
	}

	public String getManagerDto2Username() {
		return managerDto2Username;
	}

	public void setManagerDto2Username(String managerDto2Username) {
		this.managerDto2Username = managerDto2Username;
	}

	public String getManagerDto2Password() {
		return managerDto2Password;
	}

	public void setManagerDto2Password(String managerDto2Password) {
		this.managerDto2Password = managerDto2Password;
	}

	public UserDto getTeacherDto() {
		return teacherDto;
	}

	public void setTeacherDto(UserDto teacherDto) {
		this.teacherDto = teacherDto;
	}

	public String getTeacherDtoUsername() {
		return teacherDtoUsername;
	}

	public void setTeacherDtoUsername(String teacherDtoUsername) {
		this.teacherDtoUsername = teacherDtoUsername;
	}

	public String getTeacherDtoPassword() {
		return teacherDtoPassword;
	}

	public void setTeacherDtoPassword(String teacherDtoPassword) {
		this.teacherDtoPassword = teacherDtoPassword;
	}

	public UserDto getTeacherDto2() {
		return teacherDto2;
	}

	public void setTeacherDto2(UserDto teacherDto2) {
		this.teacherDto2 = teacherDto2;
	}

	public String getTeacherDto2Username() {
		return teacherDto2Username;
	}

	public void setTeacherDto2Username(String teacherDto2Username) {
		this.teacherDto2Username = teacherDto2Username;
	}

	public String getTeacherDto2Password() {
		return teacherDto2Password;
	}

	public void setTeacherDto2Password(String teacherDto2Password) {
		this.teacherDto2Password = teacherDto2Password;
	}

	public UserDto getStudentDto() {
		return studentDto;
	}

	public void setStudentDto(UserDto studentDto) {
		this.studentDto = studentDto;
	}

	public String getStudentDtoUsername() {
		return studentDtoUsername;
	}

	public void setStudentDtoUsername(String studentDtoUsername) {
		this.studentDtoUsername = studentDtoUsername;
	}

	public String getStudentDtoPassword() {
		return studentDtoPassword;
	}

	public void setStudentDtoPassword(String studentDtoPassword) {
		this.studentDtoPassword = studentDtoPassword;
	}

	public UserDto getStudentDto2() {
		return studentDto2;
	}

	public void setStudentDto2(UserDto studentDto2) {
		this.studentDto2 = studentDto2;
	}

	public String getStudentDto2Username() {
		return studentDto2Username;
	}

	public void setStudentDto2Username(String studentDto2Username) {
		this.studentDto2Username = studentDto2Username;
	}

	public String getStudentDto2Password() {
		return studentDto2Password;
	}

	public void setStudentDto2Password(String studentDto2Password) {
		this.studentDto2Password = studentDto2Password;
	}

	public void createUserDtos() {
		logger.warn(
				"*********************************CREATING_USERS**************************************************");
		
		this.managerDto = new UserDto(managerDtoUsername);
		this.managerDto2 = new UserDto(managerDto2Username);

		this.teacherDto = new UserDto(teacherDtoUsername);
		this.teacherDto2 = new UserDto(teacherDto2Username);

		this.studentDto = new UserDto(studentDtoUsername);
		this.studentDto2 = new UserDto(studentDto2Username);
		
		logger.warn(
				"****************************************************************************************************");

	}

	// MANGER********************************************
	public UserDto postManager() {
		return	managerDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserController.USERS)
				.path(UserController.MANAGERS).bearerAuth(restService.getAuthToken().getToken()).body(managerDto).post()
				.build();
	}

	public UserDto postManager2() {
		return	managerDto2 = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserController.USERS).path(UserController.MANAGERS)
				.bearerAuth(restService.getAuthToken().getToken()).body(managerDto2).post().build();
	}

	public UserDto putManager(UserDto dto) {
		return	managerDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserController.USERS)
				.path(UserController.MANAGERS).path(UserController.PATH_USERNAME).expand(dto.getUsername())
				.bearerAuth(restService.getAuthToken().getToken()).body(dto).put().build();
	}

	public UserDto getManagerByID(String id) {
		return managerDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserController.USERS).path(UserController.MANAGERS).path(UserController.ID)
				.path(UserController.PATH_ID).expand(id).bearerAuth(restService.getAuthToken().getToken()).get()
				.build();
	}

	public UserDto getManagerByUsername(String username) {
		return managerDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserController.USERS).path(UserController.MANAGERS).path(UserController.USER_NAME)
				.path(UserController.PATH_USERNAME).expand(username).bearerAuth(restService.getAuthToken().getToken())
				.get().build();
	}

	public void patchManagerResetPass(String username, String resetedPass) {
		restService.restBuilder(new RestBuilder<>()).path(UserController.USERS).path(UserController.MANAGERS)
				.path(UserController.PASS).path(UserController.PATH_USERNAME).expand(username)
				.bearerAuth(restService.getAuthToken().getToken()).body(resetedPass).patch().build();

	}

	public UserDto patchManagerSetRole(String username, Role[] roles) {
		return	managerDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserController.USERS)
				.path(UserController.MANAGERS).path(UserController.ROLE).path(UserController.PATH_USERNAME)
				.expand(username).bearerAuth(restService.getAuthToken().getToken()).body(roles).patch().build();
	}

	public void deleteManagers() {
		logger.warn(
				"*********************************DELETING_MANAGERS**************************************************");
		this.restService.loginAdmin();

		try {
			this.restService.restBuilder().path(UserController.USERS).path(UserController.MANAGERS)
					.path(UserController.PATH_USERNAME).expand(managerDto.getUsername())
					.bearerAuth(restService.getAuthToken().getToken()).delete().build();
		} catch (Exception e) {
			logger.warn("error: " + e.getMessage() + "DTO1 nothing to delete");
		}

		try {
			this.restService.restBuilder().path(UserController.USERS).path(UserController.MANAGERS)
					.path(UserController.PATH_USERNAME).expand(managerDto2.getUsername())
					.bearerAuth(restService.getAuthToken().getToken()).delete().build();
		} catch (Exception e) {
			logger.warn("error: " + e.getMessage() + "DTO2 nothing to delete");
		}
		logger.warn("***********************************************************************************************");

	}

	// TEACHER*******************************************
	public UserDto getTeacherByID(String id) {
		return teacherDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserController.USERS).path(UserController.TEACHERS).path(UserController.ID)
				.path(UserController.PATH_ID).expand(id).get().bearerAuth(restService.getAuthToken().getToken())
				.build();
	}

	public UserDto getTeacherByUsername(String username) {
		return teacherDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserController.USERS).path(UserController.TEACHERS).path(UserController.USER_NAME)
				.path(UserController.PATH_USERNAME).expand(username).bearerAuth(restService.getAuthToken().getToken())
				.get().build();
	}

	public UserDto postTeacher() {
		return	teacherDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserController.USERS)
				.path(UserController.TEACHERS).bearerAuth(restService.getAuthToken().getToken()).body(teacherDto).post()
				.build();
	}

	public UserDto postTeacher2() {
		return	teacherDto2 = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserController.USERS).path(UserController.TEACHERS)
				.bearerAuth(restService.getAuthToken().getToken()).body(teacherDto2).post().build();
	}

	public UserDto putTeacher(UserDto dto) {
		return	teacherDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserController.USERS)
				.path(UserController.TEACHERS).path(UserController.PATH_USERNAME).expand(dto.getUsername())
				.bearerAuth(restService.getAuthToken().getToken()).body(dto).put().build();
	}

	public void patchTeacherResetPass(String username, String resetedPass) {
		restService.restBuilder(new RestBuilder<>()).path(UserController.USERS).path(UserController.TEACHERS)
				.path(UserController.PASS).path(UserController.PATH_USERNAME).expand(username)
				.bearerAuth(restService.getAuthToken().getToken()).body(resetedPass).patch().build();

	}

	public UserDto patchTeacherSetRole(String username, Role[] roles) {
		return	teacherDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserController.USERS)
				.path(UserController.TEACHERS).path(UserController.ROLE).path(UserController.PATH_USERNAME)
				.expand(username).bearerAuth(restService.getAuthToken().getToken()).body(roles).patch().build();
	}

	public void deleteTeachers() {
		logger.warn(
				"*********************************DELETING_TEACHERS**************************************************");
		this.restService.loginManager();

		try {
			this.restService.restBuilder().path(UserController.USERS).path(UserController.TEACHERS)
					.path(UserController.PATH_USERNAME).expand(teacherDto.getUsername())
					.bearerAuth(restService.getAuthToken().getToken()).delete().build();
		} catch (Exception e) {
			logger.warn("error: " + e.getMessage() + "DTO1 nothing to delete");
		}

		try {
			this.restService.restBuilder().path(UserController.USERS).path(UserController.TEACHERS)
					.path(UserController.PATH_USERNAME).expand(teacherDto2.getUsername())
					.bearerAuth(restService.getAuthToken().getToken()).delete().build();
		} catch (Exception e) {
			logger.warn("error: " + e.getMessage() + "DTO2 nothing to delete");
		}
		logger.warn("***********************************************************************************************");
	}

	// STUDENT*******************************************
	public UserDto getStudentByID(String id) {
		return studentDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserController.USERS).path(UserController.STUDENTS).path(UserController.ID)
				.path(UserController.PATH_ID).expand(id).get().bearerAuth(restService.getAuthToken().getToken())
				.build();
	}

	public UserDto getStudentByUsername(String username) {
		return studentDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserController.USERS).path(UserController.STUDENTS).path(UserController.USER_NAME)
				.path(UserController.PATH_USERNAME).expand(username).bearerAuth(restService.getAuthToken().getToken())
				.get().build();
	}

	public UserDto postStudent() {
		return	studentDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserController.USERS)
				.path(UserController.STUDENTS).bearerAuth(restService.getAuthToken().getToken()).body(studentDto).post()
				.build();
	}

	public UserDto postStudent2() {
		return	studentDto2 = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class)
				.path(UserController.USERS).path(UserController.STUDENTS)
				.bearerAuth(restService.getAuthToken().getToken()).body(studentDto2).post().build();
	}

	public UserDto putStudent(UserDto dto) {
		return	studentDto = restService.restBuilder(new RestBuilder<UserDto>()).clazz(UserDto.class).path(UserController.USERS)
				.path(UserController.STUDENTS).path(UserController.PATH_USERNAME).expand(dto.getUsername())
				.bearerAuth(restService.getAuthToken().getToken()).body(dto).put().build();
	}

	public void patchStudentResetPass(String username, String resetedPass) {
		restService.restBuilder(new RestBuilder<>()).path(UserController.USERS).path(UserController.STUDENTS)
				.path(UserController.PASS).path(UserController.PATH_USERNAME).expand(username)
				.bearerAuth(restService.getAuthToken().getToken()).body(resetedPass).patch().build();

	}

	public void deleteStudents() {
		logger.warn(
				"*********************************DELETING_STUDENTS**************************************************");
		this.restService.loginManager();

		try {
			this.restService.restBuilder().path(UserController.USERS).path(UserController.STUDENTS)
					.path(UserController.PATH_USERNAME).expand(studentDto.getUsername())
					.bearerAuth(restService.getAuthToken().getToken()).delete().build();
		} catch (Exception e) {
			logger.warn("error: " + e.getMessage() + "DTO1 nothing to delete");
		}

		try {
			this.restService.restBuilder().path(UserController.USERS).path(UserController.STUDENTS)
					.path(UserController.PATH_USERNAME).expand(studentDto2.getUsername())
					.bearerAuth(restService.getAuthToken().getToken()).delete().build();
		} catch (Exception e) {
			logger.warn("error: " + e.getMessage() + "DTO2 nothing to delete");
		}

		logger.warn("***********************************************************************************************");
	}

}