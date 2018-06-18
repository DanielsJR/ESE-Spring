package nx.ESE.controllers;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import nx.ESE.documents.Avatar;
import nx.ESE.documents.Gender;
import nx.ESE.documents.Role;
import nx.ESE.documents.Token;
import nx.ESE.documents.User;
import nx.ESE.dtos.TokenOutputDto;
import nx.ESE.dtos.UserDto;
import nx.ESE.repositories.UserRepository;
import nx.ESE.utils.UtilBase64Image;

@Controller
public class TokenController {

	@Autowired
	private UserRepository userRepository;

	public TokenOutputDto login(String username) {
		User user = userRepository.findByUsername(username);
		assert user != null;
		user.setToken(new Token());
		userRepository.save(user);
		return new TokenOutputDto(user);
	}

	public Optional<UserDto> login2(String username) {
		User user = this.userRepository.findByUsername(username);
		if (user == null) {
			return Optional.empty();
		} else {
			user.setToken(new Token());
			this.setOutPutUserAvatar(user);
			userRepository.save(user);
			return Optional.of(new UserDto(user));

		}
	}

	private void setOutPutUserAvatar(User user) {
		if (user.getAvatar() != null) {
			String path = Avatar.SERVER_AVATAR_PATH + user.getAvatar().getName();
			String avatarBase64 = UtilBase64Image.encoder(path);
			user.getAvatar().setData(avatarBase64);
		} else if (user.getGender() != null && user.getGender().equals(Gender.MUJER)) {
			String femaleAvatarName = Gender.MUJER + "-" + user.getRoles()[0].toString() + ".svg";
			String femaleAvatarType = "image/svg+xml";
			String pathDefaultFemale = Avatar.SERVER_AVATAR_PATH + femaleAvatarName;
			String avatarBase64DefaultFemale = UtilBase64Image.encoder(pathDefaultFemale);
			user.setAvatar(new Avatar(femaleAvatarName, femaleAvatarType, avatarBase64DefaultFemale));
		} else {
			String maleAvatarName = Gender.HOMBRE + "-" + user.getRoles()[0].toString() + ".svg";
			String pathDefaultMale = Avatar.SERVER_AVATAR_PATH + maleAvatarName;
			String maleAvatarType = "image/svg+xml";
			String avatarBase64DefaultMale = UtilBase64Image.encoder(pathDefaultMale);
			user.setAvatar(new Avatar(maleAvatarName, maleAvatarType, avatarBase64DefaultMale));
		}
	}

}
