package nx.ESE.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import nx.ESE.documents.core.QuizStudent;


public interface QuizStudentRepository extends MongoRepository<QuizStudent, String> {
	

}
