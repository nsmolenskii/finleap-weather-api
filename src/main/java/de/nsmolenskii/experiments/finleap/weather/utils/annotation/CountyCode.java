package de.nsmolenskii.experiments.finleap.weather.utils.annotation;

import com.google.common.collect.ImmutableSet;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Locale;
import java.util.Set;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Constraint(validatedBy = {CountyCode.StringValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
public @interface CountyCode {

    String message() default "Invalid country code";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class StringValidator implements ConstraintValidator<CountyCode, String> {

        private static final Set<String> isoCountries = ImmutableSet.copyOf(Locale.getISOCountries());

        @Override
        public boolean isValid(final String value, final ConstraintValidatorContext context) {
            final boolean valid = value == null || isoCountries.contains(value);
            if (!valid) {
                context.unwrap(HibernateConstraintValidatorContext.class)
                        .addMessageParameter("invalidValue", value)
                        .addMessageParameter("isoCountries", String.join(", ", isoCountries))
                        .buildConstraintViolationWithTemplate(String.join(" ",
                                "Invalid country code: {invalidValue}.",
                                "Available values are: {isoCountries}."
                        ))
                        .addConstraintViolation()
                        .disableDefaultConstraintViolation();
            }
            return valid;
        }
    }
}
