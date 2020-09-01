package nx.ese.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import nx.ese.documents.core.Subject;
import nx.ese.documents.core.SubjectName;
import nx.ese.dtos.SubjectDto;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SubjectRepository extends MongoRepository<Subject, String>, QuerydslPredicateExecutor<Subject> {
	
	Optional<SubjectDto> findByNameAndCourse(SubjectName name, String courseId);

	Optional<SubjectDto> findFirstByTeacher(String teacherId);
	
	Optional<SubjectDto> findFirstByCourse(String courseId);

	@Query(value = "{'teacher' : ?0 }")
	List<SubjectDto> findByTeacherDto(String teacherId);

	List<Subject> findByTeacher(String teacherId);
	
	List<SubjectDto> findByCourse(String courseId);

	List<SubjectDto> findByTeacherAndCourse(String courseId);

}
