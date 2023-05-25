package ru.kpfu.itis.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import ru.kpfu.itis.model.enums.TransportKind;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "transport_advert")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"user", "files"})
@EqualsAndHashCode(exclude = {"user", "files"})
public class TransportAdvert {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "price")
    private Float price;

    @Column(name = "locality")
    private String locality;

    @Column(name = "brand")
    private String brand;

    @Column(name = "is_new")
    private Boolean isNew;

    @Column(name = "is_registered")
    private Boolean isRegistered;

    @Enumerated(EnumType.STRING)
    @Column(name = "kind")
    private TransportKind kind;

    @Column(name = "mileage")
    private Integer mileage;

    @Column(name = "description")
    private String description;

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private User user;

    @ManyToMany
    @JoinTable(name = "advert_file",
            joinColumns = @JoinColumn(name = "advert_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "file_id", referencedColumnName = "id"))
    private List<File> files;
}
