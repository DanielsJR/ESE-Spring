package nx.ESE.documents.core;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import nx.ESE.documents.User;

@Document
public class Subject {

	@Id
	private String id;

	private SubjectName name;

	//@ManyToOne // (fetch = FetchType.EAGER)
	@DBRef
	private User teacher;

	//@ManyToOne // (fetch = FetchType.EAGER)
	@DBRef
	private Course course;

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
	
	

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "SubjectTeacher [id=" + id + ", name=" + name + ", teacher=" + teacher + ", course=" + course + "]";
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
