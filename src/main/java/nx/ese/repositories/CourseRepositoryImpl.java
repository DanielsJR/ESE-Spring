package nx.ese.repositories;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import nx.ese.documents.QUser;
import nx.ese.documents.User;
import nx.ese.documents.core.CourseName;
import nx.ese.documents.core.QCourse;
import nx.ese.dtos.CourseDto;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import nx.ese.documents.core.Course;

import java.util.Optional;

public class CourseRepositoryImpl implements CourseRepositoryCustom {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private final QCourse qCourse = QCourse.course;

    public Optional<CourseDto> findByChiefTeacherAndYearOptionalDto(String teacherId, String year) {
        Query query = new Query();
        query.addCriteria(Criteria.where("chiefTeacher.$id").is(new ObjectId(teacherId))
                .and("year").is(year));

        Course c = mongoTemplate.findOne(query, Course.class);
        if (c != null)
            return Optional.of(new CourseDto(c));
        return Optional.empty();
    }

    public Optional<Course> findByStudentAndYearOptional(String studentId, String year) {
        Query query = new Query();
        query.addCriteria(Criteria.where("year").is(year)
                .and("students.$id").is(new ObjectId(studentId)));

        Course c = mongoTemplate.findOne(query, Course.class);
        if (c != null)
            return Optional.of(c);
        return Optional.empty();
    }

    public Optional<CourseDto> findByNameAndYearAndChiefTeacher(CourseName courseName, String year, String teacherId) {
        Predicate predicate = qCourse.name.eq(courseName)
                .and(qCourse.year.eq(year))
                .and(qCourse.chiefTeacher.id.eq(teacherId));
        Optional<Course> c = courseRepository.findOne(predicate);
        return c.map(CourseDto::new);
    }
}
