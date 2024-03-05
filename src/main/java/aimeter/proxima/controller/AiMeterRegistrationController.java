package aimeter.proxima.controller;

import aimeter.proxima.dto.AIMeterRegistrationRequest;
import aimeter.proxima.dto.GeoIpDTO;
import aimeter.proxima.service.AIMeterRegistrationService;
import aimeter.proxima.utils.HttpUtils;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RestController
@RequestMapping(value = "/api/register")
@RequiredArgsConstructor
public class AiMeterRegistrationController {
    
    final AIMeterRegistrationService aiMeterRegistrationService;
    
    @PostMapping(path = "/device")
    @CircuitBreaker(name = "circuit-breaker-service")
    public Mono<GeoIpDTO> deviceRegistration(ServerHttpRequest request, @Valid @RequestBody AIMeterRegistrationRequest registrationRequest) {
        String ipAddress = HttpUtils.getIPFromRequest(request);
        log.info("Device registration for IP: [{}]. Request body: [{}]", ipAddress, registrationRequest);
        return Mono.fromCallable(() -> aiMeterRegistrationService.performDeviceRegistration(ipAddress, registrationRequest))
                .publishOn(Schedulers.boundedElastic());
    }

}
