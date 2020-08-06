package nx.ese.repositories;


import nx.ese.TestConfig;
import nx.ese.documents.Role;
import nx.ese.documents.User;
import nx.ese.dtos.UserDto;
import nx.ese.dtos.UserMinDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@TestConfig
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findUsersFullAll() {
        List<UserDto> usDto = userRepository.findUsersFullAll(Role.MANAGER);
        Assert.assertTrue(usDto.size() > 0);
        Assert.assertNull(usDto.get(0).getPassword());
    }

    @Test
    public void findUsersMiniAll() {
        List<UserMinDto> usDto = userRepository.findUsersMiniAll(Role.MANAGER);
        Assert.assertTrue(usDto.size() > 0);
        Assert.assertNotNull(usDto.get(0).getFirstName());
        Assert.assertNotNull(usDto.get(0).getLastName());
    }

    @Test
    public void findByUsernameOptional() {
        Assert.assertNotNull(userRepository.findByUsernameOptional("u010"));
    }
}
