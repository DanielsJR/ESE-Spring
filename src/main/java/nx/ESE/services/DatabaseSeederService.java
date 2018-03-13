package nx.ESE.services;

import javax.annotation.PostConstruct;

//import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import nx.ESE.documents.Role;
import nx.ESE.documents.User;
import nx.ESE.repositories.UserRepository;



@Service
public class DatabaseSeederService {

	@Value("${nx.admin.username}")
	private String username;

	@Value("${nx.admin.password}")
	private String password;

	//@Value("${nx.databaseSeeder.ymlFileName:#{null}}")
	//private Optional<String> ymlFileName;

	@Autowired
	private UserRepository userRepository;

	/*
	 * @PostConstruct public void seedDatabase() { if (ymlFileName.isPresent())
	 * { this.deleteAllAndCreateAdmin(); try {
	 * this.seedDatabase(ymlFileName.get()); } catch (IOException e) {
	 * //Logger.getLogger(this.getClass()).error("File " + ymlFileName +
	 * " doesn't exist or can't be opened"); } } else {
	 * this.createAdminIfNotExist(); } }
	 */

	@PostConstruct
	public void seedDatabase() {
		this.createAdminIfNotExist();
	}

	/*
	 * public void seedDatabase(String ymlFileName) throws IOException { assert
	 * ymlFileName != null && !ymlFileName.isEmpty(); Yaml yamlParser = new
	 * Yaml(new Constructor(DatabaseGraph.class)); InputStream input = new
	 * ClassPathResource(ymlFileName).getInputStream(); DatabaseGraph dbGraph =
	 * (DatabaseGraph) yamlParser.load(input);
	 * 
	 * // Save Repositories //
	 * ----------------------------------------------------- if
	 * (dbGraph.getUserList() != null) {
	 * this.userRepository.saveAll(dbGraph.getUserList()); }
	 * 
	 * //
	 * -----------------------------------------------------------------------
	 * 
	 * //Logger.getLogger(this.getClass()).
	 * warn("------------------------- Seed: " + ymlFileName + "-----------"); }
	 */

	public void deleteAllAndCreateAdmin() {
		// Logger.getLogger(this.getClass()).warn("-------------------------
		// delete All And Create Admin-----------");
		// Delete Repositories
		// -----------------------------------------------------
		this.userRepository.deleteAll();
		this.createAdminIfNotExist();
		// -----------------------------------------------------------------------
	}

	public void createAdminIfNotExist() {
		if (this.userRepository.findByUsername(this.username) == null) {
			User user = new User(this.username, this.password);
			user.setRoles(new Role[] { Role.ADMIN });
			this.userRepository.save(user);
		}
	}

}
