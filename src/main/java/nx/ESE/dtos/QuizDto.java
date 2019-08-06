package nx.ESE.dtos;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nx.ESE.documents.core.CorrespondItem;
import nx.ESE.documents.core.IncompleteTextItem;
import nx.ESE.documents.core.MultipleSelectionItem;
import nx.ESE.documents.core.Quiz;
import nx.ESE.documents.core.QuizLevel;
import nx.ESE.documents.core.SubjectName;
import nx.ESE.documents.core.TrueFalseItem;

@NoArgsConstructor
public class QuizDto {

	@Getter
	private String id;

	@NotNull
	@Getter
	@Setter
	private String title;

	@Getter
	@Setter
	private String description;

	@NotNull
	@Valid
	@Getter
	@Setter
	private UserDto author;

	@NotNull
	@Getter
	@Setter
	private SubjectName subjectName;

	@NotNull
	@Getter
	@Setter
	private QuizLevel quizLevel;

	@Getter
	@Setter
	private List<@Valid CorrespondItem> correspondItems;

	@Getter
	@Setter
	private List<@Valid IncompleteTextItem> incompleteTextItems;

	@Getter
	@Setter
	private List<@Valid TrueFalseItem> trueFalseItems;

	@Getter
	@Setter
	private List<@Valid MultipleSelectionItem> multipleSelectionItems;

	@Getter
	private String createdBy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@Getter
	private Date createdDate;

	@Getter
	private String lastModifiedUser;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@Getter
	private Date lastModifiedDate;

	// output
	public QuizDto(Quiz quiz) {
		this.id = quiz.getId();
		this.title = quiz.getTitle();
		this.description = quiz.getDescription();
		this.author = new UserDto(quiz.getAuthor());
		this.subjectName = quiz.getSubjectName();
		this.quizLevel = quiz.getQuizLevel();
		this.correspondItems = quiz.getCorrespondItems();
		this.incompleteTextItems = quiz.getIncompleteTextItems();
		this.trueFalseItems = quiz.getTrueFalseItems();
		this.multipleSelectionItems = quiz.getMultipleSelectionItems();
		this.createdBy = quiz.getCreatedBy();
		this.createdDate = quiz.getCreatedDate();
		this.lastModifiedUser = quiz.getLastModifiedUser();
		this.lastModifiedDate = quiz.getLastModifiedDate();
	}

	@Override
	public String toString() {

		String cDate = "null";
		if (this.createdDate != null)
			cDate = new SimpleDateFormat("dd-MMM-yyyy").format(createdDate.getTime());

		String lModified = "null";
		if (this.lastModifiedDate != null)
			lModified = new SimpleDateFormat("dd-MMM-yyyy").format(lastModifiedDate.getTime());

		return "QuizDto [id=" + id + ", title=" + title + ", description=" + description + ", author=" + author
				+ ", subjectName=" + subjectName + ", quizLevel=" + quizLevel + ", correspondItems=" + correspondItems
				+ ", incompleteTextItems=" + incompleteTextItems + ", trueFalseItems=" + trueFalseItems
				+ ", multipleSelectionItems=" + multipleSelectionItems + ", createdBy=" + createdBy + ", createdDate="
				+ cDate + ", lastModifiedUser=" + lastModifiedUser + ", lastModifiedDate=" + lModified + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		QuizDto other = (QuizDto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
