package com.shubhamgandhi.PaymentService.service;

import com.shubhamgandhi.PaymentService.model.PaymentRequest;
import com.shubhamgandhi.PaymentService.model.PaymentResponse;

public interface PaymentService {
    Long doPayment(PaymentRequest paymentRequest);

    PaymentResponse getPaymentDetailsByOrderId(String orderId);
}
