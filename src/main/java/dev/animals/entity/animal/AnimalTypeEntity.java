package dev.animals.entity.animal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Table(name = "animal_types")
public class AnimalTypeEntity {

  @Id
  private String name;

  @Column(nullable = false)
  private int priority;

  @OneToMany(mappedBy = "animalType", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<AnimalTypeAttributeValueEntity> attributes = new ArrayList<>();
}
