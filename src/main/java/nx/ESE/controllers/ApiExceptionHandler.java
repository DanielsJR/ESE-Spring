package nx.ESE.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import nx.ESE.exceptions.DocumentNotFoundException;
import nx.ESE.exceptions.ErrorMessage;
import nx.ESE.exceptions.FieldAlreadyExistException;
import nx.ESE.exceptions.FieldInvalidException;
import nx.ESE.exceptions.FieldNotFoundException;
import nx.ESE.exceptions.FileException;
import nx.ESE.exceptions.ForbiddenChangeRoleException;
import nx.ESE.exceptions.ForbiddenDeleteException;
import nx.ESE.exceptions.ForbiddenException;
import nx.ESE.exceptions.PasswordNotMatchException;


@ControllerAdvice
public class ApiExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
    	FieldNotFoundException.class,
     	FileException.class,
     	DocumentNotFoundException.class})
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
    	ForbiddenChangeRoleException.class,
    	ForbiddenDeleteException.class,
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
