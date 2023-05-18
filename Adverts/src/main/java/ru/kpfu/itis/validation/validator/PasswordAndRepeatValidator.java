package ru.kpfu.itis.validation.validator;

import ru.kpfu.itis.dto.request.UserSignUpRequest;
import ru.kpfu.itis.validation.annotation.PasswordAndRepeatConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordAndRepeatValidator implements ConstraintValidator<PasswordAndRepeatConstraint, UserSignUpRequest> {

    @Override
    public boolean isValid(UserSignUpRequest request, ConstraintValidatorContext constraintValidatorContext) {
        if (!request.getPassword().equals(request.getRepeatPassword())) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("repeatPassword")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
