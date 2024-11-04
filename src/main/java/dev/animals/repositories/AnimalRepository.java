package dev.animals.repositories;

import dev.animals.entity.animal.AnimalEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalRepository extends JpaRepository<AnimalEntity, Long>, JpaSpecificationExecutor<AnimalEntity> {

  @Query("SELECT p FROM AnimalEntity p JOIN p.organization org JOIN org.auth a WHERE a.username = :username")
  List<AnimalEntity> findByOrganizationUsername(String username);

  Page<AnimalEntity> findAll(Specification<AnimalEntity> specification, Pageable pageable);
}
