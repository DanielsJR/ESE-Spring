package nx.ESE.exceptions;

public class PasswordNotMatchException extends Exception {

	private static final long serialVersionUID = -4979228840115853203L;
	public static final String DESCRIPTION = "Contraseña incorrecta";

	public PasswordNotMatchException() {
		super(DESCRIPTION);
	}

	public PasswordNotMatchException(String detail) {
		super(DESCRIPTION + ". " + detail);
	}

}
