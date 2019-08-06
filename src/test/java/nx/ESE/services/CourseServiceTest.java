package nx.ESE.services;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import nx.ESE.dtos.CourseDto;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class CourseServiceTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Autowired
	private CourseService courseService;

	@Autowired
	private CourseRestService courseRestService;
	
	@Before
	public void before() {
		courseRestService.createCoursesDto();
	}

	@After
	public void delete() {
		courseRestService.deleteCourses();
	}



}
