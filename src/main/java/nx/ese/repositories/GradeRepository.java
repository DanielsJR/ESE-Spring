package nx.ese.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import nx.ese.documents.core.Grade;
import nx.ese.dtos.GradeDto;
import org.springframework.data.mongodb.repository.Query;


public interface GradeRepository extends MongoRepository<Grade, String> {

    @Query(value = "{'_id' : ?0,}")
    Optional<GradeDto> findByIdOptionalDto(String gradeId);

    GradeDto findByStudentAndEvaluation(String studentId, String evaluationId);

    GradeDto findFirstByQuizStudent(String quizId);

    Optional<List<GradeDto>> findByEvaluation(String evaluationId);

    Optional<List<GradeDto>> findByStudent(String studentId);

}
