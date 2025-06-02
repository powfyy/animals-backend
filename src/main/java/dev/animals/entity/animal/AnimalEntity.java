package dev.animals.entity.animal;

import dev.animals.entity.OrganizationEntity;
import dev.animals.entity.UserEntity;
import dev.animals.enums.AnimalStatus;
import dev.animals.enums.GenderType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter
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
  private AnimalStatus status;

  @ManyToOne
  @JoinColumn(name = "type", nullable = false)
  private AnimalTypeEntity type;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "organization_id", nullable = false)
  private OrganizationEntity organization;

  @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<AnimalPhotosEntity> animalPhotos;

  @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<AnimalAttributeValueEntity> attributeValues = new ArrayList<>();

  @ManyToMany()
  @JoinTable(name = "user_animal",
    joinColumns = @JoinColumn(name = "animal_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
  private Set<UserEntity> adoptionRequestUsers = new HashSet<>();

}
