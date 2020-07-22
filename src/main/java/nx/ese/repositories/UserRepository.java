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
	

	
	public User findByUsername(String username);
	
	public User findByDni(String dni);
	
	public User findByMobile(String mobile);
	
	public User findByEmail(String email);
	
	public User findByRoles(Role[]  roles);
	
	@Query(value = "{'username' : ?0}")
	public Optional<User> findByUsernameOptional(String username);

	@Query(value = "{'roles' : ?0}", fields = "{ '_id' : 1, 'firstName' : 1, 'lastName' : 1}")
	public List<UserMinDto> findUsersAll(Role role);
	
	@Query(value = "{'roles' : ?0}", fields = "{ 'password' : 0 }")
	public List<UserDto> findUsersFullAll(Role role);

}
