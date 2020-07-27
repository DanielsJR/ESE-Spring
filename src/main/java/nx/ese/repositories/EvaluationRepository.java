package nx.ese.repositories;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import nx.ese.documents.core.Evaluation;
import nx.ese.documents.core.EvaluationType;
import nx.ese.dtos.EvaluationDto;


public interface EvaluationRepository extends MongoRepository<Evaluation, String> {
	
	public EvaluationDto  findFirstBySubject(String subjectId);
	
	public EvaluationDto  findFirstByQuiz(String quizId);

	public EvaluationDto findByTitleAndTypeAndSubjectAndDate(String title, EvaluationType type, String id, Date date);
	
	public Optional<List<EvaluationDto>> findBySubject(String subjectId);
	

}
