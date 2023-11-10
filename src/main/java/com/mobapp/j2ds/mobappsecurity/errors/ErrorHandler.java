package com.mobapp.j2ds.mobappsecurity.errors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class ErrorHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ErrorMessages.class)
    public ResponseEntity<ApplicationError> handleProductNotFoundException(ErrorMessages errorMessages, WebRequest webRequest){
        ApplicationError error = new ApplicationError();
        error.setCode(errorMessages.hashCode());
        error.setMessage(errorMessages.getMessage());
        return new ResponseEntity<>(error, errorMessages.getHttpStatus());
    }
}
