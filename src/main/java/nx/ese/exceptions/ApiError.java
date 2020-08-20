package nx.ese.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

public class ApiError {

    @Getter
    private String exception;
    @Getter
    private String message;
    @Getter
    private List<String> errors;
    @Getter
    private HttpStatus status;
    @Getter
    private String path;

    public ApiError(String exception, String message, List<String> errors, HttpStatus status, String path) {
        super();
        this.exception = exception;
        this.message = message;
        this.errors = errors;
        this.status = status;
        this.path = path;
    }

    public ApiError(Exception exception, List<String> errors, HttpStatus status, String path) {
        this(exception.getClass().getSimpleName(), exception.getLocalizedMessage(), errors, status, path);
    }

    public ApiError(Exception exception, String error, HttpStatus status, String path) {
        this(exception.getClass().getSimpleName(), exception.getLocalizedMessage(), Collections.singletonList(error), status, path);
    }

    @Override
    public String toString() {
        return "ApiError{" +
                "exception='" + exception + '\'' +
                ", message='" + message + '\'' +
                ", errors=" + errors +
                ", status=" + status +
                ", path='" + path + '\'' +
                '}';
    }
}
