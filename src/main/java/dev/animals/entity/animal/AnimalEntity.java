package dev.animals.entity.animal;

import dev.animals.entity.OrganizationEntity;
import dev.animals.entity.UserEntity;
import dev.animals.enums.GenderType;
import dev.animals.enums.PetStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
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
  private GenderType gender;

  @Column(nullable = false)
  private LocalDate birthDay;

  private String breed;

  private String description;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  PetStatus status;

  @ManyToOne
  @JoinColumn(name = "type", nullable = false)
  private AnimalTypeEntity type;

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
                      GenderType gender,
                      AnimalTypeEntity type,
                      LocalDate birthDay,
                      String breed,
                      String description,
                      PetStatus status) {
    if (Objects.nonNull(breed)) {
      this.breed = breed.toLowerCase();
    }
    this.id = id;
    this.name = name;
    this.gender = gender;
    this.type = type;
    this.birthDay = birthDay;
    this.description = description;
    this.status = status;
  }

  public AnimalEntity(String name,
                      GenderType gender,
                      AnimalTypeEntity type,
                      LocalDate birthDay,
                      String breed,
                      String description,
                      PetStatus status) {
    if (Objects.nonNull(breed)) {
      this.breed = breed.toLowerCase();
    }
    this.id = null;
    this.name = name;
    this.gender = gender;
    this.type = type;
    this.birthDay = birthDay;
    this.description = description;
    this.status = status;
  }
}
