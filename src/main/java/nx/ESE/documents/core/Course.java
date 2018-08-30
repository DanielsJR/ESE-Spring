package nx.ESE.documents.core;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import nx.ESE.documents.User;

@Document
public class Course {

	@Id
	private String id;

	private CourseName name;

	@DBRef
	private User chiefTeacher;

	@DBRef(lazy = true)
	private List<User> students;

	private int year;

	public Course() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Course(CourseName name, User chiefTeacher, List<User> students, int year) {
		super();
		this.name = name;
		this.chiefTeacher = chiefTeacher;
		this.students = students;
		this.year = year;
	}

	public CourseName getName() {
		return name;
	}

	public void setName(CourseName name) {
		this.name = name;
	}

	public User getChiefTeacher() {
		return chiefTeacher;
	}

	public void setChiefTeacher(User chiefTeacher) {
		this.chiefTeacher = chiefTeacher;
	}

	public List<User> getStudents() {
		return students;
	}

	public void setStudents(List<User> students) {
		this.students = students;
	}


	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + year;
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
		Course other = (Course) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name != other.name)
			return false;
		if (year != other.year)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Course [id=" + id + ", name=" + name + ", chiefTeacher=" + chiefTeacher + ", students=" + students
				+ ", year=" + year + "]";
	}

	




}
