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

import nx.ese.dtos.QuizDto;
import nx.ese.exceptions.DocumentAlreadyExistException;
import nx.ese.exceptions.DocumentNotFoundException;
import nx.ese.exceptions.FieldInvalidException;
import nx.ese.exceptions.FieldNotFoundException;
import nx.ese.exceptions.ForbiddenDeleteException;
import nx.ese.services.QuizService;



@PreAuthorize("hasRole('TEACHER')")
@RestController
@RequestMapping(QuizController.QUIZ)
public class QuizController {

	public static final String QUIZ = "/quiz";
	
	public static final String PATH_ID = "/{id}";
	public static final String USER = "/{user}";

	
	@Autowired
	private QuizService quizService;
	
	// POST
	@PreAuthorize("hasRole('TEACHER')")
	@PostMapping()
	public QuizDto createQuiz(@Valid @RequestBody QuizDto quizDto) throws FieldInvalidException, DocumentAlreadyExistException {
		
		if (!this.quizService.isIdNull(quizDto))
			throw new FieldInvalidException("Id");
		
		if (this.quizService.isQuizRepeated(quizDto))
			throw new DocumentAlreadyExistException("Quiz");
		
		return this.quizService.createQuiz(quizDto);
	}

	// PUT
	@PreAuthorize("hasRole('TEACHER')")
	@PutMapping(PATH_ID)
	public QuizDto modifyQuiz(@PathVariable String id, @Valid @RequestBody QuizDto quizDto)
			throws FieldNotFoundException, DocumentAlreadyExistException {
		
		if (this.quizService.isQuizRepeated(quizDto))
			throw new DocumentAlreadyExistException("Quiz");
		
		return this.quizService.modifyQuiz(id, quizDto).orElseThrow(() -> new FieldNotFoundException("Id"));
	}

	// DELETE
	@PreAuthorize("hasRole('TEACHER')")
	@DeleteMapping(PATH_ID)
	public QuizDto deleteQuiz(@PathVariable String id) throws FieldNotFoundException, ForbiddenDeleteException {
		
		if (this.quizService.isQuizInEvaluation(id))
			throw new ForbiddenDeleteException("Quiz esta en una evaluación");
		
		return this.quizService.deleteQuiz(id).orElseThrow(() -> new FieldNotFoundException("Id"));
	}
	
	// GET
	@PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
	@GetMapping(PATH_ID)
	public QuizDto getQuizById(@PathVariable String id) throws FieldNotFoundException {
		return this.quizService.getQuizById(id).orElseThrow(() -> new FieldNotFoundException("Id"));
	}

	@PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
	@GetMapping()
	public List<QuizDto> getFullQuizes() throws DocumentNotFoundException {
		return this.quizService.getFullQuizes().orElseThrow(() -> new DocumentNotFoundException("Quiz"));
	}
	
	@PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
	@GetMapping(USER + PATH_ID)
	public List<QuizDto> getFullQuizesByAuthor(@PathVariable String id) throws DocumentNotFoundException {
		return this.quizService.getFullQuizesByAuthor(id).orElseThrow(() -> new DocumentNotFoundException("Quiz"));
	}
	
}

