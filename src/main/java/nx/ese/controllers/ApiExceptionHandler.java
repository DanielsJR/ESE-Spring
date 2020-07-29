package nx.ese.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import nx.ese.exceptions.DocumentNotFoundException;
import nx.ese.exceptions.ErrorMessage;
import nx.ese.exceptions.FieldAlreadyExistException;
import nx.ese.exceptions.FieldInvalidException;
import nx.ese.exceptions.FieldNotFoundException;
import nx.ese.exceptions.FileException;
import nx.ese.exceptions.ForbiddenChangeRoleException;
import nx.ese.exceptions.ForbiddenDeleteException;
import nx.ese.exceptions.ForbiddenException;
import nx.ese.exceptions.PasswordNotMatchException;

@ControllerAdvice
public class ApiExceptionHandler {

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({ FieldNotFoundException.class, FileException.class, DocumentNotFoundException.class })
	@ResponseBody
	public ErrorMessage notFoundRequest(HttpServletRequest request, Exception exception) {
		return new ErrorMessage(exception,"");
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ Exception.class, FieldAlreadyExistException.class, FieldInvalidException.class,
			MethodArgumentNotValidException.class })
	@ResponseBody
	public ErrorMessage badRequest(Exception exception) {
		return new ErrorMessage(exception, "");
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler({ ForbiddenException.class, AccessDeniedException.class, PasswordNotMatchException.class,
			ForbiddenChangeRoleException.class, ForbiddenDeleteException.class, })
	@ResponseBody
	public ErrorMessage forbiddenRequest(Exception exception) {
		return new ErrorMessage(exception, "");
	}

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler({ BadCredentialsException.class })
	@ResponseBody
	public ErrorMessage unauthorizedRequest(Exception exception) {
		return new ErrorMessage(exception, "");
	}

}
