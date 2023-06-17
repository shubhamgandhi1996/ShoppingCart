package com.shubhamgandhi.CloudGateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.factory.SpringCloudCircuitBreakerResilience4JFilterFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class CloudGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudGatewayApplication.class, args);
	}

	//whatever config we have written to add those into the context do this
//
//	@Bean
//	public Customizer<SpringCloudCircuitBreakerResilience4JFilterFactory> defaultCustomizer(){
//		return
//	}

	/*
    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer(){
    return factory -> factory.defaultConfig(
            id-> new Resilience4jConfigBuilder(id)
                    .circuitBreakerConfig(
                            CircuitBreakerConfig.ofDefaults()
                    ).build()
    );


    }

*/
	@Bean //right now the redis will just create the userkey and it will store the count/times it tried to hit the uri
	//but then later we can set it for each user
	KeyResolver userKeySolver(){
		return exchange -> Mono.just("userkey");
	}


}
