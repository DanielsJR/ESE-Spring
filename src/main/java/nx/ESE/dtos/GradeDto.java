package nx.ESE.dtos;


import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import nx.ESE.documents.core.Grade;

public class GradeDto {

	private String id;

	@NotNull
	private UserDto student;

	private double grade;

	@NotNull
	private SubjectDto subject;

	@NotNull
	private String title;

	@NotNull
	private String type;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date date;
	
	private String createdBy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date createdDate;

	private String lastModifiedUser;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date lastModifiedDate;

	public GradeDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	// input
	public GradeDto(String id, UserDto student, double grade, SubjectDto subject, String title, String type,
			Date date) {
		super();
		this.id = id;
		this.student = student;
		this.grade = grade;
		this.subject = subject;
		this.title = title;
		this.type = type;
		this.date = date;
	}

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

	public String getId() {
		return id;
	}

	public UserDto getStudent() {
		return student;
	}

	public void setStudent(UserDto student) {
		this.student = student;
	}

	public double getGrade() {
		return grade;
	}

	public void setGrade(double grade) {
		this.grade = grade;
	}

	public SubjectDto getSubject() {
		return subject;
	}

	public void setSubject(SubjectDto subject) {
		this.subject = subject;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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
				+ title + ", type=" + type + ", date=" + fDate + ", createdBy=" + createdBy + ", createdDate="
				+ cDate + ", lastModifiedUser=" + lastModifiedUser + ", lastModifiedDate=" + lModified
				+ "]";
	}

	
	

}
