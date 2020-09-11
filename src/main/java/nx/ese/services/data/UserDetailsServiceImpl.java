package nx.ese.services.data;

import nx.ese.jwt.JwtAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import nx.ese.documents.Role;
import nx.ese.documents.User;
import nx.ese.repositories.UserRepository;

import java.util.*;

@Service(value = "userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        log.info("++++++++++++++ SettingUserDetails +++++++++++++++");
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return this.userBuilder(user.getUsername(), user.getPassword(), new Role[]{Role.AUTHENTICATED}, user.isActive());
//		return this.userBuilder(user.getUsername(), user.getPassword(), user.getRoles(), user.isActive());
    }

    private org.springframework.security.core.userdetails.User userBuilder(String username, String password, Role[] roles, boolean active) {
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        boolean enabled = active;
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.roleName()));
        }
        return new org.springframework.security.core.userdetails.User(username, password, enabled, accountNonExpired, credentialsNonExpired,
                accountNonLocked, authorities);
    }


}
