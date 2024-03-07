package aimeter.proxima.controller;

import aimeter.proxima.service.AIMeterSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.util.function.Tuple2;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class AIMeterIntegrationViewController {

    final AIMeterSubscriptionService aiMeterSubscriptionService;

    @GetMapping("/integration/subscribe/view/{type}")
    public String showIntegrationSubscription(@PathVariable("type") String type, @RequestParam(name = "deviceId") UUID deviceId, Model model) {
        return aiMeterSubscriptionService.getIntegrationViewTemplate(type, deviceId, model);
    }

}
