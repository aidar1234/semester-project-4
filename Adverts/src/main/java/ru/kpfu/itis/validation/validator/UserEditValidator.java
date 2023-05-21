package ru.kpfu.itis.validation.validator;

import ru.kpfu.itis.dto.request.UserEditRequest;
import ru.kpfu.itis.validation.annotation.UserEditConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserEditValidator implements ConstraintValidator<UserEditConstraint, UserEditRequest> {

    @Override
    public boolean isValid(UserEditRequest request, ConstraintValidatorContext constraintValidatorContext) {
        if (!request.getEmail().equals("")) {
            String email = request.getEmail();
            if (email.length() > 255) {
                constraintValidatorContext
                        .buildConstraintViolationWithTemplate("Максимальная длина email 255")
                        .addPropertyNode("email")
                        .addConstraintViolation();
                return false;
            }
            if (!email.matches(".+@.+")) {
                constraintValidatorContext
                        .buildConstraintViolationWithTemplate("Не соответствует email")
                        .addPropertyNode("email")
                        .addConstraintViolation();
                return false;
            }
        }
        if (!request.getFirstName().equals("")) {
            String firstName = request.getFirstName();
            if (firstName.length() > 32) {
                constraintValidatorContext
                        .buildConstraintViolationWithTemplate("Максимальная длина имени 32")
                        .addPropertyNode("firstName")
                        .addConstraintViolation();
                return false;
            }
        }
        if (!request.getLastName().equals("")) {
            String lastName = request.getLastName();
            if (lastName.length() > 32) {
                constraintValidatorContext
                        .buildConstraintViolationWithTemplate("Максимальная длина фамилии 32")
                        .addPropertyNode("lastName")
                        .addConstraintViolation();
                return false;
            }
        }
        if (!request.getLocality().equals("")) {
            String locality = request.getLocality();
            if (locality.length() > 32) {
                constraintValidatorContext
                        .buildConstraintViolationWithTemplate("Максимальная длина населённого пункта 32")
                        .addPropertyNode("locality")
                        .addConstraintViolation();
                return false;
            }
        }
        if (!request.getPhone().equals("")) {
            String phone = request.getPhone();
            if (phone.length() > 12) {
                constraintValidatorContext
                        .buildConstraintViolationWithTemplate("Максимальная длина номера 12")
                        .addPropertyNode("phone")
                        .addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}
