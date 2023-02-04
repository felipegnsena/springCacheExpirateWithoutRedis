package com.example.demo.configuration.constants;

public class Cache {

    private Cache() {
    }

    public static final String CACHE_NAME = "hello";
    public static final String CACHE_KEY = "{#partner.id}";
    public static final String CACHE_UNLESS = "#result == null";

    public static final String INIT_DATE_CACHE_PREFIX = "DateTime";
}
