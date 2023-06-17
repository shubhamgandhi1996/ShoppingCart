package com.shubhamgandhi.PaymentService.service;

import com.shubhamgandhi.PaymentService.entity.TransactionDetails;
import com.shubhamgandhi.PaymentService.model.PaymentMode;
import com.shubhamgandhi.PaymentService.model.PaymentRequest;
import com.shubhamgandhi.PaymentService.model.PaymentResponse;
import com.shubhamgandhi.PaymentService.repository.TransactionalDetailsRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class PaymentServiceImpl implements PaymentService{

    @Autowired
    private TransactionalDetailsRepository repository;

    @Override
    public Long doPayment(PaymentRequest paymentRequest) {
        log.info("Recording Payment Details {}", paymentRequest);

        TransactionDetails transactionDetails=
                TransactionDetails.builder()
                        .paymentDate(Instant.now())
                        .paymentMode(paymentRequest.getPaymentMode().name())
                        .paymentStatus("SUCCESS")
                        .orderId(paymentRequest.getOrderId())
                        .referenceNo(paymentRequest.getReferenceNumber())
                        .paymentAmount(paymentRequest.getAmount())
                        .build();

        repository.save(transactionDetails);
        log.info("Transactional completed with id {}", transactionDetails.getId());

        return transactionDetails.getId();
    }

    @Override
    public PaymentResponse getPaymentDetailsByOrderId(String orderId) {
        log.info("getting payment details for the order id {}", orderId);
        TransactionDetails transactionDetails=repository.findByOrderId(Long.valueOf(orderId));
        PaymentResponse paymentResponse= PaymentResponse.builder()
                .paymentId(transactionDetails.getId())
                .paymentMode(PaymentMode.valueOf(transactionDetails.getPaymentMode()))
                .orderId(transactionDetails.getOrderId())
                .paymentDate(transactionDetails.getPaymentDate())
                .amount(transactionDetails.getPaymentAmount())
                .status(transactionDetails.getPaymentStatus())
                .build();

        return paymentResponse;
    }
}
