package dev.animals.entity.attribute;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Table(name = "attributes")
public class AttributeEntity {

  @Id
  private String name;

  @Column(nullable = false)
  private int priority;

  @OneToMany(mappedBy = "attribute", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<AttributeValueEntity> values;
}
