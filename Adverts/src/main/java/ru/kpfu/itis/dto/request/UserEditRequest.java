package ru.kpfu.itis.dto.request;

import lombok.Builder;
import lombok.Data;
import ru.kpfu.itis.validation.annotation.UserEditConstraint;

@Data
@Builder
@UserEditConstraint
public class UserEditRequest {

    private String email;

    private String firstName;

    private String lastName;

    private String phone;

    private String locality;
}
