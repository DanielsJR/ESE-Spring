package nx.ESE.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import nx.ESE.documents.Role;
import nx.ESE.documents.User;
import nx.ESE.repositories.UserRepository;

import java.util.*;

@Service(value = "userService")
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository userDao;


	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.findByUsername(username);
		if(user == null){
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return this.userBuilder(user.getUsername(), user.getPassword(), user.getRoles(), user.isActive());
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
