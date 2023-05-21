package ru.kpfu.itis.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Builder
@Data
public class PasswordChangeRequest {

    @NotBlank(message = "Не может быть пустым")
    private String oldPassword;

    @NotBlank(message = "Не может быть пустым")
    private String newPassword;
}
