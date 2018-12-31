package nx.ESE.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
public class CourseService {

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private UserRepository userRepository;

	private Course setCourseFromDto(Course course, CourseDto courseDto) {
		course.setName(courseDto.getName());
		course.setChiefTeacher(setChiefTeacher(courseDto).get());
		course.setStudents(setStudentsList(courseDto));
		course.setYear(courseDto.getYear());
		return course;
	}

	private Optional<User> setChiefTeacher(CourseDto courseDto) {
		Optional<User> teacher = userRepository.findById(courseDto.getChiefTeacher().getId());
		if (teacher.isPresent())
			return teacher;
		return Optional.empty();
	}

	private List<User> setStudentsList(CourseDto courseDto) {
		return courseDto.getStudents()
				.stream()
				.map(userDto -> userRepository.findById(userDto.getId()))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toList());
	}

	// Exceptions*********************
	public boolean existCourseById(String id) {
		return  courseRepository.existsById(id);
	}

	// CRUD******************************
	public Optional<CourseDto> getCourseById(String id) {
		Optional<Course> course = courseRepository.findById(id);
		if (course.isPresent())
			return Optional.of(new CourseDto(course.get()));
		return Optional.empty();
	}

	public Optional<List<CourseDto>> getFullCourses() {
		List<CourseDto> list = courseRepository.findAll(new Sort(Sort.Direction.ASC, "name"))
				.stream()
				.map(c -> new CourseDto(c))
				.collect(Collectors.toList());
		if (list.isEmpty())
			return Optional.empty();
		return Optional.of(list);
	}

	public Optional<List<CourseDto>> getFullCoursesByYear(int year) {
		List<CourseDto> list = courseRepository.findByYear(year)
				.stream()
				.parallel()
				.sorted((c1,c2) -> c1.getName().toString().compareTo(c2.getName().toString()))
				.collect(Collectors.toList());
	    if (list.isEmpty())
	    	return Optional.empty();
	    return Optional.of(list);
	}

	public CourseDto createCourse(CourseDto courseDto) {
		Course course = new Course();
		courseRepository.save(this.setCourseFromDto(course, courseDto));
		return new CourseDto(courseRepository.findById(course.getId()).get());
	}

	public Optional<CourseDto> modifyCourse(String id, CourseDto courseDto) {
		Optional<Course> course = courseRepository.findById(id);
		if (course.isPresent()) {
			courseRepository.save(this.setCourseFromDto(course.get(), courseDto));
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
	
	public Optional<CourseDto> getCourseByName(String name, int year) {
		CourseDto courseDto = courseRepository.findByNameAndYear(name, year);
		if (courseDto != null)
			return Optional.of(courseDto);
		return Optional.empty();
	}
	

	public Optional<CourseDto> getCourseByChiefTeacherName(String username, int year) {
		Course course = courseRepository.findByChiefTeacherAndYear(userRepository.findByUsername(username).getId(), year);
		if (course != null)
			return Optional.of(new CourseDto(course));
		return Optional.empty();
	}

	public Optional<CourseDto> getCourseByChiefTeacherNameQdsl(String username, int year) {
		QCourse qCourse = QCourse.course;
		Predicate predicate = qCourse.chiefTeacher.eq(userRepository.findByUsername(username))
				.and(qCourse.year.eq(year));
		Optional<Course> course = courseRepository.findOne(predicate);
		if (course.isPresent())
			return Optional.of(new CourseDto(course.get()));
		return Optional.empty();
	}





}
