package nx.ESE.documents.core;

import java.util.Date;

import nx.ESE.utils.NXDateFormatter;

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
public class Grade {

    @Id
    @Getter
    private String id;

    @DBRef
    @Getter
    @Setter
    private User student;

    @DBRef
    @Getter
    @Setter
    private QuizStudent quizStudent;

    @Getter
    @Setter
    private double grade;

    @DBRef
    @Getter
    @Setter
    private Evaluation evaluation;

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

    public Grade(QuizStudent quizStudent, Evaluation evaluation) {
        super();
        this.quizStudent = quizStudent;
        this.evaluation = evaluation;
    }

    public Grade(User student, double grade, Evaluation evaluation) {
        super();
        this.student = student;
        this.grade = grade;
        this.evaluation = evaluation;
    }

    @Override
    public String toString() {
        return "Grade [id=" + id + ", student=" + student + ", evaluation=" + evaluation + ", grade=" + grade
                + ", quizStudent=" + quizStudent + ", createdBy=" + createdBy + ", createdDate="
                + NXDateFormatter.formatterDate(this.createdDate) + ", lastModifiedUser=" + lastModifiedUser + ", lastModifiedDate=" + NXDateFormatter.formatterDate(this.lastModifiedDate) + "]";

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((student == null) ? 0 : student.hashCode());
        result = prime * result + ((evaluation == null) ? 0 : evaluation.hashCode());
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
        Grade other = (Grade) obj;
        if (student == null) {
            if (other.student != null)
                return false;
        } else if (!student.equals(other.student))
            return false;
        if (evaluation == null) {
            if (other.evaluation != null)
                return false;
        } else if (!evaluation.equals(other.evaluation))
            return false;
        return true;
    }

}
