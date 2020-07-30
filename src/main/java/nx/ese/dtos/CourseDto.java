package nx.ese.dtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import nx.ese.documents.User;
import nx.ese.documents.core.Course;
import nx.ese.documents.core.CourseName;
import nx.ese.dtos.validators.NxPattern;
import nx.ese.utils.NxDateFormatter;

@NoArgsConstructor
public class CourseDto {

    @Getter
    private String id;

    @NotNull
    @Getter
    @Setter
    private CourseName name;

    @NotNull
    @Valid
    @Getter
    @Setter
    private UserDto chiefTeacher;

    @Getter
    @Setter
    private List<@Valid UserDto> students;

    @NotNull
    @Getter
    @Setter
    private String year;

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

    // output
    public CourseDto(Course course) {
        super();
        this.id = course.getId();
        this.name = course.getName();
        this.chiefTeacher = new UserDto(course.getChiefTeacher());
        this.students = this.studentsList(course);
        this.year = course.getYear();
        this.createdBy = course.getCreatedBy();
        this.createdDate = course.getCreatedDate();
        this.lastModifiedUser = course.getLastModifiedUser();
        this.lastModifiedDate = course.getLastModifiedDate();
    }

    public List<UserDto> studentsList(Course course) {
        List<UserDto> usersList = new ArrayList<>();
        for (User user : course.getStudents()) {
            usersList.add(new UserDto(user));
        }

        return usersList;
    }

    @Override
    public String toString() {
        return "CourseDto [id=" + id + ", name=" + name + ", chiefTeacher=" + chiefTeacher + ", students=" + students
                + ", year=" + year + ", createdBy=" + createdBy + ", createdDate=" + NxDateFormatter.formatterDate(this.createdDate) + ", lastModifiedUser="
                + lastModifiedUser + ", lastModifiedDate=" + NxDateFormatter.formatterDate(this.lastModifiedDate) + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((year == null) ? 0 : year.hashCode());
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
        CourseDto other = (CourseDto) obj;
        if (name != other.name)
            return false;
        if (year == null) {
            if (other.year != null)
                return false;
        } else if (!year.equals(other.year))
            return false;
        return true;
    }

}
