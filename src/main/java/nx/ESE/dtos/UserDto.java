package nx.ESE.dtos;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import nx.ESE.documents.Gender;
import nx.ESE.documents.Role;
import nx.ESE.documents.Token;

public class UserDto {

	private String id;

	private String username;

	private String password;

	private Gender gender;

	private Role[] roles;

	private Token token;

	private boolean active;

	private Date createdAt;

	private String firstName;

	private String lastName;

	private String dni;

	private int age;

	private String mobile;

	private String email;

	private String address;

	private String commune;

	public UserDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserDto(String id, String username, String password, Gender gender, Role[] roles, Token token, boolean active,
			Date createdAt, String firstName, String lastName, String dni, int age, String mobile, String email,
			String address, String commune) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.gender = gender;
		this.roles = roles;
		this.token = token;
		this.active = active;
		this.createdAt = createdAt;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dni = dni;
		this.age = age;
		this.mobile = mobile;
		this.email = email;
		this.address = address;
		this.commune = commune;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
		this.password = password;
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

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
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

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
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

	@Override
	public String toString() {
		String date = "null";
		if (createdAt != null) {
			date = new SimpleDateFormat("dd-MMM-yyyy").format(createdAt.getTime());
		}
		return "UserDto [id=" + id + ", username=" + username + ", password=" + password + ", gender=" + gender
				+ ", roles=" + Arrays.toString(roles) + ", token=" + token + ", active=" + active + ", createdAt="
				+ date + ", firstName=" + firstName + ", lastName=" + lastName + ", dni=" + dni + ", age=" + age
				+ ", mobile=" + mobile + ", email=" + email + ", address=" + address + ", commune=" + commune + "]";
	}



}
