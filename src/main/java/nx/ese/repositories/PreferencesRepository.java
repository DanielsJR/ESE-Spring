package nx.ese.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import nx.ese.documents.Preferences;

import java.util.Optional;

public interface PreferencesRepository extends MongoRepository<Preferences, String> {

    Optional<Preferences> findByUser(String id);

    Optional<Preferences> findByThemeName(String name);

}
