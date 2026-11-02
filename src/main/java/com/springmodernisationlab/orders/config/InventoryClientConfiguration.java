package com.springmodernisationlab.orders.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Configuration
public class InventoryClientConfiguration {

    @Bean
    public CloseableHttpClient inventoryHttpClient() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(2_000)
                .setConnectionRequestTimeout(1_000)
                .setSocketTimeout(5_000)
                .build();

        return HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setMaxConnTotal(50)
                .setMaxConnPerRoute(20)
                .setConnectionTimeToLive(30, TimeUnit.SECONDS)
                .evictIdleConnections(60, TimeUnit.SECONDS)
                .build();
    }

    @Bean
    public RestTemplate inventoryRestTemplate(
            RestTemplateBuilder builder,
            CloseableHttpClient inventoryHttpClient,
            @Value("${inventory.base-url}") String baseUrl) {

        return builder
                .rootUri(baseUrl)
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory(inventoryHttpClient))
                .build();
    }
}
