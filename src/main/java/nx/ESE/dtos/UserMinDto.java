package nx.ESE.dtos;

import javax.validation.constraints.NotNull;

public class UserMinDto {

	private String id;

	@NotNull
	private String firstName;

	@NotNull
	private String lastName;

	public UserMinDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserMinDto(String firstName, String lastName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "UserMinDto [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + "]";
	}

}
