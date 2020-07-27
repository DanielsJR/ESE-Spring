package nx.ese.dtos;

import java.util.Date;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import nx.ese.documents.core.Grade;
import nx.ese.dtos.validators.NX_Pattern;
import nx.ese.utils.NX_DateFormatter;

@NoArgsConstructor
public class GradeDto {

    @Getter
    private String id;

    @Valid
    @Getter
    @Setter
    private UserDto student;

    @Getter
    @Setter
    private double grade;

    @Valid
    @Getter
    @Setter
    private EvaluationDto evaluation;

    @Valid
    @Getter
    @Setter
    private QuizStudentDto quizStudent;

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

    // output
    public GradeDto(Grade grade) {
        this.id = grade.getId();
        this.student = new UserDto(grade.getStudent());
        this.grade = grade.getGrade();
        this.evaluation = new EvaluationDto(grade.getEvaluation());
        if (grade.getQuizStudent() != null) {
            this.quizStudent = new QuizStudentDto(grade.getQuizStudent());
        }
        this.createdBy = grade.getCreatedBy();
        this.createdDate = grade.getCreatedDate();
        this.lastModifiedUser = grade.getLastModifiedUser();
        this.lastModifiedDate = grade.getLastModifiedDate();
    }

    @Override
    public String toString() {
        return "GradeDto [id=" + id + ", student=" + student + ", grade=" + grade + ", evaluation=" + evaluation
                + ", quizStudent=" + quizStudent + ", createdBy=" + createdBy + ", createdDate="
                + NX_DateFormatter.formatterDate(this.createdDate) + ", lastModifiedUser=" + lastModifiedUser + ", lastModifiedDate=" + NX_DateFormatter.formatterDate(this.lastModifiedDate) + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((evaluation == null) ? 0 : evaluation.hashCode());
        long temp;
        temp = Double.doubleToLongBits(grade);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((quizStudent == null) ? 0 : quizStudent.hashCode());
        result = prime * result + ((student == null) ? 0 : student.hashCode());
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
        GradeDto other = (GradeDto) obj;
        if (evaluation == null) {
            if (other.evaluation != null)
                return false;
        } else if (!evaluation.equals(other.evaluation))
            return false;
        if (Double.doubleToLongBits(grade) != Double.doubleToLongBits(other.grade))
            return false;
        if (quizStudent == null) {
            if (other.quizStudent != null)
                return false;
        } else if (!quizStudent.equals(other.quizStudent))
            return false;
        if (student == null) {
            if (other.student != null)
                return false;
        } else if (!student.equals(other.student))
            return false;
        return true;
    }

}
