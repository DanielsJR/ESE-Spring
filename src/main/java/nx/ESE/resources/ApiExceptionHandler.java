package nx.ESE.resources;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import nx.ESE.resources.exceptions.ErrorMessage;
import nx.ESE.resources.exceptions.FieldInvalidException;
import nx.ESE.resources.exceptions.FileException;
import nx.ESE.resources.exceptions.ForbiddenException;
import nx.ESE.resources.exceptions.PasswordNotMatchException;
import nx.ESE.resources.exceptions.UserFieldAlreadyExistException;
import nx.ESE.resources.exceptions.UserIdNotFoundException;
import nx.ESE.resources.exceptions.UserUsernameNotFoundException;


@ControllerAdvice
public class ApiExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
    	UserIdNotFoundException.class,
    	UserUsernameNotFoundException.class,
    	FileException.class,})
    @ResponseBody
    public ErrorMessage notFoundRequest(HttpServletRequest request, Exception exception) {
        return new ErrorMessage(exception, request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
    	Exception.class,
    	UserFieldAlreadyExistException.class,
    	FieldInvalidException.class,
    	PasswordNotMatchException.class
    	})
    @ResponseBody
    public ErrorMessage badRequest(Exception exception) {
        return new ErrorMessage(exception, "");
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({
    	ForbiddenException.class,
    	AccessDeniedException.class})
    @ResponseBody
    public ErrorMessage forbiddenRequest(Exception exception) {
        return new ErrorMessage(exception, "");
    }
}
