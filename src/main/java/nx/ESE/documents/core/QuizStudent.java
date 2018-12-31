package nx.ESE.documents.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class QuizStudent {

	@Id
	private int id;

	private Date date;

	private Grade grade;

	private Quiz quiz;

	private Map<String, String> multipleSelectionIitemAnswers = new HashMap<String, String>();
	private Map<String, Boolean> trueFalseItemAnswers = new HashMap<String, Boolean>();
	private Map<String, Map<String, String>> correspondItemAnswers = new HashMap<String, Map<String, String>>();
	private Map<String, String> incompleteTextItemAnswers = new HashMap<String, String>();

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

	public QuizStudent(Date date, Grade grade, Quiz quiz, Map<String, String> multipleSelectionIitemAnswers,
			Map<String, Boolean> trueFalseItemAnswers, Map<String, Map<String, String>> correspondItemAnswers,
			Map<String, String> incompleteTextItemAnswers) {
		super();
		this.date = date;
		this.grade = grade;
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

	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	public Quiz getQuiz() {
		return quiz;
	}

	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}

	public Map<String, String> getMultipleSelectionIitemAnswers() {
		return multipleSelectionIitemAnswers;
	}

	public void setMultipleSelectionIitemAnswers(Map<String, String> multipleSelectionIitemAnswers) {
		this.multipleSelectionIitemAnswers = multipleSelectionIitemAnswers;
	}

	public Map<String, Boolean> getTrueFalseItemAnswers() {
		return trueFalseItemAnswers;
	}

	public void setTrueFalseItemAnswers(Map<String, Boolean> trueFalseItemAnswers) {
		this.trueFalseItemAnswers = trueFalseItemAnswers;
	}

	public Map<String, Map<String, String>> getCorrespondItemAnswers() {
		return correspondItemAnswers;
	}

	public void setCorrespondItemAnswers(Map<String, Map<String, String>> correspondItemAnswers) {
		this.correspondItemAnswers = correspondItemAnswers;
	}

	public Map<String, String> getIncompleteTextItemAnswers() {
		return incompleteTextItemAnswers;
	}

	public void setIncompleteTextItemAnswers(Map<String, String> incompleteTextItemAnswers) {
		this.incompleteTextItemAnswers = incompleteTextItemAnswers;
	}

	public int getId() {
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

		return "QuizStudent [id=" + id + ", date=" + date + ", grade=" + grade + ", quiz=" + quiz
				+ ", multipleSelectionIitemAnswers=" + multipleSelectionIitemAnswers + ", trueFalseItemAnswers="
				+ trueFalseItemAnswers + ", correspondItemAnswers=" + correspondItemAnswers
				+ ", incompleteTextItemAnswers=" + incompleteTextItemAnswers + ", createdBy=" + createdBy
				+ ", createdDate=" + cDate + ", lastModifiedUser=" + lastModifiedUser + ", lastModifiedDate="
				+ lModified + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		if (id != other.id)
			return false;
		return true;
	}

}
