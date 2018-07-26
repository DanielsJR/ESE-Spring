package nx.ESE.services;

import java.util.List;

import nx.ESE.documents.Preferences;
import nx.ESE.documents.User;

public class DatabaseGraph {

	private List<User> userList;
	
	private List<Preferences> preferencesList;


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


	
	



}
