package nx.ese.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import nx.ese.exceptions.*;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;

import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@ControllerAdvice
public class ApiExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            FieldNotFoundException.class,
            DocumentNotFoundException.class,
            FileException.class,})
    @ResponseBody
    public ApiError notFoundRequest(HttpServletRequest request, Exception exception) {
        return new ApiError(exception, exception.getMessage(), HttpStatus.NOT_FOUND, request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            FieldNullException.class,
            FieldAlreadyExistException.class,
            FieldInvalidException.class,
            DocumentAlreadyExistException.class,
            org.springframework.dao.DuplicateKeyException.class,
            org.springframework.http.converter.HttpMessageNotReadableException.class,
    })
    @ResponseBody
    public ApiError badRequest(HttpServletRequest request, Exception exception) {
        return new ApiError(exception, exception.getMessage(), HttpStatus.BAD_REQUEST, request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    public ApiError methodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<String>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        return new ApiError(ex, errors, HttpStatus.BAD_REQUEST, request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BindException.class})
    @ResponseBody
    public ApiError bindException(HttpServletRequest request, BindException ex) {
        List<String> errors = new ArrayList<String>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        return new ApiError(ex, errors, HttpStatus.BAD_REQUEST, request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseBody
    public ApiError MissingServletRequestParameterException(HttpServletRequest request, MissingServletRequestParameterException ex) {
        String error = ex.getParameterName() + " parametro faltante";
        return new ApiError(ex, error, HttpStatus.BAD_REQUEST, request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MissingServletRequestPartException.class})
    @ResponseBody
    public ApiError MissingServletRequestPartException(HttpServletRequest request, MissingServletRequestPartException ex) {
        String error = ex.getRequestPartName() + " parte faltante";
        return new ApiError(ex, error, HttpStatus.BAD_REQUEST, request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseBody
    public ApiError ConstraintViolationException(HttpServletRequest request, ConstraintViolationException ex) {
        List<String> errors = new ArrayList<String>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " +
                    violation.getPropertyPath() + ": " + violation.getMessage());
        }
        return new ApiError(ex, errors, HttpStatus.BAD_REQUEST, request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseBody
    public ApiError MethodArgumentTypeMismatchException(HttpServletRequest request, MethodArgumentTypeMismatchException ex) {
        String error = ex.getName() + " debería ser del tipo " + Objects.requireNonNull(ex.getRequiredType()).getName();
        return new ApiError(ex, error, HttpStatus.BAD_REQUEST, request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({TypeMismatchException.class})
    @ResponseBody
    public ApiError TypeMismatchException(HttpServletRequest request, TypeMismatchException ex) {
        String error = ex.getPropertyName() + " debería ser del tipo " + Objects.requireNonNull(ex.getRequiredType()).getName();
        return new ApiError(ex, error, HttpStatus.BAD_REQUEST, request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseBody
    public ApiError HttpRequestMethodNotSupportedException(HttpServletRequest request, HttpRequestMethodNotSupportedException ex) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods are ");
        Objects.requireNonNull(ex.getSupportedHttpMethods()).forEach(t -> builder.append(t).append(" "));

        String error = builder.toString();

        return new ApiError(ex, error, HttpStatus.METHOD_NOT_ALLOWED, request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    @ResponseBody
    public ApiError HttpMediaTypeNotSupportedException(HttpServletRequest request, HttpMediaTypeNotSupportedException ex) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));

        String error = builder.substring(0, builder.length() - 2);

        return new ApiError(ex, error, HttpStatus.UNSUPPORTED_MEDIA_TYPE, request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({ForbiddenException.class,
            ForbiddenChangeRoleException.class,
            ForbiddenDeleteException.class,
            PasswordNotMatchException.class,
            org.springframework.security.access.AccessDeniedException.class,})
    @ResponseBody
    public ApiError forbiddenRequest(Exception exception) {
        return new ApiError(exception, exception.getMessage(), HttpStatus.FORBIDDEN, "");
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({org.springframework.security.authentication.BadCredentialsException.class})
    @ResponseBody
    public ApiError unauthorizedRequest(Exception exception) {
        return new ApiError(exception, exception.getMessage(), HttpStatus.UNAUTHORIZED, "");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    @ResponseBody
    public ApiError exception(HttpServletRequest request, Exception exception) {
        return new ApiError(exception, "ha ocurrido un error", HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI());
    }


}
