package nx.ESE.dtos;

import java.text.DecimalFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import nx.ESE.documents.core.Grade;

public class GradeDto {

	private String id;

	private UserDto student;

	private double grade;

	private SubjectDto subject;

	private String title;

	private String type;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date date;

	public GradeDto() {
		super();
		// TODO Auto-generated constructor stub
	}

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

	public GradeDto(Grade grade) {
		this.id = grade.getId();
		this.student = new UserDto(grade.getStudent());
		this.grade = grade.getGrade();
		this.subject = new SubjectDto(grade.getSubject());
		this.title = grade.getTitle();
		this.type = grade.getType();
		this.date = grade.getDate();
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

}