package ru.kpfu.itis.dto.request;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import ru.kpfu.itis.validation.annotation.UserEditConstraint;

@Data
@Builder
@UserEditConstraint
public class UserEditRequest {

    private MultipartFile file;

    private String email;

    private String firstName;

    private String lastName;

    private String phone;

    private String locality;
}
