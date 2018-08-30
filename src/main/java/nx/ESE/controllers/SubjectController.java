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

import nx.ESE.dtos.SubjectDto;
import nx.ESE.exceptions.FieldNotFoundException;
import nx.ESE.services.SubjectService;



@PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
@RestController
@RequestMapping(SubjectController.SUBJECT)
public class SubjectController {

	public static final String SUBJECT = "/subject";

	public static final String PATH_ID = "/{id}";
	public static final String PATH_USERNAME = "/{username}";

	@Autowired
	private SubjectService subjectService;
	
	// CRUD******************************
	@PreAuthorize("hasRole('MANAGER')")
	@GetMapping(PATH_ID)
	public SubjectDto getSubjectById(@PathVariable String id) throws FieldNotFoundException {

		if (!this.subjectService.existsById(id))
			throw new FieldNotFoundException("Id");

		return this.subjectService.getSubjectById(id);
	}

	@PreAuthorize("hasRole('MANAGER')")
	@GetMapping()
	public List<SubjectDto> getFullCourses() {
		return this.subjectService.getFullSubjects();
	}
	
	@PreAuthorize("hasRole('MANAGER')")
	@PostMapping()
	public SubjectDto createSubject(@Valid @RequestBody SubjectDto subjectDto) throws FieldNotFoundException {
		return this.subjectService.createSubject(subjectDto);
	}

	@PreAuthorize("hasRole('MANAGER')")
	@PutMapping(PATH_ID)
	public SubjectDto modifySubject(@PathVariable String id, @Valid @RequestBody SubjectDto subjectDto)
			throws FieldNotFoundException {

		if (!this.subjectService.existsById(id))
			throw new FieldNotFoundException("Id");

		return this.subjectService.modifySubject(id, subjectDto);
	}

	@PreAuthorize("hasRole('MANAGER')")
	@DeleteMapping(PATH_ID)
	public boolean deleteSubject(@PathVariable String id) throws FieldNotFoundException {

		if (!this.subjectService.existsById(id))
			throw new FieldNotFoundException("Id");

		return this.subjectService.deleteSubject(id);
	}
}

