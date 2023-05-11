package ru.kpfu.itis.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "refresh_token")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString(exclude = "user")
@EqualsAndHashCode(exclude = "user")
public class RefreshToken {

    public static long EXPIRE_DAYS = 30L;

    @Id
    //@GeneratedValue(generator = "UUID")
    //@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private UUID token;

    private LocalDateTime expire;

    @OneToOne
    @JoinColumn(name = "account_id")
    private User user;
}
