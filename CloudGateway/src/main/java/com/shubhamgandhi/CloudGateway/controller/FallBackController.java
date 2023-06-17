package com.shubhamgandhi.CloudGateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallBackController {

    @GetMapping("/orderServiceFallBack")
    public String orderServiceFallBack(){
        return "Order Service is Down";
    }

    @GetMapping("/productServiceFallBack")
    public String productServiceFallBack(){
        return "product Service is Down";
    }

    @GetMapping("/paymentServiceFallBack")
    public String paymentServiceFallBack(){
        return "payment Service is Down";
    }
}
