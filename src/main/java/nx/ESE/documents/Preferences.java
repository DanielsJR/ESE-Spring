package nx.ESE.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Preferences {

	@Id
	private String id;
	
	@DBRef
	private User user;

	private Theme theme;

	public Preferences() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public Preferences(User user, Theme theme) {
		super();
		this.user = user;
		this.theme = theme;
	}


	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Theme getTheme() {
		return theme;
	}

	public void setTheme(Theme theme) {
		this.theme = theme;
	}

}
