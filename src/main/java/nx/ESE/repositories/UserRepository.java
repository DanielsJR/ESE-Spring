package nx.ESE.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import nx.ESE.documents.User;
import nx.ESE.dtos.UserDto;

public interface UserRepository extends MongoRepository<User, String> {

	public User findByUsername(String Username);

	@Query("{ 'token.value' : ?0 }")
	public User findByTokenValue(String tokenValue);

	@Query(value = "{'roles' : 'STUDENT'}", fields = "{ '_id' : 0, 'username' : 1}")
	public List<UserDto> findStudentAll();

}
