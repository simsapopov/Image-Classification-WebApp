package com.example.ics.service;

import com.google.common.util.concurrent.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ThrottleService {

    public RateLimiter rateLimiter = RateLimiter.create(10000.0/3600.0);

    public boolean shouldThrottle() {
        System.out.println(rateLimiter);
        return !rateLimiter.tryAcquire();
    }
}