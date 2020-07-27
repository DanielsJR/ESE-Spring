package nx.ese.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

@Component
public class WebSocketAuthChannelInterceptorAdapter extends ChannelInterceptorAdapter {

	public static final String TOKEN_PREFIX = "Bearer ";

	public static final String HEADER_STRING = "Authorization";

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private TokenProvider jwtTokenProvider;

	private static final Logger logger = LoggerFactory.getLogger(WebSocketAuthChannelInterceptorAdapter.class);

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		SecurityContext securityContext = SecurityContextHolder.getContext();

		if (StompCommand.CONNECT.equals(accessor.getCommand())) {
			String header = accessor.getFirstNativeHeader(HEADER_STRING);
			String username = null;
			String authToken = null;

			if (header != null && header.startsWith(TOKEN_PREFIX)) {
				authToken = header.replace(TOKEN_PREFIX, "");
				try {
					username = jwtTokenProvider.getUsernameFromToken(authToken);
				} catch (IllegalArgumentException e) {
					logger.error("an error occured during getting username from token", e);
				} catch (ExpiredJwtException e) {
					logger.warn("the token is expired and not valid anymore", e);
				} catch (SignatureException e) {
					logger.error("Authentication Failed. Username or Password not valid.");
				}
			} else {
				logger.warn("couldn't find bearer string, will ignore the header");
			}

			if (username != null && securityContext.getAuthentication() == null) {

				UserDetails userDetails = userDetailsService.loadUserByUsername(username);

				if (jwtTokenProvider.validateToken(authToken, userDetails)) {
					UsernamePasswordAuthenticationToken authentication = jwtTokenProvider.getAuthentication(authToken,
							securityContext.getAuthentication(), userDetails);

					accessor.setUser(authentication);
					logger.info("Access granted to:" + username + "in WebSocket");
				}
			}
		}
		return message;
	}

}
