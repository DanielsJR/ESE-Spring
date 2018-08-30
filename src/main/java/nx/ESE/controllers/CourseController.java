package nx.ESE.controllers;

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

import nx.ESE.dtos.CourseDto;
import nx.ESE.exceptions.DocumentNotFoundException;
import nx.ESE.exceptions.FieldNotFoundException;
import nx.ESE.services.CourseService;
import nx.ESE.services.UserService;

@PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
@RestController
@RequestMapping(CourseController.COURSE)
public class CourseController {

	public static final String COURSE = "/courses";
	public static final String YEAR = "/year";
	public static final String TEACHER_NAME = "/teacherName";

	public static final String PATH_ID = "/{id}";
	public static final String PATH_USERNAME = "/{username}";
	public static final String PATH_TEACHER_NAME = "/{teacherName}";
	public static final String PATH_YEAR = "/{year}";

	@Autowired
	private CourseService courseService;

	@Autowired
	private UserService userService;

	// CRUD******************************
	@PreAuthorize("hasRole('MANAGER')")
	@GetMapping(PATH_ID)
	public CourseDto getCourseById(@PathVariable String id) throws FieldNotFoundException {
		return this.courseService.getCourseById(id).orElseThrow(() -> new FieldNotFoundException("Id"));
	}

	@PreAuthorize("hasRole('MANAGER')")
	@GetMapping(PATH_TEACHER_NAME + PATH_YEAR)
	public CourseDto getCourseByChiefTeacherName(@PathVariable String teacherName, @PathVariable int year)
			throws DocumentNotFoundException, FieldNotFoundException {

		if (!this.userService.existsUserUsername(teacherName))
			throw new FieldNotFoundException("Username");
		
		return this.courseService.getCourseByChiefTeacherNameQdsl(teacherName, year)
				.orElseThrow(() -> new DocumentNotFoundException("Course"));
	}

	@PreAuthorize("hasRole('MANAGER')")
	@GetMapping(YEAR + PATH_YEAR)
	public List<CourseDto> getFullCoursesbyYear(@PathVariable int year) throws DocumentNotFoundException {
		return this.courseService.getFullCoursesByYear(year).orElseThrow(() -> new DocumentNotFoundException("Course"));
	}

	@PreAuthorize("hasRole('MANAGER')")
	@PostMapping()
	public CourseDto createCourse(@Valid @RequestBody CourseDto courseDto) {
		return this.courseService.createCourse(courseDto);
	}

	@PreAuthorize("hasRole('MANAGER')")
	@PutMapping(PATH_ID)
	public CourseDto modifyCourse(@PathVariable String id, @Valid @RequestBody CourseDto courseDto)
			throws FieldNotFoundException {

		return this.courseService.modifyCourse(id, courseDto).orElseThrow(() -> new FieldNotFoundException("Id"));
	}

	@PreAuthorize("hasRole('MANAGER')")
	@DeleteMapping(PATH_ID)
	public CourseDto deleteCourse(@PathVariable String id) throws FieldNotFoundException {

		return this.courseService.deleteCourse(id).orElseThrow(() -> new FieldNotFoundException("Id"));
	}

}
