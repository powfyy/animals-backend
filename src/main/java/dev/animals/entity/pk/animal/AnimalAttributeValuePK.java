package dev.animals.entity.pk.animal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class AnimalAttributeValuePK implements Serializable {

  @Column(name = "animal_id")
  private Long animalId;

  @Column(name = "animal_type")
  private String animalType;

  @Column(name = "attribute_name")
  private String attributeName;

  @Column(name = "attribute_value")
  private String attributeValue;
}
