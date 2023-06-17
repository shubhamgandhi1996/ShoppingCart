package com.shubhamgandhi.PaymentService.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;

//if you want to add the security to any services then we need to add these files
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

        //so all the req going to /payment which has scope internal
        httpSecurity
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/payment/**")
                                //.antMatchers("/payment/**") -> this was throwing error so used the above one
                                .hasAuthority("SCOPE_internal")
                                .anyRequest()
                                .authenticated())
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
        //in the order service file we just told any request to be authenticated not mentioned which path
        // //needs to be authenticated which is global method level security applied
        return httpSecurity.build();
    }
}
