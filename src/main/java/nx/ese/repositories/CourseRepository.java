package nx.ese.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import nx.ese.documents.core.Course;
import nx.ese.documents.core.CourseName;
import nx.ese.dtos.CourseDto;

public interface CourseRepository extends MongoRepository<Course, String>, QuerydslPredicateExecutor<Course>, CourseRepositoryCustom {

    Optional <List<CourseDto>> findByName(CourseName name);

    List<CourseDto> findByYear(String year);

    @Query("{}")
    List<CourseDto> findAllDto();

    @Query("{$and:[{'name':?0},{'year':?1}]}")
    Optional<Course> findByNameAndYearOptional(CourseName name, String year);

    Optional<CourseDto> findByNameAndYear(CourseName name, String year);

    CourseDto findByChiefTeacherAndYear(String chiefTeacherId, String year);

    Optional<Course> findFirstByChiefTeacher(String chiefTeacherId);

}
