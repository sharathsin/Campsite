package com.campsite.exception;

import java.util.ArrayList;
import java.util.Calendar;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.campsite.model.error.ErrorOutput;
import com.campsite.model.error.ValidationError;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GeneralExceptionHandler {

    @ExceptionHandler({CampsiteValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorOutput handleValidation( CampsiteValidationException campsiteValidationException) {
        ErrorOutput errorOutput = new ErrorOutput(campsiteValidationException.getCode(), "BADREQUEST","Input Validation Failed", "campsite",Calendar.getInstance().getTime(), campsiteValidationException.getValidationErrors());

        return errorOutput;

    }


    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorOutput handleBindExceptions(Exception ex) {
        ErrorOutput errorOutput = new ErrorOutput(HttpStatus.BAD_REQUEST.value(), "Input data validation failed","BadRequest","campsite", Calendar.getInstance().getTime(), new ArrayList<>());
        BindingResult bindingResult = getBindingResult(ex);

        if(bindingResult != null) {
            populateValidationErrorDetailsInErrorDto(bindingResult, errorOutput);
        }

        return errorOutput;
    }

    private BindingResult getBindingResult(Exception ex) {
        BindingResult bindingResult = null;
        if(ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) ex;
            bindingResult = exception.getBindingResult();
        }
        return bindingResult;
    }

    private void populateValidationErrorDetailsInErrorDto(BindingResult bindingResult, ErrorOutput errorOutput) {
        for (ObjectError error : bindingResult.getAllErrors()) {
            if (error instanceof FieldError) {
                FieldError ferr = (FieldError) error;

                String errorField = ferr.getField();


                ValidationError valErr = new ValidationError(ferr.getObjectName().replaceAll("Dto", ""), errorField, ferr.getDefaultMessage());
                errorOutput.getValidationErrors().add(valErr);
            }
        }
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorOutput> handleGenericHttpMessageNotReadableException(HttpMessageNotReadableException ex) {

        return  httpMessageNotReadableError(ex);

    }

    private ResponseEntity<ErrorOutput> httpMessageNotReadableError(HttpMessageNotReadableException ex) {

        ErrorOutput errorOutput = new ErrorOutput(HttpStatus.BAD_REQUEST.value(), "JSON packet sent is invalid", "Bad Request","campsite",Calendar.getInstance().getTime(), null);

        return new ResponseEntity<>(errorOutput, HttpStatus.BAD_REQUEST);

    }

}