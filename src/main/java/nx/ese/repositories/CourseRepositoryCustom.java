package nx.ese.repositories;

import nx.ese.documents.core.Course;
import nx.ese.documents.core.CourseName;
import nx.ese.dtos.CourseDto;

import java.util.Optional;


public interface CourseRepositoryCustom {

	Optional<CourseDto> findByChiefTeacherAndYearOptionalDto(String teacherId, String year);

	Optional<Course> findByStudentAndYearOptional(String studentId, String year);

	Optional<CourseDto> findByNameAndYearAndChiefTeacher(CourseName courseName, String year, String teacherId);

	Optional<Course> findByIdAndStudentOptional(String courseId, String studentId);

}
