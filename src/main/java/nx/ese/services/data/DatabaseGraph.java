package nx.ese.services.data;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nx.ese.documents.Preferences;
import nx.ese.documents.User;
import nx.ese.documents.core.Attendance;
import nx.ese.documents.core.Course;
import nx.ese.documents.core.Evaluation;
import nx.ese.documents.core.Grade;
import nx.ese.documents.core.Quiz;
import nx.ese.documents.core.QuizStudent;
import nx.ese.documents.core.Subject;

@NoArgsConstructor
public class DatabaseGraph {

	@Getter
	@Setter
	private List<User> userList;

	@Getter
	@Setter
	private List<Preferences> preferencesList;

	@Getter
	@Setter
	private List<Course> coursesList;

	@Getter
	@Setter
	private List<Subject> subjectsList;

	@Getter
	@Setter
	private List<Evaluation> evaluationsList;

	@Getter
	@Setter
	private List<Grade> gradesList;

	@Getter
	@Setter
	private List<Quiz> quizesList;

	@Getter
	@Setter
	private List<QuizStudent> quizesStudentList;
	
	@Getter
	@Setter
	private List<Attendance> attendancesList;

}
