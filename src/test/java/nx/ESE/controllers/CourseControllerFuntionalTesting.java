package nx.ESE.controllers;

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
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import nx.ESE.documents.core.CourseName;
import nx.ESE.services.CourseRestService;
import nx.ESE.services.HttpMatcher;
import nx.ESE.services.RestService;
import nx.ESE.services.UserRestService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class CourseControllerFuntionalTesting {
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Autowired
	private RestService restService;

	@Autowired
	private UserRestService userRestService;

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

	// POST******************************************
	@Test
	public void testPostCourse() {
		courseRestService.postCourse();
		courseRestService.postCourse2();
	}
	
	@Test
	public void testPostCourseNameNull() {
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		courseRestService.getCourseDto().setName(null);
		courseRestService.postCourse();
	}
	
	@Test
	public void testPostCourseChiefTeacherNull() {
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		courseRestService.getCourseDto().setChiefTeacher(null);
		courseRestService.postCourse();
	}
	
	@Test
	public void testPostCourseYearNull() {
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		courseRestService.getCourseDto().setYear(null);
		courseRestService.postCourse();
	}
	
	@Test
	public void testPostCourseNameRepeated() {
		courseRestService.postCourse();
	    courseRestService.getCourseDto2().setName(courseRestService.getCourseDto().getName());
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		courseRestService.postCourse2();
	}
	
	@Test
	public void testPostCourseChiefTeacherRepeated() {
		courseRestService.postCourse();
	    courseRestService.getCourseDto2().setChiefTeacher(courseRestService.getCourseDto().getChiefTeacher());
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		courseRestService.postCourse2();
	}
	
	@Test
	public void testPostCourseStudentsRepeated() {
		courseRestService.postCourse();
	    courseRestService.getCourseDto2().setStudents(courseRestService.getCourseDto().getStudents());
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		courseRestService.postCourse2();
	}

	// PUT******************************************
	@Test
	public void testPutCourse() {
		courseRestService.postCourse();
		courseRestService.getCourseDto().setChiefTeacher(courseRestService.getCourseDto2().getChiefTeacher());
		courseRestService.putCourse(courseRestService.getCourseDto().getId());
	}
	
	@Test
	public void testPutCourseNameNull() {
		courseRestService.postCourse();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		courseRestService.getCourseDto().setName(null);
		courseRestService.putCourse(courseRestService.getCourseDto().getId());
	}
	
	@Test
	public void testPutCourseChiefTeacherNull() {
		courseRestService.postCourse();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		courseRestService.getCourseDto().setChiefTeacher(null);
		courseRestService.putCourse(courseRestService.getCourseDto().getId());
	}
	
	@Test
	public void testPutCourseYearNull() {
		courseRestService.postCourse();
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		courseRestService.getCourseDto().setYear(null);
		courseRestService.putCourse(courseRestService.getCourseDto().getId());
	}
	
	@Test
	public void testPutCourseNameRepeated() {
		courseRestService.postCourse();
		courseRestService.postCourse2();
	    courseRestService.getCourseDto().setName(courseRestService.getCourseDto2().getName());
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		courseRestService.putCourse(courseRestService.getCourseDto().getId());
	}
	
	@Test
	public void testPutCourseChiefTeacherRepeated() {
		courseRestService.postCourse();
		courseRestService.postCourse2();
	    courseRestService.getCourseDto().setChiefTeacher(courseRestService.getCourseDto2().getChiefTeacher());
	    thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		courseRestService.putCourse(courseRestService.getCourseDto().getId());
	}
	
	@Test
	public void testPutCourseStudentsRepeated() {
		courseRestService.postCourse();
		courseRestService.postCourse2();
	    courseRestService.getCourseDto().setStudents(courseRestService.getCourseDto2().getStudents());
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
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
		courseRestService.getFullCoursesByYear("2018");
		Assert.assertTrue(courseRestService.getListCoursesDto().size() > 0);
	}

}
