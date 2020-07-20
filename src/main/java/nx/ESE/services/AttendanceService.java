package nx.ESE.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nx.ESE.documents.User;
import nx.ESE.documents.core.Attendance;
import nx.ESE.dtos.AttendanceDto;

import nx.ESE.repositories.AttendanceRepository;
import nx.ESE.repositories.UserRepository;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private UserRepository userRepository;

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
        attendanceRepository.insert(attendance);
        return new AttendanceDto(attendanceRepository.findById(attendance.getId()).get());
    }

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

    public Optional<AttendanceDto> modifyAttendance(String id, @Valid AttendanceDto attendanceDto) {
        Optional<Attendance> attendance = attendanceRepository.findById(id);
        if (attendance.isPresent()) {
            attendanceRepository.save(setAttendanceFromDto(attendance.get(), attendanceDto));
            return Optional.of(new AttendanceDto(attendanceRepository.findById(id).get()));
        }
        return Optional.empty();
    }

    public Optional<AttendanceDto> deleteAttendance(String id) {
        Optional<Attendance> attendance = attendanceRepository.findById(id);
        if (attendance.isPresent()) {
            attendanceRepository.deleteById(id);
            return Optional.of(new AttendanceDto(attendance.get()));
        }
        return Optional.empty();
    }

    public Optional<AttendanceDto> getAttendanceById(String id) {
        Optional<Attendance> attendance = attendanceRepository.findById(id);
        if (attendance.isPresent()) {
            return Optional.of(new AttendanceDto(attendance.get()));
        }
        return Optional.empty();
    }

    public Optional<List<AttendanceDto>> getAttendancesBySubject(String id) {
        List<AttendanceDto> list = attendanceRepository.findBySubjectId(id);
        // .stream()
        // .collect(Collectors.toList());
        if (list.isEmpty())
            return Optional.empty();
        return Optional.of(list);
    }

}
