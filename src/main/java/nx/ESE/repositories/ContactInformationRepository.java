package nx.ESE.repositories;


import org.springframework.data.mongodb.repository.MongoRepository;

import nx.ESE.documents.ContactInformation;

public interface ContactInformationRepository extends MongoRepository<ContactInformation, String> {
	
	public ContactInformation findByUser(String id);

}
