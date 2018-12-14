package nx.ESE.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import nx.ESE.documents.core.Grade;


public interface GradeRepository extends MongoRepository<Grade, String> {
	

}
