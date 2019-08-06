package nx.ESE.dtos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nx.ESE.documents.User;
import nx.ESE.documents.core.Course;
import nx.ESE.documents.core.CourseName;

@NoArgsConstructor
public class CourseDto {

	@Getter
	private String id;

	@NotNull(message = "Nombre de Curso cannot be null")
	@Getter
	@Setter
	private CourseName name;

	@NotNull(message = "Profesor Jefe cannot be null")
	@Valid
	@Getter
	@Setter
	private UserDto chiefTeacher;

	@Getter
	@Setter
	private List<@Valid UserDto> students;

	@NotNull(message = "Año cannot be null")
	@Getter
	@Setter
	private String year;

	@Getter
	private String createdBy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@Getter
	private Date createdDate;

	@Getter
	private String lastModifiedUser;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
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

		String cDate = "null";
		if (this.createdDate != null)
			cDate = new SimpleDateFormat("dd-MMM-yyyy").format(createdDate.getTime());

		String lModified = "null";
		if (this.lastModifiedDate != null)
			lModified = new SimpleDateFormat("dd-MMM-yyyy").format(lastModifiedDate.getTime());

		return "CourseDto [id=" + id + ", name=" + name + ", chiefTeacher=" + chiefTeacher + ", students=" + students
				+ ", year=" + year + ", createdBy=" + createdBy + ", createdDate=" + cDate + ", lastModifiedUser="
				+ lastModifiedUser + ", lastModifiedDate=" + lModified + "]";
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
