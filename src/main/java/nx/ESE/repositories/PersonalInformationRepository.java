package nx.ESE.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import nx.ESE.documents.PersonalInformation;

public interface PersonalInformationRepository extends MongoRepository<PersonalInformation, String>{
	
	public PersonalInformation findByUser(String id);

}
