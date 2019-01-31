package nx.ESE.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import nx.ESE.documents.core.Quiz;


public interface QuizRepository extends MongoRepository<Quiz, String> {
	

}
