package nx.ESE.dtos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import nx.ESE.documents.core.QuizStudent;

public class QuizStudentDto {

	private String id;

	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date date;

	@NotNull
	private UserDto studentDto;

	private double grade;

	@NotNull
	private SubjectDto subjectDto;

	@NotNull
	private QuizDto quizDto;

	private List<String> multipleSelectionIitemAnswers = new ArrayList<String>();

	private List<Boolean> trueFalseItemAnswers = new ArrayList<Boolean>();

	private List<String> correspondItemAnswers = new ArrayList<String>();

	private List<String> incompleteTextItemAnswers = new ArrayList<String>();

	private String createdBy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date createdDate;

	private String lastModifiedUser;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date lastModifiedDate;

	public QuizStudentDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QuizStudentDto(String id, Date date, UserDto studentDto, double grade, SubjectDto subjectDto,
			QuizDto quizDto, List<String> multipleSelectionIitemAnswers, List<Boolean> trueFalseItemAnswers,
			List<String> correspondItemAnswers, List<String> incompleteTextItemAnswers, String createdBy,
			Date createdDate, String lastModifiedUser, Date lastModifiedDate) {
		super();
		this.id = id;
		this.date = date;
		this.studentDto = studentDto;
		this.grade = grade;
		this.subjectDto = subjectDto;
		this.quizDto = quizDto;
		this.multipleSelectionIitemAnswers = multipleSelectionIitemAnswers;
		this.trueFalseItemAnswers = trueFalseItemAnswers;
		this.correspondItemAnswers = correspondItemAnswers;
		this.incompleteTextItemAnswers = incompleteTextItemAnswers;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.lastModifiedUser = lastModifiedUser;
		this.lastModifiedDate = lastModifiedDate;
	}

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

	public String getId() {
		return id;
	}

	public Date getDate() {
		return date;
	}

	public UserDto getStudentDto() {
		return studentDto;
	}

	public double getGrade() {
		return grade;
	}

	public SubjectDto getSubjectDto() {
		return subjectDto;
	}

	public QuizDto getQuizDto() {
		return quizDto;
	}

	public List<String> getMultipleSelectionIitemAnswers() {
		return multipleSelectionIitemAnswers;
	}

	public List<Boolean> getTrueFalseItemAnswers() {
		return trueFalseItemAnswers;
	}

	public List<String> getCorrespondItemAnswers() {
		return correspondItemAnswers;
	}

	public List<String> getIncompleteTextItemAnswers() {
		return incompleteTextItemAnswers;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public String getLastModifiedUser() {
		return lastModifiedUser;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
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
