package aimeter.proxima.domain.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("TELEGRAM_OTP")
public class AIMeterTelegramOTPToken extends AIMeterToken {

}
