package nx.ESE.dtos;

public class UserMinDto {
    
    private String id;
    
    private String firstName;
    
    private String lastName;
    
  
	public UserMinDto() {
		super();
		// TODO Auto-generated constructor stub
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
