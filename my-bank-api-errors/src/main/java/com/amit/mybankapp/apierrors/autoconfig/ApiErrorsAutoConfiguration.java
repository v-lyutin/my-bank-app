package com.amit.mybankapp.apierrors.autoconfig;

import com.amit.mybankapp.apierrors.client.ApiErrorResponseDecoder;
import com.amit.mybankapp.apierrors.client.RestClientErrorHandler;
import com.amit.mybankapp.apierrors.server.ApiExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@AutoConfiguration
public class ApiErrorsAutoConfiguration {

    @Bean
    @ConditionalOnClass(value = ApiExceptionHandler.class)
    @ConditionalOnMissingBean(value = ApiExceptionHandler.class)
    public ApiExceptionHandler apiExceptionHandler() {
        return new ApiExceptionHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public ApiErrorResponseDecoder apiErrorResponseDecoder(ObjectMapper objectMapper) {
        return new ApiErrorResponseDecoder(objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public RestClientErrorHandler restClientErrorHandler(ApiErrorResponseDecoder decoder) {
        return new RestClientErrorHandler(decoder);
    }

    @Bean
    @ConditionalOnClass(value = RestClient.Builder.class)
    @ConditionalOnMissingBean(RestClient.Builder.class)
    public RestClient.Builder restClientBuilder(RestClientErrorHandler handler) {
        RestClient.Builder builder = RestClient.builder();
        handler.register(builder);
        return builder;
    }

}
