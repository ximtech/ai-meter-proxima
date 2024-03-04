package aimeter.proxima.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.UUID;

import static aimeter.proxima.controller.AiMeterRegistrationController.*;

@Slf4j
@RestController
@RequestMapping(value = BASE_URL)
@RequiredArgsConstructor
public class AiMeterRegistrationController {
    
    public static final String BASE_URL = "/api/registration";

    @PostMapping(path = "/register/device/{deviceId}")
    public Mono<String> listClientAccounts(@PathVariable("deviceId") UUID deviceId) {
        log.debug("POST {}/register/device [{}]", BASE_URL, deviceId);
        return Mono.fromCallable(() -> Void.TYPE.getName())
                .publishOn(Schedulers.boundedElastic());
    }

}
