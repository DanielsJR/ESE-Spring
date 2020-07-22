package nx.ESE.dtos;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import nx.ESE.documents.core.Evaluation;
import nx.ESE.documents.core.EvaluationType;
import nx.ESE.dtos.validators.NX_Pattern;
import nx.ESE.utils.NX_DateFormatter;

@NoArgsConstructor
public class EvaluationDto {

    @Getter
    private String id;

    @NotNull
    @Getter
    @Setter
    private EvaluationType type;

    @NotNull
    @Getter
    @Setter
    private String title;

    @NotNull
    @Getter
    @Setter
    private SubjectDto subject;

    @Getter
    @Setter
    private QuizDto quiz;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = NX_Pattern.DATE_FORMAT)
    @NotNull
    @Getter
    @Setter
    private Date date;

    //@Getter
    //@Setter
    private boolean isOpen;

    @Getter
    private String createdBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = NX_Pattern.DATE_FORMAT)
    @Getter
    private Date createdDate;

    @Getter
    private String lastModifiedUser;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = NX_Pattern.DATE_FORMAT)
    @Getter
    private Date lastModifiedDate;

    public boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    // output
    public EvaluationDto(Evaluation evaluation) {
        this.id = evaluation.getId();
        this.type = evaluation.getType();
        this.title = evaluation.getTitle();
        this.subject = new SubjectDto(evaluation.getSubject());
        if (evaluation.getQuiz() != null) {
            this.quiz = new QuizDto(evaluation.getQuiz());
        }
        this.date = evaluation.getDate();
        this.isOpen = evaluation.getIsOpen();
        this.createdBy = evaluation.getCreatedBy();
        this.createdDate = evaluation.getCreatedDate();
        this.lastModifiedUser = evaluation.getLastModifiedUser();
        this.lastModifiedDate = evaluation.getLastModifiedDate();

    }

    @Override
    public String toString() {
        return "EvaluationDto [id=" + id + ", type=" + type + ", title=" + title + ", subject=" + subject + ", quiz="
                + quiz + ", date=" + NX_DateFormatter.formatterDate(this.date) + ", isOpen=" + isOpen + ", createdBy=" + createdBy + ", createdDate=" + NX_DateFormatter.formatterDate(this.createdDate)
                + ", lastModifiedUser=" + lastModifiedUser + ", lastModifiedDate=" + NX_DateFormatter.formatterDate(this.lastModifiedDate) + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((quiz == null) ? 0 : quiz.hashCode());
        result = prime * result + ((subject == null) ? 0 : subject.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        EvaluationDto other = (EvaluationDto) obj;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (quiz == null) {
            if (other.quiz != null)
                return false;
        } else if (!quiz.equals(other.quiz))
            return false;
        if (subject == null) {
            if (other.subject != null)
                return false;
        } else if (!subject.equals(other.subject))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

}
