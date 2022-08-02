package co.uk.bransby.equinetrainingtrackerapi.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class RestApiExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<RestApiError> buildRestApiErrorResponse(RestApiError apiError) {
        return ResponseEntity
                .status(apiError.getStatus())
                .body(apiError);
    }

    @ExceptionHandler
    protected ResponseEntity<RestApiError> handleEntityNotFound(HttpServletRequest req, EntityNotFoundException e) {
        RestApiError error = new RestApiError(req.getRequestURL(), HttpStatus.NOT_FOUND, e.getMessage());
        return buildRestApiErrorResponse(error);
    }

    @ExceptionHandler
    protected ResponseEntity<RestApiError> handleEntityExists(HttpServletRequest req, EntityNotFoundException e) {
        RestApiError error = new RestApiError(req.getRequestURL(), HttpStatus.CONFLICT, e.getMessage());
        return buildRestApiErrorResponse(error);
    }

}
