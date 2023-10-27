package dev.pethaven.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "pet_photos")
@Getter @Setter
@AllArgsConstructor
public class PetPhotos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (nullable = false)
    String photoRef;
    @ManyToOne (fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    Pet pet;
}
