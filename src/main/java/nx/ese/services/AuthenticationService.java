package nx.ese.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import nx.ese.config.TokenProvider;
import nx.ese.documents.AuthToken;
import nx.ese.documents.LoginUser;

@Service
public class AuthenticationService {
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenProvider jwtTokenProvider;
	
	public ResponseEntity<?> register(LoginUser loginUser) {

		final Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		final String token = jwtTokenProvider.generateToken(authentication);

		return ResponseEntity.ok(new AuthToken(token));
	}

}
