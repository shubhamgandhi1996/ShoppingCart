package com.shubhamgandhi.OrderService.external.client;


import com.shubhamgandhi.OrderService.exception.CustomException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

//we will need to add the intercepter so when we are calling other request then it will add the token in it
@CircuitBreaker(name="external", fallbackMethod = "fallBack")
//this is having only one method thats why we are mentioning on class level the fallback method but if we have multiple then
//move this annotation to the method level
@FeignClient(name="PRODUCT-SERVICE/product") //so when  we call the prod service so it will go to the prod service and hit this endpoint
public interface ProductService {

    //now which endpoint need to hit that needs to be defined
    @PutMapping("/reduceQuantity/{id}")
    ResponseEntity<Void> reduceQuantity(@PathVariable(name = "id") long productId, @RequestParam long quantity);
    // and its impl in the prod service

    //using java8 feature to create the default method
    default ResponseEntity<Void> fallBack(Exception e){
        throw new CustomException("Product Service Not available", "UNAVAILABLE", 500);
    }

}
