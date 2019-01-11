package nx.ESE.documents.core;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import nx.ESE.documents.User;

@Document
public class Subject {

	@Id
	private String id;

	private SubjectName name;

	@DBRef
	private User teacher;

	@DBRef
	private Course course;

	@CreatedBy
	private String createdBy;

	@CreatedDate
	private Date createdDate;

	@LastModifiedBy
	private String lastModifiedUser;

	@LastModifiedDate
	private Date lastModifiedDate;

	public Subject() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Subject(SubjectName name, User teacher, Course course) {
		super();
		this.name = name;
		this.teacher = teacher;
		this.course = course;
	}

	public SubjectName getName() {
		return name;
	}

	public void setName(SubjectName name) {
		this.name = name;
	}

	public User getTeacher() {
		return teacher;
	}

	public void setTeacher(User teacher) {
		this.teacher = teacher;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public String getLastModifiedUser() {
		return lastModifiedUser;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {

		String cDate = "null";
		if (this.createdDate != null)
			cDate = new SimpleDateFormat("dd-MMM-yyyy").format(createdDate.getTime());

		String lModified = "null";
		if (this.lastModifiedDate != null)
			lModified = new SimpleDateFormat("dd-MMM-yyyy").format(lastModifiedDate.getTime());

		return "Subject [id=" + id + ", name=" + name + ", teacher=" + teacher + ", course=" + course
				+ ", createdBy=" + createdBy + ", createdDate=" + cDate + ", lastModifiedUser=" + lastModifiedUser
				+ ", lastModifiedDate=" + lModified + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name != other.name)
			return false;
		return true;
	}

}
