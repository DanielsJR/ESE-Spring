package nx.ESE.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import nx.ESE.documents.core.Course;
import nx.ESE.dtos.CourseDto;



public interface CourseRepository extends MongoRepository<Course, String>,CourseRepositoryCustom, QuerydslPredicateExecutor<Course> {
	
	public Course findByName(String name);
	
	
	public Course findByChiefTeacherAndYear(String id, int year);
	
	public List<CourseDto> findByYear(int year);
	
	//@Query()
	//public List<CourseDto> findCoursesFullAll();
	
	


	
	

}
