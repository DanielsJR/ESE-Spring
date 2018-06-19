package nx.ESE.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import nx.ESE.controllers.AuthenticationController;
import nx.ESE.documents.LoginUser;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/token")
public class AuthenticationResource {

	public static final String GENERATE_TOKEN = "/generate-token";

	@Autowired
	private AuthenticationController authenticationController;

	@PostMapping(GENERATE_TOKEN)
	public ResponseEntity<?> register(@RequestBody LoginUser loginUser) throws AuthenticationException {
		return authenticationController.register(loginUser);
	}

}
