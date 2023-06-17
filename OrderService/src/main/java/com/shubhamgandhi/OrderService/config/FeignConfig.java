package com.shubhamgandhi.OrderService.config;

import com.shubhamgandhi.OrderService.external.decoder.CustomErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    //so when we need errordecoder it will pass the custom error decoder
    ErrorDecoder errorDecoder(){
        return new CustomErrorDecoder();
    }

}
