package aimeter.proxima.service;

import aimeter.proxima.config.RouterConfig;
import aimeter.proxima.domain.AIMeterSubscriptionType;
import aimeter.proxima.domain.entity.AIMeterDevice;
import aimeter.proxima.domain.entity.AIMeterTelegramOTPToken;
import aimeter.proxima.domain.repository.AIMeterDeviceRepository;
import aimeter.proxima.domain.repository.AIMeterTokenRepository;
import aimeter.proxima.dto.AIMeterSubscriptionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIMeterSubscriptionService {

    public static final int OTP_PASSWORD_LENGTH = 4;
    public static final int EXPIRATION_MINUTES = 5;
    public static final int MAX_ALLOWED_ACTIVE_OTP_COUNT = 5;

    @Value("${app.host.name}")
    String appHostName;
    @Value("${telegram.bot.name}")
    String telegramBotName;

    final AIMeterDeviceRepository aiMeterDeviceRepository;
    final OneTimePasswordService oneTimePasswordService;
    final AIMeterTokenRepository aiMeterTokenRepository;
    
    public List<AIMeterSubscriptionDTO> listAvailableSubscriptions(UUID deviceId) {
        AIMeterDevice meterDevice = aiMeterDeviceRepository.findRegisteredAIMeterDeviceOrThrow(deviceId);
        return Arrays.stream(AIMeterSubscriptionType.values())
                .map((AIMeterSubscriptionType subscriptionType) -> {
                    String subscriptionLink = "%s/integration/subscribe/view/%s?deviceId=%s".formatted(
                            appHostName, subscriptionType.name().toLowerCase(), meterDevice.getDeviceId());
                    String iconLink = "%s/%s/%s".formatted(appHostName, RouterConfig.IMAGES_ROOT_PATH, subscriptionType.getIconName());
                    return new AIMeterSubscriptionDTO(subscriptionType.getDisplayName(), subscriptionLink, iconLink);
                }).collect(Collectors.toList());
    }
    
    public String getIntegrationViewTemplate(String subscriptionType, UUID deviceId, Model model) {
        AIMeterSubscriptionType type = AIMeterSubscriptionType.valueOf(subscriptionType.toUpperCase());
        AIMeterDevice meterDevice = aiMeterDeviceRepository.findRegisteredAIMeterDeviceOrThrow(deviceId);
        switch (type) {
            case TELEGRAM -> setupTelegramSubscription(meterDevice, model);
            case WHATSAPP -> {
                // TODO: create subscription
            }
        }
        return type.name().toLowerCase();
    }
    
    private void setupTelegramSubscription(AIMeterDevice meterDevice, Model model) {
        String fourDigitOtp = oneTimePasswordService.createDigitPasswordOfLength(OTP_PASSWORD_LENGTH);
        long existingNonExpiredOTPCount = aiMeterTokenRepository.listAllTelegramOtpTokensForDevice(meterDevice.getId()).stream()
                .filter((AIMeterTelegramOTPToken token) -> token.getExpiresIn().isAfter(LocalDateTime.now()))
                .count();

        model.addAttribute("botName", telegramBotName);
        if (existingNonExpiredOTPCount > MAX_ALLOWED_ACTIVE_OTP_COUNT) {
            log.warn("Too many requests for Telegram subscriptions for meter: [{}]", meterDevice.getDeviceId());
            model.addAttribute("messageId", "Too many attempts");
            return;
        }
        AIMeterTelegramOTPToken otpToken = new AIMeterTelegramOTPToken();
        otpToken.setDevice(meterDevice);
        otpToken.setAccessToken(fourDigitOtp);
        otpToken.setExpiresIn(LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES));
        aiMeterTokenRepository.save(otpToken);
        model.addAttribute("messageId", fourDigitOtp);
    }
}
