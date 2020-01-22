package nx.ESE.services.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import nx.ESE.documents.Avatar;
import nx.ESE.documents.Commune;
import nx.ESE.documents.Gender;
import nx.ESE.documents.Preferences;
import nx.ESE.documents.Role;
import nx.ESE.documents.Theme;
import nx.ESE.documents.User;
import nx.ESE.repositories.CourseRepository;
import nx.ESE.repositories.EvaluationRepository;
import nx.ESE.repositories.GradeRepository;
import nx.ESE.repositories.PreferencesRepository;
import nx.ESE.repositories.QuizRepository;
import nx.ESE.repositories.QuizStudentRepository;
import nx.ESE.repositories.SubjectRepository;
import nx.ESE.repositories.UserRepository;
import nx.ESE.utils.UtilBase64Image;

@Service
public class DatabaseSeederService {

	@Value("${nx.admin.username}")
	private String username;

	@Value("${nx.admin.password}")
	private String password;

	@Value("${nx.databaseSeeder.ymlFileName:#{null}}")
	private Optional<String> ymlFileName;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PreferencesRepository preferencesRepository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private SubjectRepository subjectRepository;
	
	@Autowired
	private EvaluationRepository evaluationRepository;
	
	@Autowired
	private GradeRepository gradeRepository;
	
	@Autowired
	private QuizRepository quizRepository;
	
	@Autowired
	private QuizStudentRepository quizStudentRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	private static final Logger logger = LoggerFactory.getLogger(DatabaseSeederService.class);

	@PostConstruct
	public void seedDatabase() {
		if (ymlFileName.isPresent()) {
			this.deleteAllAndCreateAdmin();
			try {
				this.seedDatabase(ymlFileName.get());
				this.setAvatars();
			} catch (IOException e) {
				logger.error("File " + ymlFileName + " doesn't exist or can't be opened");
			}
		} else {
			this.createAdminIfNotExist();
		}
	}

	public void seedDatabase(String ymlFileName) throws IOException {
		assert ymlFileName != null && !ymlFileName.isEmpty();
		Yaml yamlParser = new Yaml(new Constructor(DatabaseGraph.class));
		InputStream input = new ClassPathResource(ymlFileName).getInputStream();
		DatabaseGraph dbGraph = (DatabaseGraph) yamlParser.load(input);

		// Save Repositories
		if (dbGraph.getUserList() != null) {
			this.userRepository.saveAll(dbGraph.getUserList());
		}

		if (dbGraph.getPreferencesList() != null) {
			this.preferencesRepository.saveAll(dbGraph.getPreferencesList());
		}

		if (dbGraph.getCoursesList() != null) {
			this.courseRepository.saveAll(dbGraph.getCoursesList());
		}

		if (dbGraph.getSubjectsList() != null) {
			this.subjectRepository.saveAll(dbGraph.getSubjectsList());
		}
	
		
		if (dbGraph.getQuizesList() != null) {
			this.quizRepository.saveAll(dbGraph.getQuizesList());
		}
		
		if (dbGraph.getQuizesStudentList() != null) {
			this.quizStudentRepository.saveAll(dbGraph.getQuizesStudentList());
		}
		
		if (dbGraph.getEvaluationsList() != null) {
			this.evaluationRepository.saveAll(dbGraph.getEvaluationsList());
		}
		
		if (dbGraph.getGradesList() != null) {
			this.gradeRepository.saveAll(dbGraph.getGradesList());
		}

		logger.warn("------------------------- Seed: " + ymlFileName + "-----------");
	}

	public void deleteAllAndCreateAdmin() {
		logger.warn("------------------------- delete DB and Create Admin-----------");
		mongoTemplate.getDb().drop();
		this.createAdminIfNotExist();
	}

	public void createAdminIfNotExist() {
		if (this.userRepository.findByUsername(this.username) == null) {
			User user = new User(this.username, this.password);
			user.setFirstName("Daniel Jesús");
			user.setLastName("Rubio Parra");
			user.setGender(Gender.HOMBRE);
			user.setAvatar(new Avatar("admin.png", "image/png", Avatar.SERVER_AVATAR_PATH + "admin.png"));
			user.setRoles(new Role[] { Role.ADMIN });
			user.setCommune(Commune.QUINTA_NORMAL);
			this.userRepository.save(user);

			Theme theme = new Theme("indigo-pink-dark", true, "#3F51B5");
			Preferences preference = new Preferences(user, theme);
			this.preferencesRepository.insert(preference);
		}
	}

	public void setAvatars() {
		logger.warn("------------------------- setting avatars-----------");
		List<User> users = userRepository.findAll();
		Iterator<User> it = users.iterator();
		while (it.hasNext()) {
			User user = it.next();
			if (user.getAvatar() != null) {
				String path = Avatar.SERVER_AVATAR_PATH + user.getAvatar().getName();
				String avatarBase64 = UtilBase64Image.encoder(path);
				user.getAvatar().setData(avatarBase64);
                userRepository.save(user); 
			}
		}
		
	}

}
