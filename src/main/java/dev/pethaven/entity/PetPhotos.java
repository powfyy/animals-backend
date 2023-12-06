package dev.pethaven.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "pet_photos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PetPhotos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    String photoRef;

    @JoinColumn(name = "pet_id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(targetEntity = Pet.class, fetch = FetchType.LAZY)
    private Pet pet;
    @Column(name = "pet_id")
    private Long petId;

    public PetPhotos(Long id, String photoRef, Long petId) {
        this.id = id;
        this.photoRef = photoRef;
        this.petId = petId;
    }

    public PetPhotos(String photoRef, Long petId) {
        this(null, photoRef, petId);
    }

    public void setPet(Pet pet) {
        setPetId(pet.getId());
        this.pet = pet;
    }
}
