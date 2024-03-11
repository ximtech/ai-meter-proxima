package aimeter.proxima.controller;

import aimeter.proxima.dto.TelegramOneTimePasswordDTO;
import aimeter.proxima.service.AIMeterSubscriptionService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/api/device/subscription")
@RequiredArgsConstructor
public class AIMeterSubscriptionController {
    
    final AIMeterSubscriptionService aiMeterSubscriptionService;

    @GetMapping(path = "telegram/otp/{deviceId}")
    @CircuitBreaker(name = "circuit-breaker-service")
    public Mono<TelegramOneTimePasswordDTO> telegramSubscriptionOTP(@PathVariable("deviceId") UUID deviceId) {
        log.info("OTP request for device id: [{}]", deviceId);
        return Mono.fromCallable(() -> aiMeterSubscriptionService.getTelegramSubscriptionOTP(deviceId))
                .publishOn(Schedulers.boundedElastic());
    }
}
