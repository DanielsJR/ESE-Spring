package nx.ese.controllers;

import java.util.Collections;
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

import nx.ese.documents.core.SubjectName;
import nx.ese.dtos.SubjectDto;
import nx.ese.exceptions.DocumentAlreadyExistException;
import nx.ese.exceptions.DocumentNotFoundException;
import nx.ese.exceptions.FieldInvalidException;
import nx.ese.exceptions.ForbiddenDeleteException;
import nx.ese.services.SubjectService;

@PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER') or hasRole('STUDENT')")
@RestController
@RequestMapping(SubjectController.SUBJECT)
public class SubjectController {

    public static final String SUBJECT = "/subject";
    public static final String NAME = "/name";
    public static final String TEACHER = "/teacher";
    public static final String COURSE = "/course";
    public static final String YEAR = "/year";

    public static final String PATH_ID = "/{id}";
    public static final String PATH_YEAR = "/{year}";
    public static final String PATH_NAME = "/{name}";
    public static final String PATH_USERNAME = "/{username}";


    @Autowired
    private SubjectService subjectService;

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

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping(PATH_ID)
    public SubjectDto modifySubject(@PathVariable String id, @Valid @RequestBody SubjectDto subjectDto)
            throws DocumentNotFoundException, DocumentAlreadyExistException {

        if (this.subjectService.isSubjectRepeated(subjectDto))
            throw new DocumentAlreadyExistException("Asignatura");

        return this.subjectService.modifySubject(id, subjectDto).orElseThrow(() -> new DocumentNotFoundException("Subject"));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping(PATH_ID)
    public SubjectDto deleteSubject(@PathVariable String id) throws DocumentNotFoundException, ForbiddenDeleteException {

        if (this.subjectService.isSubjectInEvaluation(id))
            throw new ForbiddenDeleteException("Asignatura esta en una evaluación");

        return this.subjectService.deleteSubject(id).orElseThrow(() -> new DocumentNotFoundException("Subject"));
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
    @GetMapping(PATH_ID)
    public SubjectDto getSubjectById(@PathVariable String id) throws DocumentNotFoundException {
        return this.subjectService.getSubjectById(id).orElseThrow(() -> new DocumentNotFoundException("Subject"));
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
    @GetMapping(NAME + PATH_NAME + PATH_ID)
    public SubjectDto getSubjectByNameAndCourse(@PathVariable SubjectName name, @PathVariable String id)
            throws DocumentNotFoundException {

        return this.subjectService.getSubjectByNameAndCourse(name, id)
                .orElseThrow(() -> new DocumentNotFoundException("Asignatura"));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping(YEAR + PATH_YEAR)
    public List<SubjectDto> getSubjectsByYear(@PathVariable String year) {
        return this.subjectService.getSubjectsByYear(year).orElse(Collections.emptyList());
    }

    @PreAuthorize("hasRole('TEACHER') and #username == authentication.principal.username")
    @GetMapping(TEACHER + PATH_USERNAME + PATH_YEAR)
    public List<SubjectDto> getSubjectsByTeacherAndYear(@PathVariable String username, @PathVariable String year) {
        return this.subjectService.getSubjectsByTeacherAndYear(username, year).orElse(Collections.emptyList());
    }

    @PreAuthorize("hasRole('STUDENT') and #username == authentication.principal.username")
    @GetMapping(COURSE + PATH_ID + PATH_USERNAME)
    public List<SubjectDto> getStudentSubjectsByCourse(@PathVariable String id, @PathVariable String username) {
        return this.subjectService.getStudentSubjectsByCourse(id, username).orElse(Collections.emptyList());
    }

}
