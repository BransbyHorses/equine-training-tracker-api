package co.uk.bransby.equinetrainingtrackerapi.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class RestApiExceptionHandler extends ResponseEntityExceptionHandler {

    StringBuilder stringBuilder = new StringBuilder();

    private ResponseEntity<RestApiError> buildRestApiErrorResponse(RestApiError apiError) {
        return ResponseEntity
                .status(apiError.getStatus())
                .body(apiError);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<RestApiError> handleEntityNotFound(HttpServletRequest req, EntityNotFoundException e) {
        RestApiError error = new RestApiError(stringBuilder.append(req.getRequestURL()), HttpStatus.NOT_FOUND, e.getMessage());
        return buildRestApiErrorResponse(error);
    }

    @ExceptionHandler(EntityExistsException.class)
    protected ResponseEntity<RestApiError> handleEntityExists(HttpServletRequest req, EntityExistsException e) {
        RestApiError error = new RestApiError(stringBuilder.append(req.getRequestURL()), HttpStatus.CONFLICT, e.getMessage());
        return buildRestApiErrorResponse(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected  ResponseEntity<RestApiError> handleIllegalArgument(HttpServletRequest req, IllegalArgumentException e) {
        RestApiError error = new RestApiError(stringBuilder.append(req.getRequestURL()), HttpStatus.BAD_REQUEST, e.getMessage());
        return buildRestApiErrorResponse(error);
    }

}
