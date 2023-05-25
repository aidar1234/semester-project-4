package ru.kpfu.itis.validation.annotation;

import ru.kpfu.itis.validation.validator.ElectronicsAdvertFilesValidator;
import ru.kpfu.itis.validation.validator.TransportAdvertFilesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ElectronicsAdvertFilesValidator.class)
public @interface ElectronicsAdvertFilesConstraint {
    String message() default "Вы не загрузили изображения";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
