package com.shubhamgandhi.OrderService.service;

import com.shubhamgandhi.OrderService.entity.Order;
import com.shubhamgandhi.OrderService.exception.CustomException;
import com.shubhamgandhi.OrderService.external.client.PaymentService;
import com.shubhamgandhi.OrderService.external.client.ProductService;
import com.shubhamgandhi.OrderService.external.request.PaymentRequest;
import com.shubhamgandhi.OrderService.external.response.PaymentResponse;
import com.shubhamgandhi.OrderService.model.OrderRequest;
import com.shubhamgandhi.OrderService.model.OrderResponse;
import com.shubhamgandhi.OrderService.model.PaymentMode;
import com.shubhamgandhi.OrderService.repository.OrderRepository;
import com.shubhamgandhi.ProductService.model.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest //to make this class as a test class
//if we rht click on method -> to check the coverage -> to check how much code is coverd
public class OrderServiceImplTest {

    @Mock//so instead of writng autowire which will inject the actual beans we will use mock
    private OrderRepository orderRepository;

    @Mock
    private ProductService productService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks //where we want to inject the mocks its in impl class so will write this
    OrderService orderService = new OrderServiceImpl();


    @DisplayName("Get Order - Success Scenario") //when test case run it will generate report and to give a particular name
    @Test //every method should be annotated with this
    void test_When_Order_Success() {
        //Mocking
        //withtin the actual method call if there is any internal calls done for that we do mocking

        Order order = getMockOrder();
        //when we will call findby id with anyLong value then return the optional order object
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));//since find method was returning optional order object

        when(restTemplate.getForObject(
                "http://PRODUCT-SERVICE/product/" + order.getProductId(),
                ProductResponse.class
        )).thenReturn(getMockProductResponse());

        when(restTemplate.getForObject(
                "http://PAYMENT-SERVICE/payment/order/" + order.getId(),
                PaymentResponse.class
        )).thenReturn(getMockPaymentResponse());

        //Actual
        OrderResponse orderResponse = orderService.getOrderDetails(1);

        //Verification
        //so some calls may be skipped dur to some conditions -> so all those scenario hanles here

        //verify that in the orderRepository is findById method passed with any long value called or not
        verify(orderRepository, times(1)).findById(anyLong());
        verify(restTemplate, times(1)).getForObject(
                "http://PRODUCT-SERVICE/product/" + order.getProductId(),
                ProductResponse.class);
        verify(restTemplate, times(1)).getForObject(
                "http://PAYMENT-SERVICE/payment/order/" + order.getId(),
                PaymentResponse.class);


        //Assert
        //once we call the mehtod we need toverify for that -> to check what we want is what we got

        assertNotNull(orderResponse);
        assertEquals(order.getId(), orderResponse.getOrderId());
    }

    @DisplayName("Get Orders - Failure Scenario")
    @Test
    void test_When_Get_Order_NOT_FOUND_then_Not_Found() {

        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(null)); //so we are telling to return null when this method is called

        //so when actully the calls happen it will then return null and null means then the method will throw exception
        //so to handle that
        CustomException exception =
                assertThrows(CustomException.class,
                        () -> orderService.getOrderDetails(1));

        assertEquals("NOT_FOUND", exception.getErrorCode());
        assertEquals(404, exception.getStatus());

        verify(orderRepository, times(1))
                .findById(anyLong());
    }

    @DisplayName("Place Order - Success Scenario")
    @Test
    void test_When_Place_Order_Success() {
        Order order = getMockOrder();
        OrderRequest orderRequest = getMockOrderRequest();
        //with before all annotations we can define some commands fields which everytest case needed so before any
        //test case is called that method will be called

        when(orderRepository.save(any(Order.class)))//save anything of class order not actually it will save
                .thenReturn(order);
        when(productService.reduceQuantity(anyLong(),anyLong()))
                .thenReturn(new ResponseEntity<Void>(HttpStatus.OK));
        when(paymentService.doPayment(any(PaymentRequest.class)))
                .thenReturn(new ResponseEntity<Long>(1L,HttpStatus.OK));

        long orderId = orderService.placeOrder(orderRequest);

        verify(orderRepository, times(2))
                .save(any());
        verify(productService, times(1))
                .reduceQuantity(anyLong(),anyLong());
        verify(paymentService, times(1))
                .doPayment(any(PaymentRequest.class));

        assertEquals(order.getId(), orderId);
    }

    @DisplayName("Place Order - Payment Failed Scenario")
    @Test
    void test_when_Place_Order_Payment_Fails_then_Order_Placed() {

        Order order = getMockOrder();
        OrderRequest orderRequest = getMockOrderRequest();

        when(orderRepository.save(any(Order.class)))
                .thenReturn(order);
        when(productService.reduceQuantity(anyLong(),anyLong()))
                .thenReturn(new ResponseEntity<Void>(HttpStatus.OK));
        when(paymentService.doPayment(any(PaymentRequest.class)))
                .thenThrow(new RuntimeException());
        //till the payment service it will call everything and it ahould work fine only the payment method is failing

        long orderId = orderService.placeOrder(orderRequest);

        verify(orderRepository, times(2))
                .save(any());
        verify(productService, times(1))
                .reduceQuantity(anyLong(),anyLong());
        verify(paymentService, times(1))
                .doPayment(any(PaymentRequest.class));

        assertEquals(order.getId(), orderId);
    }

    private OrderRequest getMockOrderRequest() {
        return OrderRequest.builder()
                .productId(1)
                .totalQuantity(10)
                .paymentMode(PaymentMode.CASH)
                .totalAmount(100)
                .build();
    }

    private PaymentResponse getMockPaymentResponse() {
        return PaymentResponse.builder()
                .paymentId(1)
                .paymentDate(Instant.now())
                .paymentMode(PaymentMode.CASH)
                .amount(200)
                .orderId(1)
                .status("ACCEPTED")
                .build();
    }

    private ProductResponse getMockProductResponse() {
        return ProductResponse.builder()
                .productId(2)
                .productName("iPhone")
                .price(100)
                .quantity(200)
                .build();
    }

    private Order getMockOrder() {
        return Order.builder()
                .orderStatus("PLACED")
                .orderDate(Instant.now())
                .id(1)
                .amount(100)
                .quantity(200)
                .productId(2)
                .build();
    }
}