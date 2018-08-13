package nx.ESE.resources;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import nx.ESE.resources.exceptions.ErrorMessage;
import nx.ESE.resources.exceptions.FieldInvalidException;
import nx.ESE.resources.exceptions.FileException;
import nx.ESE.resources.exceptions.ForbiddenException;
import nx.ESE.resources.exceptions.PasswordNotMatchException;
import nx.ESE.resources.exceptions.FieldAlreadyExistException;
import nx.ESE.resources.exceptions.FieldNotFoundException;


@ControllerAdvice
public class ApiExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
    	FieldNotFoundException.class,
     	FileException.class,})
    @ResponseBody
    public ErrorMessage notFoundRequest(HttpServletRequest request, Exception exception) {
        return new ErrorMessage(exception, request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
    	Exception.class,
    	FieldAlreadyExistException.class,
    	FieldInvalidException.class
    	})
    @ResponseBody
    public ErrorMessage badRequest(Exception exception) {
        return new ErrorMessage(exception, "");
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({
    	ForbiddenException.class,
    	AccessDeniedException.class,
    	PasswordNotMatchException.class,
    	})
    @ResponseBody
    public ErrorMessage forbiddenRequest(Exception exception) {
        return new ErrorMessage(exception, "");
    }
    
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({
    	BadCredentialsException.class
    	})
    @ResponseBody
    public ErrorMessage unauthorizedRequest(Exception exception) {
        return new ErrorMessage(exception, "");
    }
}
