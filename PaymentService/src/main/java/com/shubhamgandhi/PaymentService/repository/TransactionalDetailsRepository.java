package com.shubhamgandhi.PaymentService.repository;

import com.shubhamgandhi.PaymentService.entity.TransactionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionalDetailsRepository extends JpaRepository<TransactionDetails, Long> {

    //for getting the data based on order id
    TransactionDetails findByOrderId(long orderId); //just we need to declare the implementation is done by spring data jpa
}
