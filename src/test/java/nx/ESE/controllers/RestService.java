package nx.ESE.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import nx.ESE.controllers.AuthenticationController;
import nx.ESE.documents.AuthToken;
import nx.ESE.documents.Commune;
import nx.ESE.documents.Gender;
import nx.ESE.dtos.UserDto;

@Service
public class RestService {

	@Autowired
	private Environment environment;

	@Value("${server.servlet.contextPath}")
	private String contextPath;

	@Value("${nx.test.admin.username}")
	private String adminUsername;

	@Value("${nx.test.admin.password}")
	private String adminPassword;
	
	@Value("${nx.test.manager.username}")
	private String managerUsername;

	@Value("${nx.test.manager.password}")
	private String managerPassword;
	
	@Value("${nx.test.teacher.username}")
	private String teacherUsername;

	@Value("${nx.test.teacher.password}")
	private String teacherPassword;
	
	@Value("${nx.test.student.username}")
	private String studentUsername;

	@Value("${nx.test.student.password}")
	private String studentPassword;
	
	

	@Value("${nx.test.databaseSeeder.ymlFileName}")
	private String testFile;

	private AuthToken authToken;

	private int port() {
		return Integer.parseInt(environment.getProperty("local.server.port"));
	}

	public <T> RestBuilder<T> restBuilder(RestBuilder<T> restBuilder) {
		restBuilder.port(this.port());
		restBuilder.path(contextPath);
		return restBuilder;
	}

	public RestBuilder<Object> restBuilder() {
		RestBuilder<Object> restBuilder = new RestBuilder<>(this.port());
		restBuilder.path(contextPath);
		return restBuilder;
	}

	public RestService loginAdmin() {
		this.authToken = new RestBuilder<AuthToken>(this.port()).path(contextPath).path(AuthenticationController.TOKEN)
				.path(AuthenticationController.GENERATE_TOKEN).login(this.adminUsername, this.adminPassword)
				.clazz(AuthToken.class).post().build();
		return this;
	}



	public RestService loginManager() {
		this.authToken = new RestBuilder<AuthToken>(this.port()).path(contextPath).path(AuthenticationController.TOKEN)
				.path(AuthenticationController.GENERATE_TOKEN).login(this.managerUsername, this.managerPassword).clazz(AuthToken.class).post()
				.build();
		return this;
	}


	public RestService loginTeacher() {
		this.authToken = new RestBuilder<AuthToken>(this.port()).path(contextPath).path(AuthenticationController.TOKEN)
				.path(AuthenticationController.GENERATE_TOKEN).login(this.teacherUsername, this.teacherPassword).clazz(AuthToken.class).post()
				.build();
		return this;
	}


	public RestService loginStudent() {
		this.authToken = new RestBuilder<AuthToken>(this.port()).path(contextPath).path(AuthenticationController.TOKEN)
				.path(AuthenticationController.GENERATE_TOKEN).login(this.studentUsername, this.studentPassword).clazz(AuthToken.class).post()
				.build();
		return this;
	}

	public RestService loginUser(String user, String pass) {
		this.authToken = new RestBuilder<AuthToken>(this.port()).path(contextPath).path(AuthenticationController.TOKEN)
				.path(AuthenticationController.GENERATE_TOKEN).login(user, pass).clazz(AuthToken.class).post().build();
		return this;
	}

	public RestService logout() {
		this.authToken = null;
		return this;
	}
	/*
	 * public void reLoadTestDB() {
	 * this.loginAdmin().restBuilder().path(AdminResource.ADMINS).path(
	 * AdminResource.DB).delete().build();
	 * this.loginAdmin().restBuilder().path(AdminResource.ADMINS).path(
	 * AdminResource.DB).body(testFile).post().build(); }
	 */

	public String getAdminUsername() {
		return adminUsername;
	}

	public AuthToken getAuthToken() {
		return authToken;
	}

	public void setAuthToken(AuthToken authToken) {
		this.authToken = authToken;
	}

	public String getAdminPassword() {
		return adminPassword;
	}


}
