package nx.ese.exceptions;

public class FieldNullException extends Exception {

	private static final long serialVersionUID = -5019436814366887698L;

	public static final String DESCRIPTION = " no puede ser null.";


	public FieldNullException() {
		super("Campo" + DESCRIPTION);
	}

	public FieldNullException(String detail) {
		super(detail + DESCRIPTION);
	}

}
