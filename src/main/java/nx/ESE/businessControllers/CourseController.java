package nx.ESE.businessControllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

import com.querydsl.core.types.Predicate;

import nx.ESE.documents.User;
import nx.ESE.documents.core.Course;
import nx.ESE.documents.core.QCourse;
import nx.ESE.dtos.CourseDto;
import nx.ESE.repositories.CourseRepository;
import nx.ESE.repositories.UserRepository;

@Controller
public class CourseController {

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private UserRepository userRepository;



	private void setCourseFromDto(Course course, CourseDto courseDto) {
		course.setName(courseDto.getName());
		course.setChiefTeacher(courseDto.getChiefTeacher());
		course.setStudents(courseDto.getStudents());
		course.setYear(courseDto.getYear());
	}
	
	// Exceptions*********************


	// CRUD******************************
	public Optional<CourseDto> getCourseById(String id) {
		Optional<Course> course = courseRepository.findById(id);
		if (course.isPresent())
			return Optional.of(new CourseDto(course.get()));
		return Optional.empty();
	}

	public List<CourseDto> getFullCourses() {
		List<CourseDto> coursesList = new ArrayList<>();
		for (Course course : courseRepository.findAll(new Sort(Sort.Direction.ASC, "name"))) {
			coursesList.add(new CourseDto(course.getId(), course.getName(), course.getChiefTeacher(),
					course.getStudents(), course.getYear()));
		}

		return coursesList;
	}
	
	public Optional<List<CourseDto>> getFullCoursesByYear(int year) {
		List<CourseDto> list = courseRepository.findByYear(year);
		if(!list.isEmpty())
		  return Optional.of(list);
		return Optional.empty();
	}

	public CourseDto createCourse(CourseDto courseDto) {
		Course course = new Course();
		this.setCourseFromDto(course, courseDto);
		courseRepository.save(course);
		return new CourseDto(courseRepository.findById(course.getId()).get());
	}

	public Optional<CourseDto> modifyCourse(String id, CourseDto courseDto) {
		Optional<Course> course = courseRepository.findById(id);
		if (course.isPresent()) {
			this.setCourseFromDto(course.get(), courseDto);
			courseRepository.save(course.get());
			return Optional.of(new CourseDto(courseRepository.findById(id).get()));
		}
		return Optional.empty();
	}

	public Optional<CourseDto> deleteCourse(String id) {
		Optional<Course> course = courseRepository.findById(id);
		if (course.isPresent()) {
			courseRepository.deleteById(id);
			return Optional.of(new CourseDto(course.get()));
		}
		return Optional.empty();

	}

	public Optional<CourseDto> getCourseByChiefTeacherName(String username, int year)  {
		Course course = courseRepository.findByChiefTeacherAndYear(userRepository.findByUsername(username).getId(), year);
		if (course != null)
			return Optional.of(new CourseDto(course));
		return Optional.empty();
	}
	
	public Optional<CourseDto> getCourseByChiefTeacherNameQdsl(String username, int year)  {
		QCourse qCourse = QCourse.course;
		Predicate predicate = qCourse.chiefTeacher.eq(userRepository.findByUsername(username)).and(qCourse.year.eq(year));
		Optional<Course> course = courseRepository.findOne(predicate);
		if (course.isPresent())
			return Optional.of(new CourseDto(course.get()));
		return Optional.empty();
	}



}
