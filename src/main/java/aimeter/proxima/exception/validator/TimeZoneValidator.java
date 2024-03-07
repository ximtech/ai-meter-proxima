package aimeter.proxima.exception.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = TimeZoneConstraintValidator.class)
@Documented
public @interface TimeZoneValidator {

    String message() default "Invalid time zone field";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
