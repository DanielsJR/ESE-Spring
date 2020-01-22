package nx.ESE.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import nx.ESE.documents.core.Course;
import nx.ESE.documents.core.CourseName;
import nx.ESE.dtos.CourseDto;



public interface CourseRepository extends MongoRepository<Course, String>,CourseRepositoryCustom, QuerydslPredicateExecutor<Course> {
	
	public CourseDto findByName(String name);
	
	public CourseDto findByChiefTeacherAndYear(String id, String year);
	
	public List<CourseDto> findByYear(String year);


	//@Query()
	//public List<CourseDto> findAll();
	
	@Query("{$and:[{'name':?0},{'year':?1}]}") // not necessary
	public Course findByNameAndYear2(String name, String year);

	public CourseDto findByNameAndYear(CourseName name, String year);
	
	//is chiefTeacher
	public CourseDto findFirstByChiefTeacher(String TeacherId);


	

}
