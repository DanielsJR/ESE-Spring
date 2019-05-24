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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;

import nx.ESE.documents.core.SubjectName;
import nx.ESE.dtos.SubjectDto;
import nx.ESE.exceptions.DocumentAlreadyExistException;
import nx.ESE.exceptions.DocumentNotFoundException;
import nx.ESE.exceptions.FieldAlreadyExistException;
import nx.ESE.exceptions.FieldInvalidException;
import nx.ESE.exceptions.FieldNotFoundException;
import nx.ESE.exceptions.FieldNullException;
import nx.ESE.exceptions.ForbiddenDeleteException;
import nx.ESE.services.SubjectService;

@PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
@RestController
@RequestMapping(SubjectController.SUBJECT)
public class SubjectController {

	public static final String SUBJECT = "/subjects";
	public static final String NAME = "/name";

	public static final String PATH_ID = "/{id}";
	public static final String PATH_NAME = "/{name}";
	public static final String PATH_COURSE_ID = "/{courseId}";

	@Autowired
	private SubjectService subjectService;

	// POST
	@PreAuthorize("hasRole('MANAGER')")
	@PostMapping()
	@ResponseStatus(HttpStatus.CREATED)
	public SubjectDto createSubject(@Valid @RequestBody SubjectDto subjectDto)
			throws FieldInvalidException, DocumentAlreadyExistException {

		if (!this.subjectService.isIdNull(subjectDto))
			throw new FieldInvalidException("Id");

		if (this.subjectService.isSubjectRepeated(subjectDto))
			throw new DocumentAlreadyExistException("Asignatura");

		return this.subjectService.createSubject(subjectDto);
	}

	// PUT
	@PreAuthorize("hasRole('MANAGER')")
	@PutMapping(PATH_ID)
	public SubjectDto modifySubject(@PathVariable String id, @Valid @RequestBody SubjectDto subjectDto)
			throws FieldNotFoundException, DocumentAlreadyExistException {

		if (this.subjectService.isSubjectRepeated(subjectDto))
			throw new DocumentAlreadyExistException("Asignatura");

		return this.subjectService.modifySubject(id, subjectDto).orElseThrow(() -> new FieldNotFoundException("Id"));
	}

	// DELETE
	@PreAuthorize("hasRole('MANAGER')")
	@DeleteMapping(PATH_ID)
	public SubjectDto deleteSubject(@PathVariable String id) throws FieldNotFoundException, ForbiddenDeleteException {

		if (this.subjectService.isSubjectInGrade(id))
			throw new ForbiddenDeleteException("Asignatura tiene nota(s)");

		return this.subjectService.deleteSubject(id).orElseThrow(() -> new FieldNotFoundException("Id"));
	}

	// GET
	@PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
	@GetMapping(PATH_ID)
	public SubjectDto getSubjectById(@PathVariable String id) throws FieldNotFoundException {
		return this.subjectService.getSubjectById(id).orElseThrow(() -> new FieldNotFoundException("Id"));
	}

	@PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
	@GetMapping(PATH_NAME + PATH_COURSE_ID)
	public SubjectDto getSubjectByNameAndCourse(@PathVariable SubjectName name, @PathVariable String courseId)
			throws DocumentNotFoundException {
		return this.subjectService.getSubjectByNameAndCourse(name, courseId)
				.orElseThrow(() -> new DocumentNotFoundException("Asignatura"));
	}

	@PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
	@GetMapping()
	public List<SubjectDto> getFullSubjects() throws DocumentNotFoundException {
		return this.subjectService.getFullSubjects().orElseThrow(() -> new DocumentNotFoundException("Subject"));
	}

}
