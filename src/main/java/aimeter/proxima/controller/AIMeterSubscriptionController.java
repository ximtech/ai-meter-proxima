package aimeter.proxima.controller;

import aimeter.proxima.dto.AIMeterInitialRegistrationRequest;
import aimeter.proxima.dto.AIMeterSubscriptionDTO;
import aimeter.proxima.dto.GeoIpDTO;
import aimeter.proxima.service.AIMeterSubscriptionService;
import aimeter.proxima.utils.HttpUtils;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/api/device/subscription")
@RequiredArgsConstructor
public class AIMeterSubscriptionController {
    
    final AIMeterSubscriptionService aiMeterSubscriptionService;

    @GetMapping(path = "/{deviceId}")
    @CircuitBreaker(name = "circuit-breaker-service")
    public Mono<List<AIMeterSubscriptionDTO>> listAvailableSubscriptions(@PathVariable("deviceId") UUID deviceId) {
        log.info("List available subscriptions for device id: [{}]", deviceId);
        return Mono.fromCallable(() -> aiMeterSubscriptionService.listAvailableSubscriptions(deviceId))
                .publishOn(Schedulers.boundedElastic());
    }

}
