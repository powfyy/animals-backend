package dev.animals.entity.animal;

import dev.animals.entity.pk.animal.AnimalTypeAttributePK;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "animal_type_attributes")
public class AnimalTypeAttributeEntity {

  @EmbeddedId
  private AnimalTypeAttributePK id;

  @ManyToOne
  @JoinColumn(name = "type_name", insertable = false, updatable = false)
  private AnimalTypeEntity animalType;

  @ManyToOne
  @JoinColumn(name = "attribute_name", insertable = false, updatable = false)
  private AttributeEntity attribute;

  public AnimalTypeAttributeEntity(AnimalTypeAttributePK id) {
    this.id = id;
  }
}
