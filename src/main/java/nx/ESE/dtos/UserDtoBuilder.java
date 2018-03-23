package nx.ESE.dtos;

import java.util.Calendar;
import java.util.Date;

import nx.ESE.documents.Gender;
import nx.ESE.documents.Role;
import nx.ESE.documents.Token;

public class UserDtoBuilder {

	private UserDto userDto;

	public UserDtoBuilder() {
		super();
		this.userDto = new UserDto();
	}


	public UserDtoBuilder id(String id) {
		this.userDto.setId(id);
		return this;
	}

	public UserDtoBuilder username(String username) {
		this.userDto.setUsername(username);
		return this;
	}

	public UserDtoBuilder password(String password) {
		this.userDto.setPassword(password);
		return this;
	}
	
	public UserDtoBuilder usernamePassword(String usernamePassword) {
		this.userDto.setUsername(usernamePassword);
		this.userDto.setPassword(usernamePassword);
		return this;
	}

	public UserDtoBuilder gender(Gender gender) {
		this.userDto.setGender(gender);
		return this;
	}

	public UserDtoBuilder firstName(String firstName) {
		this.userDto.setFirstName(firstName);
		return this;
	}

	public UserDtoBuilder lastName(String lastName) {
		this.userDto.setLastName(lastName);
		return this;
	}

	public UserDtoBuilder dni(String dni) {
		this.userDto.setDni(dni);
		return this;
	}

	public UserDtoBuilder age(Date birthday) {
		this.userDto.setBirthday(birthday);
		return this;
	}

	public UserDtoBuilder mobile(String mobile) {
		this.userDto.setMobile(mobile);
		return this;
	}

	public UserDtoBuilder email(String email) {
		this.userDto.setEmail(email);
		return this;
	}

	public UserDtoBuilder address(String address) {
		this.userDto.setAddress(address);
		return this;
	}

	public UserDtoBuilder commune(String commune) {
		this.userDto.setCommune(commune);
		return this;
	}

	public UserDtoBuilder roles(Role[] roles) {
		this.userDto.setRoles(roles);
		return this;
	}

	public UserDtoBuilder createdAt(Date createdAt) {
		this.userDto.setCreatedAt(createdAt);
		return this;
	}

	public UserDtoBuilder token(Token token) {
		this.userDto.setToken(token);
		return this;
	}

	public UserDtoBuilder active(boolean active) {
		this.userDto.setActive(active);
		return this;
	}

	public UserDto build() {
		return this.userDto;
	}

}
