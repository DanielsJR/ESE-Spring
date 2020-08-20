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

import nx.ese.dtos.AttendanceDto;
import nx.ese.exceptions.DocumentAlreadyExistException;
import nx.ese.exceptions.DocumentNotFoundException;
import nx.ese.exceptions.FieldInvalidException;
import nx.ese.exceptions.FieldNotFoundException;
import nx.ese.services.AttendanceService;

@PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER') or hasRole('STUDENT')")
@RestController
@RequestMapping(AttendanceController.ATTENDANCE)
public class AttendanceController {

    public static final String ATTENDANCE = "/attendance";
    public static final String SUBJECT = "/subject";

    public static final String PATH_ID = "/{id}";
    public static final String PATH_USERNAME = "/{username}";

    @Autowired
    private AttendanceService attendanceService;


    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping()
    public AttendanceDto createAttendance(@Valid @RequestBody AttendanceDto attendanceDto)
            throws FieldInvalidException, DocumentAlreadyExistException {

        if (!this.attendanceService.isIdNull(attendanceDto))
            throw new FieldInvalidException("Id");

        if (this.attendanceService.isAttendanceRepeated(attendanceDto))
            throw new DocumentAlreadyExistException("Asistencia");

        return this.attendanceService.createAttendance(attendanceDto);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping(PATH_ID)
    public AttendanceDto modifyAttendance(@PathVariable String id, @Valid @RequestBody AttendanceDto attendanceDto)
            throws FieldNotFoundException, DocumentAlreadyExistException {

        if (this.attendanceService.isAttendanceRepeated(attendanceDto))
            throw new DocumentAlreadyExistException("Asistencia");

        return this.attendanceService.modifyAttendance(id, attendanceDto).orElseThrow(() -> new FieldNotFoundException("Id"));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping(PATH_ID)
    public AttendanceDto deleteAttendance(@PathVariable String id) throws FieldNotFoundException {
        return this.attendanceService.deleteAttendance(id).orElseThrow(() -> new FieldNotFoundException("Id"));
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
    @GetMapping(PATH_ID)
    public AttendanceDto getAttendanceById(@PathVariable String id) throws FieldNotFoundException {
        return this.attendanceService.getAttendanceById(id).orElseThrow(() -> new FieldNotFoundException("Id"));
    }


    @PreAuthorize("hasRole('MANAGER') or hasRole('TEACHER')")
    @GetMapping(SUBJECT + PATH_ID)
    public List<AttendanceDto> getAttendancesBySubject(@PathVariable String id) throws DocumentNotFoundException {
        return this.attendanceService.getAttendancesBySubject(id).orElseThrow(() -> new DocumentNotFoundException("Asistencia(s)"));
    }

}
