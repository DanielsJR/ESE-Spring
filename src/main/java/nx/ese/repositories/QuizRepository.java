package nx.ese.repositories;

import java.util.List;

import javax.validation.Valid;

import nx.ese.documents.core.*;
import org.springframework.data.mongodb.repository.MongoRepository;

import nx.ese.dtos.QuizDto;


public interface QuizRepository extends MongoRepository<Quiz, String> {


    QuizDto findByTitleAndAuthorAndSubjectNameAndQuizLevelAndCorrespondItemsAndTrueFalseItemsAndMultipleSelectionItemsAndIncompleteTextItems(
            String title, String authorId, String subjectName, String quizLevel, List<@Valid CorrespondItem> correspondItems,
            List<@Valid TrueFalseItem> trueFalseItems, List<@Valid MultipleSelectionItem> multipleSelectionItems,
            List<@Valid IncompleteTextItem> incompleteTextItems);

    List<QuizDto> findByAuthor(String authorId);

	List<QuizDto> findByAuthorAndShared(String authorId, boolean shared);

	List<QuizDto> findBySubjectName(SubjectName subjectName);

    List<QuizDto> findByQuizLevel(QuizLevel quizLevel);

    List<QuizDto> findBySubjectNameAndQuizLevel(SubjectName subjectName, QuizLevel quizLevel);

}
