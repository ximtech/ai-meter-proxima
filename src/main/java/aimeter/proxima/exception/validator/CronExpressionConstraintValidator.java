package aimeter.proxima.exception.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.springframework.scheduling.support.CronExpression;

import java.lang.invoke.MethodHandles;

public class CronExpressionConstraintValidator implements ConstraintValidator<CronValidator, String> {
    /**
     * Checks that given value represents a valid cron expression.
     *
     * @param value The cron expression string to validate.
     * @param context context in which the constraint is evaluated.
     *
     * @return Returns {@code true} if the string is valid and parsable cron expression, {@code false} otherwise.
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if ( value == null ) {
            return true;
        }
        
        return CronExpression.isValidExpression(value);
    }
}
