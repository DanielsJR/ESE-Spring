package nx.ESE.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


import nx.ESE.documents.core.Subject;

public interface SubjectRepository extends MongoRepository<Subject, String> {
	

}
