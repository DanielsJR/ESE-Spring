package nx.ESE.documents.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import nx.ESE.documents.User;

@Document
public class Quiz {

	@Id
	private int id;

	private String title;

	private String description;

	private User author;

	private Date date;

	private SubjectName subjectName;

	private List<CorrespondItem> correspondItems;

	private List<IncompleteTextItem> incompleteTextItems;

	private List<TrueFalseItem> trueFalseItems;

	private List<MultipleSelectionItem> multipleSelectionItems;

	@CreatedBy
	private String createdBy;

	@CreatedDate
	private Date createdDate;

	@LastModifiedBy
	private String lastModifiedUser;

	@LastModifiedDate
	private Date lastModifiedDate;

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

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
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

		return "Quiz [id=" + id + ", title=" + title + ", description=" + description + ", author=" + author + ", date="
				+ date + ", subjectName=" + subjectName + ", correspondItems=" + correspondItems
				+ ", incompleteTextItems=" + incompleteTextItems + ", trueFalseItems=" + trueFalseItems
				+ ", multipleSelectionItems=" + multipleSelectionItems + ", createdBy=" + createdBy + ", createdDate="
				+ cDate + ", lastModifiedUser=" + lastModifiedUser + ", lastModifiedDate=" + lModified + "]";
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
		Quiz other = (Quiz) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
