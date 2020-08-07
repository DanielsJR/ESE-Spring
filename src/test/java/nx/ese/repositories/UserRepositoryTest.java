package nx.ese.repositories;


import nx.ese.TestConfig;
import nx.ese.documents.Role;
import nx.ese.dtos.UserDto;
import nx.ese.dtos.UserMinDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@TestConfig
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findUsersByRole() {
        List<UserDto> usDto = userRepository.findUsersByRole(Role.MANAGER);
        Assert.assertTrue(usDto.size() > 0);
        Assert.assertNull(usDto.get(0).getPassword());
    }

    @Test
    public void findUsersByRoleMin() {
        List<UserMinDto> usDto = userRepository.findUsersByRoleMin(Role.MANAGER);
        Assert.assertTrue(usDto.size() > 0);
        Assert.assertNotNull(usDto.get(0).getFirstName());
        Assert.assertNotNull(usDto.get(0).getLastName());
    }

    @Test
    public void findByUsernameOptional() {
        Assert.assertNotNull(userRepository.findByUsernameOptional("u010"));
    }

    @Test
    public void findUserByUsernameAndRole() {
        Optional<UserDto> uDto = userRepository.findUserByUsernameAndRole("u010", Role.TEACHER);
        Assert.assertTrue(uDto.isPresent());
        Assert.assertTrue(Arrays.asList(uDto.get().getRoles()).contains(Role.MANAGER));
        Assert.assertTrue(Arrays.asList(uDto.get().getRoles()).contains(Role.TEACHER));
        Assert.assertFalse(Arrays.asList(uDto.get().getRoles()).contains(Role.STUDENT));
        Assert.assertNull(uDto.get().getPassword());
    }

    @Test
    public void findUserByIdAndRole() {
        Optional<UserDto> uDto = userRepository.findUserByIdAndRole(userRepository.findByUsername("u010").getId(), Role.TEACHER);
        Assert.assertTrue(uDto.isPresent());
        Assert.assertTrue(Arrays.asList(uDto.get().getRoles()).contains(Role.MANAGER));
        Assert.assertTrue(Arrays.asList(uDto.get().getRoles()).contains(Role.TEACHER));
        Assert.assertFalse(Arrays.asList(uDto.get().getRoles()).contains(Role.STUDENT));
        Assert.assertNull(uDto.get().getPassword());
    }

    @Test
    public void findUserByUsernameAndRoles() {
        Role[] roles = new Role[]{Role.MANAGER, Role.TEACHER};
        Optional<UserDto> uDto = userRepository.findUserByUsernameAndRoles("u010", roles);
        Assert.assertTrue(uDto.isPresent());
        Assert.assertTrue(Arrays.asList(uDto.get().getRoles()).containsAll(Arrays.asList(roles)));
        Assert.assertTrue(Arrays.asList(uDto.get().getRoles()).contains(Role.MANAGER));
        Assert.assertTrue(Arrays.asList(uDto.get().getRoles()).contains(Role.TEACHER));
        Assert.assertFalse(Arrays.asList(uDto.get().getRoles()).contains(Role.STUDENT));
        Assert.assertNull(uDto.get().getPassword());
    }

    @Test
    public void findByUsernameDto() {
        UserDto uDto = userRepository.findByUsernameDto("u010");
        Assert.assertNotNull(uDto);
        Assert.assertEquals("u010",uDto.getUsername());
        Assert.assertNull(uDto.getPassword());
    }

    @Test
    public void findByUsernameOptionalDto() {
        Optional<UserDto> uDto = userRepository.findByUsernameOptionalDto("u010");
        Assert.assertTrue(uDto.isPresent());
        Assert.assertEquals("u010",uDto.get().getUsername());
        Assert.assertNull(uDto.get().getPassword());
    }
}
