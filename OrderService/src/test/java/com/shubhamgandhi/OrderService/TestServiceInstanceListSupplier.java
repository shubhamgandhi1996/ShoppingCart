package com.shubhamgandhi.OrderService;

import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

//it will tell the instace available
public class TestServiceInstanceListSupplier implements ServiceInstanceListSupplier {
    @Override
    public String getServiceId() {
        return null;
    }

    @Override
    public Flux<List<ServiceInstance>> get() {
        List<ServiceInstance> result=
                new ArrayList<>();
        result.add(new DefaultServiceInstance(
                "PAYMENT-SERVICE",
                "PAYMENT-SERVICE",
                "localhost",
                8080,
                false
        ));

        result.add(new DefaultServiceInstance(
                "PRODUCT-SERVICE",
                "PRODUCT-SERVICE",
                "localhost",
                8080,
                false
        ));
        // THE SERVICE REGISTERY IS RUNNING ON PORT 8080 AND EVERYTHING IS CONNECTED TO IT

        return Flux.just(result);
    }
}
