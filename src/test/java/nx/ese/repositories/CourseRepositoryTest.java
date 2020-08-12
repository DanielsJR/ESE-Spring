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
        Optional<List<CourseDto>> cs = courseRepository.findByName(CourseName.PRIMERO_A);
        Assert.assertTrue(cs.isPresent());
        Assert.assertTrue(cs.get().size() > 0);
    }

    @Test
    public void findByYear() {
        List<CourseDto> cs = courseRepository.findByYear("2018");
        Assert.assertTrue(cs.size() > 0);
        Assert.assertEquals(CourseName.OCTAVO_C, cs.get(0).getName());

        Assert.assertEquals(0, courseRepository.findByYear("1820").size());
    }

    @Test
    public void findByNameAndYearOptional() {
        Optional<Course> c1 = courseRepository.findByNameAndYearOptional(CourseName.OCTAVO_C, "2018");
        Assert.assertTrue(c1.isPresent());
        c1.ifPresent(c -> Assert.assertEquals(CourseName.OCTAVO_C, c.getName()));

        Assert.assertFalse(courseRepository.findByNameAndYearOptional(CourseName.OCTAVO_C, "1820").isPresent());
    }

    @Test
    public void findByChiefTeacherAndYear() {
        User chiefTeacher = userRepository.findByUsername("u020");
        Assert.assertEquals(CourseName.OCTAVO_C, courseRepository.findByChiefTeacherAndYear(chiefTeacher.getId(), "2018").getName());

        User chiefTeacher2 = userRepository.findByUsername("u021");
        Assert.assertEquals(CourseName.SEPTIMO_C, courseRepository.findByChiefTeacherAndYear(chiefTeacher2.getId(), "2017").getName());

        User noChiefTeacher = userRepository.findByUsername("u010");
        Assert.assertNull(courseRepository.findByChiefTeacherAndYear(noChiefTeacher.getId(), "2018"));
    }

    @Test
    public void findByChiefTeacherAndYearOptionalDto() {
        User chiefTeacher = userRepository.findByUsername("u020");
        Optional<CourseDto> c1 = courseRepository.findByChiefTeacherAndYearOptionalDto(chiefTeacher.getId(), "2018");
        Assert.assertTrue(c1.isPresent());
        c1.ifPresent(c -> Assert.assertEquals(CourseName.OCTAVO_C, c.getName()));

        User chiefTeacher2 = userRepository.findByUsername("u021");
        Optional<CourseDto> c2 = courseRepository.findByChiefTeacherAndYearOptionalDto(chiefTeacher2.getId(), "2017");
        Assert.assertTrue(c2.isPresent());
        c2.ifPresent(c -> Assert.assertEquals(CourseName.SEPTIMO_C, c.getName()));

        User noChiefTeacher = userRepository.findByUsername("u010");
        Assert.assertFalse(courseRepository.findByChiefTeacherAndYearOptionalDto(noChiefTeacher.getId(), "2018").isPresent());
    }

    @Test
    public void findFirstByChiefTeacher() {
        User chiefTeacher = userRepository.findByUsername("u021");
        Assert.assertTrue(courseRepository.findFirstByChiefTeacher(chiefTeacher.getId()).isPresent());

        User noChiefTeacher = userRepository.findByUsername("u010");
        Assert.assertFalse(courseRepository.findFirstByChiefTeacher(noChiefTeacher.getId()).isPresent());

        User noChiefTeacher2 = userRepository.findByUsername("u011");
        Assert.assertFalse(courseRepository.findFirstByChiefTeacher(noChiefTeacher2.getId()).isPresent());
    }

    @Test
    public void findByNameAndYearAndChiefTeacher() {
        User chiefTeacher = userRepository.findByUsername("u020");
        Optional<CourseDto> c1 = courseRepository.findByNameAndYearAndChiefTeacher(CourseName.OCTAVO_C, "2018", chiefTeacher.getId());
        Assert.assertTrue(c1.isPresent());
        c1.ifPresent(c -> Assert.assertEquals("u020", c.getChiefTeacher().getUsername()));

        User chiefTeacher2 = userRepository.findByUsername("u021");
        Optional<CourseDto> c2 = courseRepository.findByNameAndYearAndChiefTeacher(CourseName.SEPTIMO_C, "2017", chiefTeacher2.getId());
        Assert.assertTrue(c2.isPresent());
        c2.ifPresent(c -> Assert.assertEquals("u021", c.getChiefTeacher().getUsername()));

        User noChiefTeacher = userRepository.findByUsername("u023");
        Assert.assertFalse(courseRepository.findByNameAndYearAndChiefTeacher(CourseName.PRIMERO_A, "2018", noChiefTeacher.getId()).isPresent());
    }

    @Test
    public void findByStudentAndYearOptional() {
        User student031 = userRepository.findByUsername("u031");
        Optional<Course> c1 = courseRepository.findByStudentAndYearOptional(student031.getId(), "2018");
        Assert.assertTrue(c1.isPresent());
        c1.ifPresent(course -> Assert.assertEquals(CourseName.OCTAVO_C, course.getName()));
        Assert.assertTrue(courseRepository.findByStudentAndYearOptional(student031.getId(), "2017").isPresent());

        User student033 = userRepository.findByUsername("u033");
        Optional<Course> c2 = courseRepository.findByStudentAndYearOptional(student033.getId(), "2018");
        Assert.assertTrue(c2.isPresent());
        c2.ifPresent(course -> Assert.assertEquals(CourseName.QUINTO_B, course.getName()));

        User student036 = userRepository.findByUsername("u036");
        Optional<Course> c3 = courseRepository.findByStudentAndYearOptional(student036.getId(), "2018");
        Assert.assertTrue(c3.isPresent());
        c3.ifPresent(course -> Assert.assertEquals(CourseName.SEGUNDO_A, course.getName()));

        User student039 = userRepository.findByUsername("u039");
        Assert.assertFalse(courseRepository.findByStudentAndYearOptional(student039.getId(), "2018").isPresent());

        User teacher = userRepository.findByUsername("u010");
        Assert.assertFalse(courseRepository.findByStudentAndYearOptional(teacher.getId(), "2018").isPresent());


    }

    @Test
    public void findByIdAndStudentOptional() {
        User student031 = userRepository.findByUsername("u031");
        User student034 = userRepository.findByUsername("u034");
        User teacher = userRepository.findByUsername("u010");

        Optional<Course> octavoC18 = courseRepository.findByNameAndYearOptional(CourseName.OCTAVO_C, "2018");
        Assert.assertTrue(octavoC18.isPresent());
        Optional<Course> quintoB18 = courseRepository.findByNameAndYearOptional(CourseName.QUINTO_B, "2018");
        Assert.assertTrue(quintoB18.isPresent());

        Optional<Course> course = courseRepository.findByIdAndStudentOptional(octavoC18.get().getId(), student031.getId());
        Assert.assertTrue(course.isPresent());
        course.ifPresent(c -> Assert.assertEquals(CourseName.OCTAVO_C, c.getName()));
        Assert.assertTrue(course.get().getStudents().stream().anyMatch(st-> st.getId().equals(student031.getId())));
        Assert.assertFalse(courseRepository.findByIdAndStudentOptional(octavoC18.get().getId(), student034.getId()).isPresent());

        Optional<Course> course2 = courseRepository.findByIdAndStudentOptional(quintoB18.get().getId(), student034.getId());
        Assert.assertTrue(course2.isPresent());
        course2.ifPresent(c -> Assert.assertEquals(CourseName.QUINTO_B, c.getName()));
        Assert.assertTrue(course2.get().getStudents().stream().anyMatch(st-> st.getId().equals(student034.getId())));
        Assert.assertFalse(courseRepository.findByIdAndStudentOptional(quintoB18.get().getId(), student031.getId()).isPresent());

        Assert.assertFalse(courseRepository.findByIdAndStudentOptional(octavoC18.get().getId(), teacher.getId()).isPresent());
        Assert.assertFalse(courseRepository.findByIdAndStudentOptional(quintoB18.get().getId(), teacher.getId()).isPresent());

    }
}
