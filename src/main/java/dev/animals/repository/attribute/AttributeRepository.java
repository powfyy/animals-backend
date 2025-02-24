package dev.animals.repository.attribute;

import dev.animals.entity.attribute.AttributeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface AttributeRepository extends JpaRepository<AttributeEntity, String> {

  List<AttributeEntity> findAllByNameIn(Set<String> names);
}
