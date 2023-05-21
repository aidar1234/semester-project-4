package ru.kpfu.itis.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {

    private String email;

    private String firstName;

    private String lastName;

    private String phone;

    private String locality;
}
