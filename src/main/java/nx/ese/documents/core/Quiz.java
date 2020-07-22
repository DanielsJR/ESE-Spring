package nx.ese.documents.core;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import nx.ese.utils.NX_DateFormatter;
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
import nx.ese.documents.User;

@NoArgsConstructor
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
        return "Quiz [id=" + id + ", title=" + title + ", description=" + description + ", author=" + author
                + ", subjectName=" + subjectName + ", quizLevel=" + quizLevel + ", correspondItems=" + correspondItems
                + ", incompleteTextItems=" + incompleteTextItems + ", trueFalseItems=" + trueFalseItems
                + ", multipleSelectionItems=" + multipleSelectionItems + ", createdBy=" + createdBy + ", createdDate="
                + NX_DateFormatter.formatterDate(this.createdDate) + ", lastModifiedUser=" + lastModifiedUser + ", lastModifiedDate=" + NX_DateFormatter.formatterDate(this.lastModifiedDate) + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((author == null) ? 0 : author.hashCode());
        result = prime * result + ((correspondItems == null) ? 0 : correspondItems.hashCode());
        result = prime * result + ((incompleteTextItems == null) ? 0 : incompleteTextItems.hashCode());
        result = prime * result + ((multipleSelectionItems == null) ? 0 : multipleSelectionItems.hashCode());
        result = prime * result + ((quizLevel == null) ? 0 : quizLevel.hashCode());
        result = prime * result + ((subjectName == null) ? 0 : subjectName.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((trueFalseItems == null) ? 0 : trueFalseItems.hashCode());
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
        if (author == null) {
            if (other.author != null)
                return false;
        } else if (!author.equals(other.author))
            return false;
        if (correspondItems == null) {
            if (other.correspondItems != null)
                return false;
        } else if (!correspondItems.equals(other.correspondItems))
            return false;
        if (incompleteTextItems == null) {
            if (other.incompleteTextItems != null)
                return false;
        } else if (!incompleteTextItems.equals(other.incompleteTextItems))
            return false;
        if (multipleSelectionItems == null) {
            if (other.multipleSelectionItems != null)
                return false;
        } else if (!multipleSelectionItems.equals(other.multipleSelectionItems))
            return false;
        if (quizLevel != other.quizLevel)
            return false;
        if (subjectName != other.subjectName)
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (trueFalseItems == null) {
            if (other.trueFalseItems != null)
                return false;
        } else if (!trueFalseItems.equals(other.trueFalseItems))
            return false;
        return true;
    }


}
