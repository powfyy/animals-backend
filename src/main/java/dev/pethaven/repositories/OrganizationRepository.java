package dev.pethaven.repositories;

import dev.pethaven.entity.Organization;
import dev.pethaven.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository <Organization, Long> {
    @Query("SELECT org FROM Organization org JOIN org.auth a WHERE a.username = :username")
    public Optional<Organization> findByUsername(String username);

    @Query("DELETE FROM Organization org WHERE org.id IN (SELECT org2.id FROM Organization org2 JOIN org2.auth a WHERE a.username = :username)")
    public void deleteByUsername(String username);
}
