package nx.ESE.resources.exceptions;

public class CourseIdNotFoundException extends Exception {

	private static final long serialVersionUID = 4199327653681853288L;

	public static final String DESCRIPTION = "Id de curso no encontrado";

	public CourseIdNotFoundException() {
		super(DESCRIPTION);
	}

	public CourseIdNotFoundException(String detail) {
		super(DESCRIPTION + ". " + detail);
	}
}