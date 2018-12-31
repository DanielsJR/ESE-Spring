package nx.ESE;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {


	@Override
	public Optional<String> getCurrentAuditor() {

	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			//System.out.println("****************************************AUDITOR NULL***************************************");
			return Optional.of("UserTest");//null;
		}

		return Optional.of(authentication.getName());
		

	}

}