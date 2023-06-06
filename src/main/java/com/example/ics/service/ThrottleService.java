package com.example.ics.service;

import com.google.common.util.concurrent.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ThrottleService {

    public RateLimiter rateLimiter = RateLimiter.create(1000/3600.0);

    public boolean shouldThrottle() {
        return !rateLimiter.tryAcquire();
    }
}