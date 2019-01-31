package nx.ESE.services.data;

import java.util.List;

import nx.ESE.documents.Preferences;
import nx.ESE.documents.User;
import nx.ESE.documents.core.Course;
import nx.ESE.documents.core.Grade;
import nx.ESE.documents.core.Quiz;
import nx.ESE.documents.core.QuizStudent;
import nx.ESE.documents.core.Subject;

public class DatabaseGraph {

	private List<User> userList;

	private List<Preferences> preferencesList;

	private List<Course> coursesList;

	private List<Subject> subjectsList;
	
	private List<Grade> gradesList;
	
	private List<Quiz> quizesList;
	
	private List<QuizStudent> quizesStudentList;

	public DatabaseGraph() {
		// Empty for framework
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public List<Preferences> getPreferencesList() {
		return preferencesList;
	}

	public void setPreferencesList(List<Preferences> preferencesList) {
		this.preferencesList = preferencesList;
	}

	public List<Course> getCoursesList() {
		return coursesList;
	}

	public void setCoursesList(List<Course> coursesList) {
		this.coursesList = coursesList;
	}

	public List<Subject> getSubjectsList() {
		return subjectsList;
	}

	public void setSubjectsList(List<Subject> subjectsList) {
		this.subjectsList = subjectsList;
	}

	public List<Grade> getGradesList() {
		return gradesList;
	}

	public void setGradesList(List<Grade> gradesList) {
		this.gradesList = gradesList;
	}

	public List<Quiz> getQuizesList() {
		return quizesList;
	}

	public void setQuizesList(List<Quiz> quizesList) {
		this.quizesList = quizesList;
	}

	public List<QuizStudent> getQuizesStudentList() {
		return quizesStudentList;
	}

	public void setQuizesStudentList(List<QuizStudent> quizesStudentList) {
		this.quizesStudentList = quizesStudentList;
	}
	
	
	
	

}
