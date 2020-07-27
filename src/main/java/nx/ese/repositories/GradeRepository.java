package nx.ese.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import nx.ese.documents.core.Grade;
import nx.ese.dtos.GradeDto;


public interface GradeRepository extends MongoRepository<Grade, String> {
	
	public GradeDto findByStudentAndEvaluation(String studentId, String evaluationId);
	
	public GradeDto findFirstByQuizStudent(String qsId);
	
	public Optional<List<GradeDto>> findByEvaluation(String evaluationId);

}
