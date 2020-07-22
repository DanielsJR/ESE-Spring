package nx.ese.documents.core;

import java.util.Date;
import java.util.List;

import nx.ese.utils.NX_DateFormatter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
        return "QuizStudent [" + "id=" + id
                + ", multipleSelectionIitems=" + multipleSelectionItems + ", trueFalseItems=" + trueFalseItems
                + ", correspondItems=" + correspondItems + ", incompleteTextItems=" + incompleteTextItems
                + ", createdBy=" + createdBy + ", createdDate=" + NX_DateFormatter.formatterDate(this.createdDate) + ", lastModifiedUser=" + lastModifiedUser
                + ", lastModifiedDate=" + NX_DateFormatter.formatterDate(this.lastModifiedDate) + "]";
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
