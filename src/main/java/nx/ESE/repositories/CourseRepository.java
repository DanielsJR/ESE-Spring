package nx.ESE.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import nx.ESE.documents.core.Course;
import nx.ESE.dtos.CourseDto;



public interface CourseRepository extends MongoRepository<Course, String> {
	
	public Course findByName(String name);
	
	public Course findByChiefTeacher(String teacher);
	
	//@Query()
	//public List<CourseDto> findCoursesFullAll();
	
	//public List<CourseDto> findByPeriodGreaterThan(Date date);
	
	
	

}
