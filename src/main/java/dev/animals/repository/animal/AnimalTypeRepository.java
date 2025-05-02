package dev.animals.repository.animal;

import dev.animals.entity.animal.AnimalTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnimalTypeRepository extends JpaRepository<AnimalTypeEntity, String> {

  Optional<AnimalTypeEntity> findByNameIgnoreCase(String name);
}
