package nx.ESE.dtos;

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
import nx.ESE.dtos.validators.NXPattern;
import nx.ESE.utils.NXDateFormatter;

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = NXPattern.DATE_FORMAT)
    @Getter
    private Date createdDate;

    @Getter
    private String lastModifiedUser;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = NXPattern.DATE_FORMAT)
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
        return "QuizDto [id=" + id + ", title=" + title + ", description=" + description + ", author=" + author
                + ", subjectName=" + subjectName + ", quizLevel=" + quizLevel + ", correspondItems=" + correspondItems
                + ", incompleteTextItems=" + incompleteTextItems + ", trueFalseItems=" + trueFalseItems
                + ", multipleSelectionItems=" + multipleSelectionItems + ", createdBy=" + createdBy + ", createdDate="
                + NXDateFormatter.formatterDate(this.createdDate) + ", lastModifiedUser=" + lastModifiedUser + ", lastModifiedDate=" + NXDateFormatter.formatterDate(this.lastModifiedDate) + "]";
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
        QuizDto other = (QuizDto) obj;
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
