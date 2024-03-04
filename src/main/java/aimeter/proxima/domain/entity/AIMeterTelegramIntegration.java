package aimeter.proxima.domain.entity;

import aimeter.proxima.domain.AIMeterIntegrationType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue(AIMeterIntegrationType.Fields.TELEGRAM)
public class AIMeterTelegramIntegration extends AIMeterIntegration {

    @Column
    Long telegramChatId;
}
