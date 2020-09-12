package nx.ese.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String HEADER_STRING = "Authorization";

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenProvider jwtProvider;

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        String header = req.getHeader(HEADER_STRING);
        String username = null;
        String authToken = null;

        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            log.info("++++++++++++++ Bearer Found +++++++++++++++");
            authToken = header.replace(TOKEN_PREFIX, "");
            try {
                username = jwtProvider.getUsernameFromToken(authToken);
            } catch (IllegalArgumentException e) {
                log.error("an error occured during getting username from token", e);
            } catch (ExpiredJwtException e) {
                log.warn("the token is expired and not valid anymore", e);
            } catch (SignatureException e) {
                log.error("Authentication Failed. Username or Password not valid.");
            }

        } else {
            log.info("++++++++++++++ Bearer NotFound +++++++++++++++");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtProvider.validateToken(authToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = jwtProvider.getAuthentication(authToken, userDetails);

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.info("Access granted to User: {}, Authorities {}", authentication.getName(), authentication.getAuthorities());
            }
        }

        chain.doFilter(req, res);

    }


}
