package dev.animals.entity;

import dev.animals.enums.PetGender;
import dev.animals.enums.PetStatus;
import dev.animals.enums.PetType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@Table(name = "animals")
public class AnimalEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private PetGender gender;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PetType typePet;

  @Column(nullable = false)
  private LocalDate birthDay;

  private String breed;

  private String description;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  PetStatus status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
  private UserEntity user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "organization_id", nullable = false, insertable = false, updatable = false)
  private OrganizationEntity organization;

  @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL)
  private List<AnimalPhotosEntity> animalPhotos;

  @ManyToMany(mappedBy = "animalSet")
  private Set<UserEntity> userSet = new HashSet<>();


  public AnimalEntity(Long id,
                      String name,
                      PetGender gender,
                      PetType typePet,
                      LocalDate birthDay,
                      String breed,
                      String description,
                      PetStatus status) {
    this.id = id;
    this.name = name;
    this.gender = gender;
    this.typePet = typePet;
    this.birthDay = birthDay;
    if (breed != null) {
      this.breed = breed.toLowerCase();
    } else {
      this.breed = null;
    }
    this.description = description;
    this.status = status;
  }

  public AnimalEntity(String name,
                      PetGender gender,
                      PetType typePet,
                      LocalDate birthDay,
                      String breed,
                      String description,
                      PetStatus status) {
    if (breed != null) {
      this.breed = breed.toLowerCase();
    } else {
      this.breed = null;
    }
    this.id = null;
    this.name = name;
    this.gender = gender;
    this.typePet = typePet;
    this.birthDay = birthDay;
    this.description = description;
    this.status = status;
  }
}
