package dev.animals.entity.animal;

import dev.animals.entity.attribute.AttributeValueEntity;
import dev.animals.entity.pk.animal.AnimalTypeAttributeValuePK;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "animal_type_attribute_values") //todo нужен скрипт liquibase
public class AnimalTypeAttributeValueEntity {

  @EmbeddedId
  private AnimalTypeAttributeValuePK id;

  @ManyToOne
  @JoinColumn(name = "type_name", insertable = false, updatable = false)
  private AnimalTypeEntity animalType;

  @ManyToOne
  @JoinColumns({
    @JoinColumn(name = "attribute_value", insertable = false, updatable = false),
    @JoinColumn(name = "attribute_name", insertable = false, updatable = false)
  })
  private AttributeValueEntity attribute;

  public AnimalTypeAttributeValueEntity(AnimalTypeAttributeValuePK id) {
    this.id = id;
  }
}
