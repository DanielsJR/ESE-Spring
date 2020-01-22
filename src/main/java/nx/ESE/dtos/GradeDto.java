package nx.ESE.dtos;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nx.ESE.documents.core.Grade;

@NoArgsConstructor
public class GradeDto {

	@Getter
	private String id;

	//@NotNull
	@Valid
	@Getter
	@Setter
	private UserDto student;

	@Getter
	@Setter
	private double grade;

	//@NotNull
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

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@Getter
	private Date createdDate;

	@Getter
	private String lastModifiedUser;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
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

		String cDate = "null";
		if (this.createdDate != null)
			cDate = new SimpleDateFormat("dd-MMM-yyyy").format(createdDate.getTime());

		String lModified = "null";
		if (this.lastModifiedDate != null)
			lModified = new SimpleDateFormat("dd-MMM-yyyy").format(lastModifiedDate.getTime());

		return "GradeDto [id=" + id + ", student=" + student + ", grade=" + grade + ", evaluation=" + evaluation
				+ ", quizStudent=" + quizStudent + ", createdBy=" + createdBy + ", createdDate="
				+ cDate + ", lastModifiedUser=" + lastModifiedUser + ", lastModifiedDate=" + lModified + "]";
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
