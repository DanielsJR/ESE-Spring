package nx.ese.services;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import nx.ese.controllers.AuthenticationController;
import nx.ese.documents.AuthToken;

@Service
public class RestService {

	@Autowired
	private Environment environment;

	@Value("${server.servlet.contextPath}")
	private String contextPath;

	@Getter
	@Value("${nx.test.admin.username}")
	private String adminUsername;

	@Getter
	@Value("${nx.test.admin.password}")
	private String adminPassword;

	@Getter
	@Value("${nx.test.manager.username}")
	private String managerUsername;

	@Getter
	@Value("${nx.test.manager.password}")
	private String managerPassword;

	@Getter
	@Value("${nx.test.teacher.username}")
	private String teacherUsername;

	@Getter
	@Value("${nx.test.teacher.password}")
	private String teacherPassword;

	@Getter
	@Value("${nx.test.student.username}")
	private String studentUsername;

	@Getter
	@Value("${nx.test.student.password}")
	private String studentPassword;

	@Getter
	@Value("${nx.test.megauser.username}")
	private String megauserUsername;

	@Getter
	@Value("${nx.test.megauser.password}")
	private String megauserPassword;

	@Value("${nx.test.databaseSeeder.ymlFileName}")
	private String testFile;

	@Getter
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
	
	public RestService loginUser(String username, String pass) {
		this.authToken = new RestBuilder<AuthToken>(this.port()).clazz(AuthToken.class)
				.path(contextPath)
				.path(AuthenticationController.TOKEN)
				.path(AuthenticationController.GENERATE_TOKEN)
				.basicAuth(username,pass)
//				.log()
				.post()
				.build();
		return this;
	}

	public RestService loginAdmin() {
		return this.loginUser(this.adminUsername, this.adminPassword);
	}

	public RestService loginManager() {
		return this.loginUser(this.managerUsername, this.managerPassword);
	}

	public RestService loginTeacher() {
		return this.loginUser(this.teacherUsername, this.teacherPassword);
	}

	public RestService loginStudent() {
		return this.loginUser(this.studentUsername, this.studentPassword);
	}
	
	public RestService loginMegaUser() {
		return this.loginUser(this.megauserUsername, this.megauserPassword);
	}

	public RestService logout() {
		this.authToken = null;
		return this;
	}

}
