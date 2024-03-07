package aimeter.proxima.exception.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.scheduling.support.CronExpression;

import java.time.ZoneId;
import java.util.TimeZone;

public class TimeZoneConstraintValidator implements ConstraintValidator<TimeZoneValidator, String> {
    /**
     * Checks that given value represents a valid time zone.
     *
     * @param value The time zone string to validate.
     * @param context context in which the constraint is evaluated.
     *
     * @return Returns {@code true} if the string is valid and parsable time zone, {@code false} otherwise.
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if ( value == null ) {
            return true;
        }

        try {
            ZoneId.of(value);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
