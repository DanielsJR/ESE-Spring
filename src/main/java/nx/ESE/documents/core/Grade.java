package nx.ESE.documents.core;

import java.time.Instant;
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
	private Subject subject;

	private double grade;

	
	private Date date;
	
	@CreatedBy
	private String user;

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

	public Grade(User student, double grade, Subject subject, String title, Date date) {
		super();
		this.student = student;
		this.grade = grade;
		this.subject = subject;
		this.title = title;
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
	
	

	public String getId() {
		return id;
	}



	@Override
	public String toString() {
		return "Grade [id=" + id + ", title=" + title + ", type=" + type + ", student=" + student + ", subject="
				+ subject + ", grade=" + grade + ", date=" + date + ", user=" + user + ", createdDate=" + createdDate
				+ ", lastModifiedUser=" + lastModifiedUser + ", lastModifiedDate=" + lastModifiedDate + "]";
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
