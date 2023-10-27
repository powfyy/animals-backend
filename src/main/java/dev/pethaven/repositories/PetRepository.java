package dev.pethaven.repositories;

import dev.pethaven.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository <Pet,Long> {
    public List <Pet> findByOrganizationId(Long organizationId);

}
