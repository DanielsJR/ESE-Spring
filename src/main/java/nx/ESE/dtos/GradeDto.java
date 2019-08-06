package nx.ESE.dtos;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nx.ESE.documents.core.Grade;

@NoArgsConstructor
public class GradeDto {

	@Getter
	private String id;

	@NotNull
	@Valid
	@Getter
	@Setter
	private UserDto student;

	@Getter
	@Setter
	private double grade;

	@NotNull
	@Valid
	@Getter
	@Setter
	private SubjectDto subject;

	@NotNull
	@Getter
	@Setter
	private String title;

	@NotNull
	@Getter
	@Setter
	private String type;

	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@Getter
	@Setter
	private Date date;

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
	public GradeDto(Grade grade) {
		this.id = grade.getId();
		this.student = new UserDto(grade.getStudent());
		this.grade = grade.getGrade();
		this.subject = new SubjectDto(grade.getSubject());
		this.title = grade.getTitle();
		this.type = grade.getType();
		this.date = grade.getDate();
		this.createdBy = grade.getCreatedBy();
		this.createdDate = grade.getCreatedDate();
		this.lastModifiedUser = grade.getLastModifiedUser();
		this.lastModifiedDate = grade.getLastModifiedDate();
	}

	@Override
	public String toString() {

		String fDate = "null";
		if (this.date != null)
			fDate = new SimpleDateFormat("dd-MMM-yyyy").format(date.getTime());

		String cDate = "null";
		if (this.createdDate != null)
			cDate = new SimpleDateFormat("dd-MMM-yyyy").format(createdDate.getTime());

		String lModified = "null";
		if (this.lastModifiedDate != null)
			lModified = new SimpleDateFormat("dd-MMM-yyyy").format(lastModifiedDate.getTime());

		return "GradeDto [id=" + id + ", student=" + student + ", grade=" + grade + ", subject=" + subject + ", title="
				+ title + ", type=" + type + ", date=" + fDate + ", createdBy=" + createdBy + ", createdDate=" + cDate
				+ ", lastModifiedUser=" + lastModifiedUser + ", lastModifiedDate=" + lModified + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((student == null) ? 0 : student.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		GradeDto other = (GradeDto) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (student == null) {
			if (other.student != null)
				return false;
		} else if (!student.equals(other.student))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

}
