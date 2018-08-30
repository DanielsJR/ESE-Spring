package nx.ESE.dtos;

import java.util.List;

import nx.ESE.documents.User;
import nx.ESE.documents.core.Course;
import nx.ESE.documents.core.CourseName;

public class CourseDto {
	
	private String id;

	private CourseName name;

	private User chiefTeacher;

	private List<User> students;

	private int year;

	public CourseDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CourseDto(String id, CourseName name, User chiefTeacher, List<User> students, int year) {
		super();
		this.id = id;
		this.name = name;
		this.chiefTeacher = chiefTeacher;
		this.students = students;
		this.year = year;
	}
	
	//output
	public CourseDto(Course course) {
		super();
		this.id = course.getId();
		this.name = course.getName();
		this.chiefTeacher = course.getChiefTeacher();
		this.students = course.getStudents();
		this.year = course.getYear();
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
	public String toString() {
		return "CourseDto [id=" + id + ", name=" + name + ", chiefTeacher=" + chiefTeacher + ", students=" + students
				+ ", year=" + year + "]";
	}
	
	

}
