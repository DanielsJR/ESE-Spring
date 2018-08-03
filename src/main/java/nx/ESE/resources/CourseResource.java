package nx.ESE.resources;

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

import nx.ESE.controllers.CourseController;
import nx.ESE.dtos.CourseDto;
import nx.ESE.resources.exceptions.CourseIdNotFoundException;

@PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
@RestController
@RequestMapping(CourseResource.COURSE)
public class CourseResource {

	public static final String COURSE = "/course";

	public static final String PATH_ID = "/{id}";
	public static final String PATH_USERNAME = "/{username}";

	@Autowired
	private CourseController courseController;

	// CRUD******************************
	@PreAuthorize("hasRole('MANAGER')")
	@GetMapping(PATH_ID)
	public CourseDto getCourseById(@PathVariable String id) throws CourseIdNotFoundException {

		return this.courseController.getCourseById(id).orElseThrow(() -> new CourseIdNotFoundException());
	}

	@PreAuthorize("hasRole('MANAGER')")
	@GetMapping()
	public List<CourseDto> getFullCourses() {
		return this.courseController.getFullCourses();
	}

	@PreAuthorize("hasRole('MANAGER')")
	@PostMapping()
	public CourseDto createCourse(@Valid @RequestBody CourseDto courseDto) {
		return this.courseController.createCourse(courseDto);
	}

	@PreAuthorize("hasRole('MANAGER')")
	@PutMapping(PATH_ID)
	public CourseDto modifyCourse(@PathVariable String id, @Valid @RequestBody CourseDto courseDto)
			throws CourseIdNotFoundException {

		return this.courseController.modifyCourse(id, courseDto).orElseThrow(() -> new CourseIdNotFoundException());
	}

	@PreAuthorize("hasRole('MANAGER')")
	@DeleteMapping(PATH_ID)
	public CourseDto deleteCourse(@PathVariable String id) throws CourseIdNotFoundException {

		return this.courseController.deleteCourse(id).orElseThrow(() -> new CourseIdNotFoundException());
	}

}
