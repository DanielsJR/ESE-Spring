package nx.ESE.services.data;

import nx.ESE.TestConfig;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;

import nx.ESE.documents.User;
import nx.ESE.repositories.UserRepository;


@TestConfig
public class DatabaseSeederServiceIT {

    @Autowired
    private UserRepository userRepository;
    
    @Test
    public void testUserSeedDatabase() {
        // this.databaseSeederService.deleteAllAndCreateAdmin();
        // this.databaseSeederService.seedDatabase("ESE-db-test.yml");
        User user = userRepository.findByUsername("u010");
        assertNotNull(user);
        assertEquals("u010", user.getUsername());
        assertEquals("e010@email.com", user.getEmail());
        assertEquals("13755572-7", user.getDni());
        assertTrue(user.getRoles().length >= 2);
    }
    
}
