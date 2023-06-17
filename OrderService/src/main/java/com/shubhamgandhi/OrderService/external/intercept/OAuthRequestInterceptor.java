package com.shubhamgandhi.OrderService.external.intercept;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

//to override the config of the feign client
@Configuration
public class OAuthRequestInterceptor implements RequestInterceptor {


    @Autowired
    private OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager; //we have created  the bean for it check main class

    @Override
    public void apply(RequestTemplate requestTemplate) {
        //in the template itself we should pass the token and that we will get from oauth2clientmanager and will pass as the header
        requestTemplate.header("Authorization", "Bearer " +
                oAuth2AuthorizedClientManager.authorize(OAuth2AuthorizeRequest
                        .withClientRegistrationId("internal-client")
                        .principal("internal")
                        .build())
                        .getAccessToken().getTokenValue());
        //so whatever token we got then we added as a bearer into the header
    }

}
