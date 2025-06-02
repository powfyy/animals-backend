package dev.animals.entity.animal;

import dev.animals.entity.pk.animal.AnimalAttributeValuePK;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "animal_attribute_values")
public class AnimalAttributeValueEntity {

  @EmbeddedId
  private AnimalAttributeValuePK id;

  @ManyToOne
  @JoinColumn(name = "animal_id", insertable = false, updatable = false)
  private AnimalEntity animal;

  @ManyToOne
  @JoinColumns({
    @JoinColumn(name = "animal_type", referencedColumnName = "type_name", insertable = false, updatable = false),
    @JoinColumn(name = "attribute_name", referencedColumnName = "attribute_name", insertable = false, updatable = false),
    @JoinColumn(name = "attribute_value", referencedColumnName = "attribute_value", insertable = false, updatable = false)
  })
  private AnimalTypeAttributeValueEntity typeAttribute;

  public AnimalAttributeValueEntity(AnimalAttributeValuePK id) {
    this.id = id;
  }
}
