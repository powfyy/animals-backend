package dev.pethaven.repositories;

import dev.pethaven.entity.Pet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository <Pet,Long>, JpaSpecificationExecutor <Pet>{
    public List <Pet> findByOrganizationId(Long organizationId);
    Page<Pet> findAll(Specification<Pet> specification, Pageable pageable);

}
