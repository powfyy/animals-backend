package dev.animals.repository.attribute;

import dev.animals.entity.attribute.AttributeValueEntity;
import dev.animals.entity.pk.animal.AttributeValuePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AttributeValueRepository extends JpaRepository<AttributeValueEntity, AttributeValuePK> {

  List<AttributeValueEntity> findAllByIdIn(Collection<AttributeValuePK> ids);
}
