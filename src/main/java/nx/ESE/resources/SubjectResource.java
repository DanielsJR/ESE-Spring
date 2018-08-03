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


import nx.ESE.controllers.SubjectController;
import nx.ESE.dtos.SubjectDto;

import nx.ESE.resources.exceptions.SubjectIdNotFoundException;

@PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
@RestController
@RequestMapping(SubjectResource.SUBJECT)
public class SubjectResource {

	public static final String SUBJECT = "/subject";

	public static final String PATH_ID = "/{id}";
	public static final String PATH_USERNAME = "/{username}";

	@Autowired
	private SubjectController subjectController;
	
	// CRUD******************************
	@PreAuthorize("hasRole('MANAGER')")
	@GetMapping(PATH_ID)
	public SubjectDto getSubjectById(@PathVariable String id) throws SubjectIdNotFoundException {

		if (!this.subjectController.isPresentSubjectId(id))
			throw new SubjectIdNotFoundException();

		return this.subjectController.getSubjectById(id);
	}

	@PreAuthorize("hasRole('MANAGER')")
	@GetMapping()
	public List<SubjectDto> getFullCourses() {
		return this.subjectController.getFullSubjects();
	}
	
	@PreAuthorize("hasRole('MANAGER')")
	@PostMapping()
	public SubjectDto createSubject(@Valid @RequestBody SubjectDto subjectDto) throws SubjectIdNotFoundException {

		if (!this.subjectController.isPresentSubjectId(subjectDto.getId()))
			throw new SubjectIdNotFoundException();

		return this.subjectController.createSubject(subjectDto);
	}

	@PreAuthorize("hasRole('MANAGER')")
	@PutMapping(PATH_ID)
	public SubjectDto modifySubject(@PathVariable String id, @Valid @RequestBody SubjectDto subjectDto)
			throws SubjectIdNotFoundException {

		if (!this.subjectController.isPresentSubjectId(id))
			throw new SubjectIdNotFoundException();

		return this.subjectController.modifySubject(id, subjectDto);
	}

	@PreAuthorize("hasRole('MANAGER')")
	@DeleteMapping(PATH_ID)
	public boolean deleteSubject(@PathVariable String id) throws SubjectIdNotFoundException {

		if (!this.subjectController.isPresentSubjectId(id))
			throw new SubjectIdNotFoundException();

		return this.subjectController.deleteSubject(id);
	}
}

