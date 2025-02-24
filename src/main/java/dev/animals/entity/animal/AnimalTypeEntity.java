package dev.animals.entity.animal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "animal_types")
public class AnimalTypeEntity {

  @Id
  private String name;

  @Column(nullable = false)
  private int priority;

  @OneToMany(mappedBy = "animalType", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<AnimalTypeAttributeValueEntity> attributes = new ArrayList<>();
}
