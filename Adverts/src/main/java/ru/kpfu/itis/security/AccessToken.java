package ru.kpfu.itis.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kpfu.itis.model.Role;
import ru.kpfu.itis.model.State;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AccessToken {

    public static final long EXPIRE_MINUTES = 15L;

    private UUID id;

    private String email;

    private Role role;

    private State state;

    private String csrf;

    private LocalDateTime expire;
}
