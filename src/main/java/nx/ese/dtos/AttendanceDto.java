package nx.ese.dtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import nx.ese.dtos.validators.NxPattern;

import nx.ese.utils.NxDateFormatter;
import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nx.ese.documents.User;
import nx.ese.documents.core.Attendance;

@NoArgsConstructor
public class AttendanceDto {

    @Id
    @Getter
    private String id;

    @NotNull
    @Getter
    @Setter
    private String subjectId;

    @Getter
    @Setter
    private List<UserDto> students;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = NxPattern.DATE_FORMAT)
    @NotNull
    @Getter
    @Setter
    private Date date;

    @Getter
    private String createdBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = NxPattern.DATE_FORMAT)
    @Getter
    private Date createdDate;

    @Getter
    private String lastModifiedUser;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = NxPattern.DATE_FORMAT)
    @Getter
    private Date lastModifiedDate;

    //output
    public AttendanceDto(Attendance attendance) {
        this.id = attendance.getId();
        this.subjectId = attendance.getSubjectId();
        if (attendance.getStudents().size() > 0) this.students = this.studentsList(attendance);
        this.date = attendance.getDate();

        this.createdBy = attendance.getCreatedBy();
        this.createdDate = attendance.getCreatedDate();
        this.lastModifiedUser = attendance.getLastModifiedUser();
        this.lastModifiedDate = attendance.getLastModifiedDate();

    }

    public List<UserDto> studentsList(Attendance attendance) {
        List<UserDto> usersList = new ArrayList<>();
        for (User user : attendance.getStudents()) {
            usersList.add(new UserDto(user));
        }

        return usersList;
    }

    @Override
    public String toString() {
        return "AttendanceDto{" +
                "id='" + id + '\'' +
                ", subjectId='" + subjectId + '\'' +
                ", students=" + students +
                ", date=" + date +
                ", createdBy='" + createdBy + '\'' +
                ", createdDate=" + NxDateFormatter.formatterDate(createdDate) +
                ", lastModifiedUser='" + lastModifiedUser + '\'' +
                ", lastModifiedDate=" + NxDateFormatter.formatterDate(lastModifiedDate) +
                '}';
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((subjectId == null) ? 0 : subjectId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AttendanceDto other = (AttendanceDto) obj;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (subjectId == null) {
            if (other.subjectId != null)
                return false;
        } else if (!subjectId.equals(other.subjectId))
            return false;
        return true;
    }

}
