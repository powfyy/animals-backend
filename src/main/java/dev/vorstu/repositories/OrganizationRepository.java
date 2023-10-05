package dev.vorstu.repositories;

import dev.vorstu.entity.Organization;
import dev.vorstu.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository <Organization, Long> {
    public Organization findByAuthId (Long authId);
}
