package aimeter.proxima.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Getter
@RequiredArgsConstructor
@FieldNameConstants(onlyExplicitlyIncluded = true)
public enum AIMeterSubscriptionType {

    @FieldNameConstants.Include TELEGRAM("Telegram"),
    @FieldNameConstants.Include WHATSAPP("Whatsapp");
    
    final String displayName;
}
