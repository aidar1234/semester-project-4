package ru.kpfu.itis.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserSignUpRequest {

    @Email
    @NotBlank
    @Size(max = 255)
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String repeatPassword;

    @NotBlank
    @Size(max = 32)
    private String firstName;

    @NotBlank
    @Size(max = 32)
    private String lastName;

    @NotBlank
    @Size(max = 12)
    private String phone;

    @NotBlank
    @Size(max = 32)
    private String locality;
}
