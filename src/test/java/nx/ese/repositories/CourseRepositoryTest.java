package nx.ese.repositories;

import nx.ese.TestConfig;
import nx.ese.documents.User;
import nx.ese.documents.core.Course;
import nx.ese.documents.core.CourseName;
import nx.ese.dtos.CourseDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@TestConfig
public class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findAllDto() {
        Assert.assertFalse(courseRepository.findAllDto().isEmpty());
    }

    @Test
    public void findByName() {
        Optional<CourseDto> c = courseRepository.findByName(CourseName.PRIMERO_A);
        Assert.assertTrue(c.isPresent());
        Assert.assertEquals(CourseName.PRIMERO_A, c.get().getName());
    }

    @Test
    public void findByYear() {
        List<CourseDto> cs = courseRepository.findByYear("2018");
        Assert.assertTrue(cs.size() > 0);
        Assert.assertEquals(cs.get(0).getName(), CourseName.PRIMERO_A);

        Assert.assertEquals(0, courseRepository.findByYear("1820").size());
    }

    @Test
    public void findByNameAndYearOptional() {
        Optional<Course> c1 = courseRepository.findByNameAndYearOptional(CourseName.PRIMERO_A, "2018");
        Assert.assertTrue(c1.isPresent());
        c1.ifPresent(c -> Assert.assertEquals(CourseName.PRIMERO_A, c.getName()));

        Assert.assertFalse(courseRepository.findByNameAndYearOptional(CourseName.PRIMERO_A, "1820").isPresent());
    }

    @Test
    public void findByChiefTeacherAndYear() {
        User chiefTeacher = userRepository.findByUsername("u020");
        Assert.assertEquals(CourseName.PRIMERO_A, courseRepository.findByChiefTeacherAndYear(chiefTeacher.getId(), "2018").getName());

        User chiefTeacher2 = userRepository.findByUsername("u021");
        Assert.assertEquals(CourseName.SEXTO_B, courseRepository.findByChiefTeacherAndYear(chiefTeacher2.getId(), "2017").getName());

        User noChiefTeacher = userRepository.findByUsername("u010");
        Assert.assertNull(courseRepository.findByChiefTeacherAndYear(noChiefTeacher.getId(), "2018"));
    }

    @Test
    public void findByChiefTeacherAndYearOptionalDto() {
        User chiefTeacher = userRepository.findByUsername("u020");
        Optional<CourseDto> c1 = courseRepository.findByChiefTeacherAndYearOptionalDto(chiefTeacher.getId(), "2018");
        Assert.assertTrue(c1.isPresent());
        c1.ifPresent(c -> Assert.assertEquals(CourseName.PRIMERO_A, c.getName()));

        User chiefTeacher2 = userRepository.findByUsername("u021");
        Optional<CourseDto> c2 = courseRepository.findByChiefTeacherAndYearOptionalDto(chiefTeacher2.getId(), "2017");
        Assert.assertTrue(c2.isPresent());
        c2.ifPresent(c -> Assert.assertEquals(CourseName.SEXTO_B, c.getName()));

        User noChiefTeacher = userRepository.findByUsername("u010");
        Assert.assertFalse(courseRepository.findByChiefTeacherAndYearOptionalDto(noChiefTeacher.getId(), "2018").isPresent());
    }

    @Test
    public void findFirstByChiefTeacher() {
        User chiefTeacher = userRepository.findByUsername("u021");
        Assert.assertTrue(courseRepository.findFirstByChiefTeacher(chiefTeacher.getId()).isPresent());

        User noChiefTeacher = userRepository.findByUsername("u010");
        Assert.assertFalse(courseRepository.findFirstByChiefTeacher(noChiefTeacher.getId()).isPresent());
    }

    @Test
    public void findByNameAndYearAndChiefTeacher() {
        User chiefTeacher = userRepository.findByUsername("u020");
        Optional<CourseDto> c1 = courseRepository.findByNameAndYearAndChiefTeacher(CourseName.PRIMERO_A, "2018", chiefTeacher.getId());
        Assert.assertTrue(c1.isPresent());
        c1.ifPresent(c -> Assert.assertEquals("u020", c.getChiefTeacher().getUsername()));

        User chiefTeacher2 = userRepository.findByUsername("u021");
        Optional<CourseDto> c2 = courseRepository.findByNameAndYearAndChiefTeacher(CourseName.SEXTO_B, "2017", chiefTeacher2.getId());
        Assert.assertTrue(c2.isPresent());
        c2.ifPresent(c -> Assert.assertEquals("u021", c.getChiefTeacher().getUsername()));

        User noChiefTeacher = userRepository.findByUsername("u023");
        Assert.assertFalse(courseRepository.findByNameAndYearAndChiefTeacher(CourseName.PRIMERO_A, "2018", noChiefTeacher.getId()).isPresent());
    }

    @Test
    public void findByStudentAndYearOptional() {
        User student1 = userRepository.findByUsername("u031");
        Optional<Course> c1 = courseRepository.findByStudentAndYearOptional(student1.getId(), "2018");
        Assert.assertTrue(c1.isPresent());
        c1.ifPresent(course -> Assert.assertEquals(CourseName.PRIMERO_A, course.getName()));

        User student2 = userRepository.findByUsername("u033");
        Optional<Course> c2 = courseRepository.findByStudentAndYearOptional(student2.getId(), "2018");
        Assert.assertTrue(c2.isPresent());
        c2.ifPresent(course -> Assert.assertEquals(CourseName.TERCERO_C, course.getName()));

        User student3 = userRepository.findByUsername("u036");
        Optional<Course> c3 = courseRepository.findByStudentAndYearOptional(student3.getId(), "2018");
        Assert.assertTrue(c3.isPresent());
        c3.ifPresent(course -> Assert.assertEquals(CourseName.SEXTO_B, course.getName()));

        User student4 = userRepository.findByUsername("u039");
        Assert.assertFalse(courseRepository.findByStudentAndYearOptional(student4.getId(), "2018").isPresent());

        User teacher = userRepository.findByUsername("u010");
        Assert.assertFalse(courseRepository.findByStudentAndYearOptional(teacher.getId(), "2018").isPresent());
    }

}
