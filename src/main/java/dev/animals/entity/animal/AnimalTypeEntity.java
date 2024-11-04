package dev.animals.entity.animal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
}
