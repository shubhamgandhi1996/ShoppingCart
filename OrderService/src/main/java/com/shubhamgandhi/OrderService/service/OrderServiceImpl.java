package com.shubhamgandhi.OrderService.service;

import com.shubhamgandhi.OrderService.entity.Order;
import com.shubhamgandhi.OrderService.exception.CustomException;
import com.shubhamgandhi.OrderService.external.client.PaymentService;
import com.shubhamgandhi.OrderService.external.client.ProductService;
import com.shubhamgandhi.OrderService.external.request.PaymentRequest;
import com.shubhamgandhi.OrderService.external.response.PaymentResponse;
import com.shubhamgandhi.OrderService.model.OrderRequest;
import com.shubhamgandhi.OrderService.model.OrderResponse;
import com.shubhamgandhi.OrderService.repository.OrderRepository;
import com.shubhamgandhi.ProductService.model.ProductResponse;
import lombok.extern.log4j.Log4j2;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public long placeOrder(OrderRequest orderRequest) {
        //we will need a repository object to store the data to the db

        //order entity -> and save the data with status order created
        //product service - block the prod(reduce the quantity)
        //payment service -> payments -> success-> complete else Canceled
        //cancelled
        log.info("Placing Order Request: "+ orderRequest);
        //so before we save the order we nned to check it first
        productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getTotalQuantity());

        log.info("creating Order with status CREATED");

        Order order= Order.builder()
                .amount(orderRequest.getTotalAmount())
                .orderStatus("CREATED")
                .productId(orderRequest.getProductId())
                .orderDate(Instant.now())
                .quantity(orderRequest.getTotalQuantity())
                .build();
        order = orderRepository.save(order);

        log.info("calling payment service to complete the payment");
        PaymentRequest paymentRequest=
                PaymentRequest.builder()
                        .orderId(order.getId())
                        .paymentMode(orderRequest.getPaymentMode())
                        .amount(orderRequest.getTotalAmount())
                        .build();

        String orderStatus=null;
        try {
            paymentService.doPayment(paymentRequest);
            log.info("payment Done successfully..changing the order status to placed");
            orderStatus="PLACED";
        }catch (Exception e){
            log.error("error occurred in payment. changing the orderstatus to failed");
            orderStatus="PAYMENT_FAILED";
        }
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);

        log.info("OrderPlaced Successfully with id: "+order.getId());
        return order.getId();
    }

    @Override
    public OrderResponse getOrderDetails(long orderId) {
        log.info("get Order details for order id {}", orderId);
        Order order=orderRepository.findById(orderId)
                .orElseThrow(()-> new CustomException("order not found for the id" + orderId, "NOTFOUND", 404));

        log.info("Invoiking product service to fetch the product with id {}", order.getProductId());
        //to get the product details from the rest template we need the object of the rest template so
        //we have defined it main class
        ProductResponse productResponse = restTemplate.getForObject(
                "http://PRODUCT-SERVICE/product/"+order.getProductId(),
                ProductResponse.class
        ); //we got the product response back with rest template and we are not mentioning the ip address

        log.info("getting payment info from the payment service");
        PaymentResponse paymentResponse=restTemplate.getForObject(
                "http://PAYMENT-SERVICE/payment/order/"+order.getId(),
                PaymentResponse.class
        );

        OrderResponse.PaymentDetails paymentDetails=OrderResponse.PaymentDetails.builder()
                .paymentId(paymentResponse.getPaymentId())
                .paymentStatus(paymentResponse.getStatus())
                .paymentDate(paymentResponse.getPaymentDate())
                .paymentMode(paymentResponse.getPaymentMode())
                .build();

        OrderResponse.ProductDetails productDetails=OrderResponse.ProductDetails.builder()
                .productId(productResponse.getProductId())
                .productName(productResponse.getProductName())
                .price(productResponse.getPrice())
                .quantity(productResponse.getQuantity())
                .build();

        OrderResponse orderResponse= OrderResponse.builder()
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .amount(order.getAmount())
                .orderDate(order.getOrderDate())
                .productDetails(productDetails)
                .paymentDetails(paymentDetails)
                .build();


        return orderResponse;
    }
}
