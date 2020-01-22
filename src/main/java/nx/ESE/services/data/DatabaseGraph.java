package nx.ESE.services.data;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nx.ESE.documents.Preferences;
import nx.ESE.documents.User;
import nx.ESE.documents.core.Course;
import nx.ESE.documents.core.Evaluation;
import nx.ESE.documents.core.Grade;
import nx.ESE.documents.core.Quiz;
import nx.ESE.documents.core.QuizStudent;
import nx.ESE.documents.core.Subject;

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

}
