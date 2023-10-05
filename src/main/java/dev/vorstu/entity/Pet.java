package dev.vorstu.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table
@Getter @Setter
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column (nullable = false)
    private String name;
    @Column (nullable = false)
    private char gender;
    @Column (nullable = false)
    private String typeAnimal;
    @Column (nullable = false)
    private String birthDay;
    private String breed;

    @ManyToOne (cascade = CascadeType.MERGE)
    private Organization organization;
    @ManyToOne (cascade = CascadeType.MERGE)
    private User user;

    public Pet(Long id, String name, char gender, String typeAnimal, String birthDay, String breed, Organization organization) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.typeAnimal = typeAnimal;
        this.birthDay = birthDay;
        this.breed = breed;
        this.organization = organization;
    }

    public Pet(Long id, String name, char gender, String typeAnimal, String birthDay, String breed, User user) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.typeAnimal = typeAnimal;
        this.birthDay = birthDay;
        this.breed = breed;
        this.user = user;
    }
}
