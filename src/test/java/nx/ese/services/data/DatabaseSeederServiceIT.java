package nx.ese.services.data;

import nx.ese.TestConfig;
import nx.ese.documents.Gender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import nx.ese.documents.User;
import nx.ese.repositories.UserRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@TestConfig
public class DatabaseSeederServiceIT {

    @Autowired
    private UserRepository userRepository;
    
    @Test
    public void testUserSeedDatabase() {
        User user = userRepository.findByUsername("u010");
        assertNotNull(user);
        assertEquals("u010", user.getUsername());
        assertEquals("Barack Hussein", user.getFirstName());
        assertEquals("e010@email.com", user.getEmail());
        assertEquals(Gender.HOMBRE, user.getGender());
        assertTrue(user.getRoles().length >= 2);
    }
    
}
