package nx.ese.dtos;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import nx.ese.documents.core.Subject;
import nx.ese.documents.core.SubjectName;
import nx.ese.dtos.validators.NX_Pattern;
import nx.ese.utils.NX_DateFormatter;

@NoArgsConstructor
public class SubjectDto {

    @Getter
    private String id;

    @NotNull
    @Getter
    @Setter
    private SubjectName name;

    @NotNull
    @Valid
    @Getter
    @Setter
    private UserDto teacher;

    @NotNull
    @Valid
    @Getter
    @Setter
    private CourseDto course;

    @Getter
    private String createdBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = NX_Pattern.DATE_FORMAT)
    @Getter
    private Date createdDate;

    @Getter
    private String lastModifiedUser;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = NX_Pattern.DATE_FORMAT)
    @Getter
    private Date lastModifiedDate;

    // output
    public SubjectDto(Subject subject) {
        this.id = subject.getId();
        this.name = subject.getName();
        this.teacher = new UserDto(subject.getTeacher());
        this.course = new CourseDto(subject.getCourse());
        this.createdBy = subject.getCreatedBy();
        this.createdDate = subject.getCreatedDate();
        this.lastModifiedUser = subject.getLastModifiedUser();
        this.lastModifiedDate = subject.getLastModifiedDate();
    }

    @Override
    public String toString() {
        return "Subject [id=" + id + ", name=" + name + ", teacher=" + teacher + ", course=" + course + ", createdBy="
                + createdBy + ", createdDate=" + NX_DateFormatter.formatterDate(this.createdDate) + ", lastModifiedUser=" + lastModifiedUser
                + ", lastModifiedDate=" + NX_DateFormatter.formatterDate(this.lastModifiedDate) + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((course == null) ? 0 : course.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        SubjectDto other = (SubjectDto) obj;
        if (course == null) {
            if (other.course != null)
                return false;
        } else if (!course.equals(other.course))
            return false;
        if (name != other.name)
            return false;
        return true;
    }


}
