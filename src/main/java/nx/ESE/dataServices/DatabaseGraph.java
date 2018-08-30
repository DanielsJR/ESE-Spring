package nx.ESE.dataServices;

import java.util.List;

import nx.ESE.documents.Preferences;
import nx.ESE.documents.User;
import nx.ESE.documents.core.Course;
import nx.ESE.documents.core.Subject;

public class DatabaseGraph {

	private List<User> userList;

	private List<Preferences> preferencesList;

	private List<Course> coursesList;

	private List<Subject> subjectsList;

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

}
