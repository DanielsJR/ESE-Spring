package nx.ese.controllers;

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

import nx.ese.dtos.EvaluationDto;
import nx.ese.exceptions.DocumentAlreadyExistException;
import nx.ese.exceptions.DocumentNotFoundException;
import nx.ese.exceptions.FieldInvalidException;
import nx.ese.exceptions.FieldNotFoundException;
import nx.ese.services.EvaluationService;

@PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
@RestController
@RequestMapping(EvaluationController.EVALUATION)
public class EvaluationController {

	public static final String EVALUATION = "/evaluations";
	public static final String SUBJECTS = "/subjects";

	public static final String PATH_ID = "/{id}";
	public static final String PATH_USERNAME = "/{username}";

	@Autowired
	private EvaluationService evaluationService;

	// POST
	@PreAuthorize("hasRole('TEACHER')")
	@PostMapping()
	public EvaluationDto createEvaluation(@Valid @RequestBody EvaluationDto evaluationDto)
			throws FieldInvalidException, DocumentAlreadyExistException {

		if (!this.evaluationService.isIdNull(evaluationDto))
			throw new FieldInvalidException("Id");

		if (this.evaluationService.isEvaluationRepeated(evaluationDto))
			throw new DocumentAlreadyExistException("Evaluation");

		return this.evaluationService.createEvaluation(evaluationDto);
	}
	
	
	// PUT
	@PreAuthorize("hasRole('TEACHER')")
	@PutMapping(PATH_ID)
	public EvaluationDto modifyEvaluation(@PathVariable String id, @Valid @RequestBody EvaluationDto evaluationDto)
			throws FieldNotFoundException, DocumentAlreadyExistException {

		if (this.evaluationService.isEvaluationRepeated(evaluationDto))
			throw new DocumentAlreadyExistException("Evaluation");

		return this.evaluationService.modifyEvaluation(id, evaluationDto).orElseThrow(() -> new FieldNotFoundException("Id"));
	}
	
	
	// DELETE
	@PreAuthorize("hasRole('TEACHER')")
	@DeleteMapping(PATH_ID)
	public EvaluationDto deleteEvaluation(@PathVariable String id) throws FieldNotFoundException {
		return this.evaluationService.deleteEvaluation(id).orElseThrow(() -> new FieldNotFoundException("Id"));
	}
	
	// GET
	@PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
	@GetMapping(PATH_ID)
	public EvaluationDto getEvaluationById(@PathVariable String id) throws FieldNotFoundException {
		return this.evaluationService.getEvaluationById(id).orElseThrow(() -> new FieldNotFoundException("Id"));
	}

	@PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
	@GetMapping()
	public List<EvaluationDto> getFullEvaluations() throws DocumentNotFoundException {
		return this.evaluationService.getFullEvaluations().orElseThrow(() -> new DocumentNotFoundException("Evaluation(s)"));
	}
	
	@PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
	@GetMapping(SUBJECTS + PATH_ID)
	public List<EvaluationDto> getEvaluationsBySubject(@PathVariable String id) throws DocumentNotFoundException {
		return this.evaluationService.getEvaluationsBySubject(id).orElseThrow(() -> new DocumentNotFoundException("Evaluation(s)"));
	}

}
