package nx.ESE.resources.exceptions;

public class FieldNotFoundException extends Exception {
	private static final long serialVersionUID = -1344640670884805385L;

	public static final String DESCRIPTION = " no encontrado.";

	public FieldNotFoundException() {
		super("Campo" + DESCRIPTION);
	}

	public FieldNotFoundException(String detail) {
		super(detail + DESCRIPTION);
	}

}
