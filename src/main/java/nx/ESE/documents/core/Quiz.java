package nx.ESE.documents.core;

import java.util.Date;
import java.util.List;

import nx.ESE.documents.User;

public class Quiz {
	

	private int id;

	private String title;

	
	//@Column(name = "content_evaluate")
	private String description;

	//@ManyToOne
	private User author;

	private Date date;

	private SubjectName subjectName;

	//@OneToMany // (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<CorrespondItem> correspondItems;

	//@OneToMany // (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<IncompleteTextItem> incompleteTextItems;

	//@OneToMany // (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<TrueFalseItem> trueFalseItems;

	//@OneToMany // (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<MultipleSelectionItem> multipleSelectionItems;

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


	
	

}
