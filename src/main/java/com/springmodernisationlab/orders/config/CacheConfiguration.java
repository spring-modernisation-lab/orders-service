package com.springmodernisationlab.orders.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * Enables Spring caching. The cache provider is chosen per environment in
 * configuration: caching is off by default and backed by Ehcache in
 * production (see application-production.yml and ehcache.xml).
 */
@Configuration
@EnableCaching
public class CacheConfiguration {
}
