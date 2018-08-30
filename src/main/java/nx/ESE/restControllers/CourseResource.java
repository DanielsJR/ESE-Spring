package nx.ESE.restControllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nx.ESE.businessControllers.CourseController;
import nx.ESE.businessControllers.UserController;
import nx.ESE.dtos.CourseDto;
import nx.ESE.exceptions.DocumentNotFoundException;
import nx.ESE.exceptions.FieldNotFoundException;

@PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
@RestController
@RequestMapping(CourseResource.COURSE)
public class CourseResource {

	public static final String COURSE = "/courses";
	public static final String YEAR = "/year";
	public static final String TEACHER_NAME = "/teacherName";

	public static final String PATH_ID = "/{id}";
	public static final String PATH_USERNAME = "/{username}";
	public static final String PATH_TEACHER_NAME = "/{teacherName}";
	public static final String PATH_YEAR = "/{year}";

	@Autowired
	private CourseController courseController;

	@Autowired
	private UserController userController;

	// CRUD******************************
	@PreAuthorize("hasRole('MANAGER')")
	@GetMapping(PATH_ID)
	public CourseDto getCourseById(@PathVariable String id) throws FieldNotFoundException {
		return this.courseController.getCourseById(id).orElseThrow(() -> new FieldNotFoundException("Id"));
	}

	@PreAuthorize("hasRole('MANAGER')")
	@GetMapping(PATH_TEACHER_NAME + PATH_YEAR)
	public CourseDto getCourseByChiefTeacherName(@PathVariable String teacherName, @PathVariable int year)
			throws DocumentNotFoundException, FieldNotFoundException {

		if (!this.userController.existsUserUsername(teacherName))
			throw new FieldNotFoundException("Username");
		
		return this.courseController.getCourseByChiefTeacherNameQdsl(teacherName, year)
				.orElseThrow(() -> new DocumentNotFoundException("Course"));
	}

	@PreAuthorize("hasRole('MANAGER')")
	@GetMapping(YEAR + PATH_YEAR)
	public List<CourseDto> getFullCoursesbyYear(@PathVariable int year) throws DocumentNotFoundException {
		return this.courseController.getFullCoursesByYear(year).orElseThrow(() -> new DocumentNotFoundException("Course"));
	}

	@PreAuthorize("hasRole('MANAGER')")
	@PostMapping()
	public CourseDto createCourse(@Valid @RequestBody CourseDto courseDto) {
		return this.courseController.createCourse(courseDto);
	}

	@PreAuthorize("hasRole('MANAGER')")
	@PutMapping(PATH_ID)
	public CourseDto modifyCourse(@PathVariable String id, @Valid @RequestBody CourseDto courseDto)
			throws FieldNotFoundException {

		return this.courseController.modifyCourse(id, courseDto).orElseThrow(() -> new FieldNotFoundException("Id"));
	}

	@PreAuthorize("hasRole('MANAGER')")
	@DeleteMapping(PATH_ID)
	public CourseDto deleteCourse(@PathVariable String id) throws FieldNotFoundException {

		return this.courseController.deleteCourse(id).orElseThrow(() -> new FieldNotFoundException("Id"));
	}

}
