package nx.ESE.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import nx.ESE.documents.core.Subject;
import nx.ESE.dtos.CourseDto;
import nx.ESE.dtos.SubjectDto;

public interface SubjectRepository extends MongoRepository<Subject, String> {
	
	public SubjectDto findByNameAndCourse(String name, String courseId);
	
	public SubjectDto findFirstByTeacher(String userId);

}
