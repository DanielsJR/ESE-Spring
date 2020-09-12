package nx.ese.controllers;

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

import nx.ese.dtos.QuizStudentDto;
import nx.ese.services.QuizStudentService;


@PreAuthorize("hasRole('TEACHER')")
@RestController
@RequestMapping(QuizStudentController.QUIZ_STUDENT)
public class QuizStudentController {

    public static final String QUIZ_STUDENT = "/quiz-student";
    public static final String STUDENT = "/student";
    public static final String TEACHER = "/teacher";

    public static final String PATH_ID = "/{id}";
    public static final String PATH_USERNAME = "/{username}";


    @Autowired
    private QuizStudentService quizStudentService;


    @PreAuthorize("hasRole('STUDENT') and #username == authentication.principal.username")
    @PostMapping(STUDENT + PATH_USERNAME)
    public QuizStudentDto createQuizStudent(@PathVariable String username, @Valid @RequestBody QuizStudentDto quizStudentDto) throws FieldInvalidException, DocumentAlreadyExistException {

        if (!this.quizStudentService.isIdNull(quizStudentDto))
            throw new FieldInvalidException("Id");

        if (this.quizStudentService.isQuizStudentRepeated(quizStudentDto))
            throw new DocumentAlreadyExistException("QuizStudent");

        return this.quizStudentService.createQuizStudent(quizStudentDto);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping(PATH_ID)
    public QuizStudentDto modifyQuizStudent(@PathVariable String id, @Valid @RequestBody QuizStudentDto quizStudentDto)
            throws FieldNotFoundException, DocumentAlreadyExistException {

        if (this.quizStudentService.isQuizStudentRepeated(quizStudentDto))
            throw new DocumentAlreadyExistException("QuizStudent");

        return this.quizStudentService.modifyQuizStudent(id, quizStudentDto).orElseThrow(() -> new FieldNotFoundException("Id"));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping(PATH_ID)
    public QuizStudentDto deleteQuizStudent(@PathVariable String id) throws FieldNotFoundException, ForbiddenDeleteException, ForbiddenException {

        if (this.quizStudentService.isQuizStudentInGrade(id))
            throw new ForbiddenDeleteException("Quiz esta en una evaluación");

        return this.quizStudentService.deleteQuizStudent(id).orElseThrow(() -> new FieldNotFoundException("Id"));
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
    @GetMapping(PATH_ID)
    public QuizStudentDto getQuizStudentById(@PathVariable String id) throws FieldNotFoundException {
        return this.quizStudentService.getQuizStudentById(id).orElseThrow(() -> new FieldNotFoundException("Id"));
    }

}

