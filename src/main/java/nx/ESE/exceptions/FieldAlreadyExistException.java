package nx.ESE.exceptions;

public class FieldAlreadyExistException extends Exception {
	
	private static final long serialVersionUID = -8638280129117812902L;
	
	public static final String DESCRIPTION = " existente.";

    public FieldAlreadyExistException() {
    	super("Campo" + DESCRIPTION);
    }

    public FieldAlreadyExistException(String detail) {
    	super(detail + DESCRIPTION);
    }

}
