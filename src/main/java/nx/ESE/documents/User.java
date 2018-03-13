package nx.ESE.documents;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Document
public class User {

	@Id
	private String id;

	@Indexed(unique = true)
	private String username;

	private String password;

	private Gender gender;

	private Role[] roles;

	private Token token;

	private boolean active;

	private Date createdAt;

	public User() {
		createdAt = new Date();
		active = true;
	}
	
	public User(String username, String password) {
		this();
		this.username = username;
		this.password = password;
		this.gender = Gender.MALE;
		this.setPassword(password);
		this.roles = new Role[] { Role.STUDENT };
	}

	public User(String username, String password, Gender gender) {
		this();
		this.username = username;
		this.password = password;
		this.gender = gender;
		this.setPassword(password);
		this.roles = new Role[] { Role.STUDENT };
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = new BCryptPasswordEncoder().encode(password);
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Role[] getRoles() {
		return roles;
	}

	public void setRoles(Role[] roles) {
		this.roles = roles;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getId() {
		return id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}
	
	

	@Override
	public String toString() {
	       String date = "null";
	        if (createdAt != null) {
	            date = new SimpleDateFormat("dd-MMM-yyyy").format(createdAt.getTime());
	        }
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", gender=" + gender
				+ ", roles=" + Arrays.toString(roles) + ", token=" + token + ", active=" + active + ", createdAt="
				+ date + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	
	

}