package com.shubhamgandhi.OrderService.external.client;

import com.shubhamgandhi.OrderService.exception.CustomException;
import com.shubhamgandhi.OrderService.external.request.PaymentRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//so whenever you are doing the api calls outside then we can add the circuit breaker there
//you wil have to implement the fallback method as well
//and for these external breaker we need to add the conig. also for cloud gateway we added the bean
//for these we add using yaml file
@CircuitBreaker(name="external", fallbackMethod = "fallBack")
@FeignClient(name = "PAYMENT-SERVICE/payment")
public interface PaymentService {

    //now which enpoint we are going to call from the payment we will just copy that here
    @PostMapping
    public ResponseEntity<Long> doPayment(@RequestBody PaymentRequest paymentRequest);


    //using java8 feature to create the default method
    default ResponseEntity<Long> fallBack(Exception e){
        throw new CustomException("Payment Service Not available", "UNAVAILABLE", 500);
    }
}
