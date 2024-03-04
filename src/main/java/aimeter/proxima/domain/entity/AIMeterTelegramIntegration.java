package aimeter.proxima.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import static aimeter.proxima.domain.AIMeterIntegrationType.Fields;

@Entity
@DiscriminatorValue(Fields.TELEGRAM)
public class AIMeterTelegramIntegration extends AIMeterIntegration {

    @Column
    Long telegramChatId;
}
