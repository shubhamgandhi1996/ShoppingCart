package com.shubhamgandhi.OrderService.Controller;

import com.shubhamgandhi.OrderService.model.OrderRequest;
import com.shubhamgandhi.OrderService.model.OrderResponse;
import com.shubhamgandhi.OrderService.service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@Log4j2 // for logging
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PreAuthorize("hasAuthority('Customer')")
    @PostMapping("/placeOrder")
    public ResponseEntity<Long> placeOrder(@RequestBody OrderRequest orderRequest){
        long orderId=orderService.placeOrder(orderRequest);
        log.info("Order Id: "+ orderId);
        return new ResponseEntity<>(orderId, HttpStatus.OK);
    }

    // no internal as we have not configured for interanl service to call
    // and the names should be same as defined in okta
    @PreAuthorize("hasAuthority('Admin') || hasAuthority('Customer')")
    @GetMapping("/{orderId}")// to get the order details for the order id
    public ResponseEntity<OrderResponse> getOrderDetails(@PathVariable long orderId){
        OrderResponse orderResponse=orderService.getOrderDetails(orderId);
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }
}
