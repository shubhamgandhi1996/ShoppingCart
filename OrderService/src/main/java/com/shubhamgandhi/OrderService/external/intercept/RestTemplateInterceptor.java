package com.shubhamgandhi.OrderService.external.intercept;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

import java.io.IOException;

public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {
//this will send the token with the header but we need to register it first in rest template


    private OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;


    public RestTemplateInterceptor(OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager){
        this.oAuth2AuthorizedClientManager=oAuth2AuthorizedClientManager;
    }

    //so in this also we are overriding this method and need to pass the access token as the authorization bearer header
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().add("Authorization",
                "Bearer " +
                oAuth2AuthorizedClientManager
                        .authorize(OAuth2AuthorizeRequest
                                .withClientRegistrationId("internal-client")
                                .principal("internal")
                                .build())
                        .getAccessToken().getTokenValue());
        return execution.execute(request, body);
    }
}
