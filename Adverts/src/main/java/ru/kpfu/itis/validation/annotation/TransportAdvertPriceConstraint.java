package ru.kpfu.itis.validation.annotation;

import ru.kpfu.itis.validation.validator.TransportAdvertPriceValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TransportAdvertPriceValidator.class)
public @interface TransportAdvertPriceConstraint {
    String message() default "Неверно введена цена";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
