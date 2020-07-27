package nx.ese.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import nx.ese.documents.core.Subject;
import nx.ese.documents.core.SubjectName;
import nx.ese.dtos.SubjectDto;

public interface SubjectRepository extends MongoRepository<Subject, String> {
	
	public Optional<SubjectDto> findByNameAndCourse(SubjectName name, String courseId);
	
	public SubjectDto findFirstByTeacher(String teacherId);
	
	public SubjectDto findFirstByCourse(String CourseId);

	public List<SubjectDto> findByTeacher(String id);
	
	public List<SubjectDto> findByCourse(String id);

}
