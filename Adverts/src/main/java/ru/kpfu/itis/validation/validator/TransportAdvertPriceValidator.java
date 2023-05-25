package ru.kpfu.itis.validation.validator;

import ru.kpfu.itis.dto.request.TransportAdvertCreateRequest;
import ru.kpfu.itis.validation.annotation.TransportAdvertPriceConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TransportAdvertPriceValidator implements ConstraintValidator<TransportAdvertPriceConstraint, TransportAdvertCreateRequest> {

    @Override
    public boolean isValid(TransportAdvertCreateRequest request, ConstraintValidatorContext constraintValidatorContext) {
        String price = request.getPrice();
        try {
            Float.parseFloat(price);
        } catch (Exception e) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("price")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
