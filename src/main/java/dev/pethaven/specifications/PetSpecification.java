package dev.pethaven.specifications;

import dev.pethaven.entity.*;
import dev.pethaven.dto.FilterFields;
import dev.pethaven.enums.PetGender;
import dev.pethaven.enums.PetStatus;
import dev.pethaven.enums.PetType;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;


public class PetSpecification implements Specification <Pet>{
    private final FilterFields filterFields;
    public PetSpecification(FilterFields filterFields){
        this.filterFields=filterFields;
    }
    @Override
    public Predicate toPredicate(Root<Pet> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if (filterFields.getGender() != null) {
            predicates.add(criteriaBuilder.equal(root.get("gender"), PetGender.valueOf(filterFields.getGender())));
        }
        if (filterFields.getPetType() != null) {
            predicates.add(criteriaBuilder.equal(root.get("typePet"), PetType.valueOf(filterFields.getPetType())));
        }
        if (filterFields.getName() != null) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + filterFields.getName().toLowerCase() + "%"));
        }
        if (filterFields.getNameOrganization() != null) {
            Join<Pet, Organization> organizationJoin = root.join("organization", JoinType.INNER);
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(organizationJoin.get("nameOrganization")),
                    "%" + filterFields.getNameOrganization().toLowerCase() + "%"));
        }
        if (filterFields.getCity() != null) {
            Join<Pet, Organization> organizationJoin = root.join("organization", JoinType.INNER);
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(organizationJoin.get("city")),
                    "%"+filterFields.getCity().toLowerCase()+"%"));
        }
        predicates.add(criteriaBuilder.equal(root.get("status"), PetStatus.valueOf("ACTIVE")));
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
