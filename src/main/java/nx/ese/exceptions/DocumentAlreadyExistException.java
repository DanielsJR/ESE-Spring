package nx.ese.exceptions;

public class DocumentAlreadyExistException extends Exception {

	private static final long serialVersionUID = 6872183083780815980L;
	
	public static final String DESCRIPTION = " existente.";

    public DocumentAlreadyExistException() {
    	super("Documento" + DESCRIPTION);
    }

    public DocumentAlreadyExistException(String detail) {
    	super(detail + DESCRIPTION);
    }

}
