package dev.animals.entity.animal;

import dev.animals.entity.attribute.AttributeValueEntity;
import dev.animals.entity.pk.AnimalTypeAttributeValuePK;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "animal_type_attribute_values")
public class AnimalTypeAttributeValueEntity {

  @EmbeddedId
  private AnimalTypeAttributeValuePK id;

  @ManyToOne
  @JoinColumn(name = "type_name", insertable = false, updatable = false)
  private AnimalTypeEntity animalType;

  @ManyToOne
  @JoinColumns({
    @JoinColumn(name = "attribute_name", insertable = false, updatable = false),
    @JoinColumn(name = "attribute_value", insertable = false, updatable = false)
  })
  private AttributeValueEntity attribute;

  public AnimalTypeAttributeValueEntity(AnimalTypeAttributeValuePK id) {
    this.id = id;
  }
}
