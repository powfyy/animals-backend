package dev.animals.entity.attribute;

import dev.animals.entity.pk.animal.AttributeValuePK;
import lombok.*;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.NONE)
@Table(name = "attribute_values")
public class AttributeValueEntity {

  @EmbeddedId
  private AttributeValuePK id;

  @Column(name = "attribute_name", nullable = false, insertable = false, updatable = false)
  private String attributeName;

  @Column(name = "value", nullable = false, insertable = false, updatable = false)
  private String value;

  @MapsId("attributeName")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "attribute_name")
  private AttributeEntity attribute;
}
