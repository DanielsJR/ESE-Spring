package nx.ESE.services;

import java.util.List;

import nx.ESE.documents.User;

public class DatabaseGraph {

	private List<User> userList;


	public DatabaseGraph() {
		// Empty for framework
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}



}
