package nx.ESE.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import nx.ESE.documents.core.Grade;
import nx.ESE.dtos.GradeDto;


public interface GradeRepository extends MongoRepository<Grade, String> {
	
	public GradeDto findByStudentAndEvaluation(String studentId, String evaluationId);
	
	public GradeDto findFirstByQuizStudent(String qsId);
	
	public List<GradeDto> findByEvaluation(String evaluationId);

}
