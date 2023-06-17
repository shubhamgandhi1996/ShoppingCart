package com.shubhamgandhi.OrderService.external.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shubhamgandhi.OrderService.exception.CustomException;
import com.shubhamgandhi.OrderService.external.response.ErrorResponse;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
//WE Need to tell the spring to use the custom error decoder instead of error decoder for that conig package
public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {
        //object mapper from the fastxml2
        ObjectMapper objectMapper =new ObjectMapper();

        log.info("::{}", response.request().url());
        log.info("::{}", response.request().headers());
        //the response which is error which is sent back -> its in the form of object
        //which was error response which we created in prod service to send it to ui

        try {
            ErrorResponse errorResponse=objectMapper.readValue(response.body().asInputStream(),
                    ErrorResponse.class);
            //once we get the exception we need to throw them as well -> like in prod services
            //so once the custom exception is created we can now pass the custom excp. from the error decoder

            return new CustomException(errorResponse.getErrorMessage(), errorResponse.getErrorCode(),response.status());
            //but whatever the exception we are throwing this needs to be undersatnd by spring as well so will need to
            //create the controller advice

        } catch (IOException e) {
            throw new CustomException("Internal Server Error", "INTERNAL_SERVER_ERROR", 500);
        }
    }
}
