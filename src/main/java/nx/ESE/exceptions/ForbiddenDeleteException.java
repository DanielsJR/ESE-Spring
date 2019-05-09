package nx.ESE.exceptions;

public class ForbiddenDeleteException extends Exception {
	
	private static final long serialVersionUID = -953974855153784631L;
	
	public static final String DESCRIPTION = "Prohibido eliminar";

    public ForbiddenDeleteException() {
        super(DESCRIPTION);
    }

    public ForbiddenDeleteException(String detail) {
        super(DESCRIPTION + ". " + detail);
    }

}
