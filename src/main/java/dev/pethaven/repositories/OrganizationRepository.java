package dev.pethaven.repositories;

import dev.pethaven.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository <Organization, Long> {
    public Organization findByAuthId (Long authId);
    public void deleteByAuthId(Long authId);
}
