package com.springmodernisationlab.orders.config;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.core5.util.TimeValue;
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
                .setConnectTimeout(2_000, TimeUnit.MILLISECONDS)
                .setConnectionRequestTimeout(1_000, TimeUnit.MILLISECONDS)
                .setResponseTimeout(5_000, TimeUnit.MILLISECONDS)
                .build();

        // Configure pooling connection manager with limits and connection TTL
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setMaxTotal(50);
        connManager.setDefaultMaxPerRoute(20);
        connManager.setDefaultConnectionConfig(
                ConnectionConfig.custom()
                        .setTimeToLive(30, TimeUnit.SECONDS)
                        .build()
        );

        return HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connManager)
                .evictIdleConnections(TimeValue.ofSeconds(60))
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
