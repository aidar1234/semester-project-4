package ru.kpfu.itis.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "file")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode(exclude = "transportAdverts")
@ToString(exclude = "transportAdverts")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hash")
    private String hash;

    @Column(name = "name")
    private String name;

    @Column(name = "size")
    private Long size;

    @ManyToMany
    @JoinTable(name = "advert_file",
            joinColumns = @JoinColumn(name = "file_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "advert_id", referencedColumnName = "id"))
    private List<TransportAdvert> transportAdverts;

    @ManyToMany
    @JoinTable(name = "advert_file",
            joinColumns = @JoinColumn(name = "file_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "advert_id", referencedColumnName = "id"))
    private List<ElectronicsAdvert> electronicsAdverts;

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}
