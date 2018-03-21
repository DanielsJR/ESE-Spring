package nx.ESE.resources;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(OkResource.STATE)
public class OkResource {

	public static final String STATE = "/state";

	@PreAuthorize("permitAll")
	@GetMapping()
	public String getState() {
		return "{\"state\":\"ok\"}";
	}

}
