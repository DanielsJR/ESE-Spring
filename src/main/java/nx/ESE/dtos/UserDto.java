package nx.ESE.dtos;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import nx.ESE.documents.Avatar;
import nx.ESE.documents.Commune;
import nx.ESE.documents.Gender;
import nx.ESE.documents.Role;
import nx.ESE.documents.User;
import nx.ESE.dtos.validators.RUTValid;
import nx.ESE.utils.Capitalizer;

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

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date birthday;

	private Gender gender;

	private Avatar avatar;

	@Pattern(regexp = nx.ESE.dtos.validators.Pattern.NINE_DIGITS)
	private String mobile;

	@Pattern(regexp = nx.ESE.dtos.validators.Pattern.EMAIL)
	private String email;

	private String address;

	private Commune commune;

	private Role[] roles;

	private boolean active;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date createdAt;

	public UserDto() {
		super();
	}

	// input
	public UserDto(String id, String username, String password, String firstName, String lastName, String dni,
			Date birthday, Gender gender, Avatar avatar, String mobile, String email, String address, Commune commune,
			Role[] roles, boolean active, Date createdAt) {
		super();
		this.id = id;
		this.setUsername(username);
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.setDni(dni);
		this.birthday = birthday;
		this.setGender(gender);
		this.avatar = avatar;
		this.mobile = mobile;
		this.setEmail(email);
		this.address = address;
		this.setCommune(commune);
		this.roles = roles;
		this.active = active;
		this.createdAt = createdAt;
	}

	public UserDto(String usernamePass) {
		this(null, usernamePass, usernamePass + "@ESE1", null, null, null, null, null, null, null, null, null, null, null,
				true, null);
	}

	// output
	public UserDto(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		// this.password = user.getPassword();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.dni = user.getDni();
		this.birthday = user.getBirthday();
		this.gender = user.getGender();
		this.avatar = user.getAvatar();
		this.mobile = user.getMobile();
		this.email = user.getEmail();
		this.address = user.getAddress();
		this.commune = user.getCommune();
		this.roles = user.getRoles();
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		if (firstName != null) {
			this.firstName = Capitalizer.capitalizer(firstName);
		} else {
			this.firstName = firstName;
		}
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		if (lastName != null) {
			this.lastName = Capitalizer.capitalizer(lastName);
		} else {
			this.lastName = lastName;
		}
	}

	public String getDni() {
		if (dni != null) {
			String dniClean = dni.replace("-", "");
			return dniClean.substring(0, dniClean.length() - 1) + "-" + dniClean.substring(dniClean.length() - 1);
		} else {
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

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Avatar getAvatar() {
		return avatar;
	}

	public void setAvatar(Avatar avatar) {
		this.avatar = avatar;
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
		if (address != null) {
			this.address = Capitalizer.capitalizer(address);
		} else {
			this.address = address;
		}
	}

	public Commune getCommune() {
		return commune;
	}

	public void setCommune(Commune commune) {
		this.commune = commune;
	}
	

	public Role[] getRoles() {
		return roles;
	}

	public void setRoles(Role[] roles) {
		this.roles = roles;
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

	@Override
	public String toString() {

		String birthdayF = "null";
		if (this.birthday != null) {
			birthdayF = new SimpleDateFormat("dd/MM/yyyy").format(birthday.getTime());
		}

		String date = "null";
		if (createdAt != null) {
			date = new SimpleDateFormat("dd/MM/yyyy").format(createdAt.getTime());
		}
		return "UserDto [id=" + id + ", username=" + username + ", password=" + password + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", dni=" + dni + ", birthday=" + birthdayF + ", gender=" + gender
				+ ", mobile=" + mobile + ", avatar=" + avatar + ", email=" + email + ", address=" + address
				+ ", commune=" + commune + ", roles=" + Arrays.toString(roles) + ", active=" + active + ", createdAt="
				+ date + "]";
	}

}
