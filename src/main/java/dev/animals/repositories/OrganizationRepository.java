package dev.animals.repositories;

import dev.animals.entity.OrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<OrganizationEntity, Long> {

  Optional<OrganizationEntity> findByAuthUsername(String username);

  void deleteByAuthUsername(String username);
}
