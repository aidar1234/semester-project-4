package ru.kpfu.itis.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import ru.kpfu.itis.model.enums.Role;
import ru.kpfu.itis.model.enums.State;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "account")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "email")
    private String email;

    @Column(name = "hash_password")
    private String hashPassword;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "locality")
    private String locality;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @OneToOne
    @JoinColumn(name = "file_id")
    private File file;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<TransportAdvert> transportAdverts;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<ElectronicsAdvert> electronicsAdverts;

    @ManyToMany
    @JoinTable(name = "favorites",
            joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "advert_id", referencedColumnName = "id"))
    private List<TransportAdvert> favoriteTransportAdverts;

    @ManyToMany
    @JoinTable(name = "favorites",
            joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "advert_id", referencedColumnName = "id"))
    private List<ElectronicsAdvert> favoriteElectronicsAdverts;

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    public boolean isConfirmed() {
        return this.state == State.CONFIRMED;
    }
}
