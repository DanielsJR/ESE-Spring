package nx.ESE.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

public class ContactInformation {
	
	@Id
	private String id;

	private String mobile;

	private String email;

	private String address;
	
	private String commune;
	
	@DBRef
	private User user;

	public ContactInformation() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ContactInformation(String mobile, String email, String address, String commune, User user) {
		super();
		this.mobile = mobile;
		this.email = email;
		this.address = address;
		this.commune = commune;
		this.user = user;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCommune() {
		return commune;
	}

	public void setCommune(String commune) {
		this.commune = commune;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getId() {
		return id;
	}
	
	

}
