package ru.kpfu.itis.validation.validator;

import ru.kpfu.itis.dto.request.TransportAdvertCreateRequest;
import ru.kpfu.itis.validation.annotation.TransportAdvertFilesConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TransportAdvertFilesValidator implements ConstraintValidator<TransportAdvertFilesConstraint, TransportAdvertCreateRequest> {

    @Override
    public boolean isValid(TransportAdvertCreateRequest request, ConstraintValidatorContext constraintValidatorContext) {
        if (request.getFiles() == null || request.getFiles().length == 0 || request.getFiles()[0].getSize() == 0) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("files")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
