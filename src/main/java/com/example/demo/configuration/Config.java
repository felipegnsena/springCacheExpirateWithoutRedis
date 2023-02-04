package com.example.demo.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.example.demo.configuration.constants.Cache.INIT_DATE_CACHE_PREFIX;

@Configuration
@EnableCaching
public class Config extends CachingConfigurerSupport {

    @Value("${cache.hello.expirate}")
    private String expirateInSeconds;

    @Override
    public CacheManager cacheManager() {

        return new ConcurrentMapCacheManager(){

            private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap(16);

            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

            @Override
            public Cache getCache(String name) {
                boolean recreateCache = false;
                Cache cache = this.cacheMap.get(name);
                recreateCache = isRecreateCache(name, recreateCache);

                boolean dynamic = true;
                if ((cache == null && dynamic) || recreateCache) {
                    synchronized(this.cacheMap) {
                        cache = this.cacheMap.get(name);
                        if (cache == null || recreateCache) {
                            cache = this.createConcurrentMapCache(name);
                            var cacheLocalDateTime = this.createConcurrentMapCache(LocalDateTime.now().format(formatter));
                            this.cacheMap.put(name, cache);
                            this.cacheMap.put(name+ INIT_DATE_CACHE_PREFIX, cacheLocalDateTime);
                        }
                    }
                }
                return cache;
            }

            private boolean isRecreateCache(String name, boolean recreateCache) {
                LocalDateTime initDateCache;
                if(this.cacheMap.get(name + INIT_DATE_CACHE_PREFIX) !=null){
                    initDateCache = LocalDateTime.parse(this.cacheMap.get(name + INIT_DATE_CACHE_PREFIX).getName(),formatter);
                    if((ChronoUnit.SECONDS.between(initDateCache, LocalDateTime.now()))>Long.parseLong(expirateInSeconds)){
                        recreateCache = true;
                    }
                }
                return recreateCache;
            }
        };
    }
}
