package nx.ese.repositories;

import nx.ese.TestConfig;
import nx.ese.documents.core.CourseName;
import nx.ese.documents.core.SubjectName;
import nx.ese.dtos.CourseDto;
import nx.ese.dtos.SubjectDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@TestConfig
public class SubjectRepositoryTest {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Test
    public void findByNameAndCourse() {
        Optional<CourseDto> cDto = courseRepository.findByNameAndYear(CourseName.OCTAVO_C,"2018");
        Assert.assertTrue(cDto.isPresent());

        Optional<SubjectDto> sDto = subjectRepository.findByNameAndCourse(SubjectName.MATEMATICAS,cDto.get().getId());
        Assert.assertTrue(sDto.isPresent());
        Assert.assertEquals(SubjectName.MATEMATICAS,sDto.get().getName());
        Assert.assertEquals(CourseName.OCTAVO_C,sDto.get().getCourse().getName());
        Assert.assertEquals("2018",sDto.get().getCourse().getYear());
    }

    @Test
    public void findFirstByCourse() {
        Optional<CourseDto> cDto = courseRepository.findByNameAndYear(CourseName.OCTAVO_C,"2018");
        Assert.assertTrue(cDto.isPresent());
        Optional<SubjectDto> sDto = subjectRepository.findFirstByCourse(cDto.get().getId());
        Assert.assertTrue(sDto.isPresent());

        Optional<CourseDto> cDto2 = courseRepository.findByNameAndYear(CourseName.PRIMERO_A,"2016");//not in any subject
        Assert.assertTrue(cDto2.isPresent());
        Optional<SubjectDto> sDto2 = subjectRepository.findFirstByCourse(cDto2.get().getId());
        Assert.assertFalse(sDto2.isPresent());
    }
}
