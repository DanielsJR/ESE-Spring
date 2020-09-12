package nx.ese.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import nx.ese.documents.core.CorrespondItem;
import nx.ese.documents.core.IncompleteTextItem;
import nx.ese.documents.core.MultipleSelectionItem;
import nx.ese.documents.core.QuizStudent;
import nx.ese.documents.core.TrueFalseItem;
import nx.ese.dtos.QuizStudentDto;


public interface QuizStudentRepository extends MongoRepository<QuizStudent, String> {


    QuizStudentDto findByCorrespondItemsAndTrueFalseItemsAndMultipleSelectionItemsAndIncompleteTextItems(
            List<CorrespondItem> correspondItems, List<TrueFalseItem> trueFalseItems,
            List<MultipleSelectionItem> multipleSelectionItems, List<IncompleteTextItem> incompleteTextItems);

    QuizStudentDto findByCreatedByAndCreatedDate(String studentUsername, Date date);
}
