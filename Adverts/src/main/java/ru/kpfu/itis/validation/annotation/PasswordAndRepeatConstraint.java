package ru.kpfu.itis.validation.annotation;

import ru.kpfu.itis.validation.validator.PasswordAndRepeatValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordAndRepeatValidator.class)
public @interface PasswordAndRepeatConstraint {
    String message() default "Пароль и подтверждение пароля не совпадают";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
