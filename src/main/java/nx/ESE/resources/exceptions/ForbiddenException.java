package nx.ESE.resources.exceptions;

public class ForbiddenException extends Exception {
    private static final long serialVersionUID = -1344640670884805385L;

    public static final String DESCRIPTION = "Prohibido. Role Insuficiente";

    public ForbiddenException() {
        super(DESCRIPTION);
    }

    public ForbiddenException(String detail) {
        super(DESCRIPTION + ". " + detail);
    }

}
