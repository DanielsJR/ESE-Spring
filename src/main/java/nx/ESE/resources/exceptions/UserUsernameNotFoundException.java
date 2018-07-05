package nx.ESE.resources.exceptions;

public class UserUsernameNotFoundException extends Exception {
	private static final long serialVersionUID = -1266945421422864441L;
	public static final String DESCRIPTION = "Nombre de usuario no encontrado.";

	public UserUsernameNotFoundException() {
		super(DESCRIPTION);
	}

	public UserUsernameNotFoundException(String detail) {
		super(DESCRIPTION + ". " + detail);
	}

}
