package nx.ESE.dtos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nx.ESE.documents.core.QuizStudent;

@NoArgsConstructor
public class QuizStudentDto {

	@Getter
	private String id;

	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@Getter
	@Setter
	private Date date;

	@NotNull
	@Valid
	@Getter
	@Setter
	private UserDto studentDto;

	@Getter
	@Setter
	private double grade;

	@NotNull
	@Valid
	@Getter
	@Setter
	private SubjectDto subjectDto;

	@NotNull
	@Getter
	@Setter
	private QuizDto quizDto;

	@Getter
	@Setter
	private List<String> multipleSelectionIitemAnswers = new ArrayList<String>();

	@Getter
	@Setter
	private List<Boolean> trueFalseItemAnswers = new ArrayList<Boolean>();

	@Getter
	@Setter
	private List<String> correspondItemAnswers = new ArrayList<String>();

	@Getter
	@Setter
	private List<String> incompleteTextItemAnswers = new ArrayList<String>();

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

	public QuizStudentDto(QuizStudent quizStudent) {
		super();
		this.id = quizStudent.getId();
		this.date = quizStudent.getDate();
		this.studentDto = new UserDto(quizStudent.getStudent());
		this.grade = quizStudent.getGrade();
		this.subjectDto = new SubjectDto(quizStudent.getSubject());
		this.quizDto = new QuizDto(quizStudent.getQuiz());
		this.multipleSelectionIitemAnswers = quizStudent.getMultipleSelectionIitemAnswers();
		this.trueFalseItemAnswers = quizStudent.getTrueFalseItemAnswers();
		this.correspondItemAnswers = quizStudent.getCorrespondItemAnswers();
		this.incompleteTextItemAnswers = quizStudent.getIncompleteTextItemAnswers();
		this.createdBy = quizStudent.getCreatedBy();
		this.createdDate = quizStudent.getCreatedDate();
		this.lastModifiedUser = quizStudent.getLastModifiedUser();
		this.lastModifiedDate = quizStudent.getLastModifiedDate();
	}

	@Override
	public String toString() {

		String fDate = "null";
		if (this.date != null)
			fDate = new SimpleDateFormat("dd-MMM-yyyy").format(date.getTime());

		String cDate = "null";
		if (this.createdDate != null)
			cDate = new SimpleDateFormat("dd-MMM-yyyy").format(createdDate.getTime());

		String lModified = "null";
		if (this.lastModifiedDate != null)
			lModified = new SimpleDateFormat("dd-MMM-yyyy").format(lastModifiedDate.getTime());

		return "QuizStudentDto [id=" + id + ", date=" + fDate + ", studentDto=" + studentDto + ", grade=" + grade
				+ ", subjectDto=" + subjectDto + ", quizDto=" + quizDto + ", multipleSelectionIitemAnswers="
				+ multipleSelectionIitemAnswers + ", trueFalseItemAnswers=" + trueFalseItemAnswers
				+ ", correspondItemAnswers=" + correspondItemAnswers + ", incompleteTextItemAnswers="
				+ incompleteTextItemAnswers + ", createdBy=" + createdBy + ", createdDate=" + cDate
				+ ", lastModifiedUser=" + lastModifiedUser + ", lastModifiedDate=" + lModified + "]";
	}

}
