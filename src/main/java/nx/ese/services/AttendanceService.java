package nx.ese.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nx.ese.documents.User;
import nx.ese.documents.core.Attendance;
import nx.ese.dtos.AttendanceDto;

import nx.ese.repositories.AttendanceRepository;
import nx.ese.repositories.UserRepository;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private UserRepository userRepository;

    private Attendance setAttendanceFromDto(Attendance attendance, @Valid AttendanceDto attendanceDto) {
        attendance.setSubjectId(attendanceDto.getSubjectId());
        attendance.setDate(attendanceDto.getDate());
        attendance.setStudents(this.setStudentsList(attendanceDto));

        return attendance;
    }

    private List<User> setStudentsList(AttendanceDto attendanceDto) {
        if (attendanceDto.getStudents().size() > 0) {
            return attendanceDto.getStudents()
                    .stream()
                    .map(userDto -> userRepository.findById(userDto.getId()))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        }

        return new ArrayList<User>();
    }

    // Exceptions*********************
    public boolean isIdNull(@Valid AttendanceDto attendanceDto) {
        return attendanceDto.getId() == null;
    }

    public boolean isAttendanceRepeated(@Valid AttendanceDto attendanceDto) {
        if (isSubjectIdNull(attendanceDto) || isDateNull(attendanceDto)) {
            return false;
        }

        AttendanceDto attendanceDB = attendanceRepository.findBySubjectIdAndDate(attendanceDto.getSubjectId(),
                attendanceDto.getDate());

        return attendanceDB != null && !attendanceDB.getId().equals(attendanceDto.getId());
    }

    private boolean isSubjectIdNull(@Valid AttendanceDto attendanceDto) {
        return attendanceDto.getSubjectId() == null;
    }

    public boolean isDateNull(@Valid AttendanceDto attendanceDto) {
        return attendanceDto.getDate() == null;
    }

    // CRUD******************************
    public AttendanceDto createAttendance(@Valid AttendanceDto attendanceDto) {
        Attendance attendance = setAttendanceFromDto(new Attendance(), attendanceDto);
        return new AttendanceDto(attendanceRepository.insert(attendance));
    }

    public Optional<AttendanceDto> modifyAttendance(String id, @Valid AttendanceDto attendanceDto) {
        Optional<Attendance> attendance = attendanceRepository.findById(id);
        return attendance.map(value -> new AttendanceDto(attendanceRepository.save(setAttendanceFromDto(value, attendanceDto))));
    }

    public Optional<AttendanceDto> deleteAttendance(String id) {
        Optional<Attendance> attendance = attendanceRepository.findById(id);
        if (attendance.isPresent()) {
            attendanceRepository.deleteById(id);
            return attendance.map(AttendanceDto::new);
        }
        return Optional.empty();
    }

    public Optional<AttendanceDto> getAttendanceById(String id) {
        return attendanceRepository.findById(id).map(AttendanceDto::new);
    }

    public Optional<List<AttendanceDto>> getAttendancesBySubject(String id) {
        List<AttendanceDto> list = attendanceRepository.findBySubjectId(id);
        if (list.isEmpty())
            return Optional.empty();
        return Optional.of(list);
    }

}
