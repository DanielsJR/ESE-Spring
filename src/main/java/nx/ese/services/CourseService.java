package nx.ese.services;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.Predicate;

import nx.ese.documents.User;
import nx.ese.documents.core.Course;
import nx.ese.documents.core.CourseName;
import nx.ese.documents.core.QCourse;
import nx.ese.dtos.CourseDto;
import nx.ese.dtos.UserDto;
import nx.ese.repositories.CourseRepository;
import nx.ese.repositories.SubjectRepository;
import nx.ese.repositories.UserRepository;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    private Course setCourseFromDto(Course course, CourseDto courseDto) {
        course.setName(courseDto.getName());
        course.setChiefTeacher(setChiefTeacher(courseDto));
        course.setStudents(setStudentsList(courseDto));
        course.setYear(courseDto.getYear());
        return course;
    }

    private User setChiefTeacher(CourseDto courseDto) {
        return userRepository.findById(courseDto.getChiefTeacher().getId())
                .orElseThrow(() -> new NoSuchElementException("ChiefTeacher"));
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
    public boolean isIdNull(CourseDto courseDto) {
        return courseDto.getId() == null;
    }

    public boolean nameAndYearRepeated(CourseDto courseDto) {
        Optional<CourseDto> courseDB = this.courseRepository.findByNameAndYear(courseDto.getName(), courseDto.getYear());
        return courseDB.isPresent() && !courseDB.get().getId().equals(courseDto.getId());
    }

    public boolean chiefTeacherRepeated(CourseDto courseDto) {
        CourseDto courseDB = this.courseRepository.findByChiefTeacherAndYear(courseDto.getChiefTeacher().getId(),
                courseDto.getYear());
        return courseDB != null && !courseDB.getId().equals(courseDto.getId());
    }

    public boolean studentsRepeatedInDto(CourseDto course) {
        return (!course.getStudents().stream().allMatch(new HashSet<>()::add));
    }

    public boolean studentsRepeatedInCoursesByYear(CourseDto course) {
        return course.getStudents()
                .parallelStream()
                .anyMatch(st -> courseRepository.findByStudentAndYearOptional(st.getId(), course.getYear())
                        .filter(c -> !c.getId().equals(course.getId()))
                        .isPresent());
    }

    public boolean isCourseInSubject(String courseId) {
        return subjectRepository.findFirstByCourse(courseId).isPresent();
    }


    // CRUD******************************
    public CourseDto createCourse(CourseDto courseDto) {
        Course course = new Course();
        Course sc = courseRepository.insert(this.setCourseFromDto(course, courseDto));
        return new CourseDto(sc);
    }

    public Optional<CourseDto> modifyCourse(String id, CourseDto courseDto) {
        Optional<Course> course = courseRepository.findById(id);
        return course.map(value -> new CourseDto(courseRepository.save(this.setCourseFromDto(value, courseDto))));
    }

    public Optional<CourseDto> deleteCourse(String id) {
        Optional<Course> course = courseRepository.findById(id);
        if (course.isPresent()) {
            courseRepository.deleteById(id);
            return course.map(CourseDto::new);
        }
        return Optional.empty();
    }

    public Optional<CourseDto> getCourseById(String id) {
        return courseRepository.findById(id).map(CourseDto::new);
    }

    public Optional<List<CourseDto>> getFullCourses() {
        List<CourseDto> list = courseRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))
                .stream()
                .map(CourseDto::new)
                .collect(Collectors.toList());
        if (list.isEmpty())
            return Optional.empty();

        return Optional.of(list);
    }

    public Optional<List<CourseDto>> getCoursesByYear(String year) {
        List<CourseDto> list = courseRepository.findByYear(year)
                .parallelStream()
                .sorted(Comparator.comparing(c -> c.getName().getCode()))
                .collect(Collectors.toList());
        if (list.isEmpty())
            return Optional.empty();

        return Optional.of(list);
    }

    public Optional<CourseDto> getCourseByNameAndYear(CourseName name, String year) {
        return courseRepository.findByNameAndYear(name, year);
    }

    public Optional<CourseDto> getCourseByChiefTeacherAndYear(String teacherUsername, String year) {
        QCourse qCourse = QCourse.course;
        Predicate predicate = qCourse
                .chiefTeacher.eq(userRepository.findByUsername(teacherUsername))
                .and(qCourse.year.eq(year));

        return courseRepository.findOne(predicate).map(CourseDto::new);
    }

    public Optional<String> getCourseIdByStudentAndYear(String studentUsername, String year) {
        QCourse qCourse = QCourse.course;
        Predicate predicate = qCourse
                .students.contains(userRepository.findByUsername(studentUsername))
                .and(qCourse.year.eq(year));

        return courseRepository.findOne(predicate).map(Course::getId);
    }

}
