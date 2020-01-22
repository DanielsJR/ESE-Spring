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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nx.ESE.documents.User;

@NoArgsConstructor
@Document
public class QuizStudent {

	@Id
	@Getter
	private String id;

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
	@Setter
	private String createdBy;

	@CreatedDate
	@Getter
	@Setter
	private Date createdDate;

	@LastModifiedBy
	@Getter
	@Setter
	private String lastModifiedUser;

	@LastModifiedDate
	@Getter
	@Setter
	private Date lastModifiedDate;

	public QuizStudent(List<MultipleSelectionItem> multipleSelectionItems,
			List<TrueFalseItem> trueFalseItems, List<CorrespondItem> correspondItems,
			List<IncompleteTextItem> incompleteTextItems) {
		super();
		this.multipleSelectionItems = multipleSelectionItems;
		this.trueFalseItems = trueFalseItems;
		this.correspondItems = correspondItems;
		this.incompleteTextItems = incompleteTextItems;
	}

	@Override
	public String toString() {

		String cDate = "null";
		if (this.createdDate != null)
			cDate = new SimpleDateFormat("dd-MMM-yyyy").format(createdDate.getTime());

		String lModified = "null";
		if (this.lastModifiedDate != null)
			lModified = new SimpleDateFormat("dd-MMM-yyyy").format(lastModifiedDate.getTime());

		return "QuizStudent [" + "id=" + id
				+ ", multipleSelectionIitems=" + multipleSelectionItems + ", trueFalseItems=" + trueFalseItems
				+ ", correspondItems=" + correspondItems + ", incompleteTextItems=" + incompleteTextItems
				+ ", createdBy=" + createdBy + ", createdDate=" + cDate + ", lastModifiedUser=" + lastModifiedUser
				+ ", lastModifiedDate=" + lModified + "]";
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
		QuizStudent other = (QuizStudent) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;

		return true;
	}

}
