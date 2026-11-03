package com.springmodernisationlab.orders.inventory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Production caches inventory availability so repeated order checks do not
 * hit the inventory service, but stock levels must be refreshed once their
 * time-to-live has passed.
 */
@SpringBootTest(properties = "spring.cache.ehcache.config=classpath:ehcache-test.xml")
@ActiveProfiles("production")
class InventoryCachingTests {

    private static final String AVAILABILITY_PATH = "/inventory/{sku}/available";

    @MockBean(name = "inventoryRestTemplate")
    private RestTemplate restTemplate;

    @Autowired
    private InventoryClient inventoryClient;

    @Test
    void availabilityIsCachedAndRefreshedAfterItsTimeToLive() throws InterruptedException {
        when(restTemplate.getForObject(AVAILABILITY_PATH, Integer.class, "SKU-1"))
                .thenReturn(5, 3);

        assertThat(inventoryClient.availableQuantity("SKU-1")).isEqualTo(5);
        assertThat(inventoryClient.availableQuantity("SKU-1")).isEqualTo(5);
        verify(restTemplate, times(1)).getForObject(AVAILABILITY_PATH, Integer.class, "SKU-1");

        // The test cache keeps entries for one second.
        Thread.sleep(2_000);

        assertThat(inventoryClient.availableQuantity("SKU-1")).isEqualTo(3);
        verify(restTemplate, times(2)).getForObject(AVAILABILITY_PATH, Integer.class, "SKU-1");
    }
}
