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

import nx.ESE.dtos.QuizDto;
import nx.ESE.exceptions.DocumentNotFoundException;
import nx.ESE.exceptions.FieldInvalidException;
import nx.ESE.exceptions.FieldNotFoundException;
import nx.ESE.exceptions.FieldNullException;
import nx.ESE.services.QuizService;



@PreAuthorize("hasRole('TEACHER')")
@RestController
@RequestMapping(QuizController.QUIZ)
public class QuizController {

	public static final String QUIZ = "/quizes";
	
	public static final String PATH_ID = "/{id}";
	public static final String PATH_USERNAME = "/{username}";

	
	@Autowired
	private QuizService quizService;
	
	// CRUD******************************
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
	
	@PreAuthorize("hasRole('TEACHER')")
	@PostMapping()
	public QuizDto createQuiz(@Valid @RequestBody QuizDto quizDto) throws FieldNotFoundException, FieldInvalidException, FieldNullException {
		
		if (!this.quizService.isIdNull(quizDto))
			throw new FieldInvalidException("Id");
		
		return this.quizService.createQuiz(quizDto);
	}

	@PreAuthorize("hasRole('TEACHER')")
	@PutMapping(PATH_ID)
	public QuizDto modifyQuiz(@PathVariable String id, @Valid @RequestBody QuizDto quizDto)
			throws FieldNotFoundException, FieldNullException {
		
		return this.quizService.modifyQuiz(id, quizDto).orElseThrow(() -> new FieldNotFoundException("Id"));
	}

	@PreAuthorize("hasRole('TEACHER')")
	@DeleteMapping(PATH_ID)
	public QuizDto deleteQuiz(@PathVariable String id) throws FieldNotFoundException {
		return this.quizService.deleteQuiz(id).orElseThrow(() -> new FieldNotFoundException("Id"));
	}
}

