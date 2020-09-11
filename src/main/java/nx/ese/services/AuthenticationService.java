package nx.ese.services;

import nx.ese.documents.Role;
import nx.ese.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import nx.ese.jwt.TokenProvider;
import nx.ese.documents.AuthToken;
import nx.ese.documents.LoginUser;

import java.util.Arrays;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenProvider jwtTokenProvider;

    public ResponseEntity<AuthToken> getToken(User activeUser) {
//		final String token = jwtTokenProvider.generateToken(SecurityContextHolder.getContext().getAuthentication());

        final String token = userRepository.findByUsernameOptional(activeUser.getUsername())
                .map(user -> {
                    String[] roles = Arrays.stream(user.getRoles()).map(Role::roleName).toArray(String[]::new);
                    return jwtTokenProvider.generateToken(user.getUsername(), roles);
                }).orElseThrow();

        return ResponseEntity.ok(new AuthToken(token));
    }

}
