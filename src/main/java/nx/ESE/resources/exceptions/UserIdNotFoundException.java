package nx.ESE.resources.exceptions;

public class UserIdNotFoundException extends Exception {
    private static final long serialVersionUID = -1344640670884805385L;

    public static final String DESCRIPTION = "Id de usuario no encontrado.";

    public UserIdNotFoundException() {
        super(DESCRIPTION);
    }

    public UserIdNotFoundException(String detail) {
        super(DESCRIPTION + ". " + detail);
    }

}
