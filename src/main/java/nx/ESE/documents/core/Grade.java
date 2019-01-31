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
public class Grade {

	@Id
	private String id;

	private String title;

	private String type;

	@DBRef
	private User student;

	@DBRef
	private QuizStudent quizStudent;

	private double grade;

	@DBRef
	private Subject subject;

	private Date date;

	@CreatedBy
	private String createdBy;

	@CreatedDate
	private Date createdDate;

	@LastModifiedBy
	private String lastModifiedUser;

	@LastModifiedDate
	private Date lastModifiedDate;

	public Grade() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Grade(String title, String type, QuizStudent quizStudent, Date date) {
		super();
		this.title = title;
		this.type = type;
		this.student = quizStudent.getStudent();
		this.grade = quizStudent.getGrade();
		this.quizStudent = quizStudent;
		this.subject = quizStudent.getSubject();
		this.date = date;
	}

	public Grade(String title, String type, User student, double grade, Subject subject, Date date) {
		super();
		this.title = title;
		this.type = type;
		this.student = student;
		this.grade = grade;
		this.subject = subject;
		this.date = date;
	}

	public User getStudent() {
		return student;
	}

	public void setStudent(User student) {
		this.student = student;
	}

	public double getGrade() {
		return grade;
	}

	public void setGrade(double grade) {
		this.grade = grade;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public QuizStudent getQuizStudent() {
		return quizStudent;
	}

	public void setQuizStudent(QuizStudent quizStudent) {
		this.quizStudent = quizStudent;
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

		return "Grade [id=" + id + ", title=" + title + ", type=" + type + ", student=" + student + ", subject="
				+ subject + ", grade=" + grade + ", quizStudent=" + quizStudent + ", date=" + date + ", createdBy="
				+ createdBy + ", createdDate=" + cDate + ", lastModifiedUser=" + lastModifiedUser
				+ ", lastModifiedDate=" + lModified + "]";

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((student == null) ? 0 : student.hashCode());
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
		Grade other = (Grade) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (student == null) {
			if (other.student != null)
				return false;
		} else if (!student.equals(other.student))
			return false;
		return true;
	}

}
