package nx.ese.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import nx.ese.documents.Preferences;

public interface PreferencesRepository extends MongoRepository<Preferences, String> {

	// @Query("{'user':{'$ref':'user','$id':?0 } }")
	public Preferences findByUserId(String id);

	public Preferences findByThemeName(String name);

}
