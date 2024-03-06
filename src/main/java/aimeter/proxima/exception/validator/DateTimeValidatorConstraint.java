package aimeter.proxima.exception.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class DateTimeValidatorConstraint implements ConstraintValidator<DateTimeValidator, String>  {

    private static final Log LOG = LoggerFactory.make(MethodHandles.lookup());
    
    private DateTimeFormatter formatter;

    @Override
    public void initialize(DateTimeValidator constraintAnnotation) {
        try {
            this.formatter = DateTimeFormatter.ofPattern(constraintAnnotation.pattern());
        } catch (IllegalArgumentException e) {
            throw LOG.getUnableToInitializeConstraintValidatorException(this.getClass(), e);
        }
    }

    /**
     * Checks that a field should be formatted as a date or time.
     *
     * @param value The date time value string to validate.
     * @param context context in which the constraint is evaluated.
     *
     * @return Returns {@code true} if the string is valid and parsable date-time, {@code false} otherwise.
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if ( value == null ) {
            return true;
        }

        try {
            LocalDateTime.parse(value, formatter);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }
}
