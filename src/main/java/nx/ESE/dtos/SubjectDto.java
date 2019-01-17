package nx.ESE.dtos;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import nx.ESE.documents.core.Subject;
import nx.ESE.documents.core.SubjectName;

public class SubjectDto {

	private String id;

	@NotNull
	private SubjectName name;

	@NotNull
	private UserDto teacher;

	@NotNull
	private CourseDto course;
	
	private String createdBy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date createdDate;

	private String lastModifiedUser;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date lastModifiedDate;

	public SubjectDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	//input
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
		this.createdBy = subject.getCreatedBy();
		this.createdDate = subject.getCreatedDate();
		this.lastModifiedUser = subject.getLastModifiedUser();
		this.lastModifiedDate = subject.getLastModifiedDate();
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
	
	

}
