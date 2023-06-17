package com.shubhamgandhi.OrderService;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class OrderServiceConfig {
    //to get the instance details we created the class but we will need the bean of it so this class is created and then we will
    //inject those class there
    @Bean
    public ServiceInstanceListSupplier supplier() {
        return new TestServiceInstanceListSupplier();
    }
}
