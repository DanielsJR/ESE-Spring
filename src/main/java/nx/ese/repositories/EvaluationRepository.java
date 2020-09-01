package nx.ese.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import nx.ese.documents.core.Evaluation;
import nx.ese.documents.core.EvaluationType;
import nx.ese.dtos.EvaluationDto;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;


public interface EvaluationRepository extends MongoRepository<Evaluation, String>, QuerydslPredicateExecutor<Evaluation> {

    @Query(value = "{'_id' : ?0 }")
    Optional<EvaluationDto> findByIdOptionalDto(String evaluationId);

    Optional<EvaluationDto> findFirstBySubject(String subjectId);

    Optional<EvaluationDto> findFirstByQuiz(String quizId);

    Optional<EvaluationDto> findByTitleAndTypeAndSubjectAndDate(String title, EvaluationType type, String subjectId, LocalDate date);

    Optional<List<EvaluationDto>> findBySubject(String subjectId);

    Optional<EvaluationDto> findBySubjectAndDate(String subjectId, LocalDate date);

    Optional<List<EvaluationDto>> findByTypeAndSubject(EvaluationType type, String subjectId);

    Optional<List<EvaluationDto>> findBySubjectAndOpen(String subjectId, boolean open);

    Optional<List<EvaluationDto>> findByTypeAndSubjectAndOpen(EvaluationType type, String subjectId, boolean open);

    Optional<EvaluationDto> findFirstByDate(LocalDate date);


}

