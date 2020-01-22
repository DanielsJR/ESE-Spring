package nx.ESE.repositories;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.mongodb.repository.MongoRepository;

import nx.ESE.documents.core.CorrespondItem;
import nx.ESE.documents.core.IncompleteTextItem;
import nx.ESE.documents.core.MultipleSelectionItem;
import nx.ESE.documents.core.Quiz;
import nx.ESE.documents.core.QuizLevel;
import nx.ESE.documents.core.SubjectName;
import nx.ESE.documents.core.TrueFalseItem;
import nx.ESE.dtos.QuizDto;


public interface QuizRepository extends MongoRepository<Quiz, String> {
		

	public QuizDto findByTitleAndAuthorAndSubjectNameAndQuizLevelAndCorrespondItemsAndTrueFalseItemsAndMultipleSelectionItemsAndIncompleteTextItems(
			String title, String authorId, String subjectName, String quizLevel, List<@Valid CorrespondItem> correspondItems,
			List<@Valid TrueFalseItem> trueFalseItems, List<@Valid MultipleSelectionItem> multipleSelectionItems,
			List<@Valid IncompleteTextItem> incompleteTextItems);
	
	public List<QuizDto> findByAuthor(String authorId);
}
