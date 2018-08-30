package nx.ESE.exceptions;

public class DocumentNotFoundException extends Exception {

	private static final long serialVersionUID = 4955564690503047631L;

	public static final String DESCRIPTION = " no encontrado.";

	public DocumentNotFoundException() {
		super("Documento" + DESCRIPTION);
	}

	public DocumentNotFoundException(String detail) {
		super(detail + DESCRIPTION);
	}

}
