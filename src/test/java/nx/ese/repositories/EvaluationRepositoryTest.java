package nx.ese.repositories;

import nx.ese.TestConfig;
import nx.ese.documents.core.CourseName;
import nx.ese.documents.core.SubjectName;
import nx.ese.dtos.validators.NxPattern;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@TestConfig
public class EvaluationRepositoryTest {

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private CourseRepository courseRepository;

    private static final Logger logger = LoggerFactory.getLogger(EvaluationRepositoryTest.class);


    @Test
    public void findAllDto() {
        Assert.assertFalse(evaluationRepository.findAll().isEmpty());
    }

    @Test
    public void findBySubjectAndDate() {
        String cId = courseRepository.findByNameAndYear(CourseName.OCTAVO_C, "2018").orElseThrow(NoSuchElementException::new).getId();
        String sId = subjectRepository.findByNameAndCourse(SubjectName.MATEMATICAS, cId).orElseThrow(NoSuchElementException::new).getId();

        LocalDate date = LocalDate.of(2018, 06, 11);
        Assert.assertTrue(evaluationRepository.findBySubjectAndDate(sId, date).isPresent());
        Assert.assertEquals("Aritmetica Básica",evaluationRepository.findBySubjectAndDate(sId, date).get().getTitle());
    }

    @Test
    public void findFirstByDate() throws ParseException {
        LocalDate date = LocalDate.of(2018, 06, 11);
        logger.warn("date1 Raw = {}", date);
        Assert.assertTrue(evaluationRepository.findFirstByDate(date).isPresent());

        LocalDate date2 = LocalDate.of(2018, 06, 20);
        logger.warn("date2 Raw = {}", date2);
        Assert.assertTrue(evaluationRepository.findFirstByDate(date2).isPresent());

        LocalDate date3 = LocalDate.of(1820, 06, 11);
        logger.warn("date3 Raw = {}", date3);
        Assert.assertFalse(evaluationRepository.findFirstByDate(date3).isPresent());
    }

}