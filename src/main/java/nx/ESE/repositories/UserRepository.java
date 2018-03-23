package nx.ESE.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import nx.ESE.documents.User;
import nx.ESE.dtos.UserDto;
import nx.ESE.dtos.UserMinDto;

public interface UserRepository extends MongoRepository<User, String> {

	public User findByUsername(String username);
	
	public User findByDni(String dni);
	
	public User findByMobile(String mobile);
	
	public User findByEmail(String email);

	@Query("{ 'token.value' : ?0 }")
	public User findByTokenValue(String tokenValue);

	@Query(value = "{'roles' : 'STUDENT'}", fields = "{ '_id' : 1, 'firstName' : 1, 'lastName' : 1}")
	public List<UserMinDto> findStudentAll();
	
	@Query(value = "{'roles' : 'STUDENT'}")
	public List<UserDto> findStudentFullAll();

}
