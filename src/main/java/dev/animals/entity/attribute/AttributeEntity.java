package dev.animals.entity.attribute;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "attributes")
public class AttributeEntity {

  @Id
  private String name;

  @Column(nullable = false)
  private int priority;

  @OneToMany(mappedBy = "attribute", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<AttributeValueEntity> values;
}
