package nx.ESE.dtos;

import nx.ESE.documents.core.Subject;
import nx.ESE.documents.core.SubjectName;

public class SubjectDto {

	private String id;

	private SubjectName name;

	private UserDto teacher;

	private CourseDto course;

	public SubjectDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SubjectDto(String id, SubjectName name, UserDto teacher, CourseDto course) {
		super();
		this.id = id;
		this.name = name;
		this.teacher = teacher;
		this.course = course;
	}

	// output
	public SubjectDto(Subject subject) {
		this.id = subject.getId();
		this.name = subject.getName();
		this.teacher = new UserDto(subject.getTeacher());
		this.course = new CourseDto(subject.getCourse());
	}

	public SubjectName getName() {
		return name;
	}

	public void setName(SubjectName name) {
		this.name = name;
	}

	public UserDto getTeacher() {
		return teacher;
	}

	public void setTeacher(UserDto teacher) {
		this.teacher = teacher;
	}

	public CourseDto getCourse() {
		return course;
	}

	public void setCourse(CourseDto course) {
		this.course = course;
	}

	public String getId() {
		return id;
	}

}
