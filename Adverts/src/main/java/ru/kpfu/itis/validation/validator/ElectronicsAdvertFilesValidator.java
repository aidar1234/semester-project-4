package ru.kpfu.itis.validation.validator;

import ru.kpfu.itis.dto.request.ElectronicsAdvertCreateRequest;
import ru.kpfu.itis.validation.annotation.ElectronicsAdvertFilesConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ElectronicsAdvertFilesValidator implements ConstraintValidator<ElectronicsAdvertFilesConstraint, ElectronicsAdvertCreateRequest> {

    @Override
    public boolean isValid(ElectronicsAdvertCreateRequest request, ConstraintValidatorContext constraintValidatorContext) {
        if (request.getFiles() == null || request.getFiles().length == 0 || request.getFiles()[0].getSize() == 0) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("files")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
