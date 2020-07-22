package nx.ESE.documents.core;

import java.util.Date;

import nx.ESE.utils.NX_DateFormatter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nx.ESE.documents.User;

@NoArgsConstructor
@Document
public class Subject {

	@Id
	@Getter
	private String id;

	@Getter
	@Setter
	private SubjectName name;

	@DBRef
	@Getter
	@Setter
	private User teacher;

	@DBRef
	@Getter
	@Setter
	private Course course;

	@CreatedBy
	@Getter
	private String createdBy;

	@CreatedDate
	@Getter
	private Date createdDate;

	@LastModifiedBy
	@Getter
	private String lastModifiedUser;

	@LastModifiedDate
	@Getter
	private Date lastModifiedDate;

	public Subject(SubjectName name, User teacher, Course course) {
		super();
		this.name = name;
		this.teacher = teacher;
		this.course = course;
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
		Subject other = (Subject) obj;
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
