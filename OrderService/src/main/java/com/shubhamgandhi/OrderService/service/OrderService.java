package com.shubhamgandhi.OrderService.service;

import com.shubhamgandhi.OrderService.model.OrderRequest;
import com.shubhamgandhi.OrderService.model.OrderResponse;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetails(long orderId);
}
