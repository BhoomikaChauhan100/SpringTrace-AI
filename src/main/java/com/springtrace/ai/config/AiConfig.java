package com.springtrace.ai.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class AiConfig {
    @Bean
    RestClient bharatCodeRestClient(
            RestClient.Builder builder,
            @Value("${bharatcode.base-url}") String baseUrl,
            @Value("${bharatcode.connect-timeout:10s}") Duration connectTimeout,
            @Value("${bharatcode.read-timeout:90s}") Duration readTimeout) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setReadTimeout(readTimeout);
        return builder.baseUrl(baseUrl).requestFactory(requestFactory).build();
    }
}
