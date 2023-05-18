package ru.kpfu.itis.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kpfu.itis.model.Role;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AccessToken {

    public static final long EXPIRE_MINUTES = 1L;

    private String email;

    private String firstName;

    private String lastName;

    private Role role;

    private LocalDateTime expire;
}
