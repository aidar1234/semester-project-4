package ru.kpfu.itis.validation.validator;

import ru.kpfu.itis.dto.request.TransportAdvertCreateRequest;
import ru.kpfu.itis.validation.annotation.TransportAdvertKindConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TransportAdvertKindValidator implements ConstraintValidator<TransportAdvertKindConstraint, TransportAdvertCreateRequest> {

    @Override
    public boolean isValid(TransportAdvertCreateRequest request, ConstraintValidatorContext constraintValidatorContext) {
        String kind = request.getKind();
        if (!("Автомобиль".equals(kind) || "Мотоцикл".equals(kind) || "Грузовик".equals(kind))) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("kind")
                    .addConstraintViolation();
            return false;
        }
        if ("Автомобиль".equals(kind)) {
            request.setKind("AUTOMOBILE");
        }
        if ("Мотоцикл".equals(kind)) {
            request.setKind("MOTORBIKE");
        }
        if ("Грузовик".equals(kind)) {
            request.setKind("TRUCK");
        }
        return true;
    }
}
