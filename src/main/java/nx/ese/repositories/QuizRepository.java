package nx.ese.repositories;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.mongodb.repository.MongoRepository;

import nx.ese.documents.core.CorrespondItem;
import nx.ese.documents.core.IncompleteTextItem;
import nx.ese.documents.core.MultipleSelectionItem;
import nx.ese.documents.core.Quiz;
import nx.ese.documents.core.TrueFalseItem;
import nx.ese.dtos.QuizDto;


public interface QuizRepository extends MongoRepository<Quiz, String> {
		

	public QuizDto findByTitleAndAuthorAndSubjectNameAndQuizLevelAndCorrespondItemsAndTrueFalseItemsAndMultipleSelectionItemsAndIncompleteTextItems(
			String title, String authorId, String subjectName, String quizLevel, List<@Valid CorrespondItem> correspondItems,
			List<@Valid TrueFalseItem> trueFalseItems, List<@Valid MultipleSelectionItem> multipleSelectionItems,
			List<@Valid IncompleteTextItem> incompleteTextItems);
	
	public List<QuizDto> findByAuthor(String authorId);
}
