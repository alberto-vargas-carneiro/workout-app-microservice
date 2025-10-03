package com.alberto.user_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor jwtRequestInterceptor() {
        return new FeignJwtRequestInterceptor();
    }
}