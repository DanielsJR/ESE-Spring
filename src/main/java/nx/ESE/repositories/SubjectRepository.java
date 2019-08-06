package nx.ESE.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import nx.ESE.documents.core.Subject;
import nx.ESE.documents.core.SubjectName;
import nx.ESE.dtos.SubjectDto;

public interface SubjectRepository extends MongoRepository<Subject, String> {
	
	public SubjectDto findByNameAndCourse(SubjectName name, String courseId);
	
	public SubjectDto findFirstByTeacher(String teacherId);
	
	public SubjectDto findFirstByCourse(String CourseId);

}
