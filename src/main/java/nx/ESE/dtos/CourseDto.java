package nx.ESE.dtos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import nx.ESE.documents.User;
import nx.ESE.documents.core.Course;
import nx.ESE.documents.core.CourseName;

public class CourseDto {

	private String id;

	@NotNull
	private CourseName name;

	@NotNull
	private UserDto chiefTeacher;

	private List<UserDto> students;

	@NotNull
	private String year;
	
	private String createdBy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date createdDate;

	private String lastModifiedUser;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date lastModifiedDate;

	public CourseDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	//input
	public CourseDto(String id, CourseName name, UserDto chiefTeacher, List<UserDto> students, String year) {
		super();
		this.id = id;
		this.name = name;
		this.chiefTeacher = chiefTeacher;
		this.students = students;
		this.year = year;
	}

	// output
	public CourseDto(Course course) {
		super();
		this.id = course.getId();
		this.name = course.getName();
		this.chiefTeacher = new UserDto(course.getChiefTeacher());
		this.students = this.usersList(course);
		this.year = course.getYear();
		this.createdBy = course.getCreatedBy();
		this.createdDate = course.getCreatedDate();
		this.lastModifiedUser = course.getLastModifiedUser();
		this.lastModifiedDate = course.getLastModifiedDate();
	}

	public List<UserDto> usersList(Course course) {
		List<UserDto> usersList = new ArrayList<>();
		for (User user : course.getStudents()) {
			usersList.add(new UserDto(user));
		}

		return usersList;
	}

	public CourseName getName() {
		return name;
	}

	public void setName(CourseName name) {
		this.name = name;
	}

	public UserDto getChiefTeacher() {
		return chiefTeacher;
	}

	public void setChiefTeacher(UserDto chiefTeacher) {
		this.chiefTeacher = chiefTeacher;
	}

	public List<UserDto> getStudents() {
		return students;
	}

	public void setStudents(List<UserDto> students) {
		this.students = students;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
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
		
		return "CourseDto [id=" + id + ", name=" + name + ", chiefTeacher=" + chiefTeacher + ", students=" + students
				+ ", year=" + year + ", createdBy=" + createdBy + ", createdDate=" + cDate
				+ ", lastModifiedUser=" + lastModifiedUser + ", lastModifiedDate=" + lModified + "]";
	}

}
