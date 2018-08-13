package nx.ESE.resources.exceptions;

public class FieldInvalidException extends Exception {
    private static final long serialVersionUID = -1344640670884805385L;

    public static final String DESCRIPTION = " invalido.";

    public FieldInvalidException() {
    	super("Campo" + DESCRIPTION);
    }

    public FieldInvalidException(String detail) {
    	super(detail + DESCRIPTION);
    }

}
