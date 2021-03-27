package nx.ese.services.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import nx.ese.LocalDateYamlConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import nx.ese.documents.Avatar;
import nx.ese.documents.Commune;
import nx.ese.documents.Gender;
import nx.ese.documents.Preferences;
import nx.ese.documents.Role;
import nx.ese.documents.Theme;
import nx.ese.documents.User;
import nx.ese.repositories.AttendanceRepository;
import nx.ese.repositories.CourseRepository;
import nx.ese.repositories.EvaluationRepository;
import nx.ese.repositories.GradeRepository;
import nx.ese.repositories.PreferencesRepository;
import nx.ese.repositories.QuizRepository;
import nx.ese.repositories.QuizStudentRepository;
import nx.ese.repositories.SubjectRepository;
import nx.ese.repositories.UserRepository;
import nx.ese.utils.NxUtilBase64Image;
import org.yaml.snakeyaml.constructor.Constructor;

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
    private AttendanceRepository attendanceRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private Environment environment;

    private static final Logger logger = LoggerFactory.getLogger(DatabaseSeederService.class);

    @PostConstruct
    public void seedDatabase() {
        String[] profiles = this.environment.getActiveProfiles();
        if (Arrays.asList(profiles).contains("dev")) {
            logger.warn("------------------------- Profile DEV -------------------------");
            if (ymlFileName.isPresent()) {
                this.deleteAllAndCreateAdmin();
                try {
                    this.seedDatabase(ymlFileName.get());
                    this.setAvatars();
                } catch (IOException e) {
                    logger.error("File {} doesn't exist or can't be opened", ymlFileName);
                }
            } else {
                this.createAdminIfNotExist();
            }

        } else if (Arrays.asList(profiles).contains("prod")) {
            logger.warn("------------------------- Profile PROD -------------------------");
            this.createAdminIfNotExist();
        }


    }

    private void seedDatabase(String ymlFileName) throws IOException {
        assert ymlFileName != null && !ymlFileName.isEmpty();
        //Yaml yamlParser = new Yaml(new Constructor(DatabaseGraph.class));
        Yaml yamlParser = new Yaml(new LocalDateYamlConstructor());
        InputStream input = new ClassPathResource(ymlFileName).getInputStream();
        //DatabaseGraph dbGraph = yamlParser.load(input);
        DatabaseGraph dbGraph = yamlParser.loadAs(input,DatabaseGraph.class);

        logger.warn("------------------------- Seeding with: {} -------------------------", ymlFileName);

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

        if (dbGraph.getAttendancesList() != null) {
            this.attendanceRepository.saveAll(dbGraph.getAttendancesList());
        }
    }

    public void deleteAllAndCreateAdmin() {
        logger.warn("------------------------- Deleting DB -------------------------");
        mongoTemplate.getDb().drop();
        this.createAdminIfNotExist();
    }

    public void createAdminIfNotExist() {
        if (this.userRepository.findByUsername(this.username) == null) {
            logger.warn("------------------------- Creating Admin -------------------------");
            User user = new User(this.username, this.password);
            user.setFirstName("Daniel Jesús");
            user.setLastName("Rubio Parra");
            user.setGender(Gender.HOMBRE);
            user.setAvatar(new Avatar("admin.png", "image/png", Avatar.SERVER_AVATAR_PATH + "admin.png"));
            user.setRoles(new Role[]{Role.ADMIN});
            user.setCommune(Commune.QUINTA_NORMAL);
            this.userRepository.save(user);

            Theme theme = new Theme("blue-orange-dark", true, "#448aff","#ffab40");
            Preferences preference = new Preferences(user, theme);
            this.preferencesRepository.insert(preference);
        }
    }

    public void setAvatars() {
        logger.warn("------------------------- Setting Avatars -------------------------");
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getAvatar() != null) {
                String path = Avatar.SERVER_AVATAR_PATH + user.getAvatar().getName();
                String avatarBase64 = NxUtilBase64Image.encoder(path);
                user.getAvatar().setData(avatarBase64);
                userRepository.save(user);
            }
        }

    }

}
