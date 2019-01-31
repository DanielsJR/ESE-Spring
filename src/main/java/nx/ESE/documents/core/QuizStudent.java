package nx.ESE.documents.core;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import nx.ESE.documents.User;

@Document
public class QuizStudent {

	@Id
	private String id;

	private Date date;

	@DBRef
	private User student;

	private double grade;

	@DBRef
	private Subject subject;

	@DBRef
	private Quiz quiz;

	private List<String> multipleSelectionIitemAnswers = new ArrayList<String>();

	private List<Boolean> trueFalseItemAnswers = new ArrayList<Boolean>();

	private List<String> correspondItemAnswers = new ArrayList<String>();

	private List<String> incompleteTextItemAnswers = new ArrayList<String>();

	@CreatedBy
	private String createdBy;

	@CreatedDate
	private Date createdDate;

	@LastModifiedBy
	private String lastModifiedUser;

	@LastModifiedDate
	private Date lastModifiedDate;

	public QuizStudent() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QuizStudent(Date date, User student, double grade, Subject subject, Quiz quiz,
			List<String> multipleSelectionIitemAnswers, List<Boolean> trueFalseItemAnswers,
			List<String> correspondItemAnswers, List<String> incompleteTextItemAnswers) {
		super();
		this.date = date;
		this.student = student;
		this.grade = grade;
		this.subject = subject;
		this.quiz = quiz;
		this.multipleSelectionIitemAnswers = multipleSelectionIitemAnswers;
		this.trueFalseItemAnswers = trueFalseItemAnswers;
		this.correspondItemAnswers = correspondItemAnswers;
		this.incompleteTextItemAnswers = incompleteTextItemAnswers;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getGrade() {
		return grade;
	}

	public void setGrade(double grade) {
		this.grade = grade;
	}

	public Quiz getQuiz() {
		return quiz;
	}

	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}

	public List<String> getMultipleSelectionIitemAnswers() {
		return multipleSelectionIitemAnswers;
	}

	public void setMultipleSelectionIitemAnswers(List<String> multipleSelectionIitemAnswers) {
		this.multipleSelectionIitemAnswers = multipleSelectionIitemAnswers;
	}

	public List<Boolean> getTrueFalseItemAnswers() {
		return trueFalseItemAnswers;
	}

	public void setTrueFalseItemAnswers(List<Boolean> trueFalseItemAnswers) {
		this.trueFalseItemAnswers = trueFalseItemAnswers;
	}

	public List<String> getCorrespondItemAnswers() {
		return correspondItemAnswers;
	}

	public void setCorrespondItemAnswers(List<String> correspondItemAnswers) {
		this.correspondItemAnswers = correspondItemAnswers;
	}

	public List<String> getIncompleteTextItemAnswers() {
		return incompleteTextItemAnswers;
	}

	public void setIncompleteTextItemAnswers(List<String> incompleteTextItemAnswers) {
		this.incompleteTextItemAnswers = incompleteTextItemAnswers;
	}

	public String getId() {
		return id;
	}

	public User getStudent() {
		return student;
	}

	public void setStudent(User student) {
		this.student = student;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
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

		return "QuizStudent [id=" + id + ", date=" + date + ", student=" + student + ", grade=" + grade + ", subject="
				+ subject + ", quiz=" + quiz + ", multipleSelectionIitemAnswers=" + multipleSelectionIitemAnswers
				+ ", trueFalseItemAnswers=" + trueFalseItemAnswers + ", correspondItemAnswers=" + correspondItemAnswers
				+ ", incompleteTextItemAnswers=" + incompleteTextItemAnswers + ", createdBy=" + createdBy
				+ ", createdDate=" + cDate + ", lastModifiedUser=" + lastModifiedUser + ", lastModifiedDate="
				+ lModified + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((quiz == null) ? 0 : quiz.hashCode());
		result = prime * result + ((student == null) ? 0 : student.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
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
		QuizStudent other = (QuizStudent) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (quiz == null) {
			if (other.quiz != null)
				return false;
		} else if (!quiz.equals(other.quiz))
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
		return true;
	}

}
