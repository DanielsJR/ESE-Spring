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
import org.springframework.web.bind.annotation.RestController;

import nx.ese.dtos.GradeDto;
import nx.ese.exceptions.DocumentAlreadyExistException;
import nx.ese.exceptions.DocumentNotFoundException;
import nx.ese.exceptions.FieldInvalidException;
import nx.ese.exceptions.FieldNotFoundException;
import nx.ese.services.GradeService;

@PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER') or hasRole('STUDENT')")
@RestController
@RequestMapping(GradeController.GRADE)
public class GradeController {

    public static final String GRADE = "/grade";
    public static final String SUBJECT = "/subject";
    public static final String TEACHER = "/teacher";
    public static final String STUDENT = "/student";

    public static final String PATH_ID = "/{id}";
    public static final String PATH_USERNAME = "/{username}";

    @Autowired
    private GradeService gradeService;

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping()
    public GradeDto createGrade(@Valid @RequestBody GradeDto gradeDto)
            throws FieldInvalidException, DocumentAlreadyExistException {

        if (!this.gradeService.isIdNull(gradeDto))
            throw new FieldInvalidException("Id");

        if (this.gradeService.isGradeRepeated(gradeDto))
            throw new DocumentAlreadyExistException("Nota");

        return this.gradeService.createGrade(gradeDto);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping(PATH_ID)
    public GradeDto modifyGrade(@PathVariable String id, @Valid @RequestBody GradeDto gradeDto)
            throws FieldNotFoundException, DocumentAlreadyExistException {

        if (this.gradeService.isGradeRepeated(gradeDto))
            throw new DocumentAlreadyExistException("Nota");

        return this.gradeService.modifyGrade(id, gradeDto).orElseThrow(() -> new FieldNotFoundException("Id"));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping(PATH_ID)
    public GradeDto deleteGrade(@PathVariable String id) throws FieldNotFoundException {
        return this.gradeService.deleteGrade(id).orElseThrow(() -> new FieldNotFoundException("Id"));
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
    @GetMapping(PATH_ID)
    public GradeDto getGradeById(@PathVariable String id) throws FieldNotFoundException {
        return this.gradeService.getGradeById(id).orElseThrow(() -> new FieldNotFoundException("Id"));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping(SUBJECT + PATH_ID)
    public List<GradeDto> getGradesBySubject(@PathVariable String id) {
        return this.gradeService.getGradesBySubject(id).orElse(Collections.emptyList());
    }

    @PreAuthorize("hasRole('TEACHER') and #username == authentication.principal.username")
    @GetMapping(SUBJECT + PATH_ID + TEACHER + PATH_USERNAME)
    public List<GradeDto> getTeacherGradesBySubject(@PathVariable String id, @PathVariable String username) {
        return this.gradeService.getTeacherGradesBySubject(id, username).orElse(Collections.emptyList());
    }

    @PreAuthorize("hasRole('STUDENT') and #username == authentication.principal.username")
    @GetMapping(SUBJECT + PATH_ID + STUDENT + PATH_USERNAME)
    public List<GradeDto> getStudentGradesBySubject(@PathVariable String id, @PathVariable String username) {
        return this.gradeService.getStudentGradesBySubject(id, username).orElse(Collections.emptyList());
    }
}
