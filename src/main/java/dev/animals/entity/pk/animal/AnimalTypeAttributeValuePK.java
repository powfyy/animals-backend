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
public class AnimalTypeAttributeValuePK implements Serializable {

  @Column(name = "type_name")
  private String typeName;

  @Column(name = "attribute_value")
  private String attributeValue;

  @Column(name = "attribute_name")
  private String attributeName;
}
