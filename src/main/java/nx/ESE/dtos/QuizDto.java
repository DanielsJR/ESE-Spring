package nx.ESE.dtos;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import nx.ESE.documents.core.CorrespondItem;
import nx.ESE.documents.core.IncompleteTextItem;
import nx.ESE.documents.core.MultipleSelectionItem;
import nx.ESE.documents.core.Quiz;
import nx.ESE.documents.core.SubjectName;
import nx.ESE.documents.core.TrueFalseItem;

public class QuizDto {

	private String id;

	@NotNull
	private String title;

	private String description;

	@NotNull
	private UserDto author;

	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date date;

	@NotNull
	private SubjectName subjectName;

	private List<CorrespondItem> correspondItems;

	private List<IncompleteTextItem> incompleteTextItems;

	private List<TrueFalseItem> trueFalseItems;

	private List<MultipleSelectionItem> multipleSelectionItems;

	private String createdBy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date createdDate;

	private String lastModifiedUser;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date lastModifiedDate;

	public QuizDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	//input
	public QuizDto(String id, String title, String description, UserDto author, Date date, SubjectName subjectName,
			List<CorrespondItem> correspondItems, List<IncompleteTextItem> incompleteTextItems,
			List<TrueFalseItem> trueFalseItems, List<MultipleSelectionItem> multipleSelectionItems) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.author = author;
		this.date = date;
		this.subjectName = subjectName;
		this.correspondItems = correspondItems;
		this.incompleteTextItems = incompleteTextItems;
		this.trueFalseItems = trueFalseItems;
		this.multipleSelectionItems = multipleSelectionItems;

	}

	//output
	public QuizDto(Quiz quiz) {
		this.id = quiz.getId();
		this.title = quiz.getTitle();
		this.description = quiz.getDescription();
		this.author = new UserDto(quiz.getAuthor());
		this.date = quiz.getDate();
		this.subjectName = quiz.getSubjectName();
		this.correspondItems = quiz.getCorrespondItems();
		this.incompleteTextItems = quiz.getIncompleteTextItems();
		this.trueFalseItems = quiz.getTrueFalseItems();
		this.multipleSelectionItems = quiz.getMultipleSelectionItems();
		this.createdBy = quiz.getCreatedBy();
		this.createdDate = quiz.getCreatedDate();
		this.lastModifiedUser = quiz.getLastModifiedUser();
		this.lastModifiedDate = quiz.getLastModifiedDate();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public UserDto getAuthor() {
		return author;
	}

	public void setAuthor(UserDto author) {
		this.author = author;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public SubjectName getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(SubjectName subjectName) {
		this.subjectName = subjectName;
	}

	public List<CorrespondItem> getCorrespondItems() {
		return correspondItems;
	}

	public void setCorrespondItems(List<CorrespondItem> correspondItems) {
		this.correspondItems = correspondItems;
	}

	public List<IncompleteTextItem> getIncompleteTextItems() {
		return incompleteTextItems;
	}

	public void setIncompleteTextItems(List<IncompleteTextItem> incompleteTextItems) {
		this.incompleteTextItems = incompleteTextItems;
	}

	public List<TrueFalseItem> getTrueFalseItems() {
		return trueFalseItems;
	}

	public void setTrueFalseItems(List<TrueFalseItem> trueFalseItems) {
		this.trueFalseItems = trueFalseItems;
	}

	public List<MultipleSelectionItem> getMultipleSelectionItems() {
		return multipleSelectionItems;
	}

	public void setMultipleSelectionItems(List<MultipleSelectionItem> multipleSelectionItems) {
		this.multipleSelectionItems = multipleSelectionItems;
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
		String fDate = "null";
		if (this.date != null)
			fDate = new SimpleDateFormat("dd-MMM-yyyy").format(date.getTime());
		
		String cDate = "null";
		if (this.createdDate != null)
			cDate = new SimpleDateFormat("dd-MMM-yyyy").format(createdDate.getTime());

		String lModified = "null";
		if (this.lastModifiedDate != null)
			lModified = new SimpleDateFormat("dd-MMM-yyyy").format(lastModifiedDate.getTime());
		
		return "QuizDto [id=" + id + ", title=" + title + ", description=" + description + ", author=" + author
				+ ", date=" + fDate + ", subjectName=" + subjectName + ", correspondItems=" + correspondItems
				+ ", incompleteTextItems=" + incompleteTextItems + ", trueFalseItems=" + trueFalseItems
				+ ", multipleSelectionItems=" + multipleSelectionItems + ", createdBy=" + createdBy + ", createdDate="
				+ cDate + ", lastModifiedUser=" + lastModifiedUser + ", lastModifiedDate=" + lModified
				+ "]";
	}
	
	

}
