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


	
	User findByDni(String dni);
	
	User findByMobile(String mobile);
	
	User findByEmail(String email);
	
	User findByRoles(Role[] roles);

	User findByUsername(String username);

	@Query(value = "{'username' : ?0}")
	Optional<User> findByUsernameOptional(String username);

	@Query(value = "{'username' : ?0}")
	UserDto findByUsernameDto(String username);

	@Query(value = "{'username' : ?0}")
	Optional<UserDto> findByUsernameOptionalDto(String username);

	@Query(value = "{'roles' : ?0}", fields = "{ '_id' : 1, 'firstName' : 1, 'lastName' : 1}")
	List<UserMinDto> findUsersMiniAll(Role role);
	
	@Query(value = "{'roles' : ?0}", fields = "{ 'password' : 0 }")
	List<UserDto> findUsersFullAll(Role role);

}
