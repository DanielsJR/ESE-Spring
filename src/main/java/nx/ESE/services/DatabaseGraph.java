package nx.ESE.services;

import java.util.List;

import nx.ESE.documents.Token;
import nx.ESE.documents.User;

public class DatabaseGraph {

	private List<User> userList;

	private List<Token> tokenList;

	public DatabaseGraph() {
		// Empty for framework
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public List<Token> getTokenList() {
		return tokenList;
	}

	public void setTokenList(List<Token> tokenList) {
		this.tokenList = tokenList;
	}

}
