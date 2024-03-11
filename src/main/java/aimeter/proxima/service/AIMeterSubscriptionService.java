package aimeter.proxima.service;

import aimeter.proxima.domain.entity.AIMeterDevice;
import aimeter.proxima.domain.entity.AIMeterTelegramOTPToken;
import aimeter.proxima.domain.repository.AIMeterDeviceRepository;
import aimeter.proxima.domain.repository.AIMeterTokenRepository;
import aimeter.proxima.dto.TelegramOneTimePasswordDTO;
import aimeter.proxima.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIMeterSubscriptionService {

    public static final int OTP_PASSWORD_LENGTH = 4;
    public static final int EXPIRATION_MINUTES = 5;
    public static final int MAX_ALLOWED_ACTIVE_OTP_COUNT = 5;
    
    @Value("${telegram.bot.name}")
    String telegramBotName;

    final AIMeterDeviceRepository aiMeterDeviceRepository;
    final OneTimePasswordService oneTimePasswordService;
    final AIMeterTokenRepository aiMeterTokenRepository;
    
    public TelegramOneTimePasswordDTO getTelegramSubscriptionOTP(UUID deviceId) {
        AIMeterDevice meterDevice = aiMeterDeviceRepository.findRegisteredAIMeterDeviceOrThrow(deviceId);
        long existingNonExpiredOTPCount = aiMeterTokenRepository.listAllTelegramOtpTokensForDevice(meterDevice.getId()).stream()
                .filter((AIMeterTelegramOTPToken token) -> token.getExpiresIn().isAfter(LocalDateTime.now()))
                .count();

        if (existingNonExpiredOTPCount > MAX_ALLOWED_ACTIVE_OTP_COUNT) {
            log.warn("Too many requests for Telegram subscriptions for meter: [{}]", meterDevice.getDeviceId());
            throw new ApiException("Too many attempts. Please wait for %s minutes".formatted(EXPIRATION_MINUTES), HttpStatus.BAD_REQUEST);
        }
        
        String fourDigitOtp = oneTimePasswordService.createDigitPasswordOfLength(OTP_PASSWORD_LENGTH);
        AIMeterTelegramOTPToken otpToken = new AIMeterTelegramOTPToken();
        otpToken.setDevice(meterDevice);
        otpToken.setAccessToken(fourDigitOtp);
        otpToken.setExpiresIn(LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES));
        aiMeterTokenRepository.save(otpToken);
        return new TelegramOneTimePasswordDTO(telegramBotName, fourDigitOtp);
    }
}
