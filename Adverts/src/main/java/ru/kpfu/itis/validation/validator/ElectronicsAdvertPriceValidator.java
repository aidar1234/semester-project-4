package ru.kpfu.itis.validation.validator;

import ru.kpfu.itis.dto.request.ElectronicsAdvertCreateRequest;
import ru.kpfu.itis.validation.annotation.ElectronicsAdvertPriceConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ElectronicsAdvertPriceValidator implements ConstraintValidator<ElectronicsAdvertPriceConstraint, ElectronicsAdvertCreateRequest> {

    @Override
    public boolean isValid(ElectronicsAdvertCreateRequest request, ConstraintValidatorContext constraintValidatorContext) {
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
