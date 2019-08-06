package nx.ESE.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

import com.querydsl.core.types.Predicate;

import nx.ESE.documents.User;
import nx.ESE.documents.core.Course;
import nx.ESE.documents.core.CourseName;
import nx.ESE.documents.core.QCourse;
import nx.ESE.dtos.CourseDto;
import nx.ESE.dtos.UserDto;
import nx.ESE.repositories.CourseRepository;
import nx.ESE.repositories.SubjectRepository;
import nx.ESE.repositories.UserRepository;

@Controller
public class CourseService {

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SubjectRepository subjectRepository;

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
		return courseDto.getStudents().stream().map(userDto -> userRepository.findById(userDto.getId()))
				.filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
	}

	// Exceptions*********************
	public boolean isIdNull(CourseDto courseDto) {
		return courseDto.getId() == null;
	}

	public boolean nameAndYearRepeated(CourseDto courseDto) {
		CourseDto courseDB = this.courseRepository.findByNameAndYear(courseDto.getName(), courseDto.getYear());
		return courseDB != null && !courseDB.getId().equals(courseDto.getId());
	}

	public boolean chiefTeacherRepeated(CourseDto courseDto) {
		CourseDto courseDB = this.courseRepository.findByChiefTeacherAndYear(courseDto.getChiefTeacher().getId(),
				courseDto.getYear());
		return courseDB != null && !courseDB.getId().equals(courseDto.getId());
	}

	private boolean isStudentInACourse(UserDto student, CourseDto course) {
		Boolean[] found = { false };
		course.getStudents().stream().forEach(s -> {
			if (s.getId().equals(student.getId()))
				found[0] = true;
		});

		System.out.println("----------------------------------------isStudentInACourse " + found[0]);
		return found[0];
	}

	private boolean isStudentInCourses(UserDto student, String courseIdExclude, List<CourseDto> courses) {
		Boolean[] found = { false };
		courses.stream().forEach(c -> {
			if (this.isStudentInACourse(student, c) && !c.getId().equals(courseIdExclude))
				found[0] = true;

		});
		System.out.println("----------------------------------------isStudentInCourses " + found[0]);
		return found[0];

	}

	public boolean studentsRepeatedInCoursesByYear(CourseDto course) {
		Boolean[] found = { false };

		if (!course.getStudents().stream().allMatch(new HashSet<>()::add)) {
			found[0] = true;
			System.out.println("----------------------------------------students not unique fromDTO " + found[0]);
			return found[0];
		}

		List<CourseDto> courses;
		if (!courseRepository.findByYear(course.getYear()).isEmpty()) {
			courses = courseRepository.findByYear(course.getYear());
			course.getStudents().stream().forEach(s -> {
				if (this.isStudentInCourses(s, course.getId(), courses))
					found[0] = true;
			});
		}
		System.out.println("----------------------------------------studentsRepeated " + found[0]);
		return found[0];

	}

	public boolean isCourseInSubject(String courseId) {
		return subjectRepository.findFirstByCourse(courseId) != null;
	}

	// CRUD******************************
	public Optional<CourseDto> getCourseById(String id) {
		Optional<Course> course = courseRepository.findById(id);
		if (course.isPresent())
			return Optional.of(new CourseDto(course.get()));
		return Optional.empty();
	}

	public Optional<List<CourseDto>> getFullCourses() {
		List<CourseDto> list = courseRepository.findAll(new Sort(Sort.Direction.ASC, "name")).stream()
				.map(c -> new CourseDto(c)).collect(Collectors.toList());
		if (list.isEmpty())
			return Optional.empty();
		return Optional.of(list);
	}

	public Optional<List<CourseDto>> getFullCoursesByYear(String year) {
		List<CourseDto> list = courseRepository.findByYear(year).stream().parallel()
				.sorted((c1, c2) -> c1.getName().toString().compareTo(c2.getName().toString()))
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

	public Optional<CourseDto> getCourseByName(CourseName name, String year) {
		CourseDto courseDto = courseRepository.findByNameAndYear(name, year);
		if (courseDto != null)
			return Optional.of(courseDto);
		return Optional.empty();
	}

	public Optional<CourseDto> getCourseByChiefTeacherName(String username, String year) {
		CourseDto course = courseRepository.findByChiefTeacherAndYear(userRepository.findByUsername(username).getId(),
				year);
		if (course != null)
			return Optional.of(course);
		return Optional.empty();
	}

	public Optional<CourseDto> getCourseByChiefTeacherNameQdsl(String username, String year) {
		QCourse qCourse = QCourse.course;
		Predicate predicate = qCourse.chiefTeacher.eq(userRepository.findByUsername(username))
				.and(qCourse.year.eq(year));
		Optional<Course> course = courseRepository.findOne(predicate);
		if (course.isPresent())
			return Optional.of(new CourseDto(course.get()));
		return Optional.empty();
	}

}
