package nx.ese.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class LoginUser {

	@Setter
	@Getter
	private String username;
	
	@Setter
	@Getter
	private String password;

}
