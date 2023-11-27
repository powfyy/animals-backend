package dev.pethaven.repositories;

import dev.pethaven.entity.Pet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository <Pet,Long>, JpaSpecificationExecutor <Pet>{
    @Query("SELECT p FROM Pet p JOIN p.organization org JOIN org.auth a WHERE a.username = :username")
    public List <Pet> findByOrganizationUsername(String username);
    Page<Pet> findAll(Specification<Pet> specification, Pageable pageable);

}
