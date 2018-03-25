package nx.ESE.dtos;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import nx.ESE.documents.Commune;
import nx.ESE.documents.Gender;
import nx.ESE.documents.Role;
import nx.ESE.documents.Token;
import nx.ESE.documents.User;
import nx.ESE.dtos.validators.RUTValid;

@JsonInclude(Include.NON_NULL)
public class UserDto {

	private String id;

	@NotNull
	@Pattern(regexp = nx.ESE.dtos.validators.Pattern.USERNAME)
	private String username;

	@Pattern(regexp = nx.ESE.dtos.validators.Pattern.PASSWORD)
	private String password;

	private String firstName;

	private String lastName;

	@RUTValid
	private String dni;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date birthday;

	private Gender gender;

	@Pattern(regexp = nx.ESE.dtos.validators.Pattern.NINE_DIGITS )
	private String mobile;

	@Pattern(regexp = nx.ESE.dtos.validators.Pattern.EMAIL)
	private String email;

	private String address;

	private Commune commune;

	private Role[] roles;

	private Token token;

	private boolean active;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date createdAt;

	public UserDto() {
		super();
	}

	public UserDto(String id, String username, String password, String firstName, String lastName, String dni,
			Date birthday, Gender gender, String mobile, String email, String address, Commune commune, Role[] roles,
			Token token, boolean active, Date createdAt) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.setDni(dni);
		this.birthday = birthday;
		this.setGender(gender);
		this.mobile = mobile;
		this.setEmail(email);
		this.address = address;
		this.commune = commune;
		this.roles = roles;
		this.token = token;
		this.active = active;
		this.createdAt = createdAt;
	}

	public UserDto(String usernamePass) {

		this(null, usernamePass, usernamePass+"@A1", null, null, null, null, null, null, null, null, null, null, null, true,
				null);
	}

	public UserDto(User user) {
		this.id = user.getId();
		this.setUsername(user.getUsername());
		this.password = user.getPassword();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.dni = user.getDni();
		this.birthday = user.getBirthday();
		this.gender = user.getGender();
		this.mobile = user.getMobile();
		this.email = user.getEmail();
		this.address = user.getAddress();
		this.commune = user.getCommune();
		this.roles = user.getRoles();
		this.token = user.getToken();
		this.active = user.isActive();
		this.createdAt = user.getCreatedAt();
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
		if (username != null) {
			this.username = username.toLowerCase();
		} else {
			this.username = username;
		}
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
		if (dni != null) {
		String dniClean = dni.replace("-", "");
		return dniClean.substring(0, dniClean.length() - 1) + "-" + dniClean.substring(dniClean.length() - 1);
		}else{
			return dni;
		}
	}

	public void setDni(String dni) {
		if (dni != null) {
			this.dni = dni.toUpperCase().replace(".", "").replace("-", "");
		} else {
			this.dni = dni;
		}
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
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
		if (email != null) {
			this.email = email.toLowerCase();
		} else {
			this.email = email;
		}
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Commune getCommune() {
		return commune;
	}

	public void setCommune(Commune commune) {
		this.commune = commune;
	}

	@Override
	public String toString() {
		String birthdayF = "null";
		if (this.birthday != null) {
			birthdayF = new SimpleDateFormat("dd-MMM-yyyy").format(birthday.getTime());
		}
		String date = "null";
		if (createdAt != null) {
			date = new SimpleDateFormat("dd-MMM-yyyy").format(createdAt.getTime());
		}
		return "UserDto [id=" + id + ", username=" + username + ", password=" + password + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", dni=" + dni + ", birthday=" + birthdayF + ", gender=" + gender
				+ ", mobile=" + mobile + ", email=" + email + ", address=" + address + ", commune=" + commune
				+ ", roles=" + Arrays.toString(roles) + ", token=" + token + ", active=" + active + ", createdAt="
				+ date + "]";
	}

}
