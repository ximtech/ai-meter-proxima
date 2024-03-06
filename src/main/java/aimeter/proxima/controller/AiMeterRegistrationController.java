package aimeter.proxima.controller;

import aimeter.proxima.dto.AIMeterCompleteRegistrationRequest;
import aimeter.proxima.dto.AIMeterInitialRegistrationRequest;
import aimeter.proxima.dto.GeoIpDTO;
import aimeter.proxima.service.AIMeterRegistrationService;
import aimeter.proxima.utils.HttpUtils;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RestController
@RequestMapping(value = "/api/register/device")
@RequiredArgsConstructor
public class AiMeterRegistrationController {
    
    final AIMeterRegistrationService aiMeterRegistrationService;
    
    @PostMapping(path = "/init")
    @CircuitBreaker(name = "circuit-breaker-service")
    public Mono<GeoIpDTO> deviceInitialRegistration(ServerHttpRequest request, @Valid @RequestBody AIMeterInitialRegistrationRequest registrationRequest) {
        String ipAddress = HttpUtils.getIPFromRequest(request);
        log.info("Device init registration for IP: [{}]. Request body: [{}]", ipAddress, registrationRequest);
        return Mono.fromCallable(() -> aiMeterRegistrationService.performDeviceInitialRegistration(ipAddress, registrationRequest))
                .publishOn(Schedulers.boundedElastic());
    }

    @PostMapping(path = "/complete")
    @CircuitBreaker(name = "circuit-breaker-service")
    public Mono<ResponseEntity<String>> deviceCompleteRegistration(@Valid @RequestBody AIMeterCompleteRegistrationRequest request) {
        log.info("Device complete registration. Request body: [{}]", request);
        return Mono.fromCallable(() -> {
                    aiMeterRegistrationService.performDeviceCompleteRegistration(request);
                    return ResponseEntity.ok("Config saved");})
                .publishOn(Schedulers.boundedElastic());
    }

}
