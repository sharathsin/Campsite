package com.campsite.exception;

import java.util.List;

import com.campsite.model.error.ValidationError;

import lombok.Data;

@Data
public class CampsiteValidationException extends Exception {
    private int code;

    private String message;
    private List<ValidationError> validationErrors;

    public CampsiteValidationException(String message, int code, List<ValidationError> validationErrors) {
        super(message);
        this.code = code;
        this.message = message;
        this.validationErrors = validationErrors;
    }
}
