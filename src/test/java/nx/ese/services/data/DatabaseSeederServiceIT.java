package nx.ese.services.data;

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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class DatabaseSeederServiceIT {

    @Autowired
    private UserRepository userRepository;
    
    @Test
    public void testUserSeedDatabase() {
        User user = userRepository.findByUsername("u010");
        assertNotNull(user);
        assertEquals("u010", user.getUsername());
        assertEquals("e010@email.com", user.getEmail());
        assertEquals("13755572-7", user.getDni());
        assertTrue(user.getRoles().length >= 2);
    }
    
}
