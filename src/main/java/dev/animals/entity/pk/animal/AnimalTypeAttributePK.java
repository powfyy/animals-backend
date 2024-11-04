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
public class AnimalTypeAttributePK implements Serializable {

  @Column(name = "type_name")
  private String typeName;

  @Column(name = "attribute_name")
  private String attributeName;
}
