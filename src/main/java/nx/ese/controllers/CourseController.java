package nx.ese.controllers;

import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import nx.ese.documents.core.CourseName;
import nx.ese.dtos.CourseDto;
import nx.ese.exceptions.DocumentAlreadyExistException;
import nx.ese.exceptions.DocumentNotFoundException;
import nx.ese.exceptions.FieldAlreadyExistException;
import nx.ese.exceptions.FieldInvalidException;
import nx.ese.exceptions.FieldNotFoundException;
import nx.ese.exceptions.FieldNullException;
import nx.ese.exceptions.ForbiddenDeleteException;
import nx.ese.services.CourseService;
import nx.ese.services.UserService;

@PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER') or hasRole('STUDENT')")
@RestController
@RequestMapping(CourseController.COURSE)
public class CourseController {

	public static final String COURSE = "/courses";
	public static final String NAME = "/name";
	public static final String YEAR = "/year";
	public static final String TEACHER_NAME = "/teacherName";
	public static final String STUDENT_NAME = "/studentName";

	public static final String PATH_ID = "/{id}";
	public static final String PATH_NAME = "/{name}";
	public static final String PATH_YEAR = "/{year}";
	public static final String PATH_TEACHER_NAME = "/{teacherName}";
	public static final String PATH_STUDENT_NAME = "/{studentName}";
	

	@Autowired
	private CourseService courseService;

	@Autowired
	private UserService userService;

	@PreAuthorize("hasRole('MANAGER')")
	@PostMapping()
	@ResponseStatus(HttpStatus.CREATED)
	public CourseDto createCourse(@Valid @RequestBody CourseDto courseDto) throws FieldAlreadyExistException,
			FieldInvalidException, DocumentAlreadyExistException {

		if (!this.courseService.isIdNull(courseDto))
			throw new FieldInvalidException("Id");

		if (this.courseService.nameAndYearRepeated(courseDto))
			throw new DocumentAlreadyExistException("Curso");

		if (this.courseService.chiefTeacherRepeated(courseDto))
			throw new FieldAlreadyExistException("Profesor Jefe");

		if (this.courseService.studentsRepeatedInCoursesByYear(courseDto))
			throw new FieldAlreadyExistException("Estudiante");

		return this.courseService.createCourse(courseDto);
	}

	@PreAuthorize("hasRole('MANAGER')")
	@PutMapping(PATH_ID)
	public CourseDto modifyCourse(@PathVariable String id, @Valid @RequestBody CourseDto courseDto)
			throws FieldNotFoundException, FieldAlreadyExistException,
			DocumentAlreadyExistException {

		if (this.courseService.nameAndYearRepeated(courseDto))
			throw new DocumentAlreadyExistException("Curso");

		if (this.courseService.chiefTeacherRepeated(courseDto))
			throw new FieldAlreadyExistException("Profesor Jefe");

		if (this.courseService.studentsRepeatedInCoursesByYear(courseDto))
			throw new FieldAlreadyExistException("Estudiante");

		return this.courseService.modifyCourse(id, courseDto).orElseThrow(() -> new FieldNotFoundException("Id"));
	}

	@PreAuthorize("hasRole('MANAGER')")
	@DeleteMapping(PATH_ID)
	public CourseDto deleteCourse(@PathVariable String id) throws FieldNotFoundException, ForbiddenDeleteException {

		if (this.courseService.isCourseInSubject(id))
			throw new ForbiddenDeleteException("Curso tiene asignatura(s)");

		return this.courseService.deleteCourse(id).orElseThrow(() -> new FieldNotFoundException("Id"));
	}

	@PreAuthorize("hasRole('MANAGER')")
	@GetMapping(PATH_ID)
	public CourseDto getCourseById(@PathVariable String id) throws FieldNotFoundException {
		return this.courseService.getCourseById(id).orElseThrow(() -> new FieldNotFoundException("Id"));
	}

	@PreAuthorize("hasRole('MANAGER')")
	@GetMapping(NAME + PATH_NAME + PATH_YEAR)
	public CourseDto getCourseByNameAndYear(@PathVariable CourseName name, @PathVariable String year)
			throws DocumentNotFoundException {

		return this.courseService.getCourseByNameAndYear(name, year)
				.orElseThrow(() -> new DocumentNotFoundException("Course"));
	}

	@PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
	@GetMapping(YEAR + PATH_YEAR)
	public List<CourseDto> getFullCoursesByYear(@PathVariable String year) {
		return this.courseService.getFullCoursesByYear(year).orElse(Collections.emptyList());
	}

	@PreAuthorize("hasRole('MANAGER')")
	@GetMapping(TEACHER_NAME + PATH_TEACHER_NAME + PATH_YEAR)
	public CourseDto getCourseByChiefTeacherNameAndYear(@PathVariable String teacherName, @PathVariable String year)
			throws DocumentNotFoundException, FieldNotFoundException {

		if (!this.userService.existsUserUsername(teacherName))
			throw new FieldNotFoundException("Nombre de Usuario");

		return this.courseService.getCourseByChiefTeacherNameQdsl(teacherName, year)
				.orElseThrow(() -> new DocumentNotFoundException("Curso"));
	}
	
	@PreAuthorize("hasRole('STUDENT')")
	@GetMapping(STUDENT_NAME + PATH_STUDENT_NAME + PATH_YEAR)
	public String getCourseIdByStudentAndYear(@PathVariable String 	studentName, @PathVariable String year)
			throws DocumentNotFoundException, FieldNotFoundException {

		if (!this.userService.existsUserUsername(studentName))
			throw new FieldNotFoundException("Nombre de Usuario");

		return this.courseService.getCourseIdByStudentAndYear(studentName, year)
				.orElseThrow(() -> new DocumentNotFoundException("Curso"));
	}

}
