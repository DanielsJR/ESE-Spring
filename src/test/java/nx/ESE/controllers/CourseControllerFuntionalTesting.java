package nx.ESE.controllers;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import nx.ESE.services.CourseRestService;
import nx.ESE.services.RestService;
import nx.ESE.services.UserRestService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class CourseControllerFuntionalTesting {

	@Autowired
	private RestService restService;

	@Autowired
	private UserRestService userRestService;

	@Autowired
	private CourseRestService courseRestService;

	@Before
	public void before() {
		courseRestService.createCourseDto();
	}

	@After
	public void delete() {
		courseRestService.deleteCourses();
	}

	// POST******************************************
	@Test
	public void testPostCourse() {
		courseRestService.postCourse();
	}

	// PUT******************************************
	@Test
	public void testPutCourse() {
		courseRestService.postCourse();
		userRestService.postTeacher2();
		courseRestService.getCourseDto().setChiefTeacher(userRestService.getTeacherDto2());
		courseRestService.putCourse(courseRestService.getCourseDto().getId());
	}

	// PATCH*****************************************
	@Test
	public void testPatchCourse() {
		courseRestService.patchCourse();
	}

	// GET******************************************
	@Test
	public void testGetCourseByName() {
		courseRestService.postCourse();
		courseRestService.getCourseByName(courseRestService.getCourseDto().getName(),
				courseRestService.getCourseDto().getYear());
	}

	@Test
	public void testGetCourseById() {
		courseRestService.postCourse();
		courseRestService.getCourseById(courseRestService.getCourseDto().getId());
	}

	@Test
	public void testGetFullCoursesByYear() {
		courseRestService.postCourse();
		courseRestService.getFullCoursesByYear(2018);
		Assert.assertTrue(courseRestService.getListCoursesDto().size() > 0);
	}

}
