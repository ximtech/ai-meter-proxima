package aimeter.proxima.domain.entity;

import aimeter.proxima.domain.AIMeterSubscriptionType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue(AIMeterSubscriptionType.Fields.TELEGRAM)
public class AIMeterTelegramSubscription extends AIMeterSubscription {

    @Column
    Long telegramChatId;
}
