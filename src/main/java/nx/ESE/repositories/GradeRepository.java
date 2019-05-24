package nx.ESE.repositories;

import java.util.Date;

import org.springframework.data.mongodb.repository.MongoRepository;

import nx.ESE.documents.core.Grade;
import nx.ESE.dtos.GradeDto;


public interface GradeRepository extends MongoRepository<Grade, String> {
	
	public GradeDto  findByTitleAndTypeAndStudentAndSubjectAndDate(String title, String type, String studentId, String subjectId, Date date);
	

}
