package aimeter.proxima.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Getter
@RequiredArgsConstructor
@FieldNameConstants(onlyExplicitlyIncluded = true)
public enum AIMeterSubscriptionType {

    @FieldNameConstants.Include TELEGRAM("Telegram", "telegram_logo.png"),
    @FieldNameConstants.Include WHATSAPP("Whatsapp", "whatsapp_icon.png");
    
    final String displayName;
    final String iconName;
    
}
