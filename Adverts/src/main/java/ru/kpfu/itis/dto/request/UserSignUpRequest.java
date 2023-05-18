package ru.kpfu.itis.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kpfu.itis.validation.annotation.PasswordAndRepeatConstraint;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@PasswordAndRepeatConstraint
public class UserSignUpRequest {

    @Email(message = "Не соответствует email")
    @NotBlank(message = "Не может быть пустым")
    @Size(max = 255, message = "Максимальная длина email 255")
    private String email;

    @NotBlank(message = "Не может быть пустым")
    private String password;

    @NotBlank(message = "Не может быть пустым")
    private String repeatPassword;

    @NotBlank(message = "Не может быть пустым")
    @Size(max = 32, message = "Макссимальная длина имени 32")
    private String firstName;

    @NotBlank(message = "Не может быть пустым")
    @Size(max = 32, message = "Максимальная длина фамилии 32")
    private String lastName;

    @NotBlank(message = "Не может быть пустым")
    @Size(max = 12, message = "Максимальная длина номера 12")
    private String phone;

    @NotBlank(message = "Не может быть пустым")
    @Size(max = 32, message = "Максимальная длина населённого пункта 32")
    private String locality;
}
