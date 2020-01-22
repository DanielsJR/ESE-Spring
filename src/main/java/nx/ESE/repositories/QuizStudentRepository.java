package nx.ESE.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import nx.ESE.documents.core.CorrespondItem;
import nx.ESE.documents.core.IncompleteTextItem;
import nx.ESE.documents.core.MultipleSelectionItem;
import nx.ESE.documents.core.QuizStudent;
import nx.ESE.documents.core.TrueFalseItem;
import nx.ESE.dtos.QuizStudentDto;


public interface QuizStudentRepository extends MongoRepository<QuizStudent, String> {
	

	public QuizStudentDto findByCorrespondItemsAndTrueFalseItemsAndMultipleSelectionItemsAndIncompleteTextItems(
			List<CorrespondItem> correspondItems, List<TrueFalseItem> trueFalseItems,
			List<MultipleSelectionItem> multipleSelectionItems, List<IncompleteTextItem> incompleteTextItems);
	

}
