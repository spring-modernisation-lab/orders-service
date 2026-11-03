package com.springmodernisationlab.orders.inventory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class InventoryClient {

    private final RestTemplate restTemplate;

    public InventoryClient(@Qualifier("inventoryRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Availability is cached briefly to protect the inventory service; the
     * time-to-live and size limits come from the cache provider configuration.
     */
    @Cacheable("inventoryAvailability")
    public int availableQuantity(String sku) {
        Integer quantity = restTemplate.getForObject(
                "/inventory/{sku}/available",
                Integer.class,
                sku);

        return quantity == null ? 0 : quantity;
    }
}
