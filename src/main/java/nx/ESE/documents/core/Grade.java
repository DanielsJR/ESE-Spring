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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nx.ESE.documents.User;

@NoArgsConstructor
@Document
public class Grade {

	@Id
	@Getter
	private String id;

	@Getter
	@Setter
	private String title;

	@Getter
	@Setter
	private String type;

	@DBRef
	@Getter
	@Setter
	private User student;

	@DBRef
	@Getter
	@Setter
	private QuizStudent quizStudent;

	@Getter
	@Setter
	private double grade;

	@DBRef
	@Getter
	@Setter
	private Subject subject;

	@Getter
	@Setter
	private Date date;

	@CreatedBy
	@Getter
	private String createdBy;

	@CreatedDate
	@Getter
	private Date createdDate;

	@LastModifiedBy
	@Getter
	private String lastModifiedUser;

	@LastModifiedDate
	@Getter
	private Date lastModifiedDate;

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
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((student == null) ? 0 : student.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (student == null) {
			if (other.student != null)
				return false;
		} else if (!student.equals(other.student))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

}
