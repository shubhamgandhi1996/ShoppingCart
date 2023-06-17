package com.shubhamgandhi.OrderService.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    //all the request comming to order will be authenticated and from order we are again calling other services using feign client
    // & rest template. so we need to override their configuration when calling other services to pass the token so then
    @Bean
    public SecurityFilterChain securityWebFilterChain(HttpSecurity httpSecurity) throws Exception {

//        we are doing the authorize request for all the http -> all the request will be authenticated  and for the oauth2resorce
        //server we will have the jwt
        httpSecurity
                .authorizeRequests(authorizeRequest -> authorizeRequest
                        .anyRequest().authenticated())
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
        return httpSecurity.build();
        //all the config we can get from the documentation
    }
}
