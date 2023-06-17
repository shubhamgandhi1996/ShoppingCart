package com.shubhamgandhi.CloudGateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
//since this service is reactive web application so we need this
public class OktaOAuth2WebSecurity {
    //we need to override the config. using filters
    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity httpSecurity){
        httpSecurity
                .authorizeExchange()
                .anyExchange().authenticated()
                .and().oauth2Login()
                .and().oauth2ResourceServer()
                .jwt();
        //basic config like all req should be authenticated and we provide default login page and for resource server it should
        //access jwt
        return httpSecurity.build();
        //implement one api from where we can call the okta from where we will login
    }
}
