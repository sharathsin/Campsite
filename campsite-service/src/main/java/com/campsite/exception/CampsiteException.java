package com.campsite.exception;

import lombok.Data;

@Data
public class CampsiteException extends Exception{
   private int code;

   private String message;

    public CampsiteException(String message, int code) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
