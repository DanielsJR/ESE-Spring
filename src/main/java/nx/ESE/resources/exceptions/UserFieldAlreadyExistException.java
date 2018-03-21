package nx.ESE.resources.exceptions;

public class UserFieldAlreadyExistException extends Exception {
	
	private static final long serialVersionUID = -8638280129117812902L;
	
	public static final String DESCRIPTION = "User Field Already Exist";

    public UserFieldAlreadyExistException() {
        super(DESCRIPTION);
    }

    public UserFieldAlreadyExistException(String detail) {
        super(DESCRIPTION + ". " + detail);
    }

}
