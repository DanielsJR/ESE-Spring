package nx.ese.controllers;

import nx.ese.documents.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import nx.ese.documents.LoginUser;
import nx.ese.services.AuthenticationService;

@PreAuthorize("authenticated")
@RestController
@RequestMapping(AuthenticationController.TOKEN)
public class AuthenticationController {
	
	public static final String TOKEN = "/token";
	public static final String GENERATE_TOKEN = "/generate-token";

	@Autowired
	private AuthenticationService authenticationService;


	@PostMapping(GENERATE_TOKEN)
	public ResponseEntity<AuthToken> getToken(@AuthenticationPrincipal User activeUser) {
		return authenticationService.getToken(activeUser);
	}

}
