package dev.animals.repository.animal;

import dev.animals.entity.animal.AnimalTypeAttributeValueEntity;
import dev.animals.entity.pk.AnimalTypeAttributeValuePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalTypeAttributeValueRepository extends JpaRepository<AnimalTypeAttributeValueEntity, AnimalTypeAttributeValuePK> {

  @Query("""
                SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END
                FROM AnimalTypeAttributeValueEntity a
                WHERE a.attribute.value = :attributeValue AND a.attribute.attributeName = :attributeName
    """)
  boolean existsByAttributeNameAndAttributeValue(String attributeValue, String attributeName);

  List<AnimalTypeAttributeValueEntity> findAllByAnimalTypeName(String typeName);
}
