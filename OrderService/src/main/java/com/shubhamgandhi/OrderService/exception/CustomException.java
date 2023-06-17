package com.shubhamgandhi.OrderService.exception;

import lombok.Data;

@Data
public class CustomException extends RuntimeException{
    //we can define different type of exception but here we are just creating only one type

    private String errorCode;
    private int status;

    public CustomException(String message, String errorCode, int status){
        super(message);
        this.errorCode=errorCode;
        this.status=status;
    }

}
