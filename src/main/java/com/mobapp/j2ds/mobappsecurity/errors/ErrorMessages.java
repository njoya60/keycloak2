package com.mobapp.j2ds.mobappsecurity.errors;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorMessages extends RuntimeException {
    private HttpStatus httpStatus;
    public ErrorMessages(String message, HttpStatus status){
        super(message);
        this.httpStatus = status;
    }
}
