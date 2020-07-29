package nx.ese.controllers;

import nx.ese.documents.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import nx.ese.documents.LoginUser;
import nx.ese.services.AuthenticationService;

@RestController
@RequestMapping(AuthenticationController.TOKEN)
public class AuthenticationController {
	
	public static final String TOKEN = "/token";
	public static final String GENERATE_TOKEN = "/generate-token";

	@Autowired
	private AuthenticationService authenticationService;

	@PostMapping(GENERATE_TOKEN)
	public ResponseEntity<AuthToken> register(@RequestBody LoginUser loginUser) {
		return authenticationService.register(loginUser);
	}

}
