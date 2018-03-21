package nx.ESE.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nx.ESE.controllers.TokenController;
import nx.ESE.dtos.TokenOutputDto;

@RestController
@RequestMapping(TokenResource.TOKENS)
public class TokenResource {

	public static final String TOKENS = "/tokens";
	
	public static final String AUTHENTICATED = "/authenticated";

	@Autowired
	private TokenController tokenController;

	@PreAuthorize("authenticated")
	@PostMapping
	public TokenOutputDto login(@AuthenticationPrincipal User activeUser) {
		return tokenController.login(activeUser.getUsername());
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('TEACHER') or hasRole('STUDENT')")
	@GetMapping(AUTHENTICATED)
	public boolean tokenRoles(@AuthenticationPrincipal User activeUser) {
		return true;
	}

}
