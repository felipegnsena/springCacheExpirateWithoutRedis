package com.example.demo.controller;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.demo.configuration.constants.Cache.CACHE_NAME;

@RestController
@RequestMapping("/api/hello")
public class HelloWorldController {

    @GetMapping
    @Cacheable(cacheNames = CACHE_NAME)
    public ResponseEntity<String> helloWorldWithDelay(){
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("Hello");
    }
}
