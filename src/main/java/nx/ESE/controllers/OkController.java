package nx.ESE.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(OkController.STATE)
public class OkController {

	public static final String STATE = "/state";

	@PreAuthorize("permitAll")
	@GetMapping()
	public String getState() {
		return "{\"state\":\"ok\"}";
	}

}
