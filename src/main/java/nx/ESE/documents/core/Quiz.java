package nx.ESE.documents.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import nx.ESE.documents.User;

@Document
public class Quiz {

	@Id
	@Getter
	private String id;

	@NotNull
	@Getter
	@Setter
	private String title;

	@Getter
	@Setter
	private String description;

	@DBRef
	@NotNull
	@Valid
	@Getter
	@Setter
	private User author;

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
	private List<CorrespondItem> correspondItems;

	@Getter
	@Setter
	private List<IncompleteTextItem> incompleteTextItems;

	@Getter
	@Setter
	private List<TrueFalseItem> trueFalseItems;

	@Getter
	@Setter
	private List<MultipleSelectionItem> multipleSelectionItems;

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

	@Override
	public String toString() {

		String cDate = "null";
		if (this.createdDate != null)
			cDate = new SimpleDateFormat("dd-MMM-yyyy").format(createdDate.getTime());

		String lModified = "null";
		if (this.lastModifiedDate != null)
			lModified = new SimpleDateFormat("dd-MMM-yyyy").format(lastModifiedDate.getTime());

		return "Quiz [id=" + id + ", title=" + title + ", description=" + description + ", author=" + author
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
		Quiz other = (Quiz) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
