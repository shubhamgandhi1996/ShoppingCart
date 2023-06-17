package com.shubhamgandhi.OrderService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {

    private long orderId;
    private Instant orderDate;
    private String orderStatus;
    private long amount;
    private ProductDetails productDetails;
    private PaymentDetails paymentDetails;

    //static inner class
    @Data
    @Builder //so that to use this class with builder pattern to build object
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductDetails {
        private String productName;
        private long productId;
        private long quantity;
        private long price;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PaymentDetails{
        private long paymentId;
        private PaymentMode paymentMode;
        private String paymentStatus;
        private Instant paymentDate;
    }


}
