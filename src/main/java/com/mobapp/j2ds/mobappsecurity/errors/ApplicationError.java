package com.mobapp.j2ds.mobappsecurity.errors;

import lombok.Data;

@Data
public class ApplicationError {
    private int code;
    private String message;
}
