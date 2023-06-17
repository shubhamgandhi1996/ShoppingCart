package com.shubhamgandhi.ProductService.Exception;

import lombok.Data;

@Data
public class ProductServiceCustomException extends RuntimeException{

    private String errorCode;

    public ProductServiceCustomException(String messagge, String errorCode){
        super(messagge);
        this.errorCode=errorCode;
    }
}
