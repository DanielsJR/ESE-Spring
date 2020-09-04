package nx.ese.controllers;

import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import nx.ese.exceptions.*;
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
import nx.ese.services.QuizService;

@RestController
@RequestMapping(QuizController.QUIZ)
public class QuizController {

    public static final String QUIZ = "/quiz";
    public static final String AUTHOR = "/author";
    public static final String TEACHER = "/teacher";

    public static final String PATH_ID = "/{id}";
    public static final String PATH_USERNAME = "/{username}";


    @Autowired
    private QuizService quizService;

    @PreAuthorize("hasRole('TEACHER') and #username == authentication.principal.username")
    @PostMapping(TEACHER + PATH_USERNAME)
    public QuizDto createQuiz(@PathVariable String username, @Valid @RequestBody QuizDto quizDto) throws FieldInvalidException, DocumentAlreadyExistException, ForbiddenException {

        if (!this.quizService.isIdNull(quizDto))
            throw new FieldInvalidException("Id");

        if (this.quizService.isQuizRepeated(quizDto))
            throw new DocumentAlreadyExistException("Quiz");

        if (!this.quizService.isTeacherInQuiz(quizDto, username))
            throw new ForbiddenException("Profesor creando Quiz por otro Profesor");

        return this.quizService.createQuiz(quizDto);
    }

    @PreAuthorize("hasRole('TEACHER') and #username == authentication.principal.username")
    @PutMapping(PATH_ID + TEACHER + PATH_USERNAME)
    public QuizDto modifyQuiz(@PathVariable String id, @PathVariable String username, @Valid @RequestBody QuizDto quizDto)
            throws FieldNotFoundException, DocumentAlreadyExistException, ForbiddenException {

        if (this.quizService.isQuizRepeated(quizDto))
            throw new DocumentAlreadyExistException("Quiz");

        if (!this.quizService.isTeacherInQuiz(quizDto, username))
            throw new ForbiddenException("Profesor modificando Quiz de otro Profesor");

        return this.quizService.modifyQuiz(id, quizDto).orElseThrow(() -> new FieldNotFoundException("Id"));
    }

    @PreAuthorize("hasRole('TEACHER') and #username == authentication.principal.username")
    @DeleteMapping(PATH_ID + TEACHER + PATH_USERNAME)
    public QuizDto deleteQuiz(@PathVariable String id, @PathVariable String username) throws FieldNotFoundException, ForbiddenDeleteException {

        if (this.quizService.isQuizInEvaluation(id))
            throw new ForbiddenDeleteException("Quiz esta en una Evaluación");

        return this.quizService.deleteQuiz(id, username).orElseThrow(() -> new FieldNotFoundException("Id"));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping(PATH_ID)
    public QuizDto getQuizById(@PathVariable String id) throws DocumentNotFoundException {
        return this.quizService.getQuizById(id).orElseThrow(() -> new DocumentNotFoundException("Quiz"));
    }

    @PreAuthorize("hasRole('TEACHER') and #username == authentication.principal.username")
    @GetMapping(PATH_ID + TEACHER + PATH_USERNAME)
    public QuizDto getTeacherQuizById(@PathVariable String id, @PathVariable String username) throws DocumentNotFoundException {
        return this.quizService.getTeacherQuizById(id, username).orElseThrow(() -> new DocumentNotFoundException("Quiz"));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping()
    public List<QuizDto> getQuizes() {
        return this.quizService.getQuizes().orElse(Collections.emptyList());
    }

    @PreAuthorize("hasRole('TEACHER') and #username == authentication.principal.username")
    @GetMapping(TEACHER + PATH_USERNAME)
    public List<QuizDto> getTeacherQuizes(@PathVariable String username) {
        return this.quizService.getTeacherQuizes(username).orElse(Collections.emptyList());
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping(AUTHOR + PATH_ID)
    public List<QuizDto> getQuizesByAuthor(@PathVariable String id) {
        return this.quizService.getQuizesByAuthor(id).orElse(Collections.emptyList());
    }

    @PreAuthorize("hasRole('TEACHER') and #username == authentication.principal.username")
    @GetMapping(AUTHOR + PATH_ID + TEACHER + PATH_USERNAME)
    public List<QuizDto> getTeacherQuizesByAuthor(@PathVariable String id, @PathVariable String username) {
        return this.quizService.getTeacherQuizesByAuthor(id, username).orElse(Collections.emptyList());
    }

}

