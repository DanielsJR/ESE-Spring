package nx.ESE.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import nx.ESE.documents.LoginUser;
import nx.ESE.services.AuthenticationService;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(AuthenticationController.TOKEN)
public class AuthenticationController {
	
	public static final String TOKEN = "/token";
	public static final String GENERATE_TOKEN = "/generate-token";

	@Autowired
	private AuthenticationService authenticationService;

	@PostMapping(GENERATE_TOKEN)
	public ResponseEntity<?> register(@RequestBody LoginUser loginUser) throws AuthenticationException {
		return authenticationService.register(loginUser);
	}

}
