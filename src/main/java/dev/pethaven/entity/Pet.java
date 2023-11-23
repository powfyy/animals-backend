package dev.pethaven.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table (name = "pets")
@Getter @Setter
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (nullable = false)
    private String name;
    @Column (nullable = false)
    @Enumerated (EnumType.STRING)
    private PetGender gender;
    @Enumerated (EnumType.STRING)
    @Column (nullable = false)
    private PetType typePet;
    @Column (nullable = false)
    private LocalDate birthDay;
    @Column
    private String breed;
    @Column
    private String description;
    @Column (nullable = false)
    @Enumerated (EnumType.STRING)
    PetStatus status;

    @ManyToOne (cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private Organization organization;
    @ManyToOne (cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private User user;
    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    private List<PetPhotos> petPhotos;
    @ManyToMany(mappedBy = "petSet")
    private Set<User> userSet = new HashSet<>();


    public Pet(Long id, String name, PetGender gender, PetType typePet, LocalDate birthDay,
               String breed, String description, PetStatus status, Organization organization) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.typePet = typePet;
        this.birthDay = birthDay;
        if(breed != null){
            this.breed = breed.toLowerCase();
        }
        else {this.breed = null;};
        this.description= description;
        this.status= status;
        this.organization = organization;
    }

    public Pet(Long id, String name, PetGender gender, PetType typePet, LocalDate birthDay,
               String breed, String description, PetStatus status, User user) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.typePet = typePet;
        this.birthDay = birthDay;
        if(breed != null){
            this.breed = breed.toLowerCase();
        }
        else {this.breed = null;};
        this.description = description;
        this.status= status;
        this.user = user;
    }
    public Pet() {

    }
}
