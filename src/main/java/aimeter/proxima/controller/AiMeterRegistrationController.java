package aimeter.proxima.controller;

import aimeter.proxima.dto.AIMeterRegistrationRequest;
import aimeter.proxima.dto.GeoIpDTO;
import aimeter.proxima.service.AIMeterRegistrationService;
import aimeter.proxima.utils.HttpUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

import static aimeter.proxima.controller.AiMeterRegistrationController.BASE_URL;

@Slf4j
@RestController
@RequestMapping(value = BASE_URL)
@RequiredArgsConstructor
public class AiMeterRegistrationController {
    
    public static final String BASE_URL = "/api/register";
    
    final AIMeterRegistrationService aiMeterRegistrationService;
    
    @PostMapping(path = "/device")
    public Mono<GeoIpDTO> deviceRegistration(ServerHttpRequest request, @Valid @RequestBody AIMeterRegistrationRequest registrationRequest) {
        String ipAddress = HttpUtils.getIPFromRequest(request);
        log.info("POST {}/register/device [{}]. Request: [{}]", BASE_URL, registrationRequest, ipAddress);
        return Mono.fromCallable(() -> aiMeterRegistrationService.performDeviceRegistration(ipAddress, registrationRequest))
                .publishOn(Schedulers.boundedElastic());
    }

}
