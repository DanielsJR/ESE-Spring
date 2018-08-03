package nx.ESE.dtos;

import java.util.Date;
import java.util.List;

import nx.ESE.documents.User;
import nx.ESE.documents.core.Course;
import nx.ESE.documents.core.CourseName;

public class CourseDto {
	
	private String id;

	private CourseName name;

	private User chiefTeacher;

	private List<User> students;

	private Date period;

	public CourseDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CourseDto(String id, CourseName name, User chiefTeacher, List<User> students, Date period) {
		super();
		this.id = id;
		this.name = name;
		this.chiefTeacher = chiefTeacher;
		this.students = students;
		this.period = period;
	}
	
	//output
	public CourseDto(Course course) {
		super();
		this.id = course.getId();
		this.name = course.getName();
		this.chiefTeacher = course.getChiefTeacher();
		this.students = course.getStudents();
		this.period = course.getPeriod();
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

	public Date getPeriod() {
		return period;
	}

	public void setPeriod(Date period) {
		this.period = period;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "CourseDto [id=" + id + ", name=" + name + ", chiefTeacher=" + chiefTeacher + ", students=" + students
				+ ", period=" + period + "]";
	}
	
	

}
