package nx.ese.exceptions;

public class ForbiddenChangeRoleException extends Exception {

	private static final long serialVersionUID = -2872771171552160753L;

	public static final String DESCRIPTION = "Prohibido cambiar role";

	public ForbiddenChangeRoleException() {
		super(DESCRIPTION);
	}

	public ForbiddenChangeRoleException(String detail) {
		super(DESCRIPTION + ". " + detail);
	}

}
