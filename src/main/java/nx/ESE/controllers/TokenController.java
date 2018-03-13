package nx.ESE.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import nx.ESE.documents.Token;
import nx.ESE.documents.User;
import nx.ESE.dtos.TokenOutputDto;
import nx.ESE.repositories.UserRepository;


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

}
