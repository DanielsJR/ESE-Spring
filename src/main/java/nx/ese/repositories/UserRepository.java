package nx.ese.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import nx.ese.documents.Role;
import nx.ese.documents.User;
import nx.ese.dtos.UserDto;
import nx.ese.dtos.UserMinDto;

public interface UserRepository extends MongoRepository<User, String> {

    @Query(fields = "{ 'password' : 0 }")
    User findByDni(String dni);

    @Query(fields = "{ 'password' : 0 }")
    User findByMobile(String mobile);

    @Query(fields = "{ 'password' : 0 }")
    User findByEmail(String email);

    User findByUsername(String username);

    @Query(value = "{'username' : ?0}", fields = "{ 'password' : 0 }")
    Optional<User> findByUsernameOptional(String username);

    @Query(value = "{'username' : ?0}", fields = "{ 'password' : 0 }")
    UserDto findByUsernameDto(String username);

    @Query(value = "{'username' : ?0}", fields = "{ 'password' : 0 }")
    Optional<UserDto> findByUsernameOptionalDto(String username);

    @Query(value = "{'roles' : ?0}", fields = "{ '_id' : 1, 'firstName' : 1, 'lastName' : 1}")
    List<UserMinDto> findUsersByRoleMin(Role role);

    @Query(value = "{'roles' : ?0}", fields = "{ 'password' : 0 }")
    List<UserDto> findUsersByRole(Role role);

    @Query(value = "{'username' : ?0, 'roles' : ?1}", fields = "{ 'password' : 0 }")
    Optional<UserDto> findUserByUsernameAndRole(String username, Role role);

    @Query(value = "{'_id' : ?0, 'roles' : ?1}", fields = "{ 'password' : 0 }")
    Optional<UserDto> findUserByIdAndRole(String id, Role role);

    @Query(fields = "{ 'password' : 0 }")
    Optional<UserDto> findUserByUsernameAndRoles(String username, Role[] roles);

}
